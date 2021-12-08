# Mysql的基本使用

## 1. Mysql的安装

[(97条消息) mysql8.0.25安装配置教程(windows 64位)最详细！！！！_@WWWxp的博客-CSDN博客_mysql安装配置教程8.0.25](https://blog.csdn.net/weixin_43579015/article/details/117228159?utm_source=app&app_version=4.19.1&code=app_1562916241&uLinkId=usr1mkqgl919blen)

## 2. Mysql的基本使用

### 数据库操作

1. **启动数据库服务** `net start mysql` 

2. **关闭数据库服务** `net stop mysql`

3. **查看所有数据库 `SHOW DATABASES`**

4. **创建数据库** `CREATE DATABASE "数据库名称"`

   1. ```sql
      CREATE DATABASE [IF NOT EXISTS] <数据库名>
      [[DEFAULT] CHARACTER SET <字符集名>] 
      [[DEFAULT] COLLATE <校对规则名>];
      <数据库名>：创建数据库的名称。
      IF NOT EXISTS：在创建数据库之前进行判断，只有该数据库目前尚不存在时才能执行操作。
      [DEFAULT] CHARACTER SET：指定数据库的字符集。指定字符集的目的是为了避免在数据库中存储的数据出现乱码的情况。不指定字符集，使用系统的默认字符集。
      [DEFAULT] COLLATE：指定字符集的默认校对规则。
      ```

   2. MySQL 的字符集（CHARACTER）和校对规则（COLLATION）是两个不同的概念。字符集是用来定义 MySQL 存储字符串的方式，校对规则定义了比较字符串的方式。

   3. ````sql
      CREATE DATABASE test_db;
      CREATE DATABASE IF NOT EXISTS test_db;
      CREATE DATABASE IF NOT EXISTS test_db_char
             DEFAULT CHARACTER SET utf8mb4
             DEFAULT COLLATE utf8mb4_general_ci;
      ````

5. **查看数据库结构 `SHOW CREATE DATABASE test_db_char;`**

   1. 模糊匹配：`SHOW DATABASES LIKE '%db'`

   2. mysql自带数据库

      1. ```sql
         information_schema：主要存储了系统中的一些数据库对象信息，比如用户表信息、列信息、权限信息、字符集信息和分区信息等。
             
         mysql：MySQL 的核心数据库，类似于 SQL Server 中的 master 表，主要负责存储数据库用户、用户访问权限等 MySQL 自己需要使用的控制和管理信息。常用的比如在 mysql 数据库的 user 表中修改 root 用户密码。
             
         performance_schema：主要用于收集数据库服务器性能参数。
             
         sakila：MySQL 提供的样例数据库，该数据库共有 16 张表，这些数据表都是比较常见的，在设计数据库时，可以参照这些样例数据表来快速完成所需的数据表。
             
         sys：MySQL 5.7 安装完成后会多一个 sys 数据库。sys 数据库主要提供了一些视图，数据都来自于 performation_schema，主要是让开发者和使用者更方便地查看性能问题。
             
         world：world 数据库是 MySQL 自动创建的数据库，该数据库中只包括 3 张数据表，分别保存城市，国家和国家使用的语言等内容。
         ```

6. 选择数据库 `USER 数据库名称`

7. 修改数据库

   1. **ALTER DATABASE** 来修改已经被创建或者存在的数据库的相关参数

   2. ```sql
      ALTER DATABASE [数据库名] { 
      [ DEFAULT ] CHARACTER SET <字符集名> |
      [ DEFAULT ] COLLATE <校对规则名>}
      
      ALTER DATABASE 用于更改数据库的全局特性。
      使用 ALTER DATABASE 需要获得数据库 ALTER 权限。
      数据库名称可以忽略，此时语句对应于默认数据库。
      CHARACTER SET 子句用于更改默认的数据库字符集。
      ```

   3. ```sql
      CREATE DATABASE test_db
             DEFAULT CHARACTER SET gb2312
             DEFAULT COLLATE gb2312_chinese_ci;
      ```

8. 删除数据库 `drop database '数据库名称'`

   1. ```java
      DROP DATABASE [ IF EXISTS ] <数据库名>
      
      <数据库名>：指定要删除的数据库名。
      IF EXISTS：用于防止当数据库不存在时发生错误。
      DROP DATABASE：删除数据库中的所有表格并同时删除数据库。使用此语句时要非常小心，以免错误删除。如果要使用 DROP DATABASE，需要获得数据库 DROP 权限。
      ```

   2. ```sql
      DROP DATABASE test_db_del;
      DROP DATABASE IF EXISTS test_db_del;
      ```

### 表操作

1. 创建表
2. 删除表
3. 修改表结构
4. 插入数据
5. 修改数据
6. 删除数据

### SQL操作

## 3. 数据库的存储引擎

数据库存储引擎是数据库底层软件组件，数据库管理系统使用数据引擎进行创建、查询、更新和删除数据操作。不同的存储引擎提供不同的存储机制、索引技巧、锁定水平等功能，使用不同的存储引擎还可以获得特定的功能。

现在许多数据库管理系统都支持多种不同的存储引擎。MySQL 的核心就是存储引擎。

**InnoDB**

InnoDB 事务型数据库的首选引擎，**支持事务安全表（ACID），支持行锁定和外键。**MySQL 5.5.5 之后，InnoDB 作为默认存储引擎。

**MyISAM** 

MyISAM 是基于 ISAM 的存储引擎，并对其进行扩展，是在 Web、数据仓储和其他应用环境下最常使用的存储引擎之一。**MyISAM 拥有较高的插入、查询速度，但不支持事务。**

MEMORY 存储引擎**将表中的数据存储到内存中，为查询和引用其他数据提供快速访问。**

**查看存储引擎**

SHOW ENGINES 语句来显示可用的数据库引擎和默认引擎。

MySQL 提供了多个不同的存储引擎，包括处理事务安全表的引擎和处理非事务安全表的引擎。在 MySQL 中，不需要在整个服务器中使用同一种存储引擎，针对具体的要求，**可以对每一个表使用不同的存储引擎**。