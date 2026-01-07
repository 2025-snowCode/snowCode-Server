package simulations;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.util.Iterator;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.function.Supplier;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class FindAllStudentsSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080") // 테스트할 서버의 URL
            .acceptHeader("application/json") // 요청 헤더 설정 -> 쿠키로 바꿔야 함.
            .userAgentHeader(
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36")
            .disableFollowRedirect();

    FeederBuilder.Batchable<String> authFeeder =
            csv("tokens_admin.csv").circular();

    Iterator<Map<String, Object>> courseIdFeeder =
            Stream.iterate(1, i -> i + 1)
                    .map(i -> Map.<String, Object>of(
                            "courseId", (i - 1) % 75 + 1
                    ))
                    .iterator();

    ChainBuilder findStudents =
            exec(
                    http("find students")
                            .get("/courses/#{courseId}/enrollments")
                            .queryParam("page", "#{randomInt(0,4)}")
                            .queryParam("pageSize", "10")
                            .header("Authorization", "Bearer #{accessToken}")
                            .check(status().is(200))
            );


    ScenarioBuilder scn =
            scenario("find students")
                    .feed(courseIdFeeder)
                    .feed(authFeeder)
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