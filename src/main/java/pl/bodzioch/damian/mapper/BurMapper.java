package pl.bodzioch.damian.mapper;

import pl.bodzioch.damian.dto.bur.AddressDTO;
import pl.bodzioch.damian.dto.bur.ServiceDTO;
import pl.bodzioch.damian.dto.bur.ServiceScheduleDTO;
import pl.bodzioch.damian.model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class BurMapper {

    public static SchedulerModel map(List<ServiceScheduleDTO> burSchedulerEntries) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        Set<Map.Entry<String, List<ServiceScheduleDTO>>> entriesByDay = burSchedulerEntries.stream()
                .collect(Collectors.groupingBy(ServiceScheduleDTO::getData))
                .entrySet();

        Map<LocalDate, List<SchedulerEntry>> mappedEntriesByDay = mapToEntriesModel(entriesByDay);

        List<SchedulerDay> days = mapToDayModel(mappedEntriesByDay);

        return SchedulerModel.builder()
                .days(days)
                .build();
    }

    private static Map<LocalDate, List<SchedulerEntry>> mapToEntriesModel(Set<Map.Entry<String, List<ServiceScheduleDTO>>> entriesByDay) {
        Map<LocalDate, List<SchedulerEntry>> mappedEntriesByDay = new HashMap<>();

        for (Map.Entry<String, List<ServiceScheduleDTO>> entry : entriesByDay) {
            List<SchedulerEntry> entries = entry.getValue().stream()
                    .map(BurMapper::map)
                    .sorted(Comparator.comparing(SchedulerEntry::getStartTime))
                    .toList();
            LocalDate date = ZonedDateTime.parse(entry.getKey()).toLocalDate();
            mappedEntriesByDay.put(date, entries);
        }
        return mappedEntriesByDay;
    }

    private static List<SchedulerDay> mapToDayModel(Map<LocalDate, List<SchedulerEntry>> mappedEntriesByDay) {
        return mappedEntriesByDay.entrySet().stream()
                .map(entry -> SchedulerDay.builder()
                        .date(entry.getKey())
                        .entries(entry.getValue())
                        .build())
                .sorted(Comparator.comparing(SchedulerDay::getDate))
                .toList();
    }

    private static SchedulerEntry map(ServiceScheduleDTO dto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        return SchedulerEntry.builder()
                .subject(dto.getTemat())
                .startTime(LocalTime.parse(dto.getGodzinaRozpoczecia(), formatter))
                .endTime(LocalTime.parse(dto.getGodzinaZakonczenia(), formatter))
                .build();
    }

    public static ServiceModel map(ServiceDTO dto) {
        return ServiceModel.builder()
                .id(dto.getId())
                .status(mapServiceStatus(dto.getStatus().getCode()))
                .number(dto.getNumer())
                .title(dto.getTytul())
                .dateBeginningOfService(ZonedDateTime.parse(dto.getDataRozpoczeciaUslugi()).toLocalDate())
                .dateCompletionOfService(ZonedDateTime.parse(dto.getDataZakonczeniaUslugi()).toLocalDate())
                .numberOfHours(dto.getLiczbaGodzin())
                .serviceProviderId(dto.getDostawcaUslug().getId())
                .serviceProviderName(mapServiceProvider(dto.getDostawcaUslug().getId()))
                .location(dto.getAdres().map(AddressDTO::getNazwaMiejscowosci).orElse(null))
                .build();
    }

    private static ServiceStatus mapServiceStatus(String serviceCode) {
        return Arrays.stream(ServiceStatus.values())
                .filter(e -> serviceCode.equals(e.getCode()))
                .findAny()
                .orElseThrow(IllegalStateException::new);
    }

    private static ServiceProvider mapServiceProvider(Long providerId) {
        return Arrays.stream(ServiceProvider.values())
                .filter(provider -> provider.getId() == providerId)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Service Provider should exists"));
    }
}
