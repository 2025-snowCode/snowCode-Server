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

public class FindAllCourseSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080") // 테스트할 서버의 URL
            .acceptHeader("application/json") // 요청 헤더 설정 -> 쿠키로 바꿔야 함.
            .userAgentHeader(
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36")
            .disableFollowRedirect();

    AtomicInteger emailIndex = new AtomicInteger(0);

//    Iterator<Map<String, Object>> emailIterator =
//            IntStream.rangeClosed(0, 3824)
//                    .mapToObj(i -> Map.<String, Object>of(
//                            "email", "test" + i + "@gmail.com"
//                    ))
//                    .iterator();

    Iterator<Map<String, Object>> emailFeeder =
            Stream.generate(new AtomicInteger(0)::getAndIncrement)
                    .map(i -> Map.<String, Object>of(
                            "email", "test" + (i % 3825) + "@gmail.com"
                    ))
                    .iterator();



    ChainBuilder login =
            exec(
                    http("login")
                            .post("/oauth2/authorization")
                            .body(StringBody("""
                    {
                      "provider": "LOCAL",
                      "role": "USER",
                      "email": "#{email}",
                      "OAuthToken": "sM0yOK1FPuGJaq8x/U76gkKNfT64GQKsityED54zG9M="
                    }
                """))
                            .asJson()
                            .header("Content-Type", "application/json")
                            .header("Accept", "application/json")

                            .check(status().is(200))
                            .check(jsonPath("$.response.accessToken").saveAs("token")) // 로그인 응답 JSON에서 accessToken 필드 값을 Gatling Session 변수 token 으로 저장

            );

    ChainBuilder myCourses =
            exec(
                    http("my courses")
                            .get("/courses/my")
                                .header("Authorization", "Bearer #{token}")
                            .check(status().is(200))
            );

    ScenarioBuilder scn =
            scenario("Login → My Courses")
                    .feed(emailFeeder)
                    .exec(login)
                    .exitHereIfFailed()
                    .exec(myCourses)
                    .exitHere();

    {
        setUp(


                // 유입 (많음)
//                scn.injectOpen(
//                        nothingFor(4), // 4초간 정지 (요청이 들어오지 않을 때 어떤 식으로 진행되는지 확인하기 위함)
//                        rampUsersPerSec(0).to(382).during(60 * 3), // 0 ~ 3분까지 유저를 382명으로 점진적으로 3분간 증가
//                        rampUsersPerSec(382).to(1875).during(60 * 7), // 3 ~ 10분까지 초당 382명의 유저에서 초당 1875명의 유저가 요청하는 형태로 균일하게 증가하게 진행
//                        constantUsersPerSec(1875).during(60 * 7), // 10 ~ 17분까지 초당 1875명의 유저가 7분 동안 요청 (유지)
//                        rampUsersPerSec(1875).to(382).during(60 * 3) // 17 ~ 20분까지 1,875 -> 382로 요청 감소
//                ).protocols(httpProtocol)

                // 유입
//                scn.injectOpen(
//                        nothingFor(4), // 4초간 정지 (요청이 들어오지 않을 때 어떤 식으로 진행되는지 확인하기 위함)
//                        rampUsers(382).during(60 * 3) // 0 ~ 3분까지 유저를 382명으로 증가
//                        rampUsers(1875-382).during(60 * 7) // 3 ~ 10분까지 1875명의 유저가 요청
//                        constantUsersPerSec(1875).during(7), // 10 ~ 17분까지 초당 1875명의 유저가 7분 동안 요청 (유지)
//                        rampUsers(382).during(60 * 3) // 17 ~ 20분까지 1,875 -> 382로 요청 감소
//                ).protocols(httpProtocol)

//                scn.injectOpen(
//                        nothingFor(4), // 4초간 정지 (요청이 들어오지 않을 때 어떤 식으로 진행되는지 확인하기 위함)
//                        rampUsers(382).during(60) // 0 ~ 1분
//                        rampUsers(1).during(1) // 0 ~ 1분
//                ).protocols(httpProtocol)

                // - 0~3분까지 0~382명
                //- 3~10분까지 382~1,875명
                //- 10~17분까지 유지
                //- 17~20분까지 1875 ~ 382명

                scn.injectClosed(
                        rampConcurrentUsers(0).to(382).during(3 * 60),
                        rampConcurrentUsers(382).to(1875).during(7 * 60),
                        constantConcurrentUsers(1875).during(7 * 60),
                        rampConcurrentUsers(1875).to(382).during(3 * 60)
                ).protocols(httpProtocol)
        );
    }
}