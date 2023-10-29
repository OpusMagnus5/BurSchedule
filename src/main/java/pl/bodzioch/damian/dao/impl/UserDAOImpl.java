package pl.bodzioch.damian.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import pl.bodzioch.damian.dao.UserDAO;
import pl.bodzioch.damian.entity.UserDbEntity;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.mapper.EntityMapper;
import pl.bodzioch.damian.model.UserModel;

import java.util.List;

@Repository
@Slf4j
public class UserDAOImpl implements UserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UserModel getUserByUsername(String username) {
        try {
            UserDbEntity user = entityManager.createQuery(
                            "SELECT user " +
                                    "FROM UserDbEntity user " +
                                    "LEFT JOIN FETCH user.roles " +
                                    "WHERE user.username = :username", UserDbEntity.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return EntityMapper.map(user);
        } catch (NoResultException | NonUniqueResultException ex) {
            log.warn("User with username {} not found", username, ex);
            throw new AppException("user.dao.user.notFound", List.of(username), HttpStatus.BAD_REQUEST, ex);
        }
    }

    @Override
    @Transactional
    public void saveNewUser(UserDbEntity userDbEntity) {
        entityManager.persist(userDbEntity);
    }
}
