package pl.bodzioch.damian.dao;

import pl.bodzioch.damian.entity.UserDbEntity;
import pl.bodzioch.damian.model.UserModel;

public interface UserDAO {

    UserModel getUserByUsername(String username);
    void saveNewUser(UserDbEntity userDbEntity);
}
