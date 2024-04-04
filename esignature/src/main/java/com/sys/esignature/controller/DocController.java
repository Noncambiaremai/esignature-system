package com.sys.esignature.controller;

import com.sys.esignature.entity.Document;
import com.sys.esignature.entity.User;
import com.sys.esignature.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/doc")
public class DocController {
    private static final String UPLOAD_PATH = "C:/Users/lenovo/Desktop/undergraduate_design/files";

    @Autowired
    private DocService docService;

    @CrossOrigin(origins = "http://localhost:8081")
    @RequestMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return "上传失败，请选择文件";
        }

        try {
            // 获取文件名并构建文件保存路径
            String fileName = file.getOriginalFilename();   //xxx.xxx
            String[] parts = fileName.split("\\.");
            String name = parts[0]; // 文件名
            String doc_type = parts[1]; // 后缀名

            // 得到当前时间戳 格式化时间戳为字符串
            long timeStamp = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String formattedTimeStamp = sdf.format(new Date(timeStamp));

            // xxx_date
            String doc_name = String.join("_", name, formattedTimeStamp);
            // path/xxx_date.xxx
            String doc_path = UPLOAD_PATH + "/" + doc_name + "." + doc_type;

            // 插入数据库
            // user_id 还没写
            docService.uploadFiles(doc_name, doc_type, doc_path, timeStamp);

            // 将文件保存到指定路径
            File dest = new File(doc_path);
            file.transferTo(dest);

            return "文件上传成功";
        }
        catch (IOException e) {
            e.printStackTrace();
            return "上传失败：" + e.getMessage();
        }
    }

    // UserId还没传进来
    @GetMapping("/selectAllByUserId")
    public List<Map<String, Object>> selectAllByUserId() {
        List<Document> documents = docService.selectAllByUserId();
        List<Map<String, Object>> documentsWithImage = new ArrayList<>();

        for (Document document : documents) {
            Map<String, Object> documentMap = new HashMap<>();
            documentMap.put("id", document.getId());
            documentMap.put("doc_id", document.getDocId());
            documentMap.put("doc_name", document.getDocName());
            documentMap.put("doc_type", document.getDocType());
            documentMap.put("doc_path", document.getDocPath());
            documentMap.put("update_time", document.getUpdateTime());
            documentMap.put("doc_status", document.getDocStatus());
            documentMap.put("download_count", document.getDownloadCount());
//
            documentsWithImage.add(documentMap);
        }
        System.out.println(documentsWithImage);
        return documentsWithImage;
    }

    @GetMapping("/deleteDocByDocId")
    public boolean deleteDocByDocId(Integer doc_id, String doc_path) {
        // 创建 File 对象表示要删除的文件
        File fileToDelete = new File(doc_path);
        // 检查文件是否存在
        if (fileToDelete.exists()) {
            boolean deleted = fileToDelete.delete();
            if (deleted) System.out.println("文件删除成功!");
            else System.out.println("文件删除失败!");
        } else System.out.println("文件不存在，无需删除");

        return docService.deleteDocByDocId(doc_id);
    }
}
