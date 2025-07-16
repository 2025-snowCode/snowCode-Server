package snowcode.snowcode.testcase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.testcase.domain.ExampleRole;
import snowcode.snowcode.testcase.domain.Testcase;
import snowcode.snowcode.testcase.dto.TestcaseRequest;
import snowcode.snowcode.testcase.dto.TestcaseResponse;
import snowcode.snowcode.testcase.repository.TestcaseRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TestcaseService {

    private final TestcaseRepository testcaseRepository;

    @Transactional
    public TestcaseResponse createTestcase(TestcaseRequest dto) {
        Testcase testcase = Testcase.createTestCase(dto.testcase(), dto.answer(), ExampleRole.of(dto.role()), dto.isPublic());
        testcaseRepository.save(testcase);
        return TestcaseResponse.from(testcase);
    }
}
