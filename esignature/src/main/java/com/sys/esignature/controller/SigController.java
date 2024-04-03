package com.sys.esignature.controller;

import org.apache.commons.io.FileUtils;
import com.sys.esignature.service.SigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

@RestController
@CrossOrigin
@RequestMapping("/api/sig")
public class SigController {
    private static final String UPLOAD_PATH = "C:/Users/lenovo/Desktop/undergraduate_design/images";

    @Autowired
    private SigService sigService;

    @CrossOrigin(origins = "http://localhost:8081")
    @PostMapping("/upload")
    public String handleSigUpload(@RequestParam("imageDataUrl") String imageDataUrl) {

        System.out.println(imageDataUrl);
        if (imageDataUrl.isEmpty()) {
            return "上传失败，请选择签名图片";
        }

        // 得到当前时间戳 格式化时间戳为字符串
        long timeStamp = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String formattedTimeStamp = sdf.format(new Date(timeStamp));

        // yyyyMMddHHmmss
        String sig_name = formattedTimeStamp;
        // path/yyyyMMddHHmmss.png
        String sig_path = UPLOAD_PATH + "/" + formattedTimeStamp + ".png";

        try {
            // 插入数据库
            // user_id 还没写
            // id sig_id sig_name sig_path create_time user_id is_deleted
            sigService.uploadSigs(sig_name, sig_path, timeStamp);

            // 下载图像数据并保存到指定路径
            String[] parts = imageDataUrl.split(",");
            byte[] imageBytes = Base64.getDecoder().decode(parts[1]);
            try (FileOutputStream fos = new FileOutputStream(sig_path)) {
                fos.write(imageBytes);
            }
            return "签名上传成功";
        } catch (IOException e) {
            e.printStackTrace();
            return "签名上传失败";
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
