package pl.bodzioch.damian.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.bodzioch.damian.dao.UserDAO;
import pl.bodzioch.damian.entity.UserDbEntity;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.mapper.EntityMapper;
import pl.bodzioch.damian.model.UserModel;
import pl.bodzioch.damian.service.UserService;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    @Override
    public void createUser(UserModel userModel) {
        UserModel existingUser = null;
        String username = userModel.getUsername();
        try {
            existingUser = userDAO.getUserByUsername(username);
        } catch (AppException ex) {
        }

        if (existingUser != null) {
            throw new AppException("create.user.alreadyExist", List.of(username), HttpStatus.BAD_REQUEST);
        }

        UserDbEntity userEntity = EntityMapper.map(userModel);
        userDAO.saveNewUser(userEntity);
    }

    @Override
    public UserModel getUser(String username) {
        return userDAO.getUserByUsername(username);
    }
}
