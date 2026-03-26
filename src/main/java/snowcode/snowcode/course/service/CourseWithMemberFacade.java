package snowcode.snowcode.course.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.auth.service.MemberService;
import snowcode.snowcode.chatRoom.exception.ChatRoomErrorCode;
import snowcode.snowcode.chatRoom.exception.ChatRoomException;
import snowcode.snowcode.chatRoom.service.ChatRoomService;
import snowcode.snowcode.course.domain.Course;
import snowcode.snowcode.course.dto.CourseDetailAdminResponse;
import snowcode.snowcode.course.dto.CourseDetailStudentResponse;
import snowcode.snowcode.unit.domain.Unit;
import snowcode.snowcode.unit.dto.UnitDetailAdminResponse;
import snowcode.snowcode.unit.dto.UnitDetailStudentResponse;
import snowcode.snowcode.unit.service.UnitService;
import snowcode.snowcode.unit.service.UnitWithAssignmentFacade;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseWithMemberFacade {
    private final CourseService courseService;
    private final UnitService unitService;
    private final UnitWithAssignmentFacade unitWithAssignmentFacade;
    private final MemberService memberService;
    private final ChatRoomService chatRoomService;

    public CourseDetailStudentResponse createStudentCourseResponse(Long memberId, Long courseId) {
        Course course = courseService.findCourse(courseId);
        List<Unit> unitList = unitService.findAllByCourseId(courseId);
        
        // 단원 및 과제 찾기
        List<UnitDetailStudentResponse> unitDtoList = new ArrayList<>();

        for (Unit unit : unitList) {
            unitDtoList.add(unitWithAssignmentFacade.createStudentUnitResponse(memberId, unit.getId()));
        }

        // admin 찾기
        Long adminId = course.getCreatedBy();

        // chatRoomId 조회
        Long chatRoomId = chatRoomService.findChatRoomByMembers(adminId, memberId)
                .orElseThrow(() -> new ChatRoomException(ChatRoomErrorCode.NOT_FOUND_CHAT_ROOM)).getId();

        return CourseDetailStudentResponse.of(course, chatRoomId, unitDtoList);
    }

    public CourseDetailAdminResponse createAdminCourseResponse(Long courseId) {
        Course course = courseService.findCourse(courseId);
        List<Unit> unitList = unitService.findAllByCourseId(courseId);

        List<UnitDetailAdminResponse> unitDtoList = new ArrayList<>();

        for (Unit unit : unitList) {
            unitDtoList.add(unitWithAssignmentFacade.createAdminUnitResponse(unit.getId()));
        }

        int size = memberService.findNonAdminByCourseIdList(courseId).size();

        return CourseDetailAdminResponse.of(course, size, unitDtoList);
    }
}
