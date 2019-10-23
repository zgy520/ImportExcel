package com.zgy.project.ImportExcel.core.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public abstract class ExcelImpl implements ExcelBase {
    protected final static Logger LOGGER = LoggerFactory.getLogger(ExcelImpl.class);
    public final static String FAIL_REASON = "failReason";
    public final static String ATTACH_OPTION = "option:";
    private Workbook workbook;
    private Sheet activeSheet; // 当前活跃的工作部
    private int totalRows;  // 行数
    private int totalColumns; // 列数
    private final String excelPath; // excel的所在路径
    private final ImportResult importResult; // 存放文件的导入结果

    public ExcelImpl(final String excelPath){
        this.excelPath = excelPath;
        this.importResult = new ImportResult();
        initWorkbookInfo(); // 初始化相关的变量
    }

    /**
     * 初始化相关的变量
     */
    private void initWorkbookInfo(){
        try {
            if (Files.notExists(Paths.get(excelPath))){
                this.workbook = new SXSSFWorkbook();
            }else {
                this.workbook = WorkbookFactory.create(new File(excelPath));
                setActiveSheet(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String[] getHeaders() {
        String[] headers = new String[this.totalColumns];
        if (totalRows > 0 && totalColumns > 0){
            // 获取第一行（header)的值
            Row headerRow = this.activeSheet.getRow(0);
            for (int i = 0; i < totalColumns; i++){
                headers[i] = headerRow.getCell(i).getStringCellValue();
            }
        }
        LOGGER.info("获取到的标题为:" + String.join(",",headers));
        return headers;
    }

    @Override
    public abstract String[] getHeaderFields();

    /**
     * 判断模板的有效性，用于在每个实现类中实现改方法，并调用isEffectiveTemplate方法
     * @return
     */
    public abstract boolean judgeTemplateEffective();

    /**
     * 对读取到的excel中的数据进行具体的处理，包括以下步骤
     * 1. 将json中的数据转换为对应的实体类
     * 2. 对数据进行验证
     * 3. 将数据插入表中
     * 4. 错误数据提取
     */
   // public abstract DefaultErrorCode handleData(JSONArray jsonArray,String attachData);


    @Override
    public boolean isEffectiveTemplate(String validateStr, Integer headerCount) {
        boolean flag = false;
        if (headerCount == null){
            headerCount = 1;
        }
        // 获取子数组
        String[] headerArray = Arrays.copyOfRange(this.getHeaders(),0,headerCount);
        if (validateStr.equals(String.join(",",headerArray))){
            flag = true;
        }
        if (!flag){
            LOGGER.info("Not correct template, please careful!");
        }
        return flag;
    }

    @Override
    public JSONArray readExcelData() {
        DataFormatter formatter = new DataFormatter();
        JSONArray jsonArray = new JSONArray();
        for (int i = 1; i < totalRows; i++){
            JSONObject jsonObject = new JSONObject();
            Row curRow = activeSheet.getRow(i);
            String[] filedHeaders = this.getHeaderFields();
            for (int col = 0; col < totalColumns; col++){
                if (col < filedHeaders.length){
                    jsonObject.put(filedHeaders[col],formatter.formatCellValue(curRow.getCell(col)));
                }else {
                    jsonObject.put("option:"+(col - filedHeaders.length + 1),formatter.formatCellValue(curRow.getCell(col)));
                }
            }
            // 在插入数组之前进行初步的数据验证
            if (validateJsonData(jsonObject)){
                jsonArray.add(jsonObject);
            }else { // 验证失败的进行记录
                importResult.addValidateFailArray(jsonObject);
            }
        }
        LOGGER.info("共获取到数据的数量为:" + jsonArray.size());
        return jsonArray;
    }

    /**
     * 设置当前活跃的工作簿
     * @param index
     */
    public void setActiveSheet(int index){
        this.activeSheet = this.workbook.getSheetAt(index);
        setTotalCount();
    }
    public void setActiveSheet(String sheetName){
        this.activeSheet = this.workbook.getSheet(sheetName);
        setTotalCount();
    }

    /**
     * 设置行和列的数量
     */
    private void setTotalCount(){
        this.totalRows = this.activeSheet.getLastRowNum() + 1; // 设置总行数
        if (this.totalRows > 0){
            this.totalColumns = this.activeSheet.getRow(0).getLastCellNum();
        }else {
            this.totalColumns = 0;
        }
       LOGGER.info("工获取到行的数量为:"+this.totalRows+",列的数量为:"+this.totalColumns);
    }

    /**
     * 下载模板
     * @param request
     * @param importExcelType
     * @return
     */
    @Override
    public ResponseEntity<Resource> downloadTemplate(HttpServletRequest request, ImportExcelType importExcelType) {
        //创建第一行
        if (this.activeSheet == null){
            this.activeSheet = workbook.createSheet();
        }
        Row row = this.activeSheet.createRow(0);
        int colLen = this.getHeaderTitles().length;
        for (int i = 0; i < colLen; i++){
            Cell cell = row.createCell(i);
            cell.setCellValue(this.getHeaderTitles()[i]);
        }
        if (this.getExampleRowData().length > 0){ // 说明有示例数据，则添加
            createExampleData(colLen,this.getExampleRowData());
        }
        String finalFilePath = ExcelFileUtils.getExcelTemplatePath(importExcelType);

        if (!Files.exists(Paths.get(finalFilePath))){  // 如果不存在模板，则产生
            try {
                Files.createFile(Paths.get(finalFilePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try(FileOutputStream outputStream = new FileOutputStream(finalFilePath)){
                workbook.write(outputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 进行模板的下载
        return ExcelFileUtils.downloadFileByFilePath(finalFilePath,request);
    }

    /**
     * 下载错误数据
     * @param request
     * @param importExcelType  下载错误数据的类型
     * @param fileName  要下载的文件名
     * @return
     */
    @Override
    public ResponseEntity<Resource> downloadErrorData(HttpServletRequest request,ImportExcelType importExcelType,String fileName){
        Path path = Paths.get(ExcelFileUtils.getExcelTempPath().toString(), importExcelType.getFilePath());
        LOGGER.info("获取到的目录为:" + path.toString());
        Path finalPath = Paths.get(path.toString(),fileName);
        return ExcelFileUtils.downloadFileByFilePath(finalPath.toString(),request);
    }
    /**
     * 创建示例数据
     * @param len
     * @param data
     */
    private void createExampleData(int len,String[] data){
        CellStyle style = getExampleRowStyle();
        Row row = this.activeSheet.createRow(1);
        for (int i = 0; i < len; i++){
            Cell cell = row.createCell(i);
            cell.setCellValue(data[i]);
            cell.setCellStyle(style);
        }
    }

    /**
     * 模板中示例数据的样式
     * @return
     */
    private CellStyle getExampleRowStyle(){
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();

        //font.setColor(HSSFColor.RED.index);
        font.setColor(IndexedColors.RED.getIndex());
        style.setFont(font);
        return style;
    }

    /**
     * 判断数据是否通过验证
     * @param jsonObject
     * @return
     */
    public abstract boolean validateJsonData(JSONObject jsonObject);

    @Override
    public ImportResult getImportResult() {
        return importResult;
    }

    /**
     * 将错误数据写入到excel中
     * @param importResult
     * @return
     */
    @Override
    public Path writeErrorDataToExcel(final ImportResult importResult,ImportExcelType importExcelType) {
        LOGGER.info("开始写入验证失败的数据");
        JSONArray validateArray = importResult.getValidateFailArray(); // 验证失败的数组
        JSONArray saveArray = importResult.getSaveFailArray(); // 保存失败的数组
        JSONArray finalArray = new JSONArray();
        finalArray.addAll(validateArray);
        finalArray.addAll(saveArray);

        String errorPath = ExcelFileUtils.getExcelErrorPath(importExcelType);
        Workbook errorWorkBook = new XSSFWorkbook();
        // 设置sheet表的名称
        Sheet errorSheet = errorWorkBook.createSheet("导入失败的数据");
        String[] headers = getHeaderTitles();
        String[] fields = getHeaderFields(); // 获取字段
        // 创建表头
        createHeader(errorSheet,headers);
        // 写入错误数据
        createValidateData(errorSheet,finalArray,fields);
        try {
            FileOutputStream fileOut = new FileOutputStream(errorPath);
            errorWorkBook.write(fileOut);
            fileOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Paths.get(errorPath);
    }

    /**
     * 创建表头；验证失败的数据
     * @param errorSheet
     * @param headers
     */
    private void createHeader(Sheet errorSheet,String[] headers){
        Row headerRow = errorSheet.createRow(0);
        for (int i = 0; i < headers.length; i++){
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }
        // 添加添加失败原因的列
        Cell failCell = headerRow.createCell(headers.length);
        failCell.setCellValue("失败原因");
    }

    /**
     * 生成验证数据
     * @param errorSheet
     * @param finalArray
     * @param fields
     */
    private void createValidateData(Sheet errorSheet, JSONArray finalArray, String[] fields){
        int dataRowIndex = 1;
        for (int i = 0; i < finalArray.size(); i++){
            JSONObject jsonObject = finalArray.getJSONObject(i);
            Row failRow = errorSheet.createRow(dataRowIndex++);
            for (int j = 0; j < fields.length; j++){
                Cell cell = failRow.createCell(j);
                cell.setCellValue(jsonObject.getString(fields[j]));
            }
            int optionIndex = fields.length;
            // 添加原因列
            Cell reasonCell = failRow.createCell(optionIndex);
            reasonCell.setCellValue(jsonObject.getString(FAIL_REASON));

            // 判断是否包含其他option列
            if (jsonObject.containsKey(ATTACH_OPTION+"1")){
                attachOptionCell(jsonObject,failRow,fields.length + 1);
            }

        }
    }

    /**
     * 添加附加数据
     * @param jsonObject
     * @param failRow
     */
    private int attachOptionCell(JSONObject jsonObject, Row failRow, int startIndex){
        // 获取所有的附加数据
        /*Set<String> keys = jsonObject.keySet();
        int maxOption = 1;
        keys = keys.stream().filter(key->key.contains(ATTACH_OPTION)).collect(Collectors.toSet()); // 筛选所有带有Option的键
        for (String key : keys){
            String digits = key.replaceAll("\\D+","");
            Integer iDigit = Integer.parseInt(digits);
            if (maxOption < iDigit){
                maxOption = iDigit;
            }
        }*/
        int maxOption = ExcelTools.getOptionCount(jsonObject,ATTACH_OPTION);

        //LOGGER.info("共有选项:" + maxOption + "个");
        for (int i = 1; i <= maxOption; i++){
            Cell cell = failRow.createCell(startIndex);
            cell.setCellValue(jsonObject.getString(ATTACH_OPTION + i));
            startIndex++;
        }
        return startIndex;
    }
}
