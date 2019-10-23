package com.zgy.project.ImportExcel.demo.service;

import com.zgy.project.ImportExcel.core.utils.ExcelBase;
import com.zgy.project.ImportExcel.core.utils.ImportType;
import com.zgy.project.ImportExcel.demo.factory.BusinessType;
import com.zgy.project.ImportExcel.demo.factory.ImportTemplateFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class TemplateDownloadService {
    /**
     * 下载业务数据的模板
     * @param request
     * @param importType
     * @return
     */
    public ResponseEntity<Resource> downloadExcelTemplate(HttpServletRequest request, BusinessType businessType, ImportType importType){
        ExcelBase templateExcel = ImportTemplateFactory.getExcelImpl(businessType,importType,null);
        return templateExcel.downloadTemplate(request, importType);
    }
}
