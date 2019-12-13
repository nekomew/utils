package com.zq.utils.excel.format;

import org.apache.commons.lang3.StringUtils;

/**
 * @author : zhangqian
 * @date : 2019-12-13 09:22
 */
public class IntFormater implements Formater<Integer> {
    @Override
    public Integer parse(String val) {
        return StringUtils.isNotEmpty(val) ? Integer.parseInt(val) : null;
    }

    @Override
    public String format(Integer integer) {
        return integer == null ? "" : integer.toString();
    }
}
