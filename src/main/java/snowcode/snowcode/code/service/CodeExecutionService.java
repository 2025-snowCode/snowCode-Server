package snowcode.snowcode.code.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import snowcode.snowcode.common.response.ResponseUtil;
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
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CodeExecutionService {

    // 작업 경로 고정
    private static final Path WORK_DIR = Paths.get(".");

    @Async("taskExecutor")
    public void runRealtime(String code, WebSocketSession session, ObjectMapper mapper) {
        String random = UUID.randomUUID().toString();
        Path filePath = createFile(random, code);

        try {
            ProcessBuilder pb = new ProcessBuilder("python3", "-u", filePath.toString());
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // 파이썬 출력을 실시간으로 읽어 웹소켓으로 전달
            Thread outputThread = runThread(session, mapper, process);

            session.getAttributes().put("process", process);
            session.getAttributes().put("writer", new BufferedWriter(
                    new OutputStreamWriter(process.getOutputStream(), StandardCharsets.UTF_8)));

            // 끝난 이후 스레드 정리
            process.waitFor();
            outputThread.join();

        } catch (Exception e) {
            throw new SubmissionException(SubmissionErrorCode.ERROR_EXECUTE_CODE);
        } finally {
            deleteFile(filePath);
        }
    }

//    private static Thread runThread(WebSocketSession session, ObjectMapper mapper, Process process) {
//        Thread outputThread = new Thread(() -> {
//            try (BufferedReader reader = new BufferedReader(
//                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    String jsonResponse = mapper.writeValueAsString(ResponseUtil.success(line + "\n"));
//                    session.sendMessage(new TextMessage(jsonResponse));
//                }
//            } catch (Exception e) {
//                // 종료된 세션에 접근하려고 한 경우
//                throw new SubmissionException(SubmissionErrorCode.INVALID_SESSION_STATE);
//            }
//        });
//        outputThread.start();
//        return outputThread;
//    }

    private static Thread runThread(WebSocketSession session, ObjectMapper mapper, Process process) {
        Thread outputThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {

                int ch;
                StringBuilder buffer = new StringBuilder();

                while ((ch = reader.read()) != -1) {
                    char c = (char) ch;
                    buffer.append(c);

                    if (c == '\n' || c == ':') {
                        String msg = buffer.toString();

                        String jsonResponse = mapper.writeValueAsString(
                                ResponseUtil.success(msg)
                        );

                        session.sendMessage(new TextMessage(jsonResponse));
                        buffer.setLength(0);
                    }
                }

                if (!buffer.isEmpty()) {
                    String jsonResponse = mapper.writeValueAsString(
                            ResponseUtil.success(buffer.toString())
                    );
                    session.sendMessage(new TextMessage(jsonResponse));
                }

            } catch (Exception e) {
                throw new SubmissionException(SubmissionErrorCode.INVALID_SESSION_STATE);
            }
        });

        outputThread.start();
        return outputThread;
    }

    @Async("taskExecutor") // 사용할 custom executor 지정
    // code, testcase(input) 받아 결과 반환하는 메서드
    public Future<String> run(String code, String testcase) throws IOException, InterruptedException {
        // 스레드 수행 내용 작성
        String random = UUID.randomUUID().toString();
        // 중복되지 않도록 UUID 값을 생성해 인풋 코드를 파일로 저장
        Path filePath = createFile(random, code);
        try {

            // 해당 소스코드 파일을 python3으로 실행하는 프로세스를 생성
            ProcessBuilder processBuilder = new ProcessBuilder("python3",
                    filePath.toString());
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
                outputList.add(result);
            }

            // 맞는지 검사 후 점수 계산
            return calcScore(testcaseList, outputList, totalScore);

        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new SubmissionException(SubmissionErrorCode.FILE_RUN_FAILED);
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
            throw new SubmissionException(SubmissionErrorCode.FILE_DELETE_FAILED);
        }
    }
}
