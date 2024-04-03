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

    public void uploadSigs(String sig_name, String sig_path, long timeStamp) {
        Timestamp timestamp = new Timestamp(timeStamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String create_time = sdf.format(timestamp);
        // userid还未处理
        String user_id = "13169901112";
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO signature (sig_id, sig_name, sig_path, create_time, user_id)\n" +
                "SELECT IFNULL(MAX(id) + 1, 1) AS next_id, :sig_name, :sig_path, :create_time, :user_id\n" +
                "FROM signature");
        Query nativeQuery = entityManager.createNativeQuery(sql.toString());
        nativeQuery.setParameter("sig_name", sig_name);
        nativeQuery.setParameter("sig_path", sig_path);
        nativeQuery.setParameter("create_time", create_time);
        nativeQuery.setParameter("user_id", user_id);
        nativeQuery.executeUpdate();
    }

    public List<Signature> selectAllByUserId() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM signature WHERE is_deleted = 0");
        Query nativeQuery = entityManager.createNativeQuery(sql.toString());
        return nativeQuery.getResultList();
    }
}
