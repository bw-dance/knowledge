# Spring

## Spring

### 1. IOC理解

SpringIOC即控制反转，即将对象的创建由手动编写交给Spring工厂创建，期间发生了职责的反转。

容器创建对象，并将它们装配在一起，配置并管理他们的完整生命周期，Spring通过依赖注入来管理组成应用的组件。

容器通过读取提供的配置元数据来接收对象进行实例化，配置和组装的指令。该元数据可以通过 XML，Java 注解或 Java 代码提供。

IOC或依赖注入减少了应用程序的代码量，它使得应用程序的测试很简单，提升了组件重用的频率，降低系统间的耦合度。

### 2. 什么是依赖注入，依赖注入的方式

不需要创建对象，由容器动态的将某个依赖关系注入到组件之中，只需要在容器中描述如何创建他们。通过在配置文件中描述那些组件需要哪些对象，由IOC容器将他们装配在一起。

通过依赖注入机制，我们只需要通过简单的配置，而无需任何代码就可指定目标需要的资源，完成自身的业务逻辑，而不需要关心具体的资源来自何处，由谁实现。

1. 构造器注入

   ```java
   <bean id=”rolel ” class=” com.ssm.chapter9.pojo.Role ” >
   <constructor-arg index 。” value 总经理 ／〉
   <constructor-arg index=” l value 公司管理者 ／〉
   </bean
   ```

2. setter注入

   ```java
   <bean id=” role2 ” class=” com . ssm . chapter9.pojo.Role” >
   <property name roleName value 高级工程师 ／〉
   <property name note value 重要人员 ／〉
   </bean>
   ```

3. 接口注入

   资源并非来自系统本身，而是外界。如数据库连接资源在tomcat下配置，通过JNDI的方式获取它，这样数据库连接资源是属于开发 外的资源，这个时候我们可以采用接口注入的形式来获取它。[一文搞懂JDBC和JNDI - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/143414856)

spring框架支持构造器注入和setter注入。

### 3. **BeanFactory** 和 **ApplicationContext**

BeanFactory是SpringIoC容器所定义的最底层接口，ApplicationContext是BeanFactory 的子接口之一。

![image-20221210103407419](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20221210103407419.png)

BeanFactory和ApplicationContext的优缺点分析：

BeanFactory的优缺点：

优点：应用启动的时候占用资源很少，对资源要求较高的应用，比较有优势；

缺点：运行速度会相对来说慢一些。而且有可能会出现空指针异常的错误，而且通过Bean工厂创建的Bean生命周期会简单一些。

ApplicationContext的优缺点：

优点：所有的Bean在启动的时候都进行了加载，系统运行的速度快；在系统启动的时候，可以发现系统中的配置问题。

缺点：把费时的操作放到系统启动中完成，所有的对象都可以预加载，缺点就是内存占用较大。

### 4. 构造器注入和setter注入

![image-20221210114809798](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20221210114809798.png)

### 5. Spring配置方式，即装配Bean的方式

1. 在XML中显示配置

   ```java
   <bean role2 class com ssm chapter9.pojo.Role > 
   <property name= " id” value=” 1 ” />
   <property name roleName value 高级工程师 ／〉
   <property name note value 重要人员 ／〉
   </bean>
   ```

2. 基于注解

   1. ＠Component 装配 Bean

      ```java
      @Component(value ＝”role”）
      public class Role { 
      @Value {” 1 ”} 
      private Long id; 
      @Value (”role name 1 ”} 
      private String roleName ; 
      @Value （” z。le te i ”} 
      private String note; 
      /**** setter and getter ****/
      ```

      注解＠Component Spring IoC 会把这个类扫描生成 Bean 实例

      ComponentScan 代表进行扫描 默认是扫描当前包的路径

   2. 自动装配一一＠Autowired

      如果一个接口有多个实现类，使用＠Autowired会报错，此时可以使用＠Primary在其中一个实现类，注入的时候就根据当前实现类注入。

      ＠Qualifier（“实现类bean名称”），注入的使用找到对应的实现注入。

      使用＠Autowired也可以注入带有构造参数的bean

      ```java
      public RoleController2( @Aut wired RoleService roleService ) { 
      this . roleService = roleService ;
      }
      ```

   3. 使用@Bean注解

      可以注解到方法之上 并且 方法返回象作为 Spring Bean 存放在 IoC 容器 中

