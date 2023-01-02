package com.seamew.propertyEditor;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;

/* spring 的 BeanWrapperImpl 会为我们向容器中注册许多常用的 editor, 当然,
 * 我们也可以向容器中注入自定义的 ProperEditor. 我们需要自己写一个注册管理器类,
 * 实现 PropertyEditorRegistrar 接口, 例如这里的 CustomEditorRegistrar,
 * 我们在 registerCustomEditors 方法中可以注册我们自定义的 PropertyEditor.
 * 除此之外, 我们还需要向容器中注入 CustomEditorConfigurer 这个 bean, 并且
 * 向它的 propertyEditorRegistrars (List) 属性中注入 我们自己写的注册管理器
 * CustomEditorRegistrar. 这里注册了自定义的 PersonEditor
 *
 * Registrar, registry, register 的逻辑关系:
 * Registrar (注册管理者) > registry (注册处) > register (注册行为)
 * Registrar (注册管理者) 使用 registry (注册处) 进行 register (注册行为),
 * registry (注册处) 只负责执行具体的注册行为 */

public class CustomEditorRegistrar implements PropertyEditorRegistrar
{
    @Override
    public void registerCustomEditors(PropertyEditorRegistry registry)
    {
        registry.registerCustomEditor(Person.class, new PersonEditor());
    }
}