package org.fly.test.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ExpenseRecord {

    private String id;
    @ExcelProperty("性名")
    private String name;
    @ExcelProperty("性名拼音")   //假设 name和nameCode一样，视为注册的数据已经存在了
    private String nameCode;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelProperty("日期")
    private String date;
    @ExcelProperty("买菜")
    private String grocery;
    @ExcelProperty("水果")
    private String fruit;
    @ExcelProperty("买米")
    private String rice;
    @ExcelProperty("衣服")
    private String clothing;
    @ExcelProperty("随礼")
    private String gift;
    @ExcelProperty("崽崽")
    private String zaizai;
    @ExcelProperty("交通")
    private String transportation;
}