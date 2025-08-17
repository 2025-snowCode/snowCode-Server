package snowcode.snowcode.auth.service;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.auth.domain.Role;
import snowcode.snowcode.auth.dto.*;
import snowcode.snowcode.auth.exception.AuthErrorCode;
import snowcode.snowcode.auth.exception.AuthException;
import snowcode.snowcode.auth.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public MemberResponse signup(MemberRequest dto) {
        Member member = Member.createMember(dto.name(), Role.of(dto.role()), dto.email());
        memberRepository.save(member);
        return MemberResponse.from(member);
    }

    @Transactional
    public AddProfileResponse updateStudentId(Member member, String studentId) {
        member.updateStudentId(studentId);
        return AddProfileResponse.from(member);
    }

    public MyProfileResponse findMemberById(Long id) {
        Member member = findMember(id);
        return MyProfileResponse.from(member);
    }

    public Member findMember(Long id) {
        return memberRepository.findById(id).orElseThrow(
                () -> new AuthException(AuthErrorCode.MEMBER_NOT_FOUND)
        );
    }

    public MemberCountListResponse findAllMember() {
        List<MemberResponse> lst = memberRepository.findAll().stream()
                .map(MemberResponse::from).toList();

        return new MemberCountListResponse(lst.size(), lst);
    }

    @Transactional(readOnly = true)
    public List<Member> findNonAdmin(Long courseId, @Nullable String studentId) {

        if (studentId == null || studentId.isBlank()) {
            return findNonAdminByCourseId(courseId);
        }
        return findNonAdminByCourseIdAndStudentId(courseId, studentId)
                .map(List::of)
                .orElseGet(List::of);
    }

    @Transactional(readOnly = true)
    public List<Member> findNonAdminByCourseId(Long courseId) {
        return memberRepository.findNonAdminByCourseId(courseId, Role.ADMIN);
    }

    @Transactional(readOnly = true)
    public Optional<Member> findNonAdminByCourseIdAndStudentId(Long courseId, String studentId) {
        return memberRepository.findNonAdminByCourseIdAndStudentId(courseId, studentId, Role.ADMIN);
    }

}
