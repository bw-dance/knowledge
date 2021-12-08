# Mysql的基本使用

## 1. Mysql的安装

[(97条消息) mysql8.0.25安装配置教程(windows 64位)最详细！！！！_@WWWxp的博客-CSDN博客_mysql安装配置教程8.0.25](https://blog.csdn.net/weixin_43579015/article/details/117228159?utm_source=app&app_version=4.19.1&code=app_1562916241&uLinkId=usr1mkqgl919blen)

## 2. Mysql基本操作

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

6. **选择数据库 `USER 数据库名称`**

7. **修改数据库**

   1. **ALTER DATABASE** 来修改已经被创建或者存在的数据库的相关参数

   2. ```sql
      ALTER DATABASE [数据库名] { 
      [ DEFAULT ] CHARACTER SET <字符集名> |
      [ DEFAULT ] COLLATE <校对规则名>}
      
      ALTER DATABASE 用于更改数据库的全局特性。
      使用 ALTER DATABASE 需要获得数据库 ALTER 权限。
      数据库名称可以忽略，此时语句对应于默认数据库。
      CHARACTER SET 子句用于更改默认的数据库字符集。
      
      
      //创建一个数据库 test_db
      ALTER DATABASE test_db
             DEFAULT CHARACTER SET gb2312
             DEFAULT COLLATE gb2312_chinese_ci;
      ```

8. **删除数据库 `drop database '数据库名称'`**

   1. ```sql
      DROP DATABASE [ IF EXISTS ] <数据库名>
      
      <数据库名>：指定要删除的数据库名。
          
      IF EXISTS：用于防止当数据库不存在时发生错误。
          
      DROP DATABASE：删除数据库中的所有表格并同时删除数据库。使用此语句时要非常小心，以免错误删除。如果要使用 DROP DATABASE，需要获得数据库 DROP 权限。
      
      //创建数据库
      CREATE DATABASE test_db_del;
      //删除数据库
      DROP DATABASE test_db_del;
      //删除数据库
      DROP DATABASE IF EXISTS test_db_del;
      ```

9. **查看存储引擎**`SHOW ENGINES`

### 表操作

1. **创建表**

   1. ```java
      CREATE TABLE <表名> ([表定义选项])[表选项][分区选项];
      
      [表定义选项]的格式为：
      <列名1> <类型1> [,…] <列名n> <类型n>
          
      CREATE TABLE：用于创建给定名称的表，必须拥有表CREATE的权限。
          
      <表名>：指定要创建表的名称，在 CREATE TABLE 之后给出，必须符合标识符命名规则。表名称被指定为 db_name.tbl_name，以便在特定的数据库中创建表。无论是否有当前数据库，都可以通过这种方式创建。
      
      在当前数据库中创建表时，可以省略 db-name。如果使用加引号的识别名，则应对数据库和表名称分别加引号。例如，'mydb'.'mytbl' 是合法的，但 'mydb.mytbl' 不合法。
       
      <表定义选项>：表创建定义，由列名（col_name）、列的定义（column_definition）以及可能的空值说明、完整性约束或表索引组成。
      默认的情况是，表被创建到当前的数据库中。若表已存在、没有当前数据库或者数据库不存在，则会出现错误。
      ```

   2. 注意：数据表中每个列（字段）的名称和数据类型，如果创建多个列，要用逗号隔开。

   3. ```java
      CREATE TABLE tb_emp1
          (
          id INT(11),
          name VARCHAR(25),
          deptId INT(11),
          salary FLOAT
          );
      ```

   4. ![image-20211208201025962](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211208201025962.png)

      1. Null：表示该列是否可以存储 NULL 值。
      2. Key：表示该列是否已编制索引。PRI 表示该列是表主键的一部分，UNI 表示该列是 UNIQUE 索引的一部分，MUL 表示在列中某个给定值允许出现多次。
      3. Default：表示该列是否有默认值，如果有，值是多少。
      4. Extra：表示可以获取的与给定列有关的附加信息，如 AUTO_INCREMENT 等。

2. **查看当前库的表**:` show tables`

3. **查看表结构**：` DESCRIBE <表名>;`

   1. 可以查看表的字段信息，包括字段名、字段数据类型、是否为主键、是否有默认值等

4. **查看表的创建语句：**`SHOW CREATE TABLE tb_emp1\G；`

   1. ```java
      Create Table: CREATE TABLE `tb_emp1` (
        `id` int(11) DEFAULT NULL,
        `name` varchar(25) DEFAULT NULL,
        `deptId` int(11) DEFAULT NULL,
        `salary` float DEFAULT NULL
      ) ENGINE=InnoDB DEFAULT CHARSET=utf8
      ```

5. **修改表**

   1. ALTER TABLE <表名> [修改选项]

   2. ```sql
      修改选项的语法格式如下：
      { ADD COLUMN <列名> <类型>
      | CHANGE COLUMN <旧列名> <新列名> <新列类型>
      | ALTER COLUMN <列名> { SET DEFAULT <默认值> | DROP DEFAULT }
      | MODIFY COLUMN <列名> <类型>
      | DROP COLUMN <列名>
      | RENAME TO <新表名> }
      ```

   3. **添加字段**

      1. ALTER TABLE <表名> ADD <新字段名> <数据类型> [约束条件] [FIRST|AFTER 已存在的字段名]；

      2. ```sql
         新字段名为需要添加的字段的名称； FIRST 为可选参数，其作用是将新添加的字段设置为表的第一个字段； AFTER 为可选参数，其作用是将新添加的字段添加到指定的已存在的字段名的后面。
         
         在表的第一列添加一个 int 类型的字段 col1
         ALTER TABLE tb_emp1
         ADD COLUMN col1 INT FIRST;
         
         在一列 name 后添加一个 int 类型的字段 col2
         ALTER TABLE tb_emp1
              ADD COLUMN col2 INT AFTER name;
         ```

   4. **修改字段数据类型**

      1. ALTER TABLE <表名> MODIFY <字段名> <数据类型>

      2. ```sql
         表名指要修改数据类型的字段所在表的名称， 字段名指需要修改的字段， 数据类型指修改后字段的新数据类型。
         
         将 name 字段的数据类型由 VARCHAR(22) 修改成 VARCHAR(30)
         ALTER TABLE tb_emp1
               MODIFY name VARCHAR(30);
         ```

   5. **删除字段**

      1. ALTER TABLE <表名> DROP <字段名>；

      2. ```sql
         字段名指需要从表中删除的字段的名称。
         
         删除 col2 字段
         ALTER TABLE tb_emp1
               DROP col2;
         ```

   6. **修改字段名称**

      1. ALTER TABLE <表名> CHANGE <旧字段名> <新字段名> <新数据类型>；

      2. ```java
         旧字段名指修改前的字段名； 新字段名指修改后的字段名； 新数据类型指修改后的数据类型，如果不需要修改字段的数据类型，可以将新数据类型设置成与原来一样，但数据类型不能为空。
             
         将 col1 字段名称改为 col3，同时将数据类型变为 CHAR(30)，
         ALTER TABLE tb_emp1
               CHANGE col1 col3 CHAR(30);
         ```

      3. 注意：由于不同类型的数据在机器中的存储方式及长度并不相同，修改数据类型可能会影响数据表中已有的数据记录，因此，当数据表中已经有数据时，不要轻易修改数据类型。

   7. **修改表名**：

      1. ALTER TABLE <旧表名> RENAME [TO] <新表名>；

      2. ```sql
         TO 为可选参数，使用与否均不影响结果。
         
         ALTER TABLE tb_emp1
               RENAME TO tb_emp2;
         ```

6. **删除表** 

   1. DROP TABLE [IF EXISTS] 表名1 [ ,表名2, 表名3 ...]

   2. ```sql
      表名1, 表名2, 表名3 ...表示要被删除的数据表的名称。DROP TABLE 可以同时删除多个表，只要将表名依次写在后面，相互之间用逗号隔开即可。
          
      IF EXISTS 用于在删除数据表之前判断该表是否存在。如果不加 IF EXISTS，当数据表不存在时 MySQL 将提示错误，中断 SQL 语句的执行；加上 IF EXISTS 后，当数据表不存在时 SQL 语句可以顺利执行，但是会发出警告（warning）。
          
      //创建表    
      CREATE TABLE tb_emp3
            (
            id INT(11),
            name VARCHAR(25),
            deptId INT(11),
            salary FLOAT
            );
      //删除表
      DROP TABLE tb_emp3;
      ```


### SQL操作

#### 练习资源

**表**

```sql
CREATE TABLE `tb_students_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id\r\n',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '姓名',
  `dept_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '部门id',
  `age` smallint(6) NOT NULL DEFAULT 0 COMMENT '年龄',
  `sex` bit(1) NOT NULL DEFAULT b'0' COMMENT '性别  0 男  1 女',
  `height` smallint(6) NOT NULL DEFAULT 0 COMMENT '身高',
  `login_date` datetime NULL DEFAULT NULL COMMENT '登陆时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;
```

**数据**

```sql
INSERT INTO `tb_students_info` VALUES (1, 'Dany', 1, 25, b'1', 160, '2021-12-08 23:25:43');
INSERT INTO `tb_students_info` VALUES (2, 'Green', 3, 23, b'0', 158, '2021-12-08 23:26:22');
INSERT INTO `tb_students_info` VALUES (3, 'Henry', 2, 23, b'1', 185, '2021-12-08 23:27:08');
INSERT INTO `tb_students_info` VALUES (4, 'Jane', 1, 22, b'1', 162, '2021-12-08 23:27:44');
INSERT INTO `tb_students_info` VALUES (5, 'Jim', 6, 21, b'0', 175, '2021-12-08 23:27:47');
INSERT INTO `tb_students_info` VALUES (6, 'John', 5, 25, b'1', 172, '2021-12-04 23:27:52');
INSERT INTO `tb_students_info` VALUES (7, 'Lily', 1, 22, b'0', 165, '2021-11-30 23:27:57');
INSERT INTO `tb_students_info` VALUES (8, 'Susan', 1, 20, b'1', 170, '2021-11-02 23:28:01');
INSERT INTO `tb_students_info` VALUES (9, 'Thomas', 4, 35, b'0', 178, '2021-12-03 23:28:06');
INSERT INTO `tb_students_info` VALUES (10, 'Tom', 3, 15, b'1', 165, '2021-12-26 23:28:10');
```

#### 查询(select)

```sql
SELECT
{* | <字段列名>}
[
FROM <表 1>, <表 2>…
[WHERE <表达式>
[GROUP BY <group by definition>
[HAVING <expression> [{<operator> <expression>}…]]
[ORDER BY <order by definition>]
[LIMIT[<offset>,] <row count>]
]
`{*|<字段列名>}`包含星号通配符的字段列表，表示查询的字段，其中字段列至少包含一个字段名称，如果要查询多个字段，多个字段之间要用逗号隔开，最后一个字段后不要加逗号。
`FROM <表 1>，<表 2>…`，表 1 和表 2 表示查询数据的来源，可以是单个或多个。
WHERE 子句是可选项，如果选择该项，将限定查询行必须满足的查询条件。
`GROUP BY< 字段 >`，该子句告诉 MySQL 如何显示查询出来的数据，并按照指定的字段分组。
`[ORDER BY< 字段 >]`，该子句告诉 MySQL 按什么样的顺序显示查询出来的数据，可以进行的排序有升序（ASC）和降序（DESC）。
`[LIMIT[<offset>，]<row count>]`，该子句告诉 MySQL 每次显示查询出来的数据条数。
```

**查询语句**

```sql
# 查询 tb_students_info 表中的所有数据
SELECT * FROM tb_students_info;
SELECT id,name,dept_id,age,sex,height,login_date FROM tb_students_info;
# 查询 tb_students_info 表中 name 列所有学生的姓名
SELECT name FROM tb_students_info;
# 从 tb_students_info 表中获取 id、name 和 height 三列
SELECT id,name,height FROM tb_students_info;
```

#### 去重(distinct)

SELECT DISTINCT <字段名> FROM <表名>;

作用：DISTINCT 关键字指示 MySQL 消除重复的记录值

```sql
# 查询 tb_students_info 表中所有 age 
SELECT age FROM tb_students_info;
# 查询 tb_students_info 表中 age 字段的值，返回 age 字段的值且不得重复
SELECT  DISTINCT age FROM tb_students_info;
```

#### 别名(as)

**表的别名**

<表名> [AS] <别名>

作用：当表名很长或者执行一些特殊查询的时候，为了方便操作或者需要多次使用相同的表时，可以为表指定别名，用这个别名代替表原来的名称。

注意：在为表取别名时，要保证不能与数据库中的其他表的名称冲突。

```sql
# 为 tb_students_info 表取别名 stu
SELECT stu.name,stu.height FROM tb_students_info AS stu;
```

**列的别名**

<列名> [AS] <列别名>

```sql
# 查询 tb_students_info 表，为 name 取别名 student_name，为 age 取别名student_age
SELECT name AS student_name,age AS student_age FROM tb_students_info;
```

注意：表别名只在执行查询时使用，并不在返回结果中显示，而列定义别名之后，将返回给客户端显示，显示的结果字段为字段列的别名。

#### 限制查询数(limit)

<LIMIT> [<位置偏移量>,] <行数>

第一个参数“位置偏移量”指示 MySQL 从哪一行开始显示，是一个可选参数，如果不指定“位置偏移量”，将会从表中的第一条记录开始（第一条记录的位置偏移量是 0，第二条记录的位置偏移量是 1，以此类推）；第二个参数“行数”指示返回的记录条数。

作用：SELECT 语句时往往返回的是所有匹配的行，有些时候我们仅需要返回第一行或者前几行，这时候就需要用到 MySQL LIMIT 子句。

注意：MySQL 5.7 中可以使用“LIMIT 4 OFFSET 3”，意思是获取从第5条记录开始的后面的3条记录，和“LIMIT 4，3”返回的结果相同

```sql
# 显示 tb_students_info 表查询结果的前 4 行
SELECT * FROM tb_students_info LIMIT 4;
# 在 tb_students_info 表中，使用 LIMIT 子句返回从第 4 条记录开始的行数为 5 的记录
SELECT * FROM tb_students_info LIMIT 3,5;
```

#### 排序(order by)

ORDER BY {<列名> | <表达式> | <位置>} [ASC|DESC]

 **列名：**指定用于排序的列。可以指定多个列，列名之间用逗号分隔。

**表达式：**指定用于排序的表达式。

**位置：**指定用于排序的列在 SELECT 语句结果集中的位置，通常是一个正整数。

**ASC|DESC：**关键字 `ASC` 表示按升序分组，关键字 `DESC` 表示按降序分组，其中 `ASC` 为默认值。这两个关键字必须位于对应的列名、表达式、列的位置之后。

使用 ORDER BY 子句注意：

- ORDER BY 子句中可以包含子查询。
- 当排序的值中存在空值时，ORDER BY 子句会将该**空值作为最小值**来对待。
- 当在 ORDER BY 子句中指定**多个列进行排序时**，MySQL 会按照列的顺序**从左到右依次进行排序**。
- 查询的数据并没有以一种特定的顺序显示，如果没有对它们进行排序，则将根据插入到数据表中的顺序显示。使用 ORDER BY 子句对指定的列数据进行排序。

```sql
# 查询 tb_students_info 表的 height 字段值，并对其进行排序
SELECT * FROM tb_students_info ORDER BY height;
# 查询 tb_students_info 表中的 name 和 height 字段，先按 height 排序，再按 name 排序
SELECT name,height FROM tb_students_info ORDER BY height,name;
```

注意：在对多列进行排序时，首行排序的**第一列必须有相同的列值，才会对第二列进行排序**。如果第一列数据中所有的值都是唯一的，将不再对第二列进行排序。

默认情况下，查询数据按字母升序进行排序（A～Z），但数据的排序并不仅限于此，还可以使用 ORDER BY 对查询结果进行降序排序（Z～A），这可以通过关键字 DESC 实现。可以对多列进行不同的顺序排序。

#### 条件查询(where)

WHERE <查询条件> {<判定运算1>，<判定运算2>，…}

判定运算其结果取值为 TRUE、FALSE 和 UNKNOWN。

```sq
判定运算的语法分类如下：
<表达式1>{=|<|<=|>|>=|<=>|<>|！=}<表达式2>
<表达式1>[NOT]LIKE<表达式2>
<表达式1>[NOT][REGEXP|RLIKE]<表达式2>
<表达式1>[NOT]BETWEEN<表达式2>AND<表达式3>
<表达式1>IS[NOT]NULL
```

##### 普通查询

```sql
在表 tb_students_info 中查询身高为 170cm 的学生的姓名
SELECT name,height
    -> FROM tb_students_info
    -> WHERE height=170;
    
    查询年龄小于 22 的学生的姓名
    
    SELECT name,age
    -> FROM tb_students_info
    -> WHERE age<22;
```

##### 多条件的查询语句

```sql
在 tb_students_info 表中查询 age 大于 21，并且 height 大于等于 175 的学生的信息
SELECT * FROM tb_students_info
    -> WHERE age>21 AND height>=175;
    
```

##### 模糊查询

<表达式1> [NOT] LIKE <表达式2>

使用运算符 LIKE 设置过滤条件，过滤条件使用通配符进行匹配运算

匹配运算的对象可以是 CHAR、VARCHAR、TEXT、DATETIME 等数据类型

**百分号（%）**

百分号是 MySQL 中常用的一种通配符，在过滤条件中，百分号可以表示任何字符串，并且该字符串可以出现任意次。

使用百分号通配符要注意以下几点：

- MySQL 默认是不区分大小写的，若要区分大小写，则需要更换字符集的校对规则。
- 百分号不匹配空值。
- 百分号可以代表搜索模式中给定位置的 0 个、1 个或多个字符。
- 尾空格可能会干扰通配符的匹配，一般可以在搜索模式的最后附加一个百分号。

**下划线（_）**

下划线通配符和百分号通配符的用途一样，下画线只匹配单个字符，而不是多个字符，也不是 0 个字符。

**注意：**不要过度使用通配符，对通配符检索的处理一般会比其他检索方式花费更长的时间

```sql
# 在 tb_students_info 表中，查找所有以“T”字母开头的学生姓名，
SELECT name FROM tb_students_info WHERE name LIKE 'T%';
# 在 tb_students_info 表中，查找所有包含“e”字母的学生姓名
SELECT name FROM tb_students_info WHERE name LIKE '%e%';
# 在 tb_students_info 表中，查找所有以字母“y”结尾，且“y”前面只有 4 个字母的学生的姓名，
SELECT name FROM tb_students_info WHERE name LIKE '____y';
```

##### BETWEEN AND 作为条件的查询语句

```sql
# 在表 tb_students_info 中查询注册日期在 2021-12-01 之前的学生的信息
SELECT * FROM tb_students_info WHERE login_date<'2021-12-01';
# 在表 tb_students_info 中查询注册日期在 2021-11-01 和 2021-12-12 之间的学生的信息
SELECT * FROM tb_students_info WHERE login_date BETWEEN '2015-10-01' AND '2016-05-01';
```

#### 内连接查询(inner join)

SELECT <列名1，列名2 …>
FROM <表名1> INNER JOIN <表名2> [ ON子句]

在 [MySQL](http://www.voidme.com/mysql) FROM 子句中使用关键字 INNER JOIN 连接两张表，并使用 ON 子句来设置连接条件。

在 FROM 子句中可以在多个表之间连续使用 INNER JOIN 或 JOIN，如此可以同时实现多个表的内连接。

``` sql
# 表 tb_students_info 和表 tb_departments 都包含相同数据类型的字段 dept_id，在两个表之间使用内连接查询。
SELECT id,name,age,dept_name FROM tb_students_info,tb_departments WHERE tb_students_info.dept_id=tb_departments.dept_id;
# 在 tb_students_info 表和 tb_departments 表之间，使用 INNER JOIN 语法进行内连接查询
SELECT id,name,age,dept_name FROM tb_students_info INNER JOIN tb_departments
WHERE tb_students_info.dept_id=tb_departments.dept_id;
```

注意：使用 WHERE 子句定义连接条件比较简单明了，而 INNER JOIN 语法是 ANSI SQL 的标准规范，使用 INNER JOIN 连接语法能够确保不会忘记连接条件，而且 WHERE 子句在某些时候会影响查询的性能。

#### 外连接查询(outer join)

[MySQL](http://www.voidme.com/mysql) 中 [内连接](http://www.voidme.com/mysql/mysql-inner-join)是在交叉连接的结果集上返回满足条件的记录；而外连接先将连接的表分为基表和参考表，再以基表为依据返回满足和不满足条件的记录。

外连接更加注重两张表之间的关系。按照连接表的顺序，可以分为左外连接和右外连接。

左外连接又称为左连接，在 FROM 子句中使用关键字 LEFT OUTER JOIN 或者 LEFT JOIN，用于接收该关键字左表（基表）的所有行，并用这些行与该关键字右表（参考表）中的行进行匹配，即匹配左表中的每一行及右表中符合条件的行。

在左外连接的结果集中，除了匹配的行之外，还包括左表中有但在右表中不匹配的行，对于这样的行，从右表中选择的列的值被设置为 NULL，即左外连接的结果集中的 NULL 值表示右表中没有找到与左表相符的记录。

``` sql
# 在 tb_students_info 表和 tb_departments 表中查询所有学生，包括没有学院的学生
SELECT name,dept_name
    FROM tb_students_info s
    LEFT OUTER JOIN tb_departments d
    ON s.dept_id = d.dept_id;
```

右外连接又称为右连接，在 FROM 子句中使用 RIGHT OUTER JOIN 或者 RIGHT JOIN。与左外连接相反，右外连接以右表为基表，连接方法和左外连接相同。在右外连接的结果集中，除了匹配的行外，还包括右表中有但在左表中不匹配的行，对于这样的行，从左表中选择的值被设置为 NULL。

```sql
SELECT name,dept_name
     FROM tb_students_info s
     RIGHT OUTER JOIN tb_departments d
     ON s.dept_id = d.dept_id;
```



#### 子查询(in/exist)

## 子查询中常用的运算符

**IN子查询**

结合关键字 IN 所使用的子查询主要用于判断一个给定值是否存在于子查询的结果集中。

<表达式> [NOT] IN <子查询>

语法:

- `<表达式>`：用于指定表达式。当表达式与子查询返回的结果集中的某个值相等时，返回 TRUE，否则返回 FALSE；若使用关键字 NOT，则返回的值正好相反。
- `<子查询>`：用于指定子查询。这里的子查询只能返回一列数据。对于比较复杂的查询要求，可以使用 SELECT 语句实现子查询的多层嵌套。

**比较运算符子查询**

比较运算符所使用的子查询主要用于对表达式的值和子查询返回的值进行比较运算。其语法格式为：

<表达式> {= | < | > | >= | <= | <=> | < > | != }
{ ALL | SOME | ANY} <子查询>

语法:

- `<子查询>`：用于指定子查询。
- `<表达式>`：用于指定要进行比较的表达式。
- `ALL`、`SOME` 和 `ANY`：可选项。用于指定对比较运算的限制。其中，关键字 ALL 用于指定表达式需要与子查询结果集中的每个值都进行比较，当表达式与每个值都满足比较关系时，会返回 TRUE，否则返回 FALSE；关键字 SOME 和 ANY 是同义词，表示表达式只要与子查询结果集中的某个值满足比较关系，就返回 TRUE，否则返回 FALSE。

**EXIST子查询**

关键字 EXIST 所使用的子查询主要用于判断子查询的结果集是否为空。

EXIST <子查询>

若子查询的结果集不为空，则返回 TRUE；否则返回 FALSE。

``` sql
# 在 tb_departments 表中查询 dept_type 为 A 的学院 ID，并根据学院 ID 查询该学院学生的名字
SELECT name FROM tb_students_info
     WHERE dept_id IN
     (SELECT dept_id
     FROM tb_departments
     WHERE dept_type= 'A' );
# 或者
SELECT dept_id
     FROM tb_departments
     WHERE dept_type='A';
     
SELECT name FROM tb_students_info
       WHERE dept_id IN(1,2);
# 使用 NOT IN 关键字
SELECT name FROM tb_students_info
     WHERE dept_id NOT IN
     (SELECT dept_id
     FROM tb_departments
     WHERE dept_type='A');
# 在 tb_departments 表中查询 dept_name 等于“Computer”的学院 id，然后在 tb_students_info 表中查询所有该学院的学生的姓名
SELECT name FROM tb_students_info
     WHERE dept_id =
     (SELECT dept_id
     FROM tb_departments
     WHERE dept_name='Computer');
# 查询 tb_departments 表中是否存在 dept_id=1 的供应商，如果存在，就查询 tb_students_info 表中的记录
SELECT * FROM tb_students_info
     WHERE EXISTS
     (SELECT dept_name
     FROM tb_departments
     WHERE dept_id=1);
# 查询 tb_departments 表中是否存在 dept_id=7 的供应商，如果存在，就查询 tb_students_info 表中的记录
SELECT * FROM tb_students_info
     WHERE EXISTS
     (SELECT dept_name
     FROM tb_departments
     WHERE dept_id=7);
```

#### 分组查询(group by)

使用 GROUP BY 子句，将结果集中的数据行根据选择列的值进行逻辑分组，以便能汇总表内容的子集，实现对每个组而不是对整个结果集进行整合。

GROUP BY { <列名> | <表达式> | <位置> } [ASC | DESC]

``` sql
`<列名>`：指定用于分组的列。可以指定多个列，彼此间用逗号分隔。
`<表达式>`：指定用于分组的表达式。通常与聚合函数一块使用，例如可将表达式 COUNT(*)AS' 人数 ' 作为 SELECT 选择列表清单的一项。
`<位置>`：指定用于分组的选择列在 SELECT 语句结果集中的位置，通常是一个正整数。例如，GROUP BY 2 表示根据 SELECT 语句列清单上的第 2 列的值进行逻辑分组。
`ASC|DESC`：关键字 ASC 表示按升序分组，关键字 DESC 表示按降序分组，其中 ASC 为默认值，注意这两个关键字必须位于对应的列名、表达式、列的位置之后。
```

注意：GROUP BY 子句中的各选择列必须也是 SELECT 语句的选择列清单中的一项。

使用注意：

- GROUP BY 子句可以包含任意数目的列，使其可以对分组进行嵌套，为数据分组提供更加细致的控制。
- GROUP BY 子句列出的每个列都必须是检索列或有效的表达式，但不能是聚合函数。若在 SELECT 语句中使用表达式，则必须在 GROUP BY 子句中指定相同的表达式。
- 除聚合函数之外，SELECT 语句中的每个列都必须在 GROUP BY 子句中给出。
- 若用于分组的列中包含有 NULL 值，则 NULL 将作为一个单独的分组返回；若该列中存在多个 NULL 值，则将这些 NULL 值所在的行分为一组。

```sql
# 根据 dept_id 对 tb_students_info 表中的数据进行分组，将每个学院的学生姓名显示出来
SELECT dept_id,GROUP_CONCAT(name) AS names
     FROM tb_students_info
     GROUP BY dept_id;
```

#### 过滤条件(having)

使用 HAVING 子句过滤分组，在结果集中规定了包含哪些分组和排除哪些分组。

HAVING <条件>

``` sql
HAVING 子句和 WHERE 子句非常相似，HAVING 子句支持 WHERE 子句中所有的操作符和语法，但是两者存在几点差异：
WHERE 子句主要用于过滤数据行，而 HAVING 子句主要用于过滤分组，即 HAVING 子句基于分组的聚合值而不是特定行的值来过滤数据，主要用来过滤分组。
WHERE 子句不可以包含聚合函数，HAVING 子句中的条件可以包含聚合函数。
HAVING 子句是在数据分组后进行过滤，WHERE 子句会在数据分组前进行过滤。WHERE 子句排除的行不包含在分组中，可能会影响 HAVING 子句基于这些值过滤掉的分组。
```

```sql
# 根据 dept_id 对 tb_students_info 表中的数据进行分组，并显示学生人数大于1的分组信息
SELECT dept_id,GROUP_CONCAT(name) AS names
    FROM tb_students_info
    GROUP BY dept_id
    HAVING COUNT(name)>1;
```



#### 正则查询(regexp)

``` sql
```

#### 插入数据(insert)

``` sql
```

#### 修改数据(update)

``` sql
```

#### MySQL删除数据(delete)

``` sql
```



## 3. 数据库的存储引擎

**介绍：**

数据库存储引擎是数据库底层软件组件，数据库管理系统使用数据引擎进行创建、查询、更新和删除数据操作。

不同的存储引擎提供不同的存储机制、索引技巧、锁定水平等功能，使用不同的存储引擎还可以获得特定的功能。

现在许多数据库管理系统都支持多种不同的存储引擎。MySQL 的核心就是存储引擎。

MySQL 提供了多个不同的存储引擎，包括处理事务安全表的引擎和处理非事务安全表的引擎。在 MySQL 中，不需要在整个服务器中使用同一种存储引擎，针对具体的要求，**可以对每一个表使用不同的存储引擎**。

**常见的存储引擎：**

1. InnoDB：

   InnoDB 事务型数据库的首选引擎，**支持事务安全表（ACID），支持行锁定和外键。**MySQL 5.5.5 之后，**InnoDB 作为默认存储引擎。**

2. MyISAM ：

   MyISAM 是基于 ISAM 的存储引擎，并对其进行扩展，是在 Web、数据仓储和其他应用环境下最常使用的存储引擎之一。**MyISAM 拥有较高的插入、查询速度，但不支持事务。**

3. MEMORY：

   MEMORY存储引擎**将表中的数据存储到内存中，为查询和引用其他数据提供快速访问。**

| 功能             | MylSAM | MEMORY | InnoDB | Archive |
| :--------------- | :----- | :----- | :----- | :------ |
| 存储限制         | 256TB  | RAM    | 64TB   | None    |
| **支持事务**     | No     | No     | Yes    | No      |
| **支持全文索引** | Yes    | No     | No     | No      |
| **支持树索引**   | Yes    | Yes    | Yes    | No      |
| 支持哈希索引     | No     | Yes    | No     | No      |
| **支持数据缓存** | No     | N/A    | Yes    | No      |
| **支持外键**     | No     | No     | Yes    | No      |

**选择 MySQL存储引擎原则：**

- 如果要提供提交、回滚和恢复的**事务**安全（ACID 兼容）能力，并要求实现并发控制，InnoDB 是一个很好的选择。
- 如果数据表主要用来**插入和查询记录**，则 MyISAM 引擎提供较高的处理效率。
- 如果只是**临时存放数据，数据量不大，并且不需要较高的数据安全性，**可以选择将数据保存在内存的 MEMORY 引擎中，MySQL 中使用该引擎作为临时表，存放查询的中间结果。
- 如果只有 INSERT 和 SELECT 操作，可以选择Archive 引擎，**Archive 存储引擎支持高并发的插入操作**，但是本身并不是事务安全的。Archive 存储引擎非常适合存储归档数据，**如记录日志信息可以使用 Archive 引擎**。

## 4. Mysql数据类型

MySQL 的数据类型有大概可以分为 5 种，分别是整数类型、浮点数类型和定点数类型、日期和时间类型、字符串类型、二进制类型等。

注意：整数类型和浮点数类型可以统称为数值数据类型。

1) **数值类型**

整数类型包括 TINYINT、SMALLINT、MEDIUMINT、INT、BIGINT，浮点数类型包括 FLOAT 和 DOUBLE，定点数类型为 DECIMAL。

2) **日期/时间类型**

包括 YEAR、TIME、DATE、DATETIME 和 TIMESTAMP。

3) **字符串类型**

