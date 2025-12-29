package snowcode.snowcode.common;

import snowcode.snowcode.common.exception.ValidationException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateTimeConverter {

    public static LocalDate stringToDate(String date) {
        if (!date.matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$")) {
            throw new ValidationException("유효한 날짜가 아닙니다.");
        }
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}