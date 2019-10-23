package com.zgy.project.ImportExcel.core.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 导入结果
 */
public class ImportResult {
    private JSONArray successArray;  // 导入成功的数组
    private JSONArray validateFailArray;  // 验证失败数组
    private JSONArray saveFailArray;  // 保存失败数组

    public ImportResult(){
        this.successArray = new JSONArray();
        this.validateFailArray = new JSONArray();
        this.saveFailArray = new JSONArray();
    }

    /**
     * 添加保存成功的数组
     * @param jsonObject
     */
    public void addSuccessArray(JSONObject jsonObject){
        this.successArray.add(jsonObject);
    }

    /**
     * 添加验证失败的数组
     * @param jsonObject
     */
    public void addValidateFailArray(JSONObject jsonObject){
        this.validateFailArray.add(jsonObject);
    }

    /**
     * 添加保存失败的数组
     * @param jsonObject
     */
    public void addSaveFailArray(JSONObject jsonObject){
        this.saveFailArray.add(jsonObject);
    }

    /**
     * 将保存过程中的json数组添加到失败数组列表中
     * @param jsonArray
     */
    public void addSaveFailArray(JSONArray jsonArray){
        this.saveFailArray.addAll(jsonArray);
    }

    public JSONArray getSuccessArray() {
        return successArray;
    }

    public JSONArray getValidateFailArray() {
        return validateFailArray;
    }

    public JSONArray getSaveFailArray() {
        return saveFailArray;
    }
}
