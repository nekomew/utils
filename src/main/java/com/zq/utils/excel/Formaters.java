package com.zq.utils.excel;

import com.nfha.frame.util.excel.format.Formater;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : zhangqian
 * @date : 2019-12-12 12:43
 */
public final class Formaters {

    public static final Map<Class<? extends Formater>, Formater> CACHE = new ConcurrentHashMap<>();

    /**
     * 获取格式化工具
     * @param cls
     * @return
     */
    public static Formater getInstanct(Class<? extends Formater> cls) {
        Formater formater = CACHE.get(cls);
        if (formater != null) {
            return formater;
        }
        synchronized (Formaters.class) {
            formater = CACHE.get(cls);

            if (formater != null) {
                return formater;
            }

            try {
                formater = cls.newInstance();
                CACHE.put(cls, formater);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return formater;
    }
}
