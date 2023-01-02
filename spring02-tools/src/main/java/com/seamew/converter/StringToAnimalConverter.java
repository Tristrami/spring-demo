package com.seamew.converter;

import org.springframework.core.convert.converter.Converter;

/* ConversionService 接口是 spring 类型转换系统的入口, 通过 ConversionService,
 * 我们可以实现类型转换, 还可以向转换系统中注册自定义的 Converter. ConversionService
 * 需要通过 ConversionServiceFactoryBean 来构造, 如果要注册我们自定义的 Converter,
 * 只需要向 ConversionServiceFactoryBean 的 converters (Set) 属性中注入自定义的
 * Converter 即可. 我们调用 ConversionService 的 convert() 方法时, ConversionService
 * 会检索它的 converters 属性, 为我们寻找一个合适的 converter 把呢我们进行类型转换. 下面的
 * 例子中把一个 StringToAnimalConverter 注册到了转换系统中, 配置文件见
 * resources/converter/converter.xml
 *
 * 下面的 StringToAnimalConverter 可以通过接收到的文本创建一个 Animal 对象,
 * 把文本的内容赋值给 name 属性. 我们向容器中注册了 StringToAnimalConverter
 * 以后, 如果要注入 Animal 类型的依赖, 我们可以通过 @Value(...) 或者
 * <property name="animal" value="..."/> 这样类似注入原语类型属性的方式来
 * 注入 Animal 类型的依赖, 因为 spring 在解析 value 这个字符串时会使用到我们
 * 注册的 StringToAnimalConverter 来进行类型转化
 *
 * 🌟 ConversionService 的继承体系:
 * ConversionService <- GenericConversionService <- DefaultConversionService
 * GenericConversionService 实现了类型转换和 converter 注册的基本逻辑,
 * DefaultConversionService 是 spring 直接使用的 ConversionService,
 * 其中为我们注册了许多常用的 Converter, 可以在字符串、数字、枚举、集合、映射和其他常见类型之间进行转换 */

public class StringToAnimalConverter implements Converter<String, Animal>
{
    @Override
    public Animal convert(String source)
    {
        return new Animal(source);
    }
}