包括 CHAR、VARCHAR、BINARY、VARBINARY、BLOB、TEXT、ENUM 和 SET 等。

4) **二进制类型**

包括 BIT、BINARY、VARBINARY、TINYBLOB、BLOB、MEDIUMBLOB 和 LONGBLOB。

### 整数类型

整数类型又称数值型数据，数值型数据类型主要用来存储数字。

MySQL 提供了多种数值型数据类型，不同的数据类型提供不同的取值范围，可以存储的值范围越大，所需的存储空间也会越大。

MySQL 主要提供的整数类型有 **TINYINT**、 **SMALLINT**、 **MEDIUMINT**、 **INT**、 **BIGINT**，其属性字段可以添加 AUTO_INCREMENT 自增约束条件。

| 类型名称      | 无符号范围                                | 有符号范围              | 存储需求 | 默认长度 |
| :------------ | :---------------------------------------- | :---------------------- | -------- | -------- |
| TINYINT       | -128〜127                                 | 0 〜255                 | 1个字节  | 4        |
| SMALLINT      | -32768〜32767                             | 0〜65535                | 2个字节  | 6        |
| MEDIUMINT     | -8388608〜8388607                         | 0〜16777215             | 3个字节  | 9        |
| INT (INTEGER) | -2147483648〜2147483647                   | 0〜4294967295           | 4个字节  | 11       |
| BIGINT        | -9223372036854775808〜9223372036854775807 | 0〜18446744073709551615 | 8个字节  | 20       |

