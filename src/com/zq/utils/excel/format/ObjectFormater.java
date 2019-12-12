package com.nfha.frame.util.excel.format;

import org.apache.poi.ss.formula.functions.T;

/**
 * @author : zhangqian
 * @date : 2019-12-12 12:07
 */
public class ObjectFormater<T> implements Formater<T, T>{
    @Override
    public T format(T t) {
        return t;
    }

    @Override
    public T reformat(T t) {
        return t;
    }
}
