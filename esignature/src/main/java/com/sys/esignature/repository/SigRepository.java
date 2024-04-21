package com.sys.esignature.repository;

import com.sys.esignature.entity.Signature;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

@Repository
@Transactional
public class SigRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public void uploadSigs(String sig_name, String sig_path, long timeStamp, String userId) {
        Timestamp timestamp = new Timestamp(timeStamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String create_time = sdf.format(timestamp);
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO signature (sig_id, sig_name, sig_path, create_time, user_id)\n" +
                "SELECT IFNULL(MAX(id) + 1, 1) AS next_id, :sig_name, :sig_path, :create_time, :user_id\n" +
                "FROM signature");
        Query nativeQuery = entityManager.createNativeQuery(sql.toString());
        nativeQuery.setParameter("sig_name", sig_name);
        nativeQuery.setParameter("sig_path", sig_path);
        nativeQuery.setParameter("create_time", create_time);
        nativeQuery.setParameter("user_id", userId);
        nativeQuery.executeUpdate();
    }

    public List<Signature> selectAllByUserId(String userId) {
        String sql = "SELECT * FROM signature WHERE user_id = :user_id AND is_deleted = 0";
        List<Signature> signature = entityManager
                .createNativeQuery(sql, Signature.class) // Signature
                .setParameter("user_id", userId)
                .getResultList();
        return signature;
    }

    public boolean deleteSigBySigId(Integer sig_id) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE signature SET is_deleted = 1 WHERE sig_id = :sig_id");
        Query nativeQuery = entityManager.createNativeQuery(sql.toString());
        nativeQuery.setParameter("sig_id", sig_id);
        nativeQuery.executeUpdate();
        return true;
    }
}
