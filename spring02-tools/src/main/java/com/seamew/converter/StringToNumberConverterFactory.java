package com.seamew.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/* 使用 ConverterFactory 可以实现某一基类及其子类的类型转换, 下面的例子
 * 可以将字符串转换为任意 Number 类型的对象 */

public class StringToNumberConverterFactory implements ConverterFactory<String, Number>
{
    @Override
    public <T extends Number> Converter<String, T> getConverter(Class<T> targetType)
    {
        return new StringToNumberConverter<>(targetType);
    }

    private static final class StringToNumberConverter<T extends Number> implements Converter<String, T>
    {
        Class<? extends Number> clazz;

        public StringToNumberConverter(Class<? extends Number> clazz)
        {
            this.clazz = clazz;
        }

        @Override
        public T convert(String source)
        {
            if (Byte.class.equals(clazz)) return (T) Byte.valueOf(source);
            else if (Short.class.equals(clazz))  return (T) Short.valueOf(source);
            else if (Integer.class.equals(clazz)) return (T) Integer.valueOf(source);
            else if (Long.class.equals(clazz)) return (T) Long.valueOf(source);
            else if (Float.class.equals(clazz)) return (T) Float.valueOf(source);
            else if (Double.class.equals(clazz)) return (T) Double.valueOf(source);
            // 其它 Number 的实现类省略 ...
            return null;
        }
    }
}