### 小数类型

浮点类型有两种，分别是单精度浮点数（ **FLOAT**）和双精度浮点数（ **DOUBLE**）；定点类型只有一种，就是 **DECIMAL**。

浮点类型和定点类型都可以用 `(M, D)`来表示，其中 `M`称为精度，表示总共的位数； `D`称为标度，表示小数的位数。

浮点数类型的取值范围为 M（1～255）和 D（1～30，且不能大于 M-2），分别表示显示宽度和小数位数。M 和 D 在 FLOAT 和DOUBLE 中是可选的，FLOAT 和 DOUBLE 类型将被保存为硬件所支持的最大精度。DECIMAL 的默认 D 值为 0、M 值为 10。

注意：如果DECIMAL 的D值为0，如果传进来小数，会四舍五入保存数据

在 MySQL 中，定点数以字符串形式存储，在对精度要求比较高的时候（如货币、科学数据），使用 DECIMAL 的类型比较好，另外两个浮点数进行减法和比较运算时也容易出问题，所以在使用浮点数时需要注意，并尽量避免做浮点数比较。

| 类型名称     | 说明                                       | 无符号范围                                       | 有符号范围                                         | 存储需求 |
| ------------ | ------------------------------------------ | ------------------------------------------------ | -------------------------------------------------- | -------- |
| FLOAT        | 单精度浮点                                 | -3.402823466E+38~-1.175494351E-38                | 0和1.175494351E-38~3.402823466E+38                 | 4个字节  |
| DOUBLE       | 双精度浮点                                 | -1.7976931348623157E-308~1.7976931348623157E+308 | 0和2.2250738585072014E-308~1.7976931348623157E+308 | 8个字节  |
| DECIMAL(M,D) | 压缩的'严格'定点数，就是指定了小数点后几位 |                                                  |                                                    |          |



