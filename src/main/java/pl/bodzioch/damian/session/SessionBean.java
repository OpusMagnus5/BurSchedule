package pl.bodzioch.damian.session;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import pl.bodzioch.damian.model.SchedulerModel;
import pl.bodzioch.damian.model.UserModel;

import java.util.Optional;

@Getter
@Setter
@Component
@SessionScope
public class SessionBean {

    private SchedulerModel scheduler = null;
    private UserModel user = null;

    public Optional<SchedulerModel> getScheduler() {
        return Optional.ofNullable(scheduler);
    }

    public Optional<UserModel> getUser() {
        return Optional.ofNullable(user);
    }
}
