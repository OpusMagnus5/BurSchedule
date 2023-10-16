package pl.bodzioch.damian.session;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import pl.bodzioch.damian.model.ScheduleEntry;

import java.util.List;

@Getter
@Setter
@Component
@SessionScope
public class SessionBean {

    private List<ScheduleEntry> scheduleEntries = null;
    private Integer numberOfDaysInScheduler;
}
