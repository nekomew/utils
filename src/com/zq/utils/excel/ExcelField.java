package com.nfha.frame.util.excel;

/**
 * @author : zhangqian
 * @date : 2019-12-12 11:56
 */

import com.nfha.frame.util.excel.format.Formater;
import com.nfha.frame.util.excel.format.ObjectFormater;

import java.lang.annotation.*;

/**
 * 描述 : 实体类映射
 * @date : 2019-11-15 10:51
 * @author : zhangqian
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelField{
    /**
     * 描述 : 对应的标题 或者 文件名
     * @date : 2019-11-15 10:44
     * @author : zhangqian
     * @return : java.lang.String
     */
    String value() default "";
    /**
     * 描述 : 排序
     * @date : 2019-11-15 10:49
     * @author : zhangqian
     * @return : int
     */
    int order() default 0;

    /**
     * <li>格式化 方式
     * <li>如果没有实现类  请继承com.nfha.frame.util.excel.format.Formater 自行实现
     * <li>参考
     * <li>- com.nfha.frame.util.excel.format.IntFormater
     * <li>- com.nfha.frame.util.excel.format.ObjectFormater
     * @return
     */
    Class<? extends Formater> format() default ObjectFormater.class;

    /**
     * 单元格宽度
     * @return
     */
    int width() default 20;
}
