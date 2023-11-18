package pl.bodzioch.damian.session;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import pl.bodzioch.damian.model.SchedulerEntry;
import pl.bodzioch.damian.model.UserModel;

import java.util.List;

@Getter
@Setter
@Component
@SessionScope
public class SessionBean {

    private List<SchedulerEntry> scheduleEntries = null;
    private UserModel user = null;
}
