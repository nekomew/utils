package com.zq.utils.excel.format;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;

/**
 * @author : zhangqian
 * @date : 2019-12-12 12:07
 */
public class ObjectFormater implements Formater<Object>{
    @Override
    public Object parse(String t) {
        return t;
    }

    @Override
    public String format(Object t) {
        return t != null ? t.toString() : "";
    }
}
