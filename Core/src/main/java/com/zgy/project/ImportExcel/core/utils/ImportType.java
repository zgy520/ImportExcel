package com.zgy.project.ImportExcel.core.utils;

/**
 * 导入类别
 * 即指的是哪一个业务的导入
 */
public class ImportType {
    final String filePath;
    final String fileName;
    final String fileExtend;
    ImportType(String filePath, String fileName, String fileExtend){
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileExtend = fileExtend;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileExtend() {
        return fileExtend;
    }
}
