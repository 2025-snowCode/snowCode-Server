package simulations;
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.IntStream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class LoginTokenSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080")
            .contentTypeHeader("application/json")
            .acceptHeader("application/json");

    private static FileWriter csvWriter;

    static {
        try {
            csvWriter = new FileWriter("tokens.csv");
            csvWriter.write(
                    "memberId,name,role,studentId,email,provider,accessToken\n"
            );
            csvWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    Iterator<Map<String, Object>> feeder =
            IntStream.rangeClosed(0, 3824)
                    .mapToObj(i -> {
                        String role = i <= 3749 ? "USER" : "ADMIN";
                        return Map.<String, Object>of(
                                "email", "test" + i + "@gmail.com",
                                "role", role
                        );
                    })
                    .iterator();

    ChainBuilder login =
            exec(
                    http("login")
                            .post("/oauth2/authorization")
                            .body(StringBody("""
                {
                  "provider": "LOCAL",
                  "role": "#{role}",
                  "email": "#{email}",
                  "OAuthToken": "sM0yOK1FPuGJaq8x/U76gkKNfT64GQKsityED54zG9M="
                }
                """))
                            .asJson()
                            .check(status().is(200))
                            .check(jsonPath("$.response.memberId").saveAs("memberId"))
                            .check(jsonPath("$.response.name").saveAs("name"))
                            .check(jsonPath("$.response.role").saveAs("respRole"))
                            .check(jsonPath("$.response.studentId").saveAs("studentId"))
                            .check(jsonPath("$.response.email").saveAs("respEmail"))
                            .check(jsonPath("$.response.provider").saveAs("provider"))
                            .check(jsonPath("$.response.accessToken").saveAs("accessToken"))
            )
                    .exec(session -> {
                        try {
                            csvWriter.write(
                                    session.getString("memberId") + "," +
                                            session.getString("name") + "," +
                                            session.getString("respRole") + "," +
                                            session.getString("studentId") + "," +
                                            session.getString("respEmail") + "," +
                                            session.getString("provider") + "," +
                                            session.getString("accessToken") + "\n"
                            );
                            csvWriter.flush();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        return session;
                    });

    ScenarioBuilder scn =
            scenario("Generate Login Tokens")
                    .feed(feeder)
                    .exec(login);

    {
        setUp(
                scn.injectOpen(
                        atOnceUsers(3825)
                )
        ).protocols(httpProtocol);
    }
}
