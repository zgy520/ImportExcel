package com.zgy.project.ImportExcel.core.utils;

/**
 * 导入类别
 * 即指的是哪一个业务的导入
 */
public class ImportType {
    final String businessType; // 业务类型
    final String filePath;
    final String fileName;
    final String fileExtend;
    public ImportType(String businessType,String filePath, String fileName, String fileExtend){
        this.businessType = businessType;
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileExtend = fileExtend;
    }

    public ImportType(String businessType,String filePath, String fileName){
        this(businessType,filePath,fileName,".xlsx");
    }

    public ImportType(String businessType, String fileName){
        this(businessType,businessType,fileName,".xlsx");
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

    public String getBusinessType() {
        return businessType;
    }
}
