# Mybatis

# 1.MyBatis框架介绍

是一个持久层框架，主要用于数据库的交互的，是java写的。可以理解为增强版的jdbc

封装了许多jdbc的操作，使开发者只需要注重sql语句本身，而不需关注注册驱动，创建链接等繁杂的过程。

它使用ORM思想实现了结果集的封装。

MyBatis持久层框架包括SQL Maps (sql映射)和 Data Access Objects（DAOS）数据访问功能

## **ORM**

Object Relational Mapping 对象关系映射

就是把**数据库的表和实体类以及实体类的属性对应起来，让我们可以操作实体类就实现操作数据库表。**

  ![image-20220106093839249](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220106093839249.png)        

要求：

实体类的属性和数据库表的字段名称保持一致。

## **MyBatis框架包含**

MyBatis是 MyBatis SQL Mapper Framework for java（sql 映射框架）

- ### **sql mapper : sql映射**

可以把数据库表中的一行数据，映射为一个java对象。

一行数据可以看做是一个java对象。操作这个对象，就相当于操作表中的数据。

- ### **Data Access Object（DAOs）：数据访问，对数据库执行增删改查**

## **MyBatis功能**

1. 提供了创建Connection , statement, Resultset的能力 ，不用开发人员创建这些对象了

2. 提供了执行sq1语句的能力， 不用你执行sql

3. 提供了循环sql,把sql的结果转为java对象，List集合的能力。

4. 1. 原始的jdbc，麻烦

​                 ![image-20220106093909736](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220106093909736.png)        

1. 提供了关闭资源的能力。

开发人员工作：

提供sql语句——mybatis处理sql——开发人员得到List集合或java对象（表中的数据）

## **总结**

- 是一个sql的映射框架，提高数据库操作的能力。增强jdbc
- 使用mybatis让开发人员集中写sql，不用考虑connection，preparement，resultset的创建，销毁，sql的执行等。

![image-20220106101559733](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220106101559733.png)