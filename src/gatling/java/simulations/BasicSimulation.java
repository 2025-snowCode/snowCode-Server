package simulations;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class BasicSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080") // 테스트할 서버의 URL
            .acceptHeader("application/json"); // 요청 헤더 설정

    ScenarioBuilder scn = scenario("Basic Scenario")
            .exec(http("request_2")
                    .get("/health") // 테스트할 엔드포인트
                    .check(status().is(200))); // 응답 상태 코드 확인

    {
        setUp(
                scn.injectOpen(atOnceUsers(100000)) // 100명의 사용자가 동시에 요청
        ).protocols(httpProtocol);
    }
}