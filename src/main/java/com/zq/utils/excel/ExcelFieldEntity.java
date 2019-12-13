package com.zq.utils.excel;

import java.lang.reflect.Field;

/**
 * @author : zhangqian
 * @date : 2019-12-12 13:32
 */
public class ExcelFieldEntity implements Comparable<ExcelFieldEntity> {
    private Field field;

    private ExcelField excelField;

    public ExcelFieldEntity() {
    }

    public ExcelFieldEntity(Field field, ExcelField excelField) {

        this.field = field;
        this.excelField = excelField;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public ExcelField getExcelField() {
        return excelField;
    }

    public void setExcelField(ExcelField excelField) {
        this.excelField = excelField;
    }

    @Override
    public int compareTo(ExcelFieldEntity o) {
        if(this.excelField == null && o.excelField == null) return 0;
        if(this.excelField == null) return -1;
        if(o.excelField == null) return 1;

        return this.excelField.order() - o.excelField.order();
    }
}
