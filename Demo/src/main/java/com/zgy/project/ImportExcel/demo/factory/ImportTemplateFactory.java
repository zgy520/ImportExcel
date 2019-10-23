package com.zgy.project.ImportExcel.demo.factory;


import com.oracle.nio.BufferSecrets;
import com.zgy.project.ImportExcel.core.utils.ExcelBase;
import com.zgy.project.ImportExcel.core.utils.ExcelFileUtils;
import com.zgy.project.ImportExcel.core.utils.ImportType;
import com.zgy.project.ImportExcel.demo.excel.QJudgeExcelImport;
import org.apache.commons.lang3.StringUtils;

public class ImportTemplateFactory {
    /**
     * 根据模板路径获取对应的实现类
     * @param importType
     * @return
     */
    public static ExcelBase getExcelImpl(BusinessType businessType,ImportType importType, String templatePath){
        if (importType == null)
            return null;
        if (StringUtils.isBlank(templatePath))
            templatePath = ExcelFileUtils.getExcelTemplatePath(importType);
        switch (businessType){
            case JUDGE:
                return new QJudgeExcelImport(templatePath);
            default:
                return null;
        }
    }
}
