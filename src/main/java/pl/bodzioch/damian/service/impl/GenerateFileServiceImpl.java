package pl.bodzioch.damian.service.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import pl.bodzioch.damian.model.SchedulerEntry;
import pl.bodzioch.damian.model.SchedulerGenerateDayParams;
import pl.bodzioch.damian.service.GenerateFileService;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class GenerateFileServiceImpl implements GenerateFileService {

    public static final String HEADER_LINE = "\"Przedmiot / temat (max 200 znaków)\";\"Prowadzący (adres email)\";\"Termin (w formacie dd-mm-yyyy)\";\"Godzina od (w formacie hh:mm)\";\"Godzina do (w formacie hh:mm)\"";

    @Override
    public byte[] generateFile(List<SchedulerGenerateDayParams> params, List<SchedulerEntry> burScheduler) {
        byte[] bomBytes = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};

        List<String> dataRows = getDataRows(params, burScheduler);
        dataRows.add(0, HEADER_LINE);
        String content = String.join("\n", dataRows);

        return ArrayUtils.addAll(bomBytes, content.getBytes(StandardCharsets.UTF_8));
    }

    private List<String> getDataRows(List<SchedulerGenerateDayParams> params, List<SchedulerEntry> burScheduler) {
        List<String> dataRows = new ArrayList<>();
        dataRows.add(mapDayParamsToContentRow(params.get(0), burScheduler.get(0)));
        for (int i = 1, j = 0; i < burScheduler.size(); i++) {
            SchedulerEntry schedulerEntry = burScheduler.get(i);
            SchedulerEntry prevSchedulerEntry = burScheduler.get(i - 1);
            if (prevSchedulerEntry.getDate().isBefore(schedulerEntry.getDate())) {
                j++;
            }
            dataRows.add(mapDayParamsToContentRow(params.get(j), schedulerEntry));
        }
        return dataRows;
    }

    private String mapDayParamsToContentRow(SchedulerGenerateDayParams dayParams, SchedulerEntry burSchedulerEntry) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        return new StringBuilder()
                .append("\"").append(burSchedulerEntry.getSubject()).append("\"").append(";")
                .append("\"").append(dayParams.getEmail()).append("\"").append(";")
                .append("\"").append(dayParams.getDate().format(dateFormatter)).append("\"").append(";")
                .append("\"").append(burSchedulerEntry.getStartTime().plusMinutes(dayParams.getTimeDifference()).format(timeFormatter)).append("\"").append(";")
                .append("\"").append(burSchedulerEntry.getEndTime().plusMinutes(dayParams.getTimeDifference()).format(timeFormatter)).append("\"")
                .toString();
    }

}
