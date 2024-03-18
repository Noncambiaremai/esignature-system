package com.sys.esignature.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Repository
@Transactional
public class DocRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public void uploadFiles(String doc_name, String doc_type, String doc_path, long timeStamp) {
        Timestamp timestamp = new Timestamp(timeStamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String update_time = sdf.format(timestamp);
        // userid还未处理
        String user_id = "13169901112";
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO document (doc_id, doc_name, doc_type, doc_path, update_time, user_id)\n" +
                "SELECT IFNULL(MAX(id) + 1, 1) AS next_id, :doc_name, :doc_type, :doc_path, :update_time, :user_id\n" +
                "FROM document");
        Query nativeQuery = entityManager.createNativeQuery(sql.toString());
        nativeQuery.setParameter("doc_name", doc_name);
        nativeQuery.setParameter("doc_type", doc_type);
        nativeQuery.setParameter("doc_path", doc_path);
        nativeQuery.setParameter("update_time", update_time);
        nativeQuery.setParameter("user_id", user_id);
        nativeQuery.executeUpdate();
    }
}
