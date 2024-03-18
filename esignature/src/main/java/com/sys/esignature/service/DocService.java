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
}