使用建议：自己开发的类建议使用自定义注解，引入的第三方包或者服务的类，使用XML的形式。

### 6. Spring的Bean的作用域

1. 单例：默认，整个应用中，Spring只会生成一个Bean的实例
2. 原型：每次注入或者通过IOC容器获取Bean的时候，Spring都会创建一个新的实例
3. 会话：在一个HTTP Session中，一个Bean定义对应一个实例。该作用域仅在基于web的Spring ApplicationContext情形下有效。
4. 请求：Web 应用中使用的，就是在 次请求中 pring 会创建 个实例，但是不同的请求会创建不同的实例。

### 7. 声明Spring bean的注解有哪些

@Autowired 注解自动装配 bean，要想把类标识成可用于 @Autowired 注解自动装配的bean 的类



@Component ：通用的注解，可标注任意类为 Spring 组件。如果一个Bean不知道属于哪个层，可以使用@Component 注解标注。

@Repository : 对应持久层即 Dao 层，主要用于数据库相关操作。

@Service : 对应服务层，主要涉及一些复杂的逻辑，需要用到 Dao层。

@Controller : 对应 Spring MVC 控制层，主要用户接受用户请求并调用 Service 层返回数据给前端页面。

### 8. Spring IOC的初始化和依赖注入过程

bean在IOC中的初始化过程：

1. Resource定位：Spring IOC根据开发者的配置，继续资源定位，读取XML或注解内容。
2. BeanDefinition载入：Spring 根据开发者的配置获取对应的 POJO,用以生成对应实例的过程
3. BeanDefinition 注册：将之前通过 BeanDefinition 载入的 POJO向Spring IoC 容器中注册

此时还没有依赖注入，spring Bean 一个配置选项一一lazy-in it 其含义就是是否初始化 Spring Bean 在没有任何配置的情况下，它的默认值为 default ，实际值为 false ，也就是 Spring IoC 默认会自动初始化 Bean。  如果为true，只有当我们使用 Spring IoC 容器的 getBean 方法获取它时，它才会进行初始化， 完成依赖注入。

### 9. Spring生命周期

生命周期主要是为了了解 Spring IoC 容器初始化和销毁 Bean 的过程。

![image-20221210124114579](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20221210124114579.png)

![image-20221210132142471](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20221210132142471.png)

主要把握创建过程和销毁过程这两个大的方面；

创建过程：首先实例化Bean，并设置Bean的属性，根据其实现的Aware接口（主要是BeanFactoryAware接口，BeanFactoryAware，ApplicationContextAware）设置依赖信息，接下来调用BeanPostProcess的postProcessBeforeInitialization方法，完成initial前的自定义逻辑；afterPropertiesSet方法做一些属性被设定后的自定义的事情;调用Bean自身定义的init方法，去做一些初始化相关的工作;然后再调用postProcessAfterInitialization去做一些bean初始化之后的自定义工作。

这四个方法的调用有点类似AOP。此时，Bean初始化完成，可以使用这个Bean了。

销毁过程：如果实现了DisposableBean的destroy方法，则调用它，如果实现了自定义的销毁方法，则调用之。

### 10. Spring的内部Bean

只有将 bean 用作另一个 bean 的属性时，才能将 bean 声明为内部 bean。为了定义 bean，Spring 的基于 XML 的配置元数据在 <property> 或 <constructor-arg> 中提供了 <bean> 元素的使用。内部bean 总是匿名的，它们总是作为原型。

![image-20221210132753384](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20221210132753384.png)

### 11. 什么是Bean的装配

将bean在Spring容器中组合在一起进行管理。

Spring 容器需要知道需要什么bean 以及容器应该如何使用依赖注入来将 bean 绑定在一起，同时装配 bean。

Spring 容器能够自动装配 bean。也就是说，可以通过检查 BeanFactory 的内容让 Spring 自动解析bean 的协作者。

自动装配的不同模式：

**no** - 这是默认设置，表示没有自动装配。应使用显式 bean 引用进行装配。

**byName** - 它根据 bean 的名称注入对象依赖项。它匹配并装配其属性与 XML 文件中由相同名称定义的 bean。

