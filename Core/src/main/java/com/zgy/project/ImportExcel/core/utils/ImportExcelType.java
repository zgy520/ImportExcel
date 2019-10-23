package com.zgy.project.ImportExcel.core.utils;

/**
 * 模板路径
 */
public enum ImportExcelType {
    TRAIN_PARTICIPANT_IMPORT_TEMPLATE("train_participant_import","参培人员导入模板",".xlsx"),
    TRAIN_QUESTION_IMPORT_FILL("train_question_fill_import","填空题导入模板",".xlsx"),
    TRAIN_QUESTION_IMPORT_SIMPLE("train_question_simple_import","简单题导入模板",".xlsx"),
    TRAIN_QUESTION_IMPORT_SINGLE("train_question_select_import","选择题导入模板",".xlsx"),
    /*TRAIN_QUESTION_IMPORT_MULTIPLE("train_question_multiple_import","多选题导入模板",".xlsx"),*/
    TRAIN_QUESTION_IMPORT_JUDGE("train_question_judge_import","判断题导入模板",".xlsx");

    final String filePath;
    final String fileName;
    final String fileExtend;
    ImportExcelType(String filePath, String fileName, String fileExtend){
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileExtend = fileExtend;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileExtend() {
        return fileExtend;
    }
}
