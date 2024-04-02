package com.sys.esignature.controller;

import com.sys.esignature.service.SigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/sig")
public class SigController {
    private static final String UPLOAD_PATH = "C:/Users/lenovo/Desktop/undergraduate_design/images";

    @Autowired
    private SigService sigService;

    @CrossOrigin(origins = "http://localhost:8081")
    @RequestMapping("/upload")
    public String handleSigUpload(@RequestParam("imageData") MultipartFile imageData) {

        System.out.println("111");
        if (imageData.isEmpty()) {
            return "上传失败，请选择签名图片";
        }

        try {
//            // 获取文件名并构建文件保存路径
//            String imageName = file.getOriginalFilename();   //xxx.xxx
//            String[] parts = fileName.split("\\.");
//            String name = parts[0]; // 文件名
//            String doc_type = parts[1]; // 后缀名

            // 得到当前时间戳 格式化时间戳为字符串
            long timeStamp = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String formattedTimeStamp = sdf.format(new Date(timeStamp));

            // xxx_date
            String sig_name = formattedTimeStamp;
            System.out.println("sig name: " + sig_name);
            // path/xxx_date.xxx
            String sig_path = UPLOAD_PATH + "/" + formattedTimeStamp + ".png";
            System.out.println("sig path: " + sig_path);

            // 插入数据库
            // user_id 还没写
            // id sig_id sig_name sig_path create_time user_id is_deleted
            sigService.uploadSigs(sig_name, sig_path, timeStamp);

            // 将文件保存到指定路径
            File dest = new File(sig_path);
            imageData.transferTo(dest);

            return "签名上传成功";
        }
        catch (IOException e) {
            e.printStackTrace();
            return "上传失败：" + e.getMessage();
        }
    }

//    // UserId还没传进来
//    @GetMapping("/selectAllByUserId")
//    public List<Document> selectAllByUserId() {
//        return docService.selectAllByUserId();
//    }
//
//    @GetMapping("/deleteDocByDocId")
//    public boolean deleteDocByDocId(Integer doc_id) {
//        return docService.deleteDocByDocId(doc_id);
//    }
}