**byType** - 它根据类型注入对象依赖项。如果属性的类型与 XML 文件中的一个 bean 名称匹配，则匹配并装配属性。

**构造函数** - 它通过调用类的构造函数来注入依赖项。它有大量的参数。

**autodetect** - 首先容器尝试通过构造函数使用 autowire 装配，如果不能，则尝试通过 byType 自动装配

### 12 自动装配局限性

1. 覆盖的可能性 - 您始终可以使用 <constructor-arg> 和 <property> 设置指定依赖项，这将覆盖自动装配。
2. 基本元数据类型 - 简单属性（如原数据类型，字符串和类）无法自动装配。
3. 为自动装配不太精确

### 13 同名bean怎么办

同一个配置文件内同名的Bean，以最上面定义的为准

不同配置文件中存在同名Bean，后解析的配置文件会覆盖先解析的配置文件

同文件中ComponentScan和@Bean出现同名Bean。同文件下@Bean的会生效，@ComponentScan扫描进来不会生效。通过@ComponentScan扫描进来的优先级是最低的，原因就是它扫描进来的Bean定义是最先被注册的

### 14 如何解决循环依赖的

循环依赖的三种情况：

1. 构造器循环依赖：spring处理不了，直接抛出异常
2. 单例模式下的settter循环依赖：三级缓存解决
3. 非单例循环依赖：无法解决

单例模式下的setter循环依赖解决：

对象初始化三步骤：

1. 实例化：调用对象的构造方法
2. 填充属性：多bean的依赖属性进行填充
3. 初始化：调用spring xml 的init方法

循环依赖主要发生在实例化和填充属性这两个步骤。

![image-20221210154200750](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20221210154200750.png)

A的某个field或者setter依赖了B的实例对象，同时B的某个field或者setter依赖了A的实例对象”这种循环依赖的情况。

A首先完成了初始化的第一步（createBeanINstance实例化），并且将自己提前曝光到singletonFactories中。此时进行初始化的第二步，发现自己依赖对象B，此时就尝试去get(B)，发现B还没有被create，所以走create流程，B在初始化第一步的时候发现自己依赖了对象A，于是尝试get(A)，尝试一级缓存singletonObjects(肯定没有，因为A还没初始化完全)，尝试二级缓存earlySingletonObjects（也没有），尝试三级缓存singletonFactories，由于A通过ObjectFactory将自己提前曝光了，所以B能够通过ObjectFactory.getObject拿到A对象(虽然A还没有初始化完全，但是总比没有好呀)，B拿到A对象后顺利完成了初始化阶段1、2、3，完全初始化之后将自己放入到一级缓存singletonObjects中。此时返回A中，A此时能拿到B的对象顺利完成自己的初始化阶段2、3，最终A也完成了初始化，进去了一级缓存singletonObjects中，而且更加幸运的是，由于B拿到了A的对象引用，所以B现在hold住的A对象完成了初始化。

### 15 Spring中的单例Bean的线程安全问题

无状态**bean**和有状态**bean**

有状态就是有数据存储功能。有状态对象(Stateful Bean)，就是有实例变量的对象，可以保存数据，是非线程安全的。在不同方法调用间不保留任何状态。

无状态就是一次操作，不能保存数据。无状态对象(Stateless Bean)，就是没有实例变量的对象 .不能保存数据，是不变类，是线程安全的。

在spring中无状态的Bean适合用不变模式，就是单例模式，这样可以共享实例提高性能。有状态的Bean在多线程环境下不安全，适合用Prototype原型模式。

Spring使用ThreadLocal解决线程安全问题。如果你的Bean有多种状态的话（比如 View Model 对象），就需要自行保证线程安全 。

### 16 什么是AOP

面向切面编程，通过反射创建代理对象的形式，将那些与业务无关，却为业务模块所共同调用的逻辑或责任封装起来，便于减少系统的重复代码，降低模块之间的耦合度，并有利于未来的可操作性和可维护性。

应用场景：

1. 权限
2. 缓存
3. 日志
4. 事务
5. 异常处理等。

### 17 AOP的实现方式

实现 AOP 的技术，主要分为两大类：

