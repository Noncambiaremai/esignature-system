package com.sys.esignature.controller;

import com.sys.esignature.entity.Signature;
import com.sys.esignature.service.SigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

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

//        System.out.println(imageDataUrl);
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

    // UserId还没传进来
    @GetMapping("/selectAllByUserId")
    public List<Map<String, Object>> selectAllByUserId() {
        List<Signature> signatures = sigService.selectAllByUserId();
        List<Map<String, Object>> signaturesWithImage = new ArrayList<>();

        for (Signature signature : signatures) {
            Map<String, Object> signatureMap = new HashMap<>();
            signatureMap.put("id", signature.getId());
            signatureMap.put("sig_id", signature.getSigId());
            signatureMap.put("sig_name", signature.getSigName());
            signatureMap.put("sig_path", signature.getSigPath());
            signatureMap.put("create_time", signature.getCreateTime());

            try {
                // 读取图片内容为字节数组
                byte[] imageBytes = Files.readAllBytes(Paths.get(signature.getSigPath()));
                // 将字节数组转换为 Base64 编码的字符串
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                signatureMap.put("image", "data:image/jpeg;base64," + base64Image);
            } catch (IOException e) {
                // 处理异常，这里可以根据具体情况进行处理
                e.printStackTrace();
            }
            signaturesWithImage.add(signatureMap);
        }
        return signaturesWithImage;
    }

    @GetMapping("/deleteSigBySigId")
    public boolean deleteSigBySigId(Integer sig_id, String sig_path) {
        // 创建 File 对象表示要删除的文件
        File imageToDelete = new File(sig_path);
        // 检查文件是否存在
        if (imageToDelete.exists()) {
            boolean deleted = imageToDelete.delete();
            if (deleted) System.out.println("文件删除成功!");
            else System.out.println("文件删除失败!");
        } else System.out.println("文件不存在，无需删除");

        return sigService.deleteSigBySigId(sig_id);
    }
}
