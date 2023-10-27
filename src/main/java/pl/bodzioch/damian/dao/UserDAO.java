package pl.bodzioch.damian.dao;

import pl.bodzioch.damian.model.UserModel;

public interface UserDAO {

    UserModel getUserByUsername(String username);
}
