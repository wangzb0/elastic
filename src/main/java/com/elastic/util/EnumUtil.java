package com.elastic.util;

import cn.hutool.core.util.StrUtil;
import com.elastic.entity.ParamException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


/**
 * 枚举帮助类
 */
@Slf4j
public final class EnumUtil {
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Map<Object, Object> enumToMap(Class<? extends Enum<?>> cls) {
        String keyMethod = "getKey";
        String valMethod = "getVal";
        return getMap(cls, keyMethod, valMethod);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Map<Object, Object> enumToMap(Class cls, String keyField, String valField) {
        String keyMethod = "getKey";
        String valMethod = "getVal";
        if (StrUtil.isNotBlank(keyField)) {
            keyMethod = "get" + keyField.substring(0, 1).toUpperCase() + keyField.substring(1);
        }
        if (StrUtil.isNotBlank(valField)) {
            valMethod = "get" + valField.substring(0, 1).toUpperCase() + valField.substring(1);
        }
        return getMap(cls, keyMethod, valMethod);
    }

    private static Map<Object, Object> getMap(Class cls, String keyMethod, String valMethod) {
        Map<Object, Object> map = new HashMap<Object, Object>();

        try {
            Method m1 = cls.getMethod(keyMethod);
            Method m2 = cls.getMethod(valMethod);
            Object[] objs = cls.getEnumConstants();
            for (Object obj : objs) {
                map.put(m1.invoke(obj), m2.invoke(obj));
            }
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }

        return map;
    }

    @NonNull
    public static String getNameFromField(Class<? extends Enum<?>> clz, String field, Object value) {
        final Map<String, Object> nameFieldMap = cn.hutool.core.util.EnumUtil.getNameFieldMap(clz, field);
        final Optional<Map.Entry<String, Object>> first = nameFieldMap
                .entrySet()
                .stream()
                .filter(entry -> value.equals(entry.getValue()))
                .findFirst();
        if (first.isPresent()) {
            return first.get().getKey();
        } else {
            final ParamException exception = new ParamException("系统内部错误");
            log.error("{}: Cannot find {} for field {}", clz.toString(), value.toString(), field);
            throw exception;
        }
    }
}
