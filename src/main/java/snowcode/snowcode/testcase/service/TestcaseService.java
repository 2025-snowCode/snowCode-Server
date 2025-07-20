package snowcode.snowcode.testcase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.assignment.domain.Assignment;
import snowcode.snowcode.testcase.domain.ExampleRole;
import snowcode.snowcode.testcase.domain.Testcase;
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
    public void deleteTestcase(Long id) {
        Testcase testcase = findById(id);
        testcaseRepository.delete(testcase);
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
