package com.sys.esignature.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository
public class DocRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public void uploadFiles(String doc_name, String doc_type, String doc_path, long timeStamp) {
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println(doc_name);
        System.out.println(doc_type);
        System.out.println(doc_path);
        System.out.println(timeStamp);
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@");


        StringBuilder sql = new StringBuilder();
        sql.append(
                "SELECT * FROM user " +
                        "WHERE user_id = :userId AND user_password = :userPassword");

        Query nativeQuery = entityManager.createNativeQuery(sql.toString());
        nativeQuery.setParameter("userId", userId);
        nativeQuery.setParameter("userPassword", userPassword);

        INSERT INTO document (doc_name, doc_type, doc_path, update_time, doc_status, download_count, user_id, is_deleted)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?);

    }
}
