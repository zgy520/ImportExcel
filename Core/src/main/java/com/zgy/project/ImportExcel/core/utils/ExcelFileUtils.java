package com.zgy.project.ImportExcel.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

/**
 * excel文件的辅助类
 */
public class ExcelFileUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelFileUtils.class);
    private static final Path excelTempPath;

    static {
        excelTempPath = Paths.get("","import").toAbsolutePath().normalize();
        initFileCreate(excelTempPath);
    }

    private static void initFileCreate(Path filePath){
        if (Files.notExists(filePath)){
            try {
                Files.createDirectory(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Path getExcelTempPath() {
        return excelTempPath;
    }

    /**
     * 将上传的excel临时保存
     * @param file
     * @return
     */
    public static String getExcelPath(MultipartFile file){
        String filePath = "";
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(excelTempPath.toString(), Timestamp.from(Instant.now()).getTime() + file.getOriginalFilename());
            filePath = path.normalize().toString(); // 获取文件路径
            Files.write(path,bytes);
        }catch (IOException ex){
            ex.printStackTrace();
        }
        LOGGER.info("上传的excel文件的路径为:" + filePath);
        return filePath;
    }

    /**
     * 获取excel的模板路径
     * @return
     */
    public static String getExcelTemplatePath(ImportExcelType importExcelType){
        String finalTemplatePath = "";
        Path path = Paths.get(excelTempPath.toString(), importExcelType.getFilePath());
        if (!Files.exists(path)){
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Path finalPath = Paths.get(path.toString(), importExcelType.getFileName() + importExcelType.getFileExtend());
        finalTemplatePath = finalPath.normalize().toString();
        return finalTemplatePath;
    }

    /**
     * 获取excel错误模板的路径
     * @param importExcelType
     * @return
     */
    public static String getExcelErrorPath(ImportExcelType importExcelType){
        String finalErrorPath = "";
        Path path = Paths.get(excelTempPath.toString(), importExcelType.getFilePath());
        if (!Files.exists(path)){
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Date now = new Date();
        Path finalPath = Paths.get(path.toString(), now.getTime() + importExcelType.getFileName() + importExcelType.getFileExtend());
        finalErrorPath = finalPath.normalize().toString();
        return finalErrorPath;
    }
    /**
     * 模板下载
     * @param fileName
     * @param request
     * @return
     */
    public static ResponseEntity<Resource> downloadFileByFilePath(String fileName, HttpServletRequest request){
        // Load file as Resource
        Resource resource = loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            LOGGER.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + new String(resource.getFilename().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1))
                .body(resource);
    }

    private static Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = Paths.get(fileName);
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                LOGGER.info("File not found " + fileName);
                //throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            //throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
        return null;
    }
}
