package com.nfha.frame.util.excel.format;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author : zhangqian
 * @date : 2019-12-12 12:10
 */
public class DateFormater implements Formater<Date> {
    @Override
    public String format(Date date) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    @Override
    public Date parse(String s) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
