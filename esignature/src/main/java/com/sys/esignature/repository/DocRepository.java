package com.sys.esignature.repository;

import com.sys.esignature.entity.Document;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
@Transactional
public class DocRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public void uploadFiles(String doc_name, String doc_type, String doc_path, long timeStamp, String userId) {
        Timestamp timestamp = new Timestamp(timeStamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String update_time = sdf.format(timestamp);
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO document (doc_id, doc_name, doc_type, doc_path, update_time, user_id)\n" +
                "SELECT IFNULL(MAX(id) + 1, 1) AS next_id, :doc_name, :doc_type, :doc_path, :update_time, :user_id\n" +
                "FROM document");
        Query nativeQuery = entityManager.createNativeQuery(sql.toString());
        nativeQuery.setParameter("doc_name", doc_name);
        nativeQuery.setParameter("doc_type", doc_type);
        nativeQuery.setParameter("doc_path", doc_path);
        nativeQuery.setParameter("update_time", update_time);
        nativeQuery.setParameter("user_id", userId);
        nativeQuery.executeUpdate();
    }

    public List<Document> selectAllByUserId(String userId) {
        String sql = "SELECT * FROM document WHERE user_id = :user_id AND is_deleted = 0";
        List<Document> documents = entityManager
                .createNativeQuery(sql, Document.class) // 将查询结果映射为Document对象
                .setParameter("user_id", userId)
                .getResultList();
        return documents;
    }

    public boolean deleteDocByDocId(Integer doc_id) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE document SET is_deleted = 1 WHERE doc_id = :doc_id");
        Query nativeQuery = entityManager.createNativeQuery(sql.toString());
        nativeQuery.setParameter("doc_id", doc_id);
        nativeQuery.executeUpdate();
        return true;
    }

    public void addSigToFile(int doc_id, int sigID, String user_id, long timeStamp) {
        Timestamp timestamp = new Timestamp(timeStamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sig_time = sdf.format(timestamp);

        // 创建更新文档状态的查询
        StringBuilder updateSql = new StringBuilder();
        updateSql.append("UPDATE document SET doc_status = 1 WHERE doc_id = :doc_id");
        Query updateQuery = entityManager.createNativeQuery(updateSql.toString());
        updateQuery.setParameter("doc_id", doc_id);
        updateQuery.executeUpdate();

        // 创建插入记录的查询
        StringBuilder insertSql = new StringBuilder();
        insertSql.append("INSERT INTO record (doc_id, sig_id, user_id, sig_time) ");
        insertSql.append("VALUES (:doc_id, :sig_id, :user_id, :sig_time)");
        Query insertQuery = entityManager.createNativeQuery(insertSql.toString());
        insertQuery.setParameter("doc_id", doc_id);
        insertQuery.setParameter("sig_id", sigID);
        insertQuery.setParameter("user_id", user_id);
        insertQuery.setParameter("sig_time", sig_time);
        insertQuery.executeUpdate();
    }
}