### 时间和日期类型

MySQL 中表示日期的数据类型： **YEAR**、 **TIME**、 **DATE**、 **DTAETIME**、 **TIMESTAMP**。

每一个类型都有合法的取值范围，当指定确定不合法的值时，系统将“零”值插入数据库中。

| 类型名称         | 说明     | 日期格式            | 日期范围                                          | 存储需求 |
| :--------------- | -------- | :------------------ | :------------------------------------------------ | :------- |
| YEAR             | 年       | YYYY                | 1901 ~ 2155                                       | 1 个字节 |
| TIME             | 时间     | HH:MM:SS            | -838:59:59 ~ 838:59:59                            | 3 个字节 |
| DATE             | 日期     | YYYY-MM-DD          | 1000-01-01 ~ 9999-12-3                            | 3 个字节 |
| DATETIME（常用） | 日期时间 | YYYY-MM-DD HH:MM:SS | 1000-01-01 00:00:00 ~ 9999-12-31 23:59:59         | 8 个字节 |
| TIMESTAMP        | 日期时间 | YYYY-MM-DD HH:MM:SS | 1980-01-01 00:00:01 UTC ~ 2040-01-19 03:14:07 UTC | 4 个字节 |

TIMESTAMP 与 DATETIME 区别：

1. 存储需求，范围不同

