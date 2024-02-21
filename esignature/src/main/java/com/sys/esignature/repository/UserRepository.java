package com.sys.esignature.repository;

import com.sys.esignature.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<User> getUser(User user) {
        System.out.println("dao");
        System.out.println(user.getUserId());
        System.out.println(user.getUserPassword());
        String userId = user.getUserId();
        String userPassword = user.getUserPassword();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM user WHERE user_id = :userId AND user_password = :userPassword");

        Query nativeQuery = entityManager.createNativeQuery(sql.toString());
        nativeQuery.setParameter("userId", userId);
        nativeQuery.setParameter("userPassword", userPassword);

        return nativeQuery.getResultList();
    }

}
