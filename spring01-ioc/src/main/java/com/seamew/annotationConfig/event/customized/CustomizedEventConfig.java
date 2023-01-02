package com.seamew.annotationConfig.event.customized;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/* spring 的事件机制基于观察者设计模式, 有三个主要角色:
 * 1. 事件本身
 * 2. 事件发布者
 * 3. 事件监听者
 *
 * spring 的事件机制是为了让容器中的 bean 可以相互通信, 发布者把事件发布到容器中, spring
 * 会通知所有监听了这个事件的监听者, 让它们执行相应的方法. spring 为我们实现了事件的发布者,
 * 也定义了容器自身的一些标准事件, 我们需要自己实现事件的监听者. 当然, 我们也可以自定义事件.
 *
 * 🔍 发布者: ApplicationEventPublisher, 可以发布任意对象
 * 🔍 标准事件:
 *    1. ContextRefreshedEvent
 *    2. ContextStartedEvent
 *    3. ContextStoppedEvent
 *    4. ContextClosedEvent
 *    5. RequestHandledEvent
 *    6. ServletRequestHandledEvent
 *
 * 自定义事件: 事件可以是任意的对象, 发布事件要调用 ApplicationEventPublisher 的 publish()
 * 方法. 通常我们可以实现 ApplicationEventPublisherAware 接口通过回调函数获得 publisher,
 * 也可以通过自动装配注入 publisher, 这里需要注意, 容器帮我们注入的 publisher 实际上就是
 * ApplicationContext 容器本身, 只不过我们在和它 ApplicationEventPublisher 这个接口的
 * 功能进行交互而已
 *
 * 实现监听者有两种方法:
 *
 * 1. 实现 ApplicationEventListener 接口, 重写 onApplicationEvent() 方法.
 *    见 xmlConfig/event/EmailBlockedNotifier
 * 2. 在监听者的相应方法上加 @EventListener 注解, 并把监听者类注册到容器中.
 *    见 annotationConfig/event/UserBlockedNotifier
 *
 * 🌟 如果监听者收到事件后会触发另一个事件, 可以让 @EventListener 方法返回另一个事件对象
 * 🌟 spring 有两种方式来判断 @EventListener 方法监听的事件类型:
 *    1. 通过方法中的参数类型判断
 *    2. 如果方法中没有指定参数, 可以通过 @EventListener({Event1.class, Event2.class})
 *       来指定方法监听的事件类型
 *
 * 我们还可以通过在方法上 @Async 注解来让方法同步处理事件
 *
 * 如过一个事件有多个监听者, 我们想让这些监听者的方法按一定的顺序被调用, 可以在方法上加上
 * @Order(priority) 注解, 其中 priority 是 int 类型整数, 值越小优先级越高, 这里指定
 * UserBlockedListener 优先级高于 UserBlockedNotifier */

@Configuration
@ComponentScan("com.seamew.annotationConfig.event.customized")
public class CustomizedEventConfig
{
}
