package com.zq.utils.excel;

import com.nfha.frame.util.excel.format.Formater;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 描述 ：
 *
 * @author : zhangqian
 * @date : 2019-11-15 08:59
 */
public class ExcelUtils {

    /**
     * 描述 : 解析excel 为实体类
     * @param in :
     * @param cls :
     * @date : 2019-11-15 12:49
     * @author : zhangqian
     * @return : java.util.List<T>
     */
    public static <T> List<T> parseExcel(InputStream in, Class<T> cls) {
        List<T> list = new ArrayList<>();
        try {
            Workbook workbook = WorkbookFactory.create(in);
            List<ExcelFieldEntity> entities = parseExcelField(cls);
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.rowIterator();
            rowIterator.next();//跳过标题

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                T entity = cls.newInstance();

                for (int i = 0; i < entities.size(); i++) {
                    Cell cell = row.getCell(i);
                    ExcelFieldEntity excelFieldEntity = entities.get(i);
                    //字段和注解
                    Field field = excelFieldEntity.getField();
                    ExcelField excelField = excelFieldEntity.getExcelField();

                    //格式化类
                    Class<? extends Formater> formatCls = excelField.format();
                    Formater formater = Formaters.getInstanct(formatCls);
                    
                    //获取值
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String value = cell.getStringCellValue();
                    
                    //设值
                    field.setAccessible(true);
                    field.set(entity, formater.parse(value));
                }

                list.add(entity);
            }

            return list;
        } catch (Exception e) {
            throw new RuntimeException("excel解析失败", e);
        }
    }

    /**
     * 数据转excel
     * @param out
     * @param list
     * @param cls
     */
    public static void toExcel(OutputStream out, List<?> list, Class<?> cls) {
        XSSFWorkbook workbook = new XSSFWorkbook();

        ExcelField sheetField = cls.getAnnotation(ExcelField.class);
        XSSFSheet sheet = sheetField == null ? workbook.createSheet() : workbook.createSheet(sheetField.value());
        //获取属性
        List<ExcelFieldEntity> entities = parseExcelField(cls);
        //设置宽度
        setColumnWidth(sheet, entities);

        //标题
        XSSFRow titleRow = sheet.createRow(0);
        setRowValue(titleRow, entities, null);

        //没有内容
        if (list == null || list.isEmpty()) {
            write(workbook, out);
            return;
        }

        //内容
        for (int i = 0; i < list.size(); i++) {
            Object t = list.get(i);
            XSSFRow row = sheet.createRow(i + 1);

            setRowValue(row, entities, t);
        }

        write(workbook, out);
    }

    /**
     * 设置 宽度
     * @param sheet
     * @param entities
     */
    private static void setColumnWidth(XSSFSheet sheet, List<ExcelFieldEntity> entities) {
        for (int i = 0; i < entities.size(); i++) {
            ExcelFieldEntity excelFieldEntity = entities.get(i);
            sheet.setColumnWidth(i, excelFieldEntity.getExcelField().width() * 256);
        }
    }

    /**
     * 写入输出流
     * @param workbook
     * @param out
     */
    private static void write(XSSFWorkbook workbook, OutputStream out) {
        try {
            workbook.write(out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置行单元格值
     * @param row
     * @param entities
     * @param obj
     */
    private static void setRowValue(XSSFRow row, List<ExcelFieldEntity> entities, Object obj) {
        for (int i = 0; i < entities.size(); i++) {
            ExcelFieldEntity excelFieldEntity = entities.get(i);
            ExcelField excelField = excelFieldEntity.getExcelField();

            String value = excelField.value();
            if (obj != null) {
                Class<? extends Formater> formatCls = excelField.format();
                Formater formater = Formaters.getInstanct(formatCls);

                Field field = excelFieldEntity.getField();
                field.setAccessible(true);

                Object o = null;
                try {
                    o = field.get(obj);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                o = formater.format(o);
                value = o == null ? "" : o.toString();
            }

            XSSFCell cell = row.createCell(i, XSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(value);
        }
    }

    /**
     *  解析实体类
     * @param cls :
     * @date : 2019-11-15 11:23
     */
    private static List<ExcelFieldEntity> parseExcelField (Class<?> cls) {
        List<ExcelFieldEntity> res = new ArrayList<>();

        for (Class<?> curCls = cls; curCls != null; curCls = curCls.getSuperclass()) {
            parseExcelField (curCls, res);
        }

        return res;
    }

    /**
     * 解析实体类
     * @param cls
     * @param res
     */
    private static void parseExcelField(Class<?> cls, List<ExcelFieldEntity> res) {
        Field[] fields = cls.getDeclaredFields();

        if (fields == null || fields.length == 0) {
            return;
        }

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            ExcelField excelField = field.getAnnotation(ExcelField.class);

            if (excelField == null) {
                continue;
            }

            res.add(new ExcelFieldEntity(field, excelField));
        }
    }

}
