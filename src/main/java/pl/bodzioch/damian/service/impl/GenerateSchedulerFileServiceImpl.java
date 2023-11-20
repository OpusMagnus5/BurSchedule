package pl.bodzioch.damian.service.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.model.ModifiedSchedulerFromServiceParams;
import pl.bodzioch.damian.model.SchedulerDay;
import pl.bodzioch.damian.model.SchedulerEntry;
import pl.bodzioch.damian.model.SchedulerModel;
import pl.bodzioch.damian.service.GenerateSchedulerFileService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenerateSchedulerFileServiceImpl implements GenerateSchedulerFileService {

    private static final String HEADER_LINE = "\"Przedmiot / temat (max 200 znaków)\";\"Prowadzący (adres email)\";\"Termin (w formacie dd-mm-yyyy)\";\"Godzina od (w formacie hh:mm)\";\"Godzina do (w formacie hh:mm)\"";

    @Override
    public byte[] generateFile(List<SchedulerDay> days) {
        byte[] bomBytes = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};

        List<String> dataRows = getDataRows(days);
        dataRows.add(0, HEADER_LINE);
        String content = String.join("\n", dataRows);

        return ArrayUtils.addAll(bomBytes, content.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public byte[] generateFile(List<ModifiedSchedulerFromServiceParams> params, SchedulerModel scheduler) {
        byte[] bomBytes = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};

        setNewSchedulerParams(params, scheduler);
        List<String> dataRows = getDataRows(scheduler.getDays());
        dataRows.add(0, HEADER_LINE);
        String content = String.join("\n", dataRows);

        return ArrayUtils.addAll(bomBytes, content.getBytes(StandardCharsets.UTF_8));
    }

    private void setNewSchedulerParams(List<ModifiedSchedulerFromServiceParams> params, SchedulerModel scheduler) {
        Iterator<ModifiedSchedulerFromServiceParams> paramsIterator = params.iterator();
        for (SchedulerDay day : scheduler.getDays()) {
            ModifiedSchedulerFromServiceParams dayParams = paramsIterator.next();
            day.setDate(dayParams.getDate());
            day.setEmail(dayParams.getEmail());
            day.getEntries().stream()
                    .peek(entry -> entry.setStartTime(entry.getStartTime().plusMinutes(dayParams.getTimeDifference())))
                    .forEach(entry -> entry.setEndTime(entry.getEndTime().plusMinutes(dayParams.getTimeDifference())));
        }
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
