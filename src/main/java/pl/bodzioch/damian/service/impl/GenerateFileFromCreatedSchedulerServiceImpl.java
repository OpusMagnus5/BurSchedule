package pl.bodzioch.damian.service.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.model.SchedulerDay;
import pl.bodzioch.damian.model.SchedulerEntry;
import pl.bodzioch.damian.service.GenerateFileFromCreatedSchedulerService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenerateFileFromCreatedSchedulerServiceImpl implements GenerateFileFromCreatedSchedulerService {

    @Override
    public byte[] generateFile(List<SchedulerDay> days) {
        byte[] bomBytes = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};

        List<String> dataRows = getDataRows(days);
        dataRows.add(0, GenerateFileServiceImpl.HEADER_LINE);
        String content = String.join("\n", dataRows);

        return ArrayUtils.addAll(bomBytes, content.getBytes(StandardCharsets.UTF_8));
    }

    private List<String> getDataRows(List<SchedulerDay> days) {
        return days.stream()
                .map(this::mapDayParamsToContentRows)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private List<String> mapDayParamsToContentRows(SchedulerDay dayParams) {
        String email = dayParams.getEmail().orElseThrow(AppException::getGeneralInternalError);
        return dayParams.getEntries().stream()
                .map(record -> mapRecordParamsToContentRow(record, email, dayParams.getDate()))
                .collect(Collectors.toList());
    }

    private String mapRecordParamsToContentRow(SchedulerEntry record, String email, LocalDate date) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        return new StringBuilder()
                .append("\"").append(record.getSubject()).append("\"").append(";")
                .append("\"").append(email).append("\"").append(";")
                .append("\"").append(date.format(dateFormatter)).append("\"").append(";")
                .append("\"").append(record.getStartTime().format(timeFormatter)).append("\"").append(";")
                .append("\"").append(record.getEndTime().format(timeFormatter)).append("\"")
                .toString();
    }
}
