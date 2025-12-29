package snowcode.snowcode.student.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.auth.exception.AuthErrorCode;
import snowcode.snowcode.auth.exception.AuthException;
import snowcode.snowcode.auth.repository.MemberRepository;
import snowcode.snowcode.student.dto.StudentRequest;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudentService {
    private final MemberRepository memberRepository;

    public Member findByStudentId(String studentId) {
        return memberRepository.findByStudentId(studentId)
                .orElseThrow(() -> new AuthException(AuthErrorCode.STUDENT_NOT_FOUND));
    }

    public List<Member> findStudents(List<StudentRequest> students) {
        if (students == null) return new ArrayList<>();
        List<String> studentIds = extractStudentIdsFromDto(students);
        return findAllStudentsByStudentIds(studentIds);
    }

    private List<Member> findAllStudentsByStudentIds(List<String> studentIds) {
        return memberRepository.findAllByStudentIdIn(studentIds);
    }

    private List<String> extractStudentIdsFromDto(List<StudentRequest> students) {
        List<String> studentIds = new ArrayList<>();
        for (StudentRequest student : students) {
            studentIds.add(student.studentId());
        }
        return studentIds;
    }

    public void addAdminInMembers(Member member, List<Member> members) {
        members.add(member);
    }
}