2. DATETIME 在存储日期数据时，按实际输入的格式存储，即输入什么就存储什么，与时区无关；

3. 而 TIMESTAMP 值的存储是以 UTC（世界标准时间）格式保存的，存储时对当前时区进行转换，检索时再转换回当前时区。即查询时，根据当前时区的不同，显示的时间值是不同的。

协调世界时又称为世界统一时间、世界标准时间、国际协调时间。英文（CUT）和法文（TUC）的缩写不同，作为妥协，简称 UTC。

### 字符串类型和二进制

MySQL中的字符串类型有 **CHAR**、 **VARCHAR**、 **TINYTEXT**、 **TEXT**、 **MEDIUMTEXT**、 **LONGTEXT**、 **ENUM**、 **SET** 等。

 M表示可以为其指定长度。

VARCHAR 和 TEXT 类型是变长类型，其存储需求取决于列值的实际长度（在前面的表格中用 L 表示），而不是取决于类型的最大可能尺寸。

**加粗为二进制**

| **类型**          | **大小**              | **用途**                                     | **存储需求**                                               |
| :---------------- | :-------------------- | :------------------------------------------- | ---------------------------------------------------------- |
| CHAR              | 0-255 bytes           | 定长字符串                                   | M 字节，1<=M<=255                                          |
| VARCHAR           | 0-65535 bytes         | 变长字符串                                   | L+1字节，在此，L< = M和 1<=M<=255                          |
| **TINYBLOB**      | **0-255 bytes**       | **不超过 255 个字符的二进制字符串**          | **L+1 字节，在此，L<2^8**                                  |
| TINYTEXT          | 0-255 bytes           | 短文本字符串                                 | L+1字节，在此，L<2^8                                       |
| **BLOB**          | **0-65 535 bytes**    | **二进制形式的长文本数据**                   | **L+2 字节，在此，L<2^16**                                 |
| TEXT              | 0-65 535 bytes        | 长文本数据                                   | L+2字节，在此，L<2^16                                      |
| **MEDIUMBLOB**    | 0-16 777 215 bytes    | 二进制形式的中等长度文本数据                 | L+3 字节，在此，L<2^24                                     |
| MEDIUMTEXT        | 0-16 777 215 bytes    | 中等长度文本数据                             | L+3字节，在此，L<2^24                                      |
| **LONGBLOB**      | 0-4 294 967 295 bytes | 二进制形式的极大文本数据                     | L+4 字节，在此，L<2^32                                     |
| LONGTEXT          | 0-4 294 967 295 bytes | 极大文本数据                                 |                                                            |
| ENUM              |                       | 枚举类型，只能有一个枚举字符串值             | 1或2个字节，取决于枚举值的数目 (最大值为65535)             |
| SET               |                       | 一个设置，字符串对象可以有零个或 多个SET成员 | 1、2、3、4或8个字节，取决于集合 成员的数量（最多64个成员） |
| **BIT(M)**        | 1～64                 | 位字段类型                                   | 大约 (M+7)/8 字节                                          |
| **BINARY(M)**     | 1~8000                | 固定长度二进制字符串                         | M 字节                                                     |
| **VARBINARY (M)** | 1~8000                | 可变长度二进制字符串                         | M+1 字节                                                   |

