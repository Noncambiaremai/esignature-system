package com.sys.esignature.controller;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.layout.element.Image;
import com.sys.esignature.entity.Document;
//import com.itextpdf.layout.Document as document;
import com.sys.esignature.entity.User;
import com.sys.esignature.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
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

        if (file.isEmpty()) { return "上传失败，请选择文件"; }

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

    @GetMapping("/selectFileByDocPath")
    public Map<String, Object> selectFileByDocPath(@RequestParam("filePath") String filePath) {
        System.out.println(filePath);
        Map<String, Object> FileMap = new HashMap<>();
        try {
            // 读取文件内容为字节数组
            byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
            // 将字节数组转换为 Base64 编码的字符串
            String base64File = Base64.getEncoder().encodeToString(fileBytes);
            FileMap.put("file", base64File);
            System.out.println("data:application/pdf;base64," + base64File);
        }
        catch (IOException e) { e.printStackTrace(); }
        return FileMap;
    }

    @GetMapping("/deleteDocByDocId")
    public boolean deleteDocByDocId(Integer doc_id, String doc_path) {
        // 创建 File 对象表示要删除的文件
        File fileToDelete = new File(doc_path);
        // 检查文件是否存在
        if (fileToDelete.exists()) {
            boolean deleted = fileToDelete.delete();
        }

        return docService.deleteDocByDocId(doc_id);
    }

    @PostMapping("/addSigToFile")
    public String addSigToFile(
            @RequestParam("imageDataUrl") String imageDataUrl,
            @RequestParam("positionX") float positionX,
            @RequestParam("positionY") float positionY,
            @RequestParam("width") float width,
            @RequestParam("height") float height,
            @RequestParam("pageNo") int pageNo,
            @RequestParam("pdfID") int doc_id,
            @RequestParam("sigID") int sigID,
            @RequestParam("pdfName") String pdfName) {

        // 输入PDF文件路径
        String inputPdfPath = "C:/Users/lenovo/Desktop/undergraduate_design/files/"
                + pdfName + ".pdf";
        // 输出PDF文件路径
        String outputPdfPath = "C:/Users/lenovo/Desktop/undergraduate_design/files/"
                + "temp.pdf";

        try {
            String[] parts = imageDataUrl.split(",");
            byte[] imageBytes = Base64.getDecoder().decode(parts[1]);
            ImageData imageData = ImageDataFactory.create(imageBytes);

            PdfReader reader = new PdfReader(inputPdfPath);

            // 检查是否成功打开 PDF 文件
            if (reader.isOpenedWithFullPermission()) {
                // 如果成功打开，则执行后续操作
                PdfWriter writer = new PdfWriter(outputPdfPath);
                PdfDocument pdfDocument = new PdfDocument(reader, writer);

                PdfPage page = pdfDocument.getPage(pageNo);
                PdfCanvas pdfCanvas = new PdfCanvas(page);

                Rectangle rect = new Rectangle(positionX, positionY, width, height);
                pdfCanvas.addImageFittedIntoRectangle(imageData, rect, true);
                pdfDocument.close();
                reader.close();

                // 删除原始 PDF 文件
                Files.deleteIfExists(Paths.get(inputPdfPath));
                // 重命名临时文件为原始文件的名称
                Files.move(Paths.get(outputPdfPath), Paths.get(inputPdfPath));

                // 更新数据库
                long timeStamp = System.currentTimeMillis();
                String user_id = "13169901112";
                docService.addSigToFile(doc_id, sigID, user_id, timeStamp);

                return "签名成功添加入文件";
            } else {
                // 如果无法完全打开，返回错误消息
                return "无法打开 PDF 文件";
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "签名未能添加入文件";
        }
    }

    @GetMapping("/downloadDoc")
    @ResponseBody
    public ResponseEntity<byte[]> downloadDoc(@RequestParam String doc_name) throws IOException {
        // 根据文件路径创建 File 对象
        File file = new File(UPLOAD_PATH + "/" + doc_name + ".pdf");
        System.out.println(UPLOAD_PATH + "/" + doc_name + ".pdf");

        // 检查文件是否存在
        if (!file.exists() || !file.isFile()) {
            return ResponseEntity.notFound().build(); // 如果文件不存在，返回 404
        }

        // 读取文件内容
        Path path = Paths.get(UPLOAD_PATH + "/" + doc_name + ".pdf");
        byte[] fileBytes = Files.readAllBytes(path);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header("Content-Disposition", "attachment; filename=\"" + doc_name + "\"")
                .body(fileBytes);
    }
}
