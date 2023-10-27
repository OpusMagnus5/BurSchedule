package pl.bodzioch.damian.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import pl.bodzioch.damian.dao.UserDAO;
import pl.bodzioch.damian.entity.UserDbEntity;
import pl.bodzioch.damian.mapper.EntityMapper;
import pl.bodzioch.damian.model.UserModel;

@Repository
@Slf4j
public class UserDAOImpl implements UserDAO {

    @PersistenceContext
    private EntityManager entityManager;

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
            throw new NoResultException("user.dao.user.notFound");
        } //TODO dodać obsługe błedu w kontrolerze
    }
}