一个 VARCHAR(10) 列能保存一个最大长度为 10 个字符的字符串，实际的存储需要字符串的长度 L 加上一个字节以记录字符串的长度。对于字符 “abcd”，L 是 4，而存储要求 5 个字节。

#### CHAR 和 VARCHAR 类型

CHAR(M) 为固定长度字符串，在定义时指定字符串列长。当保存时，在右侧填充空格以达到指定的长度。M 表示列的长度，范围是 0～255 个字符。例如，CHAR(4) 定义了一个固定长度的字符串列，包含的字符个数最大为 4。当检索到 CHAR 值时，尾部的空格将被删除。

VARCHAR(M) 是长度可变的字符串，M 表示最大列的长度，M 的范围是 0～65535。VARCHAR 的最大实际长度由最长的行的大小和使用的字符集确定，而实际占用的空间为字符串的实际长度加 1。

例如，VARCHAR(50) 定义了一个最大长度为 50 的字符串，如果插入的字符串只有 10 个字符，则实际存储的字符串为 10 个字符和一个字符串结束字符。VARCHAR 在值保存和检索时尾部的空格仍保留。

【实例】下面将不同的字符串保存到 CHAR(4) 和 VARCHAR(4) 列，说明 CHAR 和 VARCHAR 之间的差别，如下表所示。

