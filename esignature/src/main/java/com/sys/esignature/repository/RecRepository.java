package com.sys.esignature.repository;

import com.sys.esignature.entity.Record;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class RecRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Map<String, Object>> selectRecordList(String userId) {
        String sql = "SELECT r.sig_time, d.doc_name, s.sig_name\n" +
                "FROM record r\n" +
                "LEFT OUTER JOIN signature s ON r.sig_id = s.sig_id\n" +
                "LEFT OUTER JOIN document d ON r.doc_id = d.doc_id\n" +
                "WHERE r.user_id = :user_id AND r.is_deleted = 0";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("user_id", userId);

        List<Object[]> resultList = query.getResultList();
        List<Map<String, Object>> mappedResultList = new ArrayList<>();

        for (Object[] row : resultList) {
            Map<String, Object> rowMap = new HashMap<>();
            rowMap.put("sig_time", row[0]);
            rowMap.put("doc_name", row[1]);
            rowMap.put("sig_name", row[2]);
            mappedResultList.add(rowMap);
        }

        return mappedResultList;
    }
}
