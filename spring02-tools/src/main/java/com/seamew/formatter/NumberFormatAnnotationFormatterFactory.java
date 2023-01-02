package com.seamew.formatter;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.number.CurrencyStyleFormatter;
import org.springframework.format.number.NumberStyleFormatter;
import org.springframework.format.number.PercentStyleFormatter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/* 通过注解驱动的格式化:
 * AnnotationFormatterFactory 接口可以通过字段的注解或者字段的 Class 配置
 * 相应的 Parser 和 Printer.
 * getFieldTypes() 方法返回可以放置注解, 也就是可以进行格式化的字段类型
 * getPrinter() 方法可以通过解析注解或者 Class 中的信息返回相应的 Printer
 * getParser() 方法可以通过解析注解或者 Class 中的信息返回相应的 Parser
 * 常用的注解有: @NumberFormat 和 @DateTimeFormat
 * 下面是 spring 的 NumberFormatAnnotationFormatFactory */

public final class NumberFormatAnnotationFormatterFactory
        implements AnnotationFormatterFactory<NumberFormat>
{

    public Set<Class<?>> getFieldTypes()
    {
        return new HashSet<>(Arrays.asList(new Class<?>[]{
                Short.class, Integer.class, Long.class, Float.class,
                Double.class, BigDecimal.class, BigInteger.class}));
    }

    public Printer<Number> getPrinter(NumberFormat annotation, Class<?> fieldType)
    {
        return configureFormatterFrom(annotation, fieldType);
    }

    public Parser<Number> getParser(NumberFormat annotation, Class<?> fieldType)
    {
        return configureFormatterFrom(annotation, fieldType);
    }

    private Formatter<Number> configureFormatterFrom(NumberFormat annotation, Class<?> fieldType)
    {
        if (!annotation.pattern().isEmpty())
        {
            return new NumberStyleFormatter(annotation.pattern());
        }
        else
        {
            NumberFormat.Style style = annotation.style();
            if (style == NumberFormat.Style.PERCENT)
            {
                return new PercentStyleFormatter();
            }
            else if (style == NumberFormat.Style.CURRENCY)
            {
                return new CurrencyStyleFormatter();
            }
            else
            {
                return new NumberStyleFormatter();
            }
        }
    }
}