| 插入值   | CHAR(4) | 存储需求 | VARCHAR(4) | 存储需求 |
| :------- | :------ | :------- | :--------- | :------- |
| ' '      | '  '    | 4字节    | ''         | 1字节    |
| 'ab'     | 'ab '   | 4字节    | 'ab'       | 3字节    |
| 'abc'    | 'abc '  | 4字节    | 'abc'      | 4字节    |
| 'abcd'   | 'abcd'  | 4字节    | 'abcd'     | 5字节    |
| 'abcdef' | 'abcd'  | 4字节    | 'abcd'     | 5字节    |

CHAR(4) 定义了固定长度为 4 的列，无论存入的数据长度为多少，所占用的空间均为 4 个字节。VARCHAR(4) 定义的列所占的字节数为实际长度加 1。

## 5. 常用运算符

MySQL 支持 4 种运算符，分别是：

### 算术运算符

执行算术运算，例如：加、减、乘、除等。

| 算术运算符 | 说明               |
| :--------- | :----------------- |
| +          | 加法运算           |
| -          | 减法运算           |
| *          | 乘法运算           |
| /          | 除法运算，返回商   |
| %          | 求余运算，返回余数 |

### 比较运算符

​		包括大于、小于、等于或者不等于，等等。主要用于数值的比较、字符串的匹配等方面。例如：LIKE、IN、BETWEEN AND 和 IS NULL 等都是比较运算符，还包括正则表达式的 REGEXP 也是比较运算符。

