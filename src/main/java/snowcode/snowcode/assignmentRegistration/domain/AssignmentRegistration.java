package snowcode.snowcode.assignmentRegistration.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import snowcode.snowcode.assignment.domain.Assignment;
import snowcode.snowcode.unit.domain.Unit;

@Getter @Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "assignment_registration")
public class AssignmentRegistration {

    @Id @Column(name = "assignment_registration_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id")
    private Unit unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    private AssignmentRegistration(Unit unit, Assignment assignment) {
        this.unit = unit;
        this.assignment = assignment;
    }

    public AssignmentRegistration createRegistration(Unit unit, Assignment assignment) {
        return new AssignmentRegistration(unit, assignment);
    }
}
