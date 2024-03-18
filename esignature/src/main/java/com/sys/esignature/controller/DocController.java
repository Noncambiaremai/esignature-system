package com.sys.esignature.controller;

import com.sys.esignature.entity.User;
import com.sys.esignature.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/doc")
public class DocController {
    private static final String UPLOAD_PATH = "C:/Users/lenovo/Desktop/undergraduate_design/files";

    @Autowired
    private DocService docService;

//    @PostMapping("/upload")
//    public List<User> getUser(@RequestBody User user) {
//        return userService.getUser(user);
//    }

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
            // id自增 doc_id = id
            // doc_name doc_type doc_path update_time
            // doc_status默认0 download_count默认0
            // user_id默认0 is_deleted默认0
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

}
