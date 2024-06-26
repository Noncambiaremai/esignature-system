package com.sys.esignature.repository;

import com.sys.esignature.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<User> getUser(User user) {
//        System.out.println("dao");
//        System.out.println(user.getUserId());
//        System.out.println(user.getUserPassword());
        String userId = user.getUserId();
        String userPassword = user.getUserPassword();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM user WHERE user_id = :userId AND user_password = :userPassword");

        Query nativeQuery = entityManager.createNativeQuery(sql.toString());
        nativeQuery.setParameter("userId", userId);
        nativeQuery.setParameter("userPassword", userPassword);

        return nativeQuery.getResultList();
    }

    public boolean isAccountExists(String userId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM user WHERE user_id = :user_id");
        Query nativeQuery = entityManager.createNativeQuery(sql.toString());
        nativeQuery.setParameter("user_id", userId);
        return !nativeQuery.getResultList().isEmpty();
    }

    public List<User> login(String userId, String userPassword) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM user WHERE user_id = :userId AND user_password = :userPassword");

        Query nativeQuery = entityManager.createNativeQuery(sql.toString());
        nativeQuery.setParameter("userId", userId);
        nativeQuery.setParameter("userPassword", userPassword);

        return nativeQuery.getResultList();
    }

    public List<User> selectAll() {
        String sql = "SELECT * FROM user WHERE role_id = 0 AND is_deleted = 0";
        List<User> documents = entityManager
                .createNativeQuery(sql, User.class) // 将查询结果映射为Document对象
                .getResultList();
        return documents;
    }
}
