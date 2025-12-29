package snowcode.snowcode.testcase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.assignment.domain.Assignment;
import snowcode.snowcode.testcase.domain.ExampleRole;
import snowcode.snowcode.testcase.domain.Testcase;
import snowcode.snowcode.testcase.dto.TestcaseCreateRequest;
import snowcode.snowcode.testcase.dto.TestcaseInfoResponse;
import snowcode.snowcode.testcase.dto.TestcaseRequest;
import snowcode.snowcode.testcase.dto.TestcaseResponse;
import snowcode.snowcode.testcase.exception.TestcaseErrorCode;
import snowcode.snowcode.testcase.exception.TestcaseException;
import snowcode.snowcode.testcase.repository.TestcaseRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TestcaseService {

    private final TestcaseRepository testcaseRepository;

    @Transactional
    public TestcaseResponse createTestcase(Assignment assignment, TestcaseRequest dto) {
        Testcase testcase = Testcase.createTestCase(assignment, dto.testcase(), dto.answer(), ExampleRole.of(dto.role()), dto.isPublic());
        testcaseRepository.save(testcase);
        return TestcaseResponse.from(testcase);
    }

    @Transactional
    public List<TestcaseInfoResponse> createTestcases(Assignment assignment, List<TestcaseCreateRequest> dtoList) {
        List<TestcaseInfoResponse> testcaseInfoResponseList = new ArrayList<>();
        if (dtoList == null) {
            return testcaseInfoResponseList;
        }
        for (TestcaseCreateRequest dto : dtoList) {
            Testcase testCase = Testcase.createTestCase(assignment, dto.testcase(), dto.answer(), ExampleRole.EXAMPLE, true);
            testcaseRepository.save(testCase);
            testcaseInfoResponseList.add(TestcaseInfoResponse.of(testCase));
        }
        return testcaseInfoResponseList;
    }

    @Transactional
    public void deleteTestcase(Testcase testcase) {
        testcaseRepository.delete(testcase);
    }

    @Transactional
    public void deleteTestcaseByAssignmentId(Long id) {
        testcaseRepository.deleteAllByAssignmentId(id);
    }

    public Testcase findById(Long id) {
        return testcaseRepository.findById(id).orElseThrow(
                () -> new TestcaseException(TestcaseErrorCode.TESTCASE_NOT_FOUND)
        );
    }

    private List<Testcase> findByAssignmentId(Long assignmentId) {
        return testcaseRepository.findByAssignmentId(assignmentId);
    }

    public List<TestcaseInfoResponse> findByTestcases(Long assignmentId) {
        List<Testcase> testcases = findByAssignmentId(assignmentId);

        List<TestcaseInfoResponse> dtoList = new ArrayList<>();
        for (Testcase testcase : testcases) {
            dtoList.add(new TestcaseInfoResponse(testcase.getId(), testcase.getTestcase(), testcase.getAnswer()));
        }
        return dtoList;
    }
}
