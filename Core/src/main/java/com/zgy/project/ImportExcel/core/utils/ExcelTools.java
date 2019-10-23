package com.zgy.project.ImportExcel.core.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * excel工具
 */
public class ExcelTools {
    /**
     * 获取json中所有的带有Option的字段的数量
     * @param jsonObject
     * @return
     */
    public static int getOptionCount(JSONObject jsonObject, String option){
        Set<String> keys = jsonObject.keySet();
        int maxOption = 1;
        keys = keys.stream().filter(key->key.contains(option)).collect(Collectors.toSet()); // 筛选所有带有Option的键
        for (String key : keys){
            String digits = key.replaceAll("\\D+","");
            Integer iDigit = Integer.parseInt(digits);
            if (maxOption < iDigit){
                maxOption = iDigit;
            }
        }
        return maxOption;
    }
}
