package com.zgy.project.ImportExcel.core.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Path;

public interface ExcelBase {
    /**
     * 获取excel中的表头
     * @return
     */
    String[] getHeaders();

    /**
     * 设置示例数据
     * @return
     */
    String[] getExampleRowData();

    /**
     * 获取模板的表头信息
     * @return
     */
    String[] getHeaderTitles();

    /**
     * 下载模板
     */
    ResponseEntity<Resource> downloadTemplate(HttpServletRequest request,ImportType importType);

    /**
     *
     * 下载导入过程中错误的数据
     * @param request
     * @param importType
     * @param fileName
     * @return
     */
    ResponseEntity<Resource> downloadErrorData(HttpServletRequest request, ImportType importType, String fileName);

    /**
     * 获取表头中的每列所对应的表中的字段
     * @return
     */
    String[] getHeaderFields();

    /**
     * 调用isEffectiveTemplate方法
     * @return
     */
    boolean judgeTemplateEffective();
    /**
     * 是否是有效模板
     * @param validateStr  待验证的数据,需要用逗号隔开
     * @param headerCount 需要验证的表头的前几列,默认为1
     * @return
     */
    boolean isEffectiveTemplate(String validateStr, Integer headerCount);

    /**
     * 读取excel数据
     * @return
     */
    JSONArray readExcelData();

    /**
     * 验证json数据
     * @param jsonObject
     * @return
     */
    boolean validateJsonData(JSONObject jsonObject);

    /**
     * 获取对应的导入结果
     * @return
     */
    ImportResult getImportResult();

    /**
     * 将验证失败的数据写入excel中
     * 需提供下载的url
     * @param importResult
     * @param importType
     */
    Path writeErrorDataToExcel(final ImportResult importResult, ImportType importType);
}