| 比较运算符        | 说明                               |
| :---------------- | :--------------------------------- |
| =                 | 等于                               |
| <                 | 小于                               |
| <=                | 小于等于                           |
| >                 | 大于                               |
| >=                | 大于等于                           |
| <=>               | 安全的等于，不会返回 UNKNOWN       |
| <> 或!=           | 不等于                             |
| IS NULL 或 ISNULL | 判断一个值是否为 NULL              |
| IS NOT NULL       | 判断一个值是否不为 NULL            |
| LEAST             | 当有两个或多个参数时，返回最小值   |
| GREATEST          | 当有两个或多个参数时，返回最大值   |
| BETWEEN AND       | 判断一个值是否落在两个值之间       |
| IN                | 判断一个值是IN列表中的任意一个值   |
| NOT IN            | 判断一个值不是IN列表中的任意一个值 |
| LIKE              | 通配符匹配                         |
| REGEXP            | 正则表达式匹配                     |

### 逻辑运算符

​		包括与、或、非和异或等逻辑运算符。其返回值为布尔型，真值（1 或 true）和假值（0 或 false）。

| 逻辑运算符   | 说明     |
| :----------- | :------- |
| NOT 或者 !   | 逻辑非   |
| AND 或者 &&  | 逻辑与   |
| OR 或者 \|\| | 逻辑或   |
| XOR          | 逻辑异或 |

### 位运算符

​		包括按位与、按位或、按位取反、按位异或、按位左移和按位右移等位运算符。位运算必须先将数据转换为二进制，然后在二进制格式下进行操作,运算完成后，将二进制的值转换为原来的类型，返回给用户。

| 位运算符 | 说明                   |
| :------- | :--------------------- |
| \|       | 按位或                 |
| &        | 按位与                 |
| ^        | 按位异或               |
| <<       | 按位左移               |
| >>       | 按位右移               |
| ~        | 按位取反，反转所有比特 |

### 优先级顺序

| 优先级由低到高排列 | 运算符                                                       |
| :----------------- | :----------------------------------------------------------- |
| 1                  | =(赋值运算）、:=                                             |
| 2                  | II、OR                                                       |
| 3                  | XOR                                                          |
| 4                  | &&、AND                                                      |
| 5                  | NOT                                                          |
| 6                  | BETWEEN、CASE、WHEN、THEN、ELSE                              |
| 7                  | =(比较运算）、<=>、>=、>、<=、<、<>、!=、 IS、LIKE、REGEXP、IN |
| 8                  | \|                                                           |
| 9                  | &                                                            |
| 10                 | <<、>>                                                       |
| 11                 | -(减号）、+                                                  |
| 12                 | *、/、%                                                      |
| 13                 | ^                                                            |
| 14                 | -(负号）、〜（位反转）                                       |
| 15                 | !                                                            |

