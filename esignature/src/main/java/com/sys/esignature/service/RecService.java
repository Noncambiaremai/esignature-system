package com.sys.esignature.service;

import com.sys.esignature.entity.Record;
import com.sys.esignature.repository.RecRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RecService {
    @Autowired
    private RecRepository recRepository;

    public List<Map<String, Object>> selectRecordList() {
        return recRepository.selectRecordList();
    }
}
