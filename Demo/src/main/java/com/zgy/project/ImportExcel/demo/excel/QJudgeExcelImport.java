package com.zgy.project.ImportExcel.demo.excel;

import com.alibaba.fastjson.JSONObject;
import com.zgy.project.ImportExcel.core.utils.ExcelImpl;
import org.apache.commons.lang3.StringUtils;

public class QJudgeExcelImport extends ExcelImpl {
    public QJudgeExcelImport(String excelPath) {
        super(excelPath);
    }

    @Override
    public String[] getExampleRowData() {
        String[] example = {"安全培训","灭火器是通过挤压进行使用的","否","灭火器是直接使用的"};
        return example;
    }

    @Override
    public String[] getHeaderTitles() {
        String[] titles = {"培训类型","题干","答案","解析"};
        return titles;
    }

    @Override
    public String[] getHeaderFields() {
        String[] fields = {"trainType","title","answer","analysis"};
        return fields;
    }

    @Override
    public boolean judgeTemplateEffective() {
        return isEffectiveTemplate("培训类型",1);
    }

    @Override
    public boolean validateJsonData(JSONObject jsonObject) {
        // 判断培训类型和题干是否为空，如果为空则不能进行数据的插入
        boolean flag = true;
        StringBuilder sb = new StringBuilder();
        // 判断培训类型是否为空
        boolean isEmptyOfTrainType = jsonObject.containsKey("trainType") && StringUtils.isNotBlank(jsonObject.getString("trainType"));
        // 判断题干是否为空
        boolean isEmptyOfTitle = jsonObject.containsKey("title") && StringUtils.isNotBlank(jsonObject.getString("title"));
        String answerStr = "是,否";
        boolean isEmptyOfAnswer = jsonObject.containsKey("answer") && StringUtils.isNotBlank(jsonObject.getString("answer"))
                && answerStr.contains(jsonObject.getString("answer"));
        /*if (isEmptyOfTrainType && isEmptyOfTitle)
            return true;*/
        if (!isEmptyOfTrainType){
            sb.append("培训类型不能为空;");
            flag = false;
        }
        if (!isEmptyOfTitle){
            sb.append("题干不能为空;");
            flag = false;
        }
        if (!isEmptyOfAnswer){
            sb.append("答案不正确,应该仅仅为是或者否;");
            flag = false;
        }
        if (!flag){
            jsonObject.put(FAIL_REASON,sb.toString());
        }
        return flag;
    }
}
