package com.nfha.frame.util.excel.format;

/**
 * @author : zhangqian
 * @date : 2019-12-12 12:03
 */
public interface Formater<T> {
    /**
     * 解析
     * @param val
     * @return
     */
     T parse(String val);

    /**
     * 格式化
     * @param t
     * @return
     */
    String format(T t);
}
