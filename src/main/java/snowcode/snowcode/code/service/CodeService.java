package snowcode.snowcode.code.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.code.domain.Code;
import snowcode.snowcode.code.domain.Language;
import snowcode.snowcode.code.dto.CodeRequest;
import snowcode.snowcode.code.dto.CodeResponse;
import snowcode.snowcode.code.exception.CodeCode;
import snowcode.snowcode.code.exception.CodeException;
import snowcode.snowcode.code.repository.CodeRepository;
import snowcode.snowcode.submission.domain.Submission;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CodeService {
    private final CodeRepository codeRepository;

    @Transactional
    public CodeResponse createCode(Submission submission, CodeRequest dto) {
        Code code = Code.createCode(dto.code(), true, Language.of(dto.language()), submission);
        codeRepository.save(code);
        return CodeResponse.from(code);
    }

    public Code findById(Long id) {
        return codeRepository.findById(id).orElseThrow(
                () -> new CodeException(CodeCode.CODE_NOT_FOUND)
        );
    }

    public CodeResponse findCode(Long id) {
        Code code = findById(id);
        return CodeResponse.from(code);
    }

    @Transactional
    public void deleteAllBySubmissionIdIn(List<Long> submissionIds) {
        codeRepository.deleteAllBySubmissionIdIn(submissionIds);
    }
}
