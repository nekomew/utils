package com.nfha.frame.util.excel.format;

/**
 * @author : zhangqian
 * @date : 2019-12-12 12:03
 */
public interface Formater<T, R> {
    /**
     * 格式化
     * @param t
     * @return
     */
    R format(T t);

    /**
     * 反向格式化
     * @param r
     * @return
     */
    T reformat(R r);
}
