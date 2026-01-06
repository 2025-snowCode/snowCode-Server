package simulations;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.util.Iterator;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.function.Supplier;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class FindStudentSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080") // 테스트할 서버의 URL
            .acceptHeader("application/json") // 요청 헤더 설정 -> 쿠키로 바꿔야 함.
            .userAgentHeader(
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36")
            .disableFollowRedirect();

    private static Map<Integer, String> loadAdminTokenMap() {
        Map<Integer, String> tokenMap = new HashMap<>();

        try {
            InputStream is = FindStudentSimulation.class
                    .getClassLoader()
                    .getResourceAsStream("tokens_admin.csv");

            if (is == null) {
                throw new RuntimeException("tokens_admin.csv not found in resources");
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine(); // header skip

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int memberId = Integer.parseInt(parts[0].trim());
                String accessToken = parts[6].trim();
                tokenMap.put(memberId, accessToken);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return tokenMap;
    }

    Map<Integer, String> adminTokenMap = loadAdminTokenMap();



//    FeederBuilder.Batchable<String> authFeeder =
//            csv("tokens_admin.csv").circular();

    // memberId (3751 ~ 3825 순환)
//    Iterator<Map<String, Object>> courseStudentFeeder =
//            Stream.iterate(0, i -> i + 1)
//                    .map(i -> {
//                        int courseIndex = i % 75;        // 0 ~ 74
//                        int courseId = courseIndex + 1;  // 1 ~ 75
//
//                        // 각 course마다 50명 고정
//                        int studentOffset = (i / 75) % 50; // 0 ~ 49
//                        int memberId =
//                                courseIndex * 50 + studentOffset + 1;
//                        // 1 ~ 3750, 해당 course에 정확히 속한 학생
//
//                        return Map.<String, Object>of(
//                                "courseId", courseId,
//                                "memberId", memberId
//                        );
//                    })
//                    .iterator();

    Iterator<Map<String, Object>> secureFeeder =
            Stream.iterate(0, i -> i + 1)
                    .map(i -> {
                        int courseIndex = i % 75;          // 0 ~ 74
                        int courseId = courseIndex + 1;    // 1 ~ 75

                        // 🔥 강의 주인 ADMIN
                        int adminMemberId = 3751 + courseIndex;
                        String accessToken = adminTokenMap.get(adminMemberId);

                        if (accessToken == null) {
                            throw new RuntimeException(
                                    "No token for admin memberId=" + adminMemberId
                            );
                        }

                        // 학생 (해당 강의의 50명 중 1명)
                        int studentOffset = (i / 75) % 50; // 0 ~ 49
                        int studentMemberId =
                                courseIndex * 50 + studentOffset + 1;

                        return Map.<String, Object>of(
                                "courseId", courseId,
                                "memberId", studentMemberId,
                                "accessToken", accessToken
                        );
                    })
                    .iterator();






    ChainBuilder findStudents =
            exec(
                    http("find student")
                            .get("/courses/#{courseId}/enrollments/#{memberId}")
                                .header("Authorization", "Bearer #{accessToken}")
                            .check(status().is(200))
            );

    ScenarioBuilder scn =
            scenario("find students")
                    .feed(secureFeeder)
//                    .feed(authFeeder)
//                    .feed(courseStudentFeeder)
                    .exec(findStudents)
                    .exitHere();

    {
        setUp(
                // - 0~3분까지 0~382명
                //- 3~10분까지 382~1,875명
                //- 10~17분까지 유지
                //- 17~20분까지 1875 ~ 382명

                scn.injectClosed(
                        constantConcurrentUsers(0).during(60),   // 1분간 정지 (유저 수 0명)
                        rampConcurrentUsers(0).to(382).during(3 * 60),
                        rampConcurrentUsers(382).to(1875).during(7 * 60),
                        constantConcurrentUsers(1875).during(7 * 60),
                        rampConcurrentUsers(1875).to(382).during(3 * 60)
//                        rampConcurrentUsers(0).to(10).during(60)
                ).protocols(httpProtocol)
        );
    }
}