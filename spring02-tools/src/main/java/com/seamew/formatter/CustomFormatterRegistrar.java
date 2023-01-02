package com.seamew.formatter;

import org.springframework.format.FormatterRegistrar;
import org.springframework.format.FormatterRegistry;

/*
 * 1. FormattingConversionService 接口:
 *    这个接口是对 ConversionService 的扩展, 可以理解为 spring 格式化系统的入口.
 *    我们可以通过这个接口向系统中注册我们自定义的 Formatter, Parser, Printer.
 *    并且, 它重写了父类 ConversionService 中的 convert() 方法, 也就是说, 当我们
 *    调用 FormattingConversionService 的 convert() 方法想要把对象格式化为字符串
 *    时, FormattingConversionService 会为我们寻找合适的 Printer 进行格式化, 字符
 *    串转对象同理.
 *
 * 2. 向 spring 类型转换系统中注册自定义 Formatter 的方式:
 *   (1) 向容器注入一个 FormattingConversionServiceFactoryBean, 并将自定义的 Formatter
 *       注入到它的 formatters (Set) 属性中
 *   (2) 实现 FormatterRegistrar, 例如下面的 CustomFormatterRegistrar, 使用
 *       registerFormatters() 方法注册 Formatter | Parser | Printer. 再向容器注入一个
 *       FormattingConversionServiceFactoryBean, 并将 CustomFormatterRegistrar 注
 *       入到它的 formatterRegistrars (Set) 属性中
 *
 * 🌟 我们注册 Formatter 时不仅可以注入 Formatter 本身, 也可以注入 AnnotationFormatterFactory
 *
 * 🌟 FormattingConversionService 实现了 FormatterRegistry 接口, 传入 registerFormatter()
 *   方法中的 registry 实际上是 DefaultFormattingConversionService, 并且它会帮我们提前注册好
 *   一些 Formatter, 例如 NumberFormatAnnotationFormatterFactory, DateTimeFormatter 等等 */

public class CustomFormatterRegistrar implements FormatterRegistrar
{
    @Override
    public void registerFormatters(FormatterRegistry registry)
    {
        registry.addFormatter(new StudentFormatter());
    }
}
