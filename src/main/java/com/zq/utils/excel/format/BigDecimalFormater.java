package com.zq.utils.excel.format;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * @author : zhangqian
 * @date : 2019-12-13 08:57
 */
public class BigDecimalFormater implements Formater<BigDecimal> {

    @Override
    public BigDecimal parse(String o) {
        return StringUtils.isNotBlank(o) ? new BigDecimal(o) : null;
    }
    @Override
    public String format(BigDecimal o) {
        return o != null ? o.toString() : "";
    }
}
