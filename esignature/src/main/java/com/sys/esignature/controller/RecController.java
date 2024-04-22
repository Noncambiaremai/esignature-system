package com.sys.esignature.controller;

import com.sys.esignature.service.RecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sys.esignature.entity.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rec")
public class RecController {
    @Autowired
    private RecService recService;

    @GetMapping("/selectRecordList")
    public List<Map<String, Object>> selectRecordList(String userId) {
        return recService.selectRecordList(userId);
    }

    @GetMapping("/selectRecordListAll")
    public List<Map<String, Object>> selectRecordListAll() {
        return recService.selectRecordListAll();
    }

}