1. 静态代理（spring不使用） - 指使用 AOP 框架提供的命令进行编译，从而在编译阶段就可生成 AOP 代理类，因此也称为编译时增强；

   1. 编译时编织（特殊编译器实现）

   1. 类加载时编织（特殊的类加载器实现）。

2. 动态代理 - 在运行时在内存中“临时”生成 AOP 动态代理类，因此也被称为运行时增强。

   1. jdk动态代理：

      通过反射来接收被代理的类，并且要求被代理的类必须实现一个接口 。JDK动态代理的核心是 InvocationHandler 接口和 Proxy 类 。

   1. cglib动态代理

      CGLIB 动态代理： 如果目标类没有实现接口，那么 Spring AOP 会选择使用 CGLIB 来动态代理目标类 。 CGLIB （ Code Generation Library ），是一个代码生成的类库，可以在运行时动态的生成某个类的子类，注意， CGLIB 是通过继承的方式做的动态代理，因此如果某个类被标记为 final ，那么它是无法使用 CGLIB 做动态代理的。

### 18. Spring AOP 和 **AspectJ AOP** 有什么区别

Spring AOP 基于动态代理方式实现；AspectJ 基于静态代理方式实现。

Spring AOP 仅支持方法级别的 PointCut；AspectJ AOP提供了完全的 AOP 支持，它还支持属性级别的 PointCut。

### 19. Spring框架使用到了哪些设计模式

工厂设计模式 : Spring使用工厂模式通过 BeanFactory 、 ApplicationContext 创建 bean 对象。

代理设计模式 : Spring AOP 功能的实现。

单例设计模式 : Spring 中的 Bean 默认都是单例的。

模板方法模式 : Spring 中 jdbcTemplate 、 hibernateTemplate 等以 Template 结尾的对数据库操作

的类，它们就使用到了模板模式。

包装器设计模式 : 我们的项目需要连接多个数据库，而且不同的客户在每次访问中根据需要会去访问不

同的数据库。这种模式让我们可以根据客户的需求能够动态切换不同的数据源。

观察者模式**:** Spring 事件驱动模型就是观察者模式很经典的一个应用。

适配器模式 :Spring AOP 的增强或通知(Advice)使用到了适配器模式、spring MVC 中也是用到了适配器模式适配 Controller 。

### 20. Spring事务实现方式有哪些

编程式事务管理：这意味着你可以通过编程的方式管理事务，这种方式带来了很大的灵活性，但很难维护。

声明式事务管理：这种方式意味着你可以将事务管理和业务代码分离。你只需要通过注解或者XML配置管理事务。

### 21. Spring框架事务管理器优点

1. 提供了跨不同事务api（如JTA、JDBC、Hibernate、JPA和JDO）的一致编程模型。

2. 简化编程时事务的API
3. 支持声明式事务

4. 它很好地集成了Spring的各种数据访问抽象（MYSQL，ORACLE等）。

### 22. **说一下Spring的事务传播行为**

spring事务的传播行为说的是，当多个事务同时存在的时候，spring如何处理这些事务的行为。

① PROPAGATION_REQUIRED：如果当前没有事务，就创建一个新事务，如果当前存在事务，就加入该事务，该设置是最常用的设置。

④ PROPAGATION_REQUIRES_NEW：创建新事务，无论当前存不存在事务，都创建新事务。

② PROPAGATION_SUPPORTS：支持当前事务，如果当前存在事务，就加入该事务，如果当前不存在事务，就以非事务执行。

⑤ PROPAGATION_NOT_SUPPORTED：以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。

③ PROPAGATION_MANDATORY：支持当前事务，如果当前存在事务，就加入该事务，如果当前不存在事务，就抛出异常。

⑥ PROPAGATION_NEVER：以非事务方式执行，如果当前存在事务，则抛出异常。

⑦ PROPAGATION_NESTED：如果当前存在事务，则在嵌套事务内执行。如果当前没有事务，就创建一个新事务。

### 23. 声明式事务可能产生的问题

1. 事务粒度变大，造成与数据库连接时间过长，增大数据库压力。
2. 如果在事务中进行了远程调用，远程调用执行成功，当前事务内业务执行失败，远程调用无法回滚。
3. 如果注解添加在private上，或被二次调用，可能会产生问题。

