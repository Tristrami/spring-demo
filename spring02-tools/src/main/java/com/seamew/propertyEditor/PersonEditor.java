package com.seamew.propertyEditor;

import java.beans.PropertyEditorSupport;

/* PropertyEditor 接口是 Java 为方便用户通过图形化 GUI 界面修改属性而定义的接口,
 * 其中的 setAsText() 方法定义了一种可以将用户传递的字符串文本转化为属性值的一种能力,
 * 换句话说, 就是可以将一段字符串进行解析, 将其转化为相应的对象, 赋值给目标属性.
 * getAsText() 方法定义了一种可以将 editor 中的对象转化为便于人理解文本的能力.
 * PropertyEditorSupport 是 Java 对PropertyEditor 的基本实现, 其中的 value
 * 是被编辑的属性值, 也就是上面所说的目标属性. 我们可以在这个基类这个上 spring 基于
 * PropertyEditorSupport 这个基类扩展出了许多种类的 PropertyEditor, 例如
 * ClassEditor, 它的 setAsText() 方法可以将某个类的全限定名称的字符串转化为对应的
 * Class 对象, getAsText() 方法可以将 editor 中的 Class 对象转化为对应类的
 * 全限定类名. spring 使用 ClassEditor 来解析 beanDefinition 中的全限定类名
 *
 * 自定义 PropertyEditor: 我们同样可以通过继承 PropertyEditorSupport 基类的
 * 方式来实现自己的 PropertyEditor, 例如下面的例子 PersonEditor, 可以通过接收
 * 到的文本创建一个 Person 对象, 把文本的内容赋值给 name 属性. 我们向容器中注册了
 * PersonEditor 以后, 如果要注入 Person 类型的依赖, 我们可以通过 @Value(...)
 * 或者 <property name="person" value="..."/> 这样类似注入原语类型属性的方式
 * 来注入 Person 类型的依赖, 因为 spring 在解析 value 这个字符串时会使用到我们
 * 注册的 PersonEditor 来进行类型转化. 例子见 School 类和 property-editor.xml
 *
 * ProperEditor 的注册: 见 CustomEditorRegistrar */

public class PersonEditor extends PropertyEditorSupport
{
    public PersonEditor()
    {
    }

    public PersonEditor(Object source)
    {
        super(source);
    }

    @Override
    public String getAsText()
    {
        return this.getSource().toString();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException
    {
        Person person = (Person) this.getValue();
        if (person != null)
        {
            person.setName(text);
            this.setValue(person);
        }
        else
        {
            this.setValue(new Person(text));
        }
    }
}
