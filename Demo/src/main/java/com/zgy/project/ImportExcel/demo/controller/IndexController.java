package com.zgy.project.ImportExcel.demo.controller;

import com.zgy.project.ImportExcel.core.utils.ImportType;
import com.zgy.project.ImportExcel.demo.factory.BusinessType;
import com.zgy.project.ImportExcel.demo.service.TemplateDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@RequestMapping(value = "/demo")
@Controller
public class IndexController {
    @Autowired
    private TemplateDownloadService templateDownloadService;

    @RequestMapping(value = "index")
    public String index(){
        return "index";
    }

    @RequestMapping(value = "download")
    public ResponseEntity<Resource> downloadTemplate(HttpServletRequest request, BusinessType businessType,String templateName){
        // 下载判断题的导入模板
        ImportType importType = new ImportType(businessType.name(),templateName);
        return templateDownloadService.downloadExcelTemplate(request, businessType,importType);
    }
}
