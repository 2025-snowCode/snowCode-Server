package snowcode.snowcode.auth.service;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.auth.domain.Role;
import snowcode.snowcode.auth.dto.MemberCountListResponse;
import snowcode.snowcode.auth.dto.MemberResponse;
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
    public Page<Member> findNonAdmin(Long courseId, @Nullable String studentId, int page, int pageSize) {
        // page: 현재 페이지 지정, 페이지 안 총 리소스 지정 (10개), 해당 페이지를 오름차순으로 정렬

        if (studentId == null || studentId.isBlank()) {
            Pageable pageable = PageRequest.of(page, pageSize);
            return findNonAdminByCourseId(courseId, pageable);
        }
        // 학번으로 한 명만 찾기
        List<Member> findMemberByStudentId = findNonAdminByCourseIdAndStudentId(courseId, studentId).stream().toList();
        if (findMemberByStudentId.isEmpty()) {
            return Page.empty();
        }
        return new PageImpl<>(findMemberByStudentId);

    }


    @Transactional(readOnly = true)
    public Page<Member> findNonAdminByCourseId(Long courseId, Pageable pageable) {
        return memberRepository.findNonAdminByCourseId(courseId, Role.ADMIN, pageable);
    }

    @Transactional(readOnly = true)
    public List<Member> findNonAdminByCourseIdList(Long courseId) {
        return memberRepository.findNonAdminByCourseIdList(courseId, Role.ADMIN);
    }

    @Transactional(readOnly = true)
    public Optional<Member> findNonAdminByCourseIdAndStudentId(Long courseId, String studentId) {
        return memberRepository.findNonAdminByCourseIdAndStudentId(courseId, studentId, Role.ADMIN);
    }
}