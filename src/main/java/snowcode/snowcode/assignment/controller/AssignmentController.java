package snowcode.snowcode.assignment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import snowcode.snowcode.assignment.dto.AssignmentRequest;
import snowcode.snowcode.assignment.dto.AssignmentResponse;
import snowcode.snowcode.assignment.service.AssignmentService;
import snowcode.snowcode.common.response.BasicResponse;
import snowcode.snowcode.common.response.ResponseUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    @PostMapping
    public BasicResponse<AssignmentResponse> createAssignment(@Valid @RequestBody AssignmentRequest dto) {
        AssignmentResponse assignment = assignmentService.createAssignment(dto);
        return ResponseUtil.success(assignment);
    }
}
