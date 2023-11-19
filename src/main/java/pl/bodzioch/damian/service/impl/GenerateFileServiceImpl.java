package pl.bodzioch.damian.service.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.model.SchedulerDay;
import pl.bodzioch.damian.model.SchedulerEntry;
import pl.bodzioch.damian.model.ModifiedSchedulerFromServiceParams;
import pl.bodzioch.damian.model.SchedulerModel;
import pl.bodzioch.damian.service.GenerateFileService;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class GenerateFileServiceImpl implements GenerateFileService {

    public static final String HEADER_LINE = "\"Przedmiot / temat (max 200 znaków)\";\"Prowadzący (adres email)\";\"Termin (w formacie dd-mm-yyyy)\";\"Godzina od (w formacie hh:mm)\";\"Godzina do (w formacie hh:mm)\"";

    @Override
    public byte[] generateFile(List<ModifiedSchedulerFromServiceParams> params, SchedulerModel scheduler) {
        byte[] bomBytes = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};

        List<String> dataRows = getDataRows(params, scheduler);
        dataRows.add(0, HEADER_LINE);
        String content = String.join("\n", dataRows);

        return ArrayUtils.addAll(bomBytes, content.getBytes(StandardCharsets.UTF_8));
    }

    private List<String> getDataRows(List<ModifiedSchedulerFromServiceParams> params, SchedulerModel scheduler) {
        List<String> dataRows = new ArrayList<>();
        List<SchedulerDay> days = scheduler.getDays();
        if (params.size() != days.size()) {
            throw AppException.getGeneralInternalError();
        }
        Iterator<SchedulerDay> daysIterator = days.iterator();
        Iterator<ModifiedSchedulerFromServiceParams> paramsIterator = params.iterator();
        while (daysIterator.hasNext() && paramsIterator.hasNext()) {
            List<String> contentRows = daysIterator.next().getEntries().stream()
                    .map(entry -> mapToContentRow(paramsIterator.next(), entry))
                    .toList();
            dataRows.addAll(contentRows);
        }
        return dataRows;
    }

    private String mapToContentRow(ModifiedSchedulerFromServiceParams dayParams, SchedulerEntry burSchedulerEntry) {
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