## Spring MVC

### 1. 执行流程

![image-20221210170049687](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20221210170049687.png)

1.  客户端（浏览器）发送请求，直接请求到 DispatcherServlet 。

2. DispatcherServlet 根据请求信息调用 HandlerMapping ，解析请求对应的 Handler 。

3. 解析到对应的 Handler （也就是我们平常说的 Controller 控制器）后，开始由HandlerAdapter 适配器处理。

4. HandlerAdapter 会根据 Handler 来调用真正的处理器开处理请求，并处理相应的业务逻辑。

5. 处理器处理完业务后，会返回一个 ModelAndView 对象， Model 是返回的数据对象， View 是个逻辑上的 View 。

6. ViewResolver 会根据逻辑 View 查找实际的 View 。

7. DispaterServlet 把返回的 Model 传给 View （视图渲染）。

8. 把 View 返回给请求者（浏览器）

### 2. @Controller作用

@Controller 注解标记一个类为 Spring Web MVC 控制器 Controller。Spring MVC 会将扫描到该注解的类，然后扫描这个类下面带有 @RequestMapping 注解的方法，根据注解信息，为这个方法生成一个对应的处理器对象，存在 HandlerMapping 中过。

当然，除了添加 @Controller 注解这种方式以外，你还可以实现 Spring MVC 提供的 Controller 或者 HttpRequestHandler 接口，对应的实现类也会被作为一个处理器对象

### 3. **@RequestMapping** 注解有什么用？

@RequestMapping 注解，配置处理器的 HTTP 请求方法，URI等信息，这样才能将请求和方法进行映射。这个注解可以作用于类上面，也可以作用于方法上面，在类上面一般是配置这个控制器的 URI 前缀

### 4. **@RestController** 和 **@Controller** 有什么区别

@RestController 注解，在 @Controller 基础上，增加了 @ResponseBody 注解，更加适合目前前后端分离的架构下，提供 Restful API ，返回例如 JSON 数据格式。当然，返回什么样的数据格式，根据客户端的 ACCEPT 请求头来决定。

### 5. **@RequestMapping** 和 **@GetMapping** 注解的不同之处在哪里？

1. @RequestMapping ：可注解在类和方法上； @GetMapping 仅可注册在方法上

2. @RequestMapping ：可进行 GET、POST、PUT、DELETE 等请求方法； @GetMapping 是@RequestMapping 的 GET 请求方法的特例，目的是为了提高清晰度。

### 6. **@RequestParam** 和 **@PathVariable** 两个注解的区别

两个注解都用于方法参数，获取参数值的方式不同， @RequestParam 注解的参数从请求携带的参数中获取，而 @PathVariable 注解从请求的 URI 中获取

### 7. 返回 **JSON** 格式使用什么注解？

可以使用 @ResponseBody 注解，或者使用包含 @ResponseBody 注解的 @RestController 注解。

当然，还是需要配合相应的支持 JSON 格式化的 HttpMessageConverter 实现类。例如，Spring MVC默认使用 MappingJackson2HttpMessageConverter。

### 8. 什么是拦截器

Spring的处理程序映射机制包括处理程序拦截器，当你希望将特定功能应用于某些请求时，例如，检查用户主题时，这些拦截器非常有用。拦截器必须实现org.springframework.web.servlet包的HandlerInterceptor。此接口定义了三种方法：

preHandle：在执行实际处理程序之前调用。

postHandle：在执行完实际程序之后调用。

afterCompletion：在完成请求后调用。

### 9. 什么是Rest

Rest是一种架构风格，使用HTTP动词表示增删改查资源。 GET：查询，POST：新增，PUT：更新，DELETE：删除

Rest是无状态的，通常是不安全的，需要开发人员自己实现安全机制。

## SpringBoot

### 1. 为什么要使用SpringBoot

Spring框架开发过程中，需要配置许多Spring框架的一俩，这些配置通常需要重复添加，而且需要做很多框架使用及环境参数的重复配置，如开启注解、配置日志等。

Spring Boot致力于弱化这些不必要的操作，提供默认配置，当然这些默认配置是可以按需修改的，快速搭建、开发和运行Spring应用。

好处：

