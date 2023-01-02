package com.seamew.converter;

import org.springframework.core.convert.converter.Converter;

/* Converter<S, T> 接口定义了一种把 S (source) 类型对象转换为
   T (target) 类型对象的能力.  */

public class StringToIntegerConverter implements Converter<String, Integer>
{
    @Override
    public Integer convert(String source)
    {
        return Integer.valueOf(source);
    }
}
