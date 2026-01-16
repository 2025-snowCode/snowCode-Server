package snowcode.snowcode.code.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.submission.exception.SubmissionErrorCode;
import snowcode.snowcode.submission.exception.SubmissionException;
import snowcode.snowcode.testcase.dto.TestcaseInfoResponse;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CodeExecutionService {

    // 작업 경로 고정
    private static final Path WORK_DIR = Paths.get(".");

    @Async("taskExecutor") // 사용할 custom executor 지정
    public Future<String> run(String code, String testcase) throws IOException, InterruptedException {
        // 스레드 수행 내용 작성
        String random = UUID.randomUUID().toString();
        // 중복되지 않도록 UUID 값을 생성해 인풋 코드를 파일로 저장
        Path filePath = createFile(random, code);
        try {

            // 해당 소스코드 파일을 python3으로 실행하는 프로세스를 생성
            ProcessBuilder processBuilder = new ProcessBuilder("python3",
                    filePath.toAbsolutePath().toString());
            Process process = processBuilder.start();

            try (BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(process.getOutputStream(), StandardCharsets.UTF_8))) {
                bw.write(testcase);
                bw.flush();
            }
            process.waitFor();

            // 프로세스 실행 아웃풋 반환
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            return CompletableFuture.completedFuture(getOutput(br));
        } finally {
            deleteFile(filePath);
        }
    }

    public int judgeSubmission(List<TestcaseInfoResponse> testcaseList, String code, int totalScore) {
        try {
            List<String> outputList = new ArrayList<>();
            // 계산해서 결과 쌍 얻어내기
            for (TestcaseInfoResponse testcase : testcaseList) {
                String result = run(code, testcase.testcase()).get();
                log.info(result);
                outputList.add(result);
            }

            // 맞는지 검사 후 점수 계산
            return calcScore(testcaseList, outputList, totalScore);

        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new SubmissionException(SubmissionErrorCode.FILE_NOT_FOUND);
        } catch (IOException ex) {
            throw new SubmissionException(SubmissionErrorCode.FILE_NOT_FOUND);
        } catch (
                ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private int calcScore(List<TestcaseInfoResponse> testcaseList, List<String> outputList, int totalScore) {
        // 계산
        int totalScoreSize = testcaseList.size();
        int scoreSize = 0;

        for (int i=0; i<totalScoreSize; i++) {
            if (testcaseList.get(i).answer().equals(outputList.get(i))) {
                scoreSize++;
            }
        }
        return scoreSize * totalScore / totalScoreSize;
    }

    private String getOutput(BufferedReader bufferedReader) throws IOException {
        // 결과 읽어서 String으로 합친 후 반환
        StringBuilder sb = new StringBuilder();
        String line;
        boolean first = true;
        while ((line = bufferedReader.readLine()) != null) {
            if (first) first = false;
            else sb.append("\n");
            sb.append(line);
        }
        bufferedReader.close();
        return sb.toString();
    }

    private Path createFile(String random, String code) {
        try {
            String filename = random + ".py";
            Path filePath = WORK_DIR.resolve(filename);

            Files.writeString(
                    filePath,
                    code,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
            return filePath;
        } catch (IOException e) {
            throw new SubmissionException(SubmissionErrorCode.FILE_CREATE_FAILED);
        }
    }

    private void deleteFile(Path filePath) {
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new SubmissionException(SubmissionErrorCode.FILE_NOT_FOUND);
        }
    }
}
