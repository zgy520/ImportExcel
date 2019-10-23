package com.zgy.project.ImportExcel.demo.controller;

import com.zgy.project.ImportExcel.core.utils.ImportType;
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
    public ResponseEntity<Resource> downloadTrainPersonalTemplate(HttpServletRequest request, String importExcelType){
        // 下载参培人员的导入模板
        ImportType importType = new ImportType(importExcelType,"train_question_judge_import","判断题导入模板",".xlsx");
        return templateDownloadService.downloadTrainPersonalTemplate(request, importType);
    }
}