1. 自动配置，使用基于类路径和应用程序上下文的智能默认值，当然也可以根据需要重写它们以满足开发人员的需求。
2. 创建Spring Boot Starter 项目时，可以选择选择需要的功能，Spring Boot将为你管理依赖关系。
3. SpringBoot项目可以打包成jar文件。可以使用Java-jar命令从命令行将应用程序作为独立的Java应用程序运行。
4. 在开发web应用程序时，springboot会配置一个嵌入式Tomcat服务器，以便它可以作为独立的应用程序运行。（Tomcat是默认的，当然你也可以配置Jetty或Undertow）
5. SpringBoot包括许多有用的非功能特性（例如安全和健康检查）。

### 2. SpringBoot如何实现对不同环境的属性配置文件支持

在application.properties文件中添加spring.profiles.active=dev。指定环境

application-{profile}.properties可以分别创建dev，test，prod的配置文件。

### 3. SpringBoot的核心注解

@SpringBootApplication，它也是 Spring Boot 的核心注解，主要组合包含了以下3 个注解：

@SpringBootConfiguration：组合了 @Configuration 注解，实现配置文件的功能。

@EnableAutoConfiguration：打开自动配置的功能，也可以关闭某个自动配置的选项，如关闭数据源自动配置功能： @SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })。

@ComponentScan：Spring组件扫描。

### 4. 如何理解SpringBoot的Starters？

Starters可以理解为启动器，它包含了一系列可以集成到应用里面的依赖包，你可以一站式集成 Spring及其他技术，而不需要到处找示例代码和依赖包。

如你想使用 Spring JPA 访问数据库，只要加入spring-boot-starter-data-jpa 启动器依赖就能使用了。

工作原理：

Spring Boot 在启动的时候会干这几件事情：

1. Spring Boot 在启动时会去依赖的 Starter 包中寻找 resources/META-INF/spring.factories 文件，然后根据文件中配置的 Jar 包去扫描项目所依赖的 Jar 包。
2. 根据 spring.factories 配置加载 AutoConfigure 类
3. 根据 @Conditional 注解的条件，进行自动配置并将 Bean 注入 Spring Context

 Spring Boot 在启动的时候，按照约定去读取 Spring Boot Starter 的配置信息，再根据配置信息对资源进行初始化，并注入到 Spring 容器中。这样 Spring Boot 启动完毕后，就已经准备好了一切资源，使用过程中直接注入对应 Bean 资源即可。

### 5. Spring SpringBoot SpringCloud区别

Spring是一个框架，有两个核心功能IOC和AOP，通过Spring容器帮我们管理bean，减少代码编写和重复，提高编码效率。。同时AOP让开发者将业务与功能抽离，降低系统耦合度。

SpringBoot是在Spring基础上发展的，让开发者更容易上手使用Spring，他提供了常用依赖的默认配置，提升开发者效率。

Spring Cloud 是为了解决微服务架构中服务治理而提供的一系列功能的开发框架集合，利用SpringBoot的便利性简化了分布式系统基础设施的开发，如注册中心，配置中心，负载均衡等都开始使用SpringBoot风格做到一键启动和部署。

## Mybatis

### 1. Mybatis是什么

1. 半ORM框架，内部封装了JDBC，加载驱动、创建连接、创建statement等繁杂的过程，开发者开发只需要关注sql语句。
2. MyBatis 可以使用 XML 或注解来配置和映射原生信息，将 POJO映射成数据库中的记录，避免了几乎所有的 JDBC 代码和手动设置参数以及获取结果集。
3. 通过xml 文件或注解的方式将要执行的各种 statement 配置起来，并通过java对象和 statement中sql的动态参数进行映射生成最终执行的sql语句，最后由mybatis框架执行sql并将结果映射为java对象并返回。
4. 由于MyBatis专注于SQL本身，灵活度高，所以比较适合对性能的要求很高，或者需求变化较多的项目，如互联网项目。

### 2. Mybatis优点和缺点

优点：

1. 与JDBC相比，减少大量冗余代码
2. 基于sql语句编程，将sql写在xml中，解除sql与业务逻辑耦合，便于统一管理，支持动态sql得到编写
3. 能够和Spring做很好的集成
4. 提供映射标签，支持对象与数据库的ORM字段关系映射；提供对象关系映射标签，支持对象关系组件维护。

