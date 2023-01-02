package com.seamew.formatter;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

/* 1. Formatter 接口解决的问题:
 *    Formatter 接口用于解决客户端与服务端之间互相传输的数据的格式化问题. 客户端接收到服务端的
 *    数据后, 需要将这个数据渲染到客户端界面上进行展示; 服务端在接收到客户端发回的数据后, 需要将
 *    这个数据转化为服务端可以识别操作的格式. 在这两个场景中就涉及到了数据格式化的问题, 也就是客
 *    户端需要把服务端发来的数据转化为可以渲染展示的格式, 服务端也需要把客户端发回的数据转化为自己
 *    可以识别处理的格式. 由此, Formatter 接口继承了了两个父接口, Printer 和 Parser, 分别对
 *    应解决这两种需求. 此外, Formatter 还可以将数据进行 "本土化" (localize) 处理, 例如客户端
 *    需要将服务端传来的一个 Double 类型的数据转化为 '货币' 的格式显示出来, 即 '¥10' 这样货币符
 *    号加上数据的格式, 根据我们所设置的国家地区, spring 会为我们设置相应的货币符号, 比如中国
 *    (Locale.CHINA) 就是 '¥', 美国 (Locale.AMERICA) 就是 '$'.
 *
 * 2. Printer 接口和 Parser 接口:
 *    Printer 接口用于解决客户端对服务端发来的数据的格式化问题. Printer 接口中只定义了一个 print()
 *    方法, 它可以根据指定的地区把一个对象格式化为一个客户端所需要的, 并且 "本土化" 的字符串让客户端来
 *    进行渲染. Parser 接口用于解决服务端对客户端发回数据的解析问题. Parser 接口中只定义了一个 parse()
 *    方法, 它可以将客户端发回的字符串按照特定的规则、地区信息解析为服务端可以处理的对象.
 *
 * 3. Formatter 和 Printer, Parser 的关系:
 *    Formatter 接口继承自 Printer 和 Parser 接口, 也就是说, Formatter 同时拥有将格式化的字符串
 *    解析为对象的能力, 和把对象格式化为特定样式的字符串的能力.
 *
 * 4. Formatter 举例:
 *    与日期相关: DateFormatter
 *    与数字相关: NumberStyleFormatter, PercentStyleFormatter, CurrencyStyleFormatter */

public class StudentFormatter implements Formatter<Student>
{
    @Override
    public Student parse(String text, Locale locale) throws ParseException
    {
        try
        {
            // 格式化字符串的格式: [id name]
            String[] idAndName = text.split(" ");
            Long id = Long.valueOf(idAndName[0]);
            String name = idAndName[1];
            return new Student(id, name);
        }
        catch (Exception e)
        {
            throw new ParseException("学生信息有误", 0);
        }
    }

    @Override
    public String print(Student object, Locale locale)
    {
        return object.brief();
    }
}
