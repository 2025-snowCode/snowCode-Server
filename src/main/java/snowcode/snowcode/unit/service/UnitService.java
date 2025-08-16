package snowcode.snowcode.unit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.common.DateTimeConverter;
import snowcode.snowcode.course.domain.Course;
import snowcode.snowcode.unit.domain.Unit;
import snowcode.snowcode.unit.dto.UnitProgressResponse;
import snowcode.snowcode.unit.dto.UnitRequest;
import snowcode.snowcode.unit.dto.UnitResponse;
import snowcode.snowcode.unit.dto.UnitUpdateRequest;
import snowcode.snowcode.unit.exception.UnitErrorCode;
import snowcode.snowcode.unit.exception.UnitException;
import snowcode.snowcode.unit.repository.UnitRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UnitService {
    private final UnitRepository unitRepository;

    @Transactional
    public Unit createUnit(Course course, UnitRequest dto) {
        LocalDate releaseDate = DateTimeConverter.stringToDate(dto.releaseDate());
        LocalDate dueDate = DateTimeConverter.stringToDate(dto.dueDate());

        Unit unit = Unit.createUnit(dto.title(), releaseDate, dueDate, course);
        unitRepository.save(unit);
        return unit;
    }

    public Unit findUnit(Long id) {
        return unitRepository.findById(id).orElseThrow(
                () -> new UnitException(UnitErrorCode.UNIT_NOT_FOUND));
    }

    @Transactional
    public UnitResponse updateUnit(Long unitId, UnitUpdateRequest dto) {
        Unit unit = findUnit(unitId);

        LocalDate releaseDate = DateTimeConverter.stringToDate(dto.releaseDate());
        LocalDate dueDate = DateTimeConverter.stringToDate(dto.dueDate());

        unit.updateUnit(dto.title(), releaseDate, dueDate);
        return UnitResponse.from(unit);
    }

    @Transactional
    public void deleteAllById(List<Long> unitId) {
        unitRepository.deleteAllById(unitId);
    }

    @Transactional
    public void deleteUnit(Long unitId) {
        unitRepository.deleteById(unitId);
    }

    public Map<Long, Integer> countUnitsByCourseId(List<Long> courseIds) {
        List<Object[]> results = unitRepository.countUnitsByCourseIds(courseIds);

        return results.stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> ((Long) row[1]).intValue()
                ));
    }

    public boolean isOpenUnit(LocalDate releaseDate) {
        return !releaseDate.isAfter(LocalDate.now());
    }

    public List<Unit> findAllByCourseId(Long courseId) {
        return unitRepository.findAllByCourseId(courseId);
    }

    public List<Long> findIdsByCourseId(Long courseId) {
        return unitRepository.findIdByCourseId(courseId);
    }

    public List<Object[]> countAssignmentsByCourseId(Long courseId) {
        return unitRepository.findUnitIdTitleAndAssignmentCount(courseId);
    }

//    public List<UnitProgressResponse> getUnitProgress(Long courseId) {
//        // boolean isSubmitted <판별용> -> false
//        // 1. 강의 id로 모든 단원 조회
//        // 2. 단원마다 모든 과제 조회
//        // 3. 단원 하나의 과제 하나씩 순회 돌기
//            // 3-0. isSubmitted == false
//            // 3-1. score 가져오기 => totalScore 에 더하기
//            // 3-2. Submission 테이블에서 score 찾기
//                // 3-3. 존재하지 않으면 continue
//                // 3-4. 존재하면 newScore 에 더하기 (Member, 등록된 과제로 검색), isSubmitted = true 로 바꾸기
//        // 4. totalScore vs newScore 비교
//            // 4-0. isSubmitted == false 이면 NOT_SUBMITTED
//            // 4-1. totalScore == newScore 이면 PASSED
//            // 4-2. totalScore > newScore 이면
//                // 4-3. newScore != 0이면 PARTIAL
//                // 4-4. newScore == 0이면 FAILED
//    }
}
