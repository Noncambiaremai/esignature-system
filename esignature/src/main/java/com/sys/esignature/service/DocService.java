package com.sys.esignature.service;

import com.sys.esignature.entity.Document;
import com.sys.esignature.repository.DocRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocService {
    @Autowired
    private DocRepository docRepository;

    public void uploadFiles(String doc_name, String doc_type, String doc_path, long timeStamp) {
        docRepository.uploadFiles(doc_name, doc_type, doc_path, timeStamp);
    }

    public List<Document> selectAllByUserId() {
        return docRepository.selectAllByUserId();
    }

    public boolean deleteDocByDocId(Integer doc_id) {
        return docRepository.deleteDocByDocId(doc_id);
    }

    public void addSigToFile(int doc_id, int sigID, String user_id, long timeStamp) {
        docRepository.addSigToFile(doc_id, sigID, user_id, timeStamp);
    }
}
