package somonitores.adapter;

import jakarta.json.bind.adapter.JsonbAdapter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateTimeAdapter implements JsonbAdapter<LocalDateTime, String> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public String adaptToJson(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(formatter) : "";
    }

    @Override
    public LocalDateTime adaptFromJson(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateTimeStr, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format for fechamento: " + dateTimeStr);
        }
    }

}
