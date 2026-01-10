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
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CodeExecutionService {

    @Async("taskExecutor") // 사용할 custom executor 지정
    public Future<String> run(String code) throws IOException, InterruptedException {
        // 스레드 수행 내용 작성
        String random = UUID.randomUUID().toString();
        try {
            // 중복되지 않도록 UUID 값을 생성해 인풋 코드를 파일로 저장
            createFile(random, code);

            // 해당 소스코드 파일을 python3으로 실행하는 프로세스를 생성
            ProcessBuilder processBuilder = new ProcessBuilder("python3",
                    Paths.get(String.format("%s.py", random)).toString());
            Process process = processBuilder.start();
            process.waitFor();

            // 프로세스 실행 아웃풋 반환
            BufferedReader br =  new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            return CompletableFuture.completedFuture(getOutput(br));
        }
        finally {
            // 작성한 파일 삭제
            deleteFile(random); // FIXME : 서버 환경에서 path 설정 필요
        }
    }

    public int judgeSubmission(List<TestcaseInfoResponse> testcaseList, String result, int totalScore) {
        int totalScoreSize = testcaseList.size();
        int scoreSize = 0;

        for (TestcaseInfoResponse dto : testcaseList) {
            if (dto.answer().equals(result)) {
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

    private void createFile(String random, String code) {
        File file = new File(random +".py");

        try {
            if (file.createNewFile()) {
                //생성된 파일에 Buffer 를 사용하여 텍스트 입력
                FileWriter fw = new FileWriter(file);
                BufferedWriter writer  = new BufferedWriter(fw);

                // 데이터 입력
                writer.write(code);

                // Bufferd 종료
                writer.close();
            } else {
                System.out.println("File already exists");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteFile(String random) {
        // 파일 지우기
        File file = new File(String.valueOf(Paths.get(random + ".py")));
        if (!file.delete()) {
            throw new SubmissionException(SubmissionErrorCode.FILE_NOT_FOUND);
        }
    }
}
