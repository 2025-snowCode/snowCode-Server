package snowcode.snowcode.unit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.common.DateTimeConverter;
import snowcode.snowcode.course.domain.Course;
import snowcode.snowcode.unit.domain.Unit;
import snowcode.snowcode.unit.dto.UnitCountListResponse;
import snowcode.snowcode.unit.dto.UnitListResponse;
import snowcode.snowcode.unit.dto.UnitRequest;
import snowcode.snowcode.unit.dto.UnitResponse;
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
    public UnitResponse createUnit(Course course, UnitRequest dto) {
        LocalDate releaseDate = DateTimeConverter.stringToDate(dto.releaseDate());
        LocalDate dueDate = DateTimeConverter.stringToDate(dto.dueDate());

        Unit unit = Unit.createUnit(dto.title(), releaseDate, dueDate, course);
        unitRepository.save(unit);
        return UnitResponse.from(unit);
    }

    public Unit findUnit(Long id) {
        return unitRepository.findById(id).orElseThrow(
                () -> new UnitException(UnitErrorCode.UNIT_NOT_FOUND));
    }

    public UnitResponse findById(Long id) {
        return UnitResponse.from(findUnit(id));
    }

    public UnitCountListResponse findAllUnit() {
        List<UnitListResponse> list = unitRepository.findAll().stream()
                .map(UnitListResponse::from)
                .toList();
        return new UnitCountListResponse(list.size(), list);
    }

    @Transactional
    public UnitResponse updateUnit(Long unitId, UnitRequest dto) {
        Unit unit = findUnit(unitId);

        LocalDate releaseDate = DateTimeConverter.stringToDate(dto.releaseDate());
        LocalDate dueDate = DateTimeConverter.stringToDate(dto.dueDate());

        unit.updateUnit(dto.title(), releaseDate, dueDate);
        return UnitResponse.from(unit);
    }

    @Transactional
    public void deleteUnit(Long unitId) {
        Unit unit = findUnit(unitId);
        unitRepository.delete(unit);
    }

    @Transactional
    public void deleteUnitWithCourseId(Long courseId) {
        unitRepository.deleteByCourseId(courseId);
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
}
