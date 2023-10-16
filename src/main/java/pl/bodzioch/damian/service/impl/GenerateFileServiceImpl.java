package pl.bodzioch.damian.service.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import pl.bodzioch.damian.model.ScheduleEntry;
import pl.bodzioch.damian.model.SchedulerDayParams;
import pl.bodzioch.damian.service.GenerateFileService;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenerateFileServiceImpl implements GenerateFileService {

    private final String HEADER_LINE = "\"Przedmiot / temat (max 200 znaków)\";\"Prowadzący (adres email)\";\"Termin (w formacie dd-mm-yyyy)\";\"Godzina od (w formacie hh:mm)\";\"Godzina do (w formacie hh:mm)\"";

    @Override
    public byte[] generateFile(List<SchedulerDayParams> params, List<ScheduleEntry> burScheduler) {
        byte[] bomBytes = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};

        Iterator<ScheduleEntry> iterator = burScheduler.iterator();
        List<String> rows = params.stream()
                .map(day -> mapDayParamsToContentRow(day, iterator.next()))
                .collect(Collectors.toList());
        rows.add(0, HEADER_LINE);
        String content = String.join("\n", rows);

        return ArrayUtils.addAll(bomBytes, content.getBytes(StandardCharsets.UTF_8));
    }

    private String mapDayParamsToContentRow(SchedulerDayParams dayParams, ScheduleEntry burScheduleEntry) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        return new StringBuilder()
                .append("\"").append(burScheduleEntry.getSubject()).append("\"").append(";")
                .append("\"").append(dayParams.getEmail()).append("\"").append(";")
                .append("\"").append(dayParams.getDate().format(dateFormatter)).append("\"").append(";")
                .append("\"").append(burScheduleEntry.getStartTime().plusMinutes(dayParams.getTimeDifference()).format(timeFormatter)).append("\"").append(";")
                .append("\"").append(burScheduleEntry.getEndTime().plusMinutes(dayParams.getTimeDifference()).format(timeFormatter)).append("\"")
                .toString();
    }

}