缺点：

1. sql语句编写工作量答
2. sql语句依赖数据库，导致数据库移植性差，不能随意更换数据库

### 3. 半自动和全自动区别

Hibernate属于全自动ORM映射工具，使用Hibernate查询关联对象或者关联集合对象时，可以根据对象关系模型直接获取，所以它是全自动的。

Mybatis在查询关联对象或关联集合对象时，需要手动编写sql来完成，所以，称之为半自动ORM映射工具。

### 4. JDBC编程不足，Mybatis是如何解决的

1. 数据库连接的创建，释放频繁造成系统浪费，影响系统性能，使用数据库连接池解决。

   在SqlMapConfig.xml中配置数据链接池，使用连接池管理数据库链接。

2. Sql语句写在代码中造成代码不易维护，开发中sql变化需要改变Java代码

   将sql语句配置在mapper.xml中，与java代码分离

3. 向sql语句传参数麻烦，sql语句的where条件不一定，需要占位符和参数对应

   mybatis将java对象映射到sql中

4. 结果解析麻烦

   Mybatis自动将sql执行结果映射至java对象

### 5. Mybatis编程步骤

1. 创建SqlSessionFactory 

2. 通过SqlSessionFactory创建SqlSession

3. 通过sqlsession执行数据库操作

4. 调用session.commit()提交事务

5. 调用session.close()关闭会话

### 6. #{}和${}区别

1. \#{}是占位符，预编译处理；${}是拼接符，字符串替换，没有预编译处理。
2. \#{}是以PreparedStatement来编译，能防止sql注入；${}以Statement来编译，不能防止sql注入
3. \#{} 的变量替换是在DBMS 中；${} 的变量替换是在 DBMS 外

### 7. 通常一个**Xml**映射文件，都会写一个**Dao**接口与之对应，那么这个**Dao**接口的工作原理是什么？**Dao**接口里的方法、参数不同时，方法能重载吗？

Dao接口即Mapper接口是没有实现类的，当调用接口方法时，接口全限名+方法名的拼接字符串作为key值，可唯一定位一个MapperStatement。即映射文件中的哪个sql

1. Dao接口里的方法，是不能重载的，因为是全限名+方法名的保存和寻找策略。
2. Dao接口的工作原理是JDK动态代理，Mybatis运行时会使用JDK动态代理为Dao接口生成代理proxy对象，代理对象proxy会拦截接口方法，转而执行MappedStatement所代表的sql，然后将sql执行结果返回

### 8. Mapper中如何传递多个参数

1. 如果接口接受多个参数，则#{0}表示第一个，#{1}表示第二个。
2. @Param注解，注解内的参数名为传递到Mapper中的参数名。
3. 多个参数封装程map，以map形式传递到mapper中。

### 9. 动态sql有什么用，执行原理是什么？有哪些动态sql？

在xml中，以标签的形式编写动态sql。执行原理是根据表达式值完成逻辑判断，并动态拼接sql得功能。

Mybatis提供了9种动态sql标签：trim、where、set、foreach、if、choose、when、otherwise、bind

### 10. xml映射文件中，不同xml的映射文件id是否可以重复？

如果配置了namespace，可以重复，如果没有，则不能重复。

是namespace+id是作为Map<String,MapperStatement>的key使用的，如果没有namespace，就剩下id，那么id重复会导致数据互相覆盖。有了namespace，自然id就可以重复，namespace不同，namespace+id自然也不同。

### 11. 一对一、一对多的查询方式

