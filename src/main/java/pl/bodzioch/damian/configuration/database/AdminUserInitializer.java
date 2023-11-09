package pl.bodzioch.damian.configuration.database;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.configuration.security.UserRoles;
import pl.bodzioch.damian.dao.UserDAO;
import pl.bodzioch.damian.entity.UserDbEntity;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.mapper.EntityMapper;
import pl.bodzioch.damian.model.UserModel;

import java.util.List;

@Component
public class AdminUserInitializer {

    private final UserDAO userDAO;

    @Autowired
    public AdminUserInitializer(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Value("${admin.username}")
    String adminUsername;
    @Value("${admin.password}")
    String adminPassword;

    @PostConstruct
    public void init() {
        try {
            userDAO.getUserByUsername(adminUsername);
        } catch (AppException ex) {
            UserModel admin = UserModel.builder()
                    .roles(List.of(UserRoles.ADMIN, UserRoles.USER))
                    .password(adminPassword)
                    .username(adminUsername)
                    .build();

            UserDbEntity adminEntity = EntityMapper.map(admin);

            userDAO.saveNewUser(adminEntity);
        }
    }

}
