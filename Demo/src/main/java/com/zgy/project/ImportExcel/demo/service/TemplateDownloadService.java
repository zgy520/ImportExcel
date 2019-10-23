package com.zgy.project.ImportExcel.demo.service;

import com.zgy.project.ImportExcel.core.utils.ExcelBase;
import com.zgy.project.ImportExcel.core.utils.ImportType;
import com.zgy.project.ImportExcel.demo.factory.ImportTemplateFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class TemplateDownloadService {
    public ResponseEntity<Resource> downloadTrainPersonalTemplate(HttpServletRequest request, ImportType importType){
        ExcelBase templateExcel = ImportTemplateFactory.getExcelImpl(importType,null);
        return templateExcel.downloadTemplate(request, importType);
    }
}