[(185条消息) Mybatis多表联合查询，嵌套查询，动态SQL_Debug_EDM的博客-CSDN博客](https://blog.csdn.net/debugEDM/article/details/108393710)

#### 一对一

1. 联合查询：在resultMap配置association节点的属性映射，使用sql语句查询时，指定使用的resultMap

   ```xml
    <resultMap id="userAccountMap" type="org.example.domain.User">
           <id column="id" property="id"></id>
           <result column="username" property="username"></result>
           <result column="birthday" property="birthday"></result>
           <result column="sex" property="sex"></result>
           <result column="address" property="address"></result>
   <!--        一对一的关系映射：配置封装account的内容-->
           <association property="account" column="aid">
               <id property="id" column="aid"></id>
               <result property="uid" column="uid"></result>
               <result property="money" column="money"></result>
           </association>
   
       </resultMap>
       <select id="selectUserAndCount" resultMap="userAccountMap">
           select a.id as aid,a.uid,a.money,u.* from account a,user u where a.uid=u.id
       </select>
   

2. 嵌套查询:先查一个表获取另一个表的唯一属性后，再去查另一个表

   ```java
   <select id="findById" resultType="customer">
           select * from t_customer where id = #{id}
       </select>
   
       <resultMap id="orderAndCustomer2" type="order">
           <id property="id" column="id"/>
           <result property="goods" column="goods"/>
           <result property="cid" column="cid"/>
           <association property="customer"
                        javaType="customer"
                        column="c_id"
                        select="findById"/>
       </resultMap>
   
       <select id="findByGoods2" resultMap="orderAndCustomer2">
           select * from t_order where goods = #{goods}
       </select>
   ```

#### 一对多

1. 联合查询:过在resultMap里面的collection节点配置一对多的类就可以完成；

   ```java
       <resultMap id="userAccountMaps" type="org.example.domain.User">
           <id property="id" column="id"></id>
           <result property="username" column="username"></result>
           <result property="address" column="address"></result>
           <result property="sex" column="sex"></result>
           <result property="birthday" column="birthday"></result>
   <!--        配置user对象中accounts集合的映射  property：属性名    ofType:集合中对象的数据类型-->
           <collection property="accounts" ofType="org.example.domain.Account">
               <id property="id" column="aid"></id>
               <result property="uid" column="uid"></result>
               <result property="money" column="money"></result>
           </collection>
       </resultMap>
           <select id="selectUserMap" resultMap="userAccountMaps">
            SELECT u.*,a.id AS aid,a.money,a.uid FROM account a,USER u WHERE u.`id`=a.`uid`;
           </select>
   
   ```

2. 嵌套查询：先查一个表获取另一个表的唯一属性后，再去查另一个表

   ```java
   AccountDao.xml
   <mapper namespace="org.example.dao.AccountDao">
       <!--    查询账户信息的sql-->
       <select id="selectAccount" resultType="org.example.domain.Account">
   --  查询账户名对应的账户
           select * from account where uid=#{uid}
       </select>
   <!--    根据账户id查询账户的sql：namespace.id      org.example.dao.AccountDao.selectAccount-->
   </mapper>
   
   
   
   
   UserDao.xml
   <!--   定义user的resultMap-->
   <!--   一对多-->
       <resultMap id="userAccountMaps" type="org.example.domain.User">
       <id property="id" column="id"></id>
       <result property="username" column="username"></result>
       <result property="address" column="address"></result>
       <result property="sex" column="sex"></result>
       <result property="birthday" column="birthday"></result>
       <!--        配置user对象中accounts集合的映射  property：属性名    ofType:集合中对象的数据类型-->
       <collection property="accounts" ofType="org.example.domain.Account" column="id" select="org.example.dao.AccountDao.selectAccount">
   <!--        <id property="id" column="aid"></id>-->
   <!--        <result property="uid" column="uid"></result>-->
   <!--        <result property="money" column="money"></result>-->
       </collection>
   
   </resultMap>
       <select id="selectUserMap" resultMap="userAccountMaps">
           select*from user
           </select>
   
   ```

### 12. Mybatis的一级缓存和二级缓存

 一级缓存：基于hashmap的PerpetualCache，作用域为Session，Session关闭或者flush后，缓存清楚，默认开启。

![image-20221211145012929](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20221211145012929.png)

二级缓存，默认基于hashmap的PerpetualCache，可以继承分布式缓存redis等。作用域为同一个namespace

![image-20221211144904894](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20221211144904894.png)

对于缓存数据更新机制，当某一个作用域（一级缓存Session/二级缓存Namespace）进行了增/删/改操作后，默认该作用域下所有select中的缓存将被clear。

### 13. Mybatis使用注意

1. mapper的方法名，参数，返回值必须分别和xml中每个sql的id，parameterType，resultType相同
2. mapper的的namespace即是mapper接口的类路径。

