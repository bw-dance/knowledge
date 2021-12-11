# Mysql的基本使用

## 1. Mysql的安装

[(97条消息) mysql8.0.25安装配置教程(windows 64位)最详细！！！！_@WWWxp的博客-CSDN博客_mysql安装配置教程8.0.25](https://blog.csdn.net/weixin_43579015/article/details/117228159?utm_source=app&app_versiON=4.19.1&code=app_1562916241&uLinkId=usr1mkqgl919blen)

## 2. Mysql基本操作

### 数据库操作

1. **启动数据库服务** net start mysql 

1. **登陆账户** mysql -u root -p

2. **关闭数据库服务** net stop mysql

3. **查看所有数据库 SHOW DATABASES;**

5. **创建数据库** CREATE DATABASE "数据库名称"

   1. ```txt
      CREATE DATABASE [IF NOT EXISTS] <数据库名>
      [[DEFAULT] CHARACTER SET <字符集名>] 
      [[DEFAULT] COLLATE <校对规则名>];
      
      utf8mb4
      
      <数据库名>:创建数据库的名称。
      
      IF NOT EXISTS:在创建数据库之前进行判断,只有该数据库目前尚不存在时才能执行操作。
      
      [DEFAULT] CHARACTER SET:指定数据库的字符集。指定字符集的目的是为了避免在数据库中存储的数据出现乱码的情况。不指定字符集，使用系统的默认字符集。
      
      [DEFAULT] COLLATE:指定字符集的默认校对规则。
      ```
      
   2. MySQL 的字符集（CHARACTER）和校对规则（COLLATION）是两个不同的概念。字符集是用来定义 MySQL 存储字符串的方式，校对规则定义了比较字符串的方式。

   3. ````sql
      CREATE DATABASE test_db;
      
      CREATE DATABASE IF NOT EXISTS test_db;
      
      CREATE DATABASE IF NOT EXISTS test_db_char
             DEFAULT CHARACTER SET utf8mb4
             DEFAULT COLLATE utf8mb4_general_ci;
      ````

5. **查看数据库结构 SHOW CREATE DATABASE test_db_char;**

   1. 模糊匹配:SHOW DATABASES LIKE '%db'

   2. mysql自带数据库

      1. ```txt
         informatiON_schema:主要存储了系统中的一些数据库对象信息，比如用户表信息、列信息、权限信息、字符集信息和分区信息等。
             
         mysql:MySQL 的核心数据库，类似于 SQL Server 中的 mASter 表，主要负责存储数据库用户、用户访问权限等 MySQL 自己需要使用的控制和管理信息。常用的比如在 mysql 数据库的 user 表中修改 root 用户密码。
             
         performance_schema:主要用于收集数据库服务器性能参数。
             
         sakila:MySQL 提供的样例数据库，该数据库共有 16 张表，这些数据表都是比较常见的，在设计数据库时，可以参照这些样例数据表来快速完成所需的数据表。
             
         sys:MySQL 5.7 安装完成后会多一个 sys 数据库。sys 数据库主要提供了一些视图，数据都来自于 performatiON_schema，主要是让开发者和使用者更方便地查看性能问题。
             
         world:world 数据库是 MySQL 自动创建的数据库，该数据库中只包括 3 张数据表，分别保存城市，国家和国家使用的语言等内容。
         ```

6. **选择数据库 USE 数据库名称**

7. **修改数据库**

   1. **ALTER DATABASE** 来修改已经被创建或者存在的数据库的相关参数

   2. ```txt
      ALTER DATABASE [数据库名] { 
      [ DEFAULT ] CHARACTER SET <字符集名> |
      [ DEFAULT ] COLLATE <校对规则名>}
      
      ALTER DATABASE 用于更改数据库的全局特性。
      使用 ALTER DATABASE 需要获得数据库 ALTER 权限。
      数据库名称可以忽略，此时语句对应于默认数据库。
      CHARACTER SET 子句用于更改默认的数据库字符集。
      
      
      # 修改一个数据库 test_db
      ALTER DATABASE test_db
             DEFAULT CHARACTER SET gb2312
             DEFAULT COLLATE gb2312_chinese_ci;
      ```

9. **删除数据库 drop databASe '数据库名称'**

   1. ```text
      DROP DATABASE [ IF EXISTS ] <数据库名>
      
      <数据库名>:指定要删除的数据库名。
          
      IF EXISTS:用于防止当数据库不存在时发生错误。
          
      DROP DATABASE:删除数据库中的所有表格并同时删除数据库。使用此语句时要非常小心，以免错误删除。如果要使用 DROP DATABASE，需要获得数据库 DROP 权限。
      
      //创建数据库
      CREATE DATABASE test_db_del;
      //删除数据库
      DROP DATABASE test_db_del;
      //删除数据库
      DROP DATABASE IF EXISTS test_db_del;
      ```
      
      

9. **查看存储引擎**SHOW ENGINES

### 表操作

1. **创建表**

   1. ```text
      CREATE TABLE <表名> ([表定义选项])[表选项][分区选项];
      
      [表定义选项]的格式为:
      <列名1> <类型1> [,…] <列名n> <类型n>
          
      CREATE TABLE:用于创建给定名称的表，必须拥有表CREATE的权限。
          
      <表名>:指定要创建表的名称，在 CREATE TABLE 之后给出，必须符合标识符命名规则。表名称被指定为 db_name.tbl_name，以便在特定的数据库中创建表。无论是否有当前数据库，都可以通过这种方式创建。
      
      在当前数据库中创建表时，可以省略 db-name。如果使用加引号的识别名，则应对数据库和表名称分别加引号。例如，'mydb'.'mytbl' 是合法的，但 'mydb.mytbl' 不合法。
       
      <表定义选项>:表创建定义，由列名（col_name）、列的定义（column_definitiON）以及可能的空值说明、完整性约束或表索引组成。
      默认的情况是，表被创建到当前的数据库中。若表已存在、没有当前数据库或者数据库不存在，则会出现错误。
      ```

   2. 注意:数据表中每个列（字段）的名称和数据类型，如果创建多个列，要用逗号隔开。

   3. ```sql
      CREATE TABLE tb_emp1
          (
          id INT(11),
          name VARCHAR(25),
          deptId INT(11),
          salary FLOAT
          );
      ```

   4. ![image-20211208201025962](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211208201025962.png)

      1. Null:表示该列是否可以存储 NULL 值。
      2. Key:表示该列是否已编制索引。PRI 表示该列是表主键的一部分，UNI 表示该列是 UNIQUE 索引的一部分，MUL 表示在列中某个给定值允许出现多次。
      3. Default:表示该列是否有默认值，如果有，值是多少。
      4. Extra:表示可以获取的与给定列有关的附加信息，如 AUTO_INCREMENT 等。

2. **查看当前库的表**: show tables

3. **查看表结构**: DESCRIBE <表名>;

   1. 可以查看表的字段信息，包括字段名、字段数据类型、是否为主键、是否有默认值等

4. **查看表的创建语句:**SHOW CREATE TABLE tb_emp1\G；

   1. ```sql
      Create Table: CREATE TABLE tb_emp1 (
        id int(11) DEFAULT NULL,
        name varchar(25) DEFAULT NULL,
        deptId int(11) DEFAULT NULL,
        salary float DEFAULT NULL
      ) ENGINE=InnoDB DEFAULT CHARSET=utf8
      ```

5. **修改表**

   1. ALTER TABLE <表名> [修改选项]

   2. ```text
      修改选项的语法格式如下:
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
         # 新字段名为需要添加的字段的名称； FIRST 为可选参数，其作用是将新添加的字段设置为表的第一个字段； AFTER 为可选参数，其作用是将新添加的字段添加到指定的已存在的字段名的后面。
         
         # 在表的第一列添加一个 int 类型的字段 col1
         ALTER TABLE tb_emp1
         ADD COLUMN col1 INT FIRST;
         
         # 在一列 name 后添加一个 int 类型的字段 col2
         ALTER TABLE tb_emp1
              ADD COLUMN col2 INT AFTER name;
         ```

   4. **修改字段数据类型**

      1. ALTER TABLE <表名> MODIFY <字段名> <数据类型>

      2. ```sql
         # 表名指要修改数据类型的字段所在表的名称， 字段名指需要修改的字段， 数据类型指修改后字段的新数据类型。
         
         # 将 name 字段的数据类型由 VARCHAR(22) 修改成 VARCHAR(30)
         ALTER TABLE tb_emp1
               MODIFY name VARCHAR(30);
         ```

   5. **删除字段**

      1. ALTER TABLE <表名> DROP <字段名>；

      2. ```sql
         #字段名指需要从表中删除的字段的名称。
         
         # 删除 col2 字段
         ALTER TABLE tb_emp1
               DROP col2;
         ```

   6. **修改字段名称**

      1. ALTER TABLE <表名> CHANGE <旧字段名> <新字段名> <新数据类型>；

      2. ```sql
         # 旧字段名指修改前的字段名； 新字段名指修改后的字段名； 新数据类型指修改后的数据类型，如果不需要修改字段的数据类型，可以将新数据类型设置成与原来一样，但数据类型不能为空。
             
         # 将 col1 字段名称改为 col3，同时将数据类型变为 CHAR(30)，
         ALTER TABLE tb_emp1
               CHANGE col1 col3 CHAR(30);
         ```

      3. 注意:由于不同类型的数据在机器中的存储方式及长度并不相同，修改数据类型可能会影响数据表中已有的数据记录，因此，当数据表中已经有数据时，不要轻易修改数据类型。

   7. **修改表名**:

      1. ALTER TABLE <旧表名> RENAME [TO] <新表名>；

      2. ```sql
         # TO 为可选参数，使用与否均不影响结果。
         
         ALTER TABLE tb_emp1
               RENAME TO tb_emp2;
         ```

6. **删除表** 

   1. DROP TABLE [IF EXISTS] 表名1 [ ,表名2, 表名3 ...]

   2. ```sql
      # 表名1, 表名2, 表名3 ...表示要被删除的数据表的名称。DROP TABLE 可以同时删除多个表，只要将表名依次写在后面，相互之间用逗号隔开即可。
          
      # IF EXISTS 用于在删除数据表之前判断该表是否存在。如果不加 IF EXISTS，当数据表不存在时 MySQL 将提示错误，中断 SQL 语句的执行；加上 IF EXISTS 后，当数据表不存在时 SQL 语句可以顺利执行，但是会发出警告（warning）。
          
      # 创建表    
      CREATE TABLE tb_emp3
            (
            id INT(11),
            name VARCHAR(25),
            deptId INT(11),
            salary FLOAT
            );
      # 删除表
      DROP TABLE tb_emp3;
      ```


### SQL操作

#### 练习资源

**表**

**tb_students_info**

```sql
CREATE TABLE tb_students_info  (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id\r\n',
  name varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '姓名',
  dept_id bigint(20) NOT NULL DEFAULT 0 COMMENT '部门id',
  age smallint(6) NOT NULL DEFAULT 0 COMMENT '年龄',
  sex bit(1) NOT NULL DEFAULT b'0' COMMENT '性别  0 男  1 女',
  height smallint(6) NOT NULL DEFAULT 0 COMMENT '身高',
  login_date datetime NULL DEFAULT NULL COMMENT '登陆时间',
  PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;
```

**tb_departments**

```sql
CREATE TABLE tb_departments  (
  dept_id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门id',
  dept_type varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '部门类型',
  dept_name varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '部门名称',
  PRIMARY KEY (dept_id) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

```

**数据**

```sql
INSERT INTO tb_students_info VALUES (1, 'Dany', 1, 25, b'1', 160, '2021-12-08 23:25:43');
INSERT INTO tb_students_info VALUES (2, 'Green', 3, 23, b'0', 158, '2021-12-08 23:26:22');
INSERT INTO tb_students_info VALUES (3, 'Henry', 2, 23, b'1', 185, '2021-12-08 23:27:08');
INSERT INTO tb_students_info VALUES (4, 'Jane', 1, 22, b'1', 162, '2021-12-08 23:27:44');
INSERT INTO tb_students_info VALUES (5, 'Jim', 6, 21, b'0', 175, '2021-12-08 23:27:47');
INSERT INTO tb_students_info VALUES (6, 'John', 5, 25, b'1', 172, '2021-12-04 23:27:52');
INSERT INTO tb_students_info VALUES (7, 'Lily', 1, 22, b'0', 165, '2021-11-30 23:27:57');
INSERT INTO tb_students_info VALUES (8, 'Susan', 1, 20, b'1', 170, '2021-11-02 23:28:01');
INSERT INTO tb_students_info VALUES (9, 'ThomAS', 4, 35, b'0', 178, '2021-12-03 23:28:06');
INSERT INTO tb_students_info VALUES (10, 'Tom', 3, 15, b'1', 165, '2021-12-26 23:28:10');
```

**数据**

```sql
INSERT INTO tb_departments VALUES (1, 'A', 'Computer');
INSERT INTO tb_departments VALUES (2, 'B', 'JS');
INSERT INTO tb_departments VALUES (3, 'A', 'JAVA');
INSERT INTO tb_departments VALUES (4, 'C', 'PYTHON');
INSERT INTO tb_departments VALUES (5, 'A', 'C++');
INSERT INTO tb_departments VALUES (6, 'B', 'C#');
INSERT INTO tb_departments VALUES (7, 'A', 'JQ');
```

##### 课堂练习

```sql
条件查询


# 查询id=8的数据
SELECT id,name,dept_id,age FROM tb_students_info WHERE id = 8;

# 查询id>3的数据
SELECT id,name,dept_id,age FROM tb_students_info WHERE id >=3; 

# 查询id>3并且age<25的数据
SELECT id,name,dept_id,age FROM tb_students_info WHERE id >=3 AND age<25;

# 查询所有数据
SELECT * FROM tb_students_info;

# 查询id>5并且age<25的数据
SELECT * FROM tb_students_info WHERE id>5 AND age<25;

# 获取用户的所有部门
SELECT  dept_id FROM tb_students_info;

# 用户部门去重
SELECT DISTINCT dept_id FROM tb_students_info;

# 查询用户的id，name，dept_name
SELECT id,name,dept_name FROM tb_departments,tb_students_info WHERE
tb_students_info.dept_id=tb_departments.dept_id;

# 查询用户的id，name，dept_id,dept_name
SELECT id,name,tb_students_info.dept_id,dept_name FROM tb_departments,tb_students_info  WHERE
tb_students_info.dept_id=tb_departments.dept_id;

别名查询

# 查询用户的id，name，dept_id,dept_name 表使用别名  AS可省略
SELECT 
id,name,s.dept_id,dept_name 
FROM 
tb_departments AS d,tb_students_info AS s 
WHERE
s.dept_id=d.dept_id;

# 查询用户的id，name，dept_id,dept_name 字段使用别名  
SELECT 
id AS "人员编号",name AS "姓名",s.dept_id AS "部门编号",dept_name AS "部门名称" 
FROM 
tb_departments AS d,tb_students_info AS s 
WHERE
s.dept_id=d.dept_id;

# 查询用户的id，name，dept_id,dept_name 不同表的相同字段使用不同别名  
SELECT 
id,name,s.dept_id AS sid,d.dept_id AS did ,dept_name 
FROM 
tb_departments AS d,tb_students_info AS s 
WHERE
s.dept_id=d.dept_id;

从指定位置查询

# 查询表中前两条数据
SELECT * FROM tb_students_info LIMIT 0,2;
# 查询表中第三，四条数据
SELECT * FROM tb_students_info LIMIT 2 ,2;
# 查询表中五条数据
SELECT * FROM tb_students_info LIMIT 0,5;
SELECT * FROM tb_students_info LIMIT 5,5;

排序

# 将表中数据按照年龄从大到小展示
SELECT * FROM tb_students_info ORDER BY age DESC
# 将表中数据按照年龄从小到大展示
SELECT * FROM tb_students_info ORDER BY age ASC

# 查询结果：id正序，height倒序，age正序（倒序也试试）
INSERT INTO tb_students_info VALUE
(11,"Jerry",1,15,b'0',170,'2021-12-08 23:27:47');

SELECT * FROM 
tb_students_info 
ORDER BY 
dept_id ASC, height DESC,age ASC;

模糊查询

# 模糊查询有字母为J的同学
SELECT * FROM tb_students_info WHERE name LIKE 'J%';

# 模糊查询尾字母为m的同学
SELECT * FROM tb_students_info WHERE name LIKE '%m';

# 模糊查询尾字母为an的同学
SELECT * FROM tb_students_info WHERE name LIKE '%an';

# 查询名字包含e的同学
INSERT INTO tb_students_info VALUE
(12,"earry",1,15,b'0',170,'2021-12-08 23:27:47');

INSERT INTO tb_students_info VALUE
(13,"Earry",1,15,b'0',170,'2021-12-08 23:27:47');

SELECT * FROM tb_students_info WHERE name LIKE '%e%';

# 查询以n结尾，但是前面有三个字母的同学
SELECT * FROM tb_students_info WHERE name LIKE '___n';

# 查询身高在160-170的数据
 SELECT * FROM tb_students_info WHERE height BETWEEN 160 AND 170;
 
# 查询登陆日期在2021-10-30 23:27:57和2021-11-30 23:27:57之间的数据
SELECT * FROM tb_students_info WHERE login_date BETWEEN ' 2021-10-30 23:27:57' AND '2021-11-30 23:27:57';

# 查询身高在160-170的数据，年龄大于25。
 SELECT * FROM tb_students_info WHERE height BETWEEN 160 AND 170 AND age<25;
 
 内外链接
 # 等值链接
   SELECT id,name,s.dept_id,d.dept_name FROM tb_students_info AS s , tb_departments AS d WHERE s.dept_id=d.dept_id;
 
 # 使用内连接
 SELECT id,name,tb_students_info.dept_id FROM tb_students_info INNER JOIN tb_departments ON tb_students_info.dept_id=tb_departments.dept_id;
 # 改装
SELECT id,name,s.dept_id FROM tb_students_info AS s INNER JOIN tb_departments AS d ON s.dept_id=d.dept_id;
  
# 使用左外链接
 SELECT id,name,s.dept_id ,d.dept_name FROM tb_students_info AS s LEFT JOIN tb_departments AS d ON s.dept_id=d.dept_id;
 
# 使用右外连接
 SELECT id,name,s.dept_id ,d.dept_name FROM tb_students_info AS s RIGHT JOIN tb_departments AS d ON s.dept_id=d.dept_id;
 
# 添加dept_id为10的部门，使用左连接和右链接查看
INSERT INTO tb_departments VALUE(7,'','');
 
# 添加dept_id为0的学生，使用左连接和右链接查看
INSERT INTO tb_students_info VALUE
(15,"Earry",0,15,b'0',170,'2021-12-08 23:27:47');
INSERT INTO tb_students_info VALUE
(16,"Earry",10,15,b'0',170,'2021-12-08 23:27:47');
 
# 实践：查出所有学生及其部门，没有部门的学生只显示信息。使用左连接解决。

子查询
# 查询部门id为1,3,5的人员信息
SELECT * FROM tb_students_info WHERE id = 1 OR id=3 or id = 5;
SELECT * FROM tb_students_info WHERE id IN (1,3,5);


# 在 tb_departments 表中查询 dept_type 为 A 的学院 ID，并根据学院 ID 查询该学院学生的名字
SELECT 
id,name,s.dept_id ,d.dept_name 
FROM 
tb_students_info s,tb_departments d 
WHERE 
s.dept_id=d.dept_id AND d.dept_type = 'A';

SELECT 
id,name,s.dept_id
FROM tb_students_info s 
WHERE 
s.dept_id IN (
    SELECT dept_id 
    FROM 
    tb_departments 
    WHERE dept_type = 'A'
);

GROUP BY
# 问题写法：
SELECT id,name,dept_name FROM tb_students_info s ,tb_departments d WHERE   s.dept_id = d.dept_id  GROUP BY dept_name;


# 获取员工的id，name，dept_name。每个部门的人在一起。
SELECT id,name,dept_name FROM tb_students_info s ,tb_departments d WHERE s.dept_id = d.dept_id  GROUP BY dept_name,id,name;

AVG

# 获取每个部门的平均身高
SELECT s.dept_id,d.dept_name,AVG(s.height) FROM tb_students_info s INNER JOIN tb_departments d
ON s.dept_id=d.dept_id GROUP BY s.dept_id,d.dept_name;


# 求公司所有人的平均年龄
SELECT AVG(age) FROM tb_students_info;
# 求每个部门人的平均年龄
SELECT dept_id,AVG(age) FROM tb_students_info GROUP BY dept_id;

SELECT dept_id,SUM(age)/COUNT(age) FROM tb_students_info GROUP BY dept_id;


# 求每个部门的平均身高，并展示出 部门名称和平均身高。
SELECT dept_name,AVG(height) FROM tb_students_info  s INNER JOIN tb_departments d ON  s.dept_id = d.dept_id GROUP BY dept_name;

SELECT dept_name,SUM(height)/COUNT(height) AS AVG_HEIGHT FROM tb_students_info  s INNER JOIN tb_departments d ON  s.dept_id = d.dept_id GROUP BY dept_name;



COUNT

# 求公司人数
 SELECT COUNT(*) FROM tb_students_info;
# 求每个部门的人数
SELECT dept_name,COUNT(id) FROM tb_students_info  s INNER JOIN tb_departments d ON  s.dept_id = d.dept_id GROUP BY dept_name;
# 求身高小于175的人数
SELECT COUNT(height) FROM tb_students_info WHERE height<=175;
# 求每个部门身高小于175的人数
SELECT dept_name,COUNT(id) 
FROM 
tb_students_info s,tb_departments d
WHERE
s.dept_id = d.dept_id AND s.height<175 
GROUP BY dept_name;

SELECT dept_name,COUNT(id) 
FROM 
tb_students_info s 
INNER JOIN 
tb_departments d
ON s.dept_id=d.dept_id WHERE s.height<175
GROUP BY dept_name;

SELECT dept_name,COUNT(id) 
FROM 
tb_students_info s 
INNER JOIN 
tb_departments d
ON s.dept_id=d.dept_id AND s.height<175
GROUP BY dept_name;

# 求各个部门，身高最高的人员的信息
SELECT d.dept_id,dept_name,MAX(height)
FROM 
tb_students_info s 
INNER JOIN 
tb_departments d
ON s.dept_id=d.dept_id 
GROUP BY d.dept_name,d.dept_id;

SELECT id,name,age,s.dept_id,s.height 
FROM tb_students_info s 
RIGHT JOIN
(SELECT d.dept_id,dept_name,MAX(height) height
FROM 
tb_students_info s 
INNER JOIN 
tb_departments d
ON s.dept_id=d.dept_id 
GROUP BY d.dept_name,d.dept_id) sd
ON s.dept_id = sd.dept_id 
AND s.height=sd.height;

# 最高身高在170-180之间的部门信息

SELECT d.dept_id,dept_name,MAX(height)
AS MAX_height FROM 
tb_students_info s 
INNER JOIN 
tb_departments d
ON s.dept_id=d.dept_id  AND height BETWEEN 170 AND 180
GROUP BY d.dept_name,d.dept_id;
写法有问题

 我们要的是部门所有人平均身高在170-175之间的部门信息。而不是部门人员身高在170-175之间人员的平均身高信息。
SELECT d.dept_id,dept_name,AVG(height)
AS MAX_height FROM 
tb_students_info s 
INNER JOIN 
tb_departments d
ON s.dept_id=d.dept_id  AND height BETWEEN 170 AND 175
GROUP BY d.dept_name,d.dept_id;

正确写法：
SELECT d.dept_id,dept_name,AVG(height) 
AS MAX_height FROM 
tb_students_info s 
INNER JOIN 
tb_departments d
ON s.dept_id=d.dept_id 
GROUP BY d.dept_name,d.dept_id
HAVING MAX_height BETWEEN 170 AND 175;

# 更新操作 更新id=1的学生的年龄为30
UPDATE tb_students_info SET age=30 WHERE id =1 ;

# 删除操作  删除id = 1 的学生信息
 DELETE FROM tb_students_info WHERE id = 1 ;
```

#### 查询(SELECT)

```txt
SELECT
{* | <字段列名>}
[
FROM <表 1>, <表 2>…
[WHERE <表达式>
[GROUP BY <GROUP BY definitiON>
[HAVING <expressiON> [{<operator> <expressiON>}…]]
[ORDER BY <ORDER BY definitiON>]
[LIMIT[<offSET>,] <row count>]
]
{*|<字段列名>}包含星号通配符的字段列表，表示查询的字段，其中字段列至少包含一个字段名称，如果要查询多个字段，多个字段之间要用逗号隔开，最后一个字段后不要加逗号。
FROM <表 1>，<表 2>…，表 1 和表 2 表示查询数据的来源，可以是单个或多个。
WHERE 子句是可选项，如果选择该项，将限定查询行必须满足的查询条件。
GROUP BY< 字段 >，该子句告诉 MySQL 如何显示查询出来的数据，并按照指定的字段分组。
[ORDER BY< 字段 >]，该子句告诉 MySQL 按什么样的顺序显示查询出来的数据，可以进行的排序有升序（ASC）和降序（DESC）。
[LIMIT[<offSET>，]<row count>]，该子句告诉 MySQL 每次显示查询出来的数据条数。
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

作用:DISTINCT 关键字指示 MySQL 消除重复的记录值

```sql
# 查询 tb_students_info 表中所有 age 
SELECT age FROM tb_students_info;
# 查询 tb_students_info 表中 age 字段的值，返回 age 字段的值且不得重复
SELECT  DISTINCT age FROM tb_students_info;
```

#### 别名(AS)

**表的别名**

<表名> [AS] <别名>

作用:当表名很长或者执行一些特殊查询的时候，为了方便操作或者需要多次使用相同的表时，可以为表指定别名，用这个别名代替表原来的名称。

注意:在为表取别名时，要保证不能与数据库中的其他表的名称冲突。

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

注意:表别名只在执行查询时使用，并不在返回结果中显示，而列定义别名之后，将返回给客户端显示，显示的结果字段为字段列的别名。

#### 限制查询数(limit)

<LIMIT> [<位置偏移量>,] <行数>

第一个参数“位置偏移量”指示 MySQL 从哪一行开始显示，是一个可选参数，如果不指定“位置偏移量”，将会从表中的第一条记录开始（第一条记录的位置偏移量是 0，第二条记录的位置偏移量是 1，以此类推）；第二个参数“行数”指示返回的记录条数。

作用:SELECT 语句时往往返回的是所有匹配的行，有些时候我们仅需要返回第一行或者前几行，这时候就需要用到 MySQL LIMIT 子句。

注意:MySQL 5.7 中可以使用“LIMIT 4 OFFSET 3”，意思是获取从第5条记录开始的后面的3条记录，和“LIMIT 4，3”返回的结果相同

```sql
# 显示 tb_students_info 表查询结果的前 4 行
SELECT * FROM tb_students_info LIMIT 4;
# 在 tb_students_info 表中，使用 LIMIT 子句返回从第 4 条记录开始的行数为 5 的记录
SELECT * FROM tb_students_info LIMIT 3,5;
```

#### 排序(ORDER BY)

ORDER BY {<列名> | <表达式> | <位置>} [ASC|DESC]

 **列名:**指定用于排序的列。可以指定多个列，列名之间用逗号分隔。

**表达式:**指定用于排序的表达式。

**位置:**指定用于排序的列在 SELECT 语句结果集中的位置，通常是一个正整数。

**ASC|DESC:**关键字 ASC 表示按升序分组，关键字 DESC 表示按降序分组，其中 ASC 为默认值。这两个关键字必须位于对应的列名、表达式、列的位置之后。

使用 ORDER BY 子句注意:

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

注意:在对多列进行排序时，首行排序的**第一列必须有相同的列值，才会对第二列进行排序**。如果第一列数据中所有的值都是唯一的，将不再对第二列进行排序。

默认情况下，查询数据按字母升序进行排序（A～Z），但数据的排序并不仅限于此，还可以使用 ORDER BY 对查询结果进行降序排序（Z～A），这可以通过关键字 DESC 实现。可以对多列进行不同的顺序排序。

#### 条件查询(WHERE)

WHERE <查询条件> {<判定运算1>，<判定运算2>，…}

判定运算其结果取值为 TRUE、FALSE 和 UNKNOWN。

```
判定运算的语法分类如下:
<表达式1>{=|<|<=|>|>=|<=>|<>|！=}<表达式2>
<表达式1>[NOT]LIKE<表达式2>
<表达式1>[NOT][REGEXP|RLIKE]<表达式2>
<表达式1>[NOT]BETWEEN<表达式2>AND<表达式3>
<表达式1>IS[NOT]NULL
```

##### 普通查询

```mysql
# 在表 tb_students_info 中查询身高为 170cm 的学生的姓名
SELECT name,height
    FROM tb_students_info
    WHERE height=170;
    
# 查询年龄小于 22 的学生的姓名
    
    SELECT name,age
    FROM tb_students_info
    WHERE age<22;
```

##### 多条件的查询语句

```mysql
# 在 tb_students_info 表中查询 age 大于 21，并且 height 大于等于 175 的学生的信息

SELECT * FROM tb_students_info
       WHERE age>21 AND height>=175;    
```

##### 模糊查询

<表达式1> [NOT] LIKE <表达式2>

使用运算符 LIKE 设置过滤条件，过滤条件使用通配符进行匹配运算

匹配运算的对象可以是 CHAR、VARCHAR、TEXT、DATETIME 等数据类型

**百分号（%）**

百分号是 MySQL 中常用的一种通配符，在过滤条件中，百分号可以表示任何字符串，并且该字符串可以出现任意次。

使用百分号通配符要注意以下几点:

- MySQL 默认是不区分大小写的，若要区分大小写，则需要更换字符集的校对规则。
- 百分号不匹配空值。
- 百分号可以代表搜索模式中给定位置的 0 个、1 个或多个字符。
- 尾空格可能会干扰通配符的匹配，一般可以在搜索模式的最后附加一个百分号。

**下划线（_）**

下划线通配符和百分号通配符的用途一样，下画线只匹配单个字符，而不是多个字符，也不是 0 个字符。

**注意:**不要过度使用通配符，对通配符检索的处理一般会比其他检索方式花费更长的时间

```mysql
# 在 tb_students_info 表中，查找所有以“T”字母开头的学生姓名，
SELECT name FROM tb_students_info WHERE name LIKE 'T%';
# 在 tb_students_info 表中，查找所有包含“e”字母的学生姓名
SELECT name FROM tb_students_info WHERE name LIKE '%e%';
# 在 tb_students_info 表中，查找所有以字母“y”结尾，且“y”前面只有 4 个字母的学生的姓名，
SELECT name FROM tb_students_info WHERE name LIKE '____y';
```

##### BETWEEN AND

```mysql
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

``` mysql
# 表 tb_students_info 和表 tb_departments 都包含相同数据类型的字段 dept_id，在两个表之间使用等值连接查询。
SELECT id,name,age,dept_name FROM tb_students_info,tb_departments WHERE tb_students_info.dept_id=tb_departments.dept_id;

# 在 tb_students_info 表和 tb_departments 表之间，使用 INNER JOIN 语法进行内连接查询
SELECT id,name,age,dept_name FROM tb_students_info INNER JOIN tb_departments
ON tb_students_info.dept_id=tb_departments.dept_id;
```

注意:使用 WHERE 子句定义连接条件比较简单明了，而 INNER JOIN 语法是 ANSI SQL 的标准规范，使用 INNER JOIN 连接语法能够确保不会忘记连接条件，而且 WHERE 子句在某些时候会影响查询的性能。

**等值连接和内连接区别**

等值连接：2个表会先进行笛卡尔乘积运算，生成一个新表格，占据在电脑内存里，当表的数据量很大时，很耗内存，这种方法效率比较低，尽量不用。

内连接：

1. 2个表根据共同ID进行逐条匹配，不会出现笛卡尔乘积的现象，效率比较高，优先使用这种方法。
2. 对于`t1`表中的每一行，`INNER JOIN`子句将它与`t2`表的每一行进行比较，以检查它们是否都满足连接条件。当满足连接条件时，`INNER JOIN`将返回由`t1`和`t2`表中的列组成的新行。

#### 外连接查询(outer join)

[MySQL](http://www.voidme.com/mysql) 中 [内连接](http://www.voidme.com/mysql/mysql-inner-join)是在交叉连接的结果集上返回满足条件的记录；而外连接先将连接的表分为**基表和参考表**，再以**基表为依据返回满足和不满足条件的记录。**

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

子查询中常用的运算符

**IN子查询**

结合关键字 IN 所使用的子查询主要用于判断一个给定值是否存在于子查询的结果集中。

<表达式> [NOT] IN <子查询>

语法:

- <表达式>:用于指定表达式。当表达式与子查询返回的结果集中的某个值相等时，返回 TRUE，否则返回 FALSE；若使用关键字 NOT，则返回的值正好相反。
- <子查询>:用于指定子查询。这里的子查询只能返回一列数据。对于比较复杂的查询要求，可以使用 SELECT 语句实现子查询的多层嵌套。

**比较运算符子查询**

比较运算符所使用的子查询主要用于对表达式的值和子查询返回的值进行比较运算。其语法格式为:

<表达式> {= | < | > | >= | <= | <=> | < > | != }
{ ALL | SOME | ANY} <子查询>

语法:

- <子查询>:用于指定子查询。
- <表达式>:用于指定要进行比较的表达式。
- ALL、SOME 和 ANY:可选项。用于指定对比较运算的限制。其中，关键字 ALL 用于指定表达式需要与子查询结果集中的每个值都进行比较，当表达式与每个值都满足比较关系时，会返回 TRUE，否则返回 FALSE；关键字 SOME 和 ANY 是同义词，表示表达式只要与子查询结果集中的某个值满足比较关系，就返回 TRUE，否则返回 FALSE。

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

#### 分组查询(GROUP BY)

使用 GROUP BY 子句，将结果集中的数据行根据选择列的值进行逻辑分组，以便能汇总表内容的子集，实现对每个组而不是对整个结果集进行整合。

GROUP BY { <列名> | <表达式> | <位置> } [ASC | DESC]

``` txt
<列名>:指定用于分组的列。可以指定多个列，彼此间用逗号分隔。
<表达式>:指定用于分组的表达式。通常与聚合函数一块使用，例如可将表达式 COUNT(*)AS' 人数 ' 作为 SELECT 选择列表清单的一项。
<位置>:指定用于分组的选择列在 SELECT 语句结果集中的位置，通常是一个正整数。例如，GROUP BY 2 表示根据 SELECT 语句列清单上的第 2 列的值进行逻辑分组。
ASC|DESC:关键字 ASC 表示按升序分组，关键字 DESC 表示按降序分组，其中 ASC 为默认值，注意这两个关键字必须位于对应的列名、表达式、列的位置之后。
```

注意:GROUP BY 子句中的各选择列必须也是 SELECT 语句的选择列清单中的一项。

使用注意:

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

``` txt
HAVING 子句和 WHERE 子句非常相似，HAVING 子句支持 WHERE 子句中所有的操作符和语法，但是两者存在几点差异:
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

#### 聚合函数

Mysql聚合函数：AVG()函数，COUNT()函数，SUM()函数，MAX()函数，MIN()函数。

**特点：**

1. 除了 COUNT 以外，聚合函数忽略空值。
2. 聚合函数经常与 SELECT 语句的 GROUP BY 子句一同使用。
3. 所有聚合函数都具有确定性。任何时候用一组给定的输入值调用它们时，都返回相同的值。
4. 标量函数：只能对单个的数字或值进行计算。主要包括字符函数、日期/时间函数、数值函数和转换函数这四类。

##### AVG()

AVG()函数计算一组值的平均值。 它计算过程中是忽略NULL值的。

```sql
# 计算平均身高
SELECT AVG(height) avg_height FROM tb_students_info;

# 求每个部门的平均身高
 SELECT AVG(height) avg_height,dept_id FROM tb_students_info GROUP BY dept_id;
```

##### COUNT()

COUNT()函数返回表中的行数。

```sql
# 计算总跳数
SELECT COUNT(*) avg_height FROM tb_students_info;
SELECT COUNT(height) avg_height FROM tb_students_info;
```

##### SUM()

求和

```sql
# 身高总和
 SELECT SUM(height) FROM tb_students_info;
```

##### MAX()

求最大值

```sql
# 最高的身高
 SELECT MAX(height) FROM tb_students_info;
 
 # 求每个部门的最高身高
  SELECT MAX(height) avg_height,dept_id FROM tb_students_info GROUP BY dept_id;
```

##### MIN()

求最小值

```sql
# 最低的身高
 SELECT MAX(height) FROM tb_students_info;
 
# 求每个部门的最低身高
SELECT MAX(height) avg_height,dept_id FROM tb_students_info GROUP BY dept_id;
```

#### 插入数据(insert)

##### 插入

1. INSERT INTO <表名> [ <列名1> [ , … <列名n>] ]
   VALUES (值1) [… , (值n) ];

``` txt
<表名>:指定被操作的表名。
<列名>:指定需要插入数据的列名。若向表中的所有列插入数据，则全部的列名均可以省略，直接采用 INSERT<表名>VALUES(…) 即可。
VALUES 或 VALUE 子句:该子句包含要插入的数据清单。数据清单中数据的顺序要和列的顺序相对应。
```

2. INSERT INTO <表名>
   SET <列名1> = <值1>,
           <列名2> = <值2>,

在 MySQL 中，用单条 INSERT 语句处理多个插入要比使用多条 INSERT 语句更快。

**创建表tb_courses**

```sql
CREATE TABLE tb_courses
     (
     course_id INT NOT NULL AUTO_INCREMENT,
     course_name CHAR(40) NOT NULL,
     course_grade FLOAT NOT NULL,
     course_info CHAR(100) NULL,
     PRIMARY KEY(course_id)
     );
```

**插入练习**

```sql
# 在 tb_courses 表中插入一条新记录，course_id 值为 1，course_name 值为“Network”，course_grade 值为 3，info 值为“Computer Network”。
INSERT INTO tb_courses
     (course_id,course_name,course_grade,course_info)
     VALUES(1,'Network',3,'Computer Network');
     
# 注意:插入的时候，数值要与列名的相对应

# 在 tb_courses 表中插入一条新记录，course_id 值为 2，course_name 值为“DatabASe”，course_grade 值为 3，info值为“MySQL”。
INSERT INTO tb_courses
     (course_name,course_info,course_id,course_grade)
     VALUES('DatabASe','MySQL',2,3);

# 不指定列表名，但是值的顺序要和表结构一致
     
# 在 tb_courses 表中插入一条新记录，course_id 值为 3，course_name 值为“ Java”，course_grade 值为 4，info 值为“Jave EE”

INSERT INTO tb_courses
     VLAUES(3,'Java',4,'Java EE');
     
# 向表中指定字段添加值

# 在 tb_courses 表中插入一条新记录，course_name 值为“System”，course_grade 值为 3，course_info 值为“Operating System”

INSERT INTO tb_courses
     (course_name,course_grade,course_info)
     VALUES('System',3,'OperatiON System');
```

##### 复制

INSERT INTO…SELECT…FROM 语句用于快速地从一个或多个表中取出数据，并将这些数据作为行数据插入另一个表中。

```sql
# 在数据库 test_db 中创建一个与 tb_courses 表结构相同的数据表 tb_courses_new
CREATE TABLE tb_courses_new
     (
     course_id INT NOT NULL AUTO_INCREMENT,
     course_name CHAR(40) NOT NULL,
     course_grade FLOAT NOT NULL,
     course_info CHAR(100) NULL,
     PRIMARY KEY(course_id)
     );
# 从 tb_courses 表中查询所有的记录，并将其插入 tb_courses_new 表中。
INSERT INTO tb_courses_new
     (course_id,course_name,course_grade,course_info)
     SELECT course_id,course_name,course_grade,course_info
     FROM tb_courses;
```

#### 修改数据(update)

UPDATE <表名> SET 字段 1=值 1 [,字段 2=值 2… ] [WHERE 子句 ]
[ORDER BY 子句] [LIMIT 子句]

```text
<表名>：用于指定要更新的表名称。
SET 子句：用于指定表中要修改的列名及其列值。其中，每个指定的列值可以是表达式，也可以是该列对应的默认值。如果指定的是默认值，可用关键字 DEFAULT 表示列值。
WHERE 子句：可选项。用于限定表中要修改的行。若不指定，则修改表中所有的行。
ORDER BY 子句：可选项。用于限定表中的行被修改的次序。
LIMIT 子句：可选项。用于限定被修改的行数。
```

注意：修改一行数据的多个列值时，SET 子句的每个值用逗号分开即可。

``` sql
# 在 tb_courses_new 表中，更新所有行的 course_grade 字段值为 4
 UPDATE tb_courses_new SET course_grade=4;
# 在 tb_courses 表中，更新 course_id 值为 2 的记录，将 course_grade 字段值改为 3.5，将 course_name 字段值改为“DB”
```

#### MySQL删除数据(delete)

DELETE FROM <表名> [WHERE 子句] [ORDER BY 子句] [LIMIT 子句]

```text
<表名>：指定要删除数据的表名。
ORDER BY 子句：可选项。表示删除时，表中各行将按照子句中指定的顺序进行删除。
WHERE 子句：可选项。表示为删除操作限定删除条件，若省略该子句，则代表删除该表中的所有行。
LIMIT 子句：可选项。用于告知服务器在控制命令被返回到客户端前被删除行的最大值。
```

注意：在不使用 WHERE 条件的时候，将删除所有数据.

``` sql
# 在 tb_courses_new 表中，删除 course_id 为 4 的记录
DELETE FROM tb_courses WHERE course_id=4;
     
# 删除 tb_courses_new 表中的全部数据
DELETE FROM tb_courses_new;
```

### Java与Mysql数据类型比对

![image-20211209101854787](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211209101854787.png)

## 3. 数据库的存储引擎

**介绍:**

数据库存储引擎是数据库底层软件组件，数据库管理系统使用数据引擎进行创建、查询、更新和删除数据操作。

不同的存储引擎提供不同的存储机制、索引技巧、锁定水平等功能，使用不同的存储引擎还可以获得特定的功能。

现在许多数据库管理系统都支持多种不同的存储引擎。MySQL 的核心就是存储引擎。

MySQL 提供了多个不同的存储引擎，包括处理事务安全表的引擎和处理非事务安全表的引擎。在 MySQL 中，不需要在整个服务器中使用同一种存储引擎，针对具体的要求，**可以对每一个表使用不同的存储引擎**。

**常见的存储引擎:**

1. InnoDB:

   InnoDB 事务型数据库的首选引擎，**支持事务安全表（ACID），支持行锁定和外键。**MySQL 5.5.5 之后，**InnoDB 作为默认存储引擎。**

2. MyISAM :

   MyISAM 是基于 ISAM 的存储引擎，并对其进行扩展，是在 Web、数据仓储和其他应用环境下最常使用的存储引擎之一。**MyISAM 拥有较高的插入、查询速度，但不支持事务。**

3. MEMORY:

   MEMORY存储引擎**将表中的数据存储到内存中，为查询和引用其他数据提供快速访问。**

| 功能             | MylSAM | MEMORY | InnoDB | Archive |
| :--------------- | :----- | :----- | :----- | :------ |
| 存储限制         | 256TB  | RAM    | 64TB   | NONe    |
| **支持事务**     | No     | No     | Yes    | No      |
| **支持全文索引** | Yes    | No     | No     | No      |
| **支持树索引**   | Yes    | Yes    | Yes    | No      |
| 支持哈希索引     | No     | Yes    | No     | No      |
| **支持数据缓存** | No     | N/A    | Yes    | No      |
| **支持外键**     | No     | No     | Yes    | No      |

**选择 MySQL存储引擎原则:**

- 如果要提供提交、回滚和恢复的**事务**安全（ACID 兼容）能力，并要求实现并发控制，InnoDB 是一个很好的选择。
- 如果数据表主要用来**插入和查询记录**，则 MyISAM 引擎提供较高的处理效率。
- 如果只是**临时存放数据，数据量不大，并且不需要较高的数据安全性，**可以选择将数据保存在内存的 MEMORY 引擎中，MySQL 中使用该引擎作为临时表，存放查询的中间结果。
- 如果只有 INSERT 和 SELECT 操作，可以选择Archive 引擎，**Archive 存储引擎支持高并发的插入操作**，但是本身并不是事务安全的。Archive 存储引擎非常适合存储归档数据，**如记录日志信息可以使用 Archive 引擎**。

## 4. Mysql数据类型

MySQL 的数据类型有大概可以分为 5 种，分别是整数类型、浮点数类型和定点数类型、日期和时间类型、字符串类型、二进制类型等。

注意:整数类型和浮点数类型可以统称为数值数据类型。

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

| 类型名称      | 有符号范围                                | 无符号范围              | 存储需求 | 默认长度 |
| :------------ | :---------------------------------------- | :---------------------- | -------- | -------- |
| TINYINT       | -128〜127                                 | 0 〜255                 | 1个字节  | 4        |
| SMALLINT      | -32768〜32767                             | 0〜65535                | 2个字节  | 6        |
| MEDIUMINT     | -8388608〜8388607                         | 0〜16777215             | 3个字节  | 9        |
| INT (INTEGER) | -2147483648〜2147483647                   | 0〜4294967295           | 4个字节  | 11       |
| BIGINT        | -9223372036854775808〜9223372036854775807 | 0〜18446744073709551615 | 8个字节  | 20       |

### 小数类型

浮点类型有两种，分别是单精度浮点数（ **FLOAT**）和双精度浮点数（ **DOUBLE**）；定点类型只有一种，就是 **DECIMAL**。

浮点类型和定点类型都可以用 (M, D)来表示，其中 M称为精度，表示总共的位数； D称为标度，表示小数的位数。

浮点数类型的取值范围为 M（1～255）和 D（1～30，且不能大于 M-2），分别表示显示宽度和小数位数。M 和 D 在 FLOAT 和DOUBLE 中是可选的，FLOAT 和 DOUBLE 类型将被保存为硬件所支持的最大精度。DECIMAL 的默认 D 值为 0、M 值为 10。

注意:如果DECIMAL 的D值为0，如果传进来小数，会四舍五入保存数据

在 MySQL 中，定点数以字符串形式存储，在对精度要求比较高的时候（如货币、科学数据），使用 DECIMAL 的类型比较好，另外两个浮点数进行减法和比较运算时也容易出问题，所以在使用浮点数时需要注意，并尽量避免做浮点数比较。

| 类型名称     | 说明                                       | 无符号范围                                       | 有符号范围                                         | 存储需求 |
| ------------ | ------------------------------------------ | ------------------------------------------------ | -------------------------------------------------- | -------- |
| FLOAT        | 单精度浮点                                 | -3.402823466E+38~-1.175494351E-38                | 0和1.175494351E-38~3.402823466E+38                 | 4个字节  |
| DOUBLE       | 双精度浮点                                 | -1.7976931348623157E-308~1.7976931348623157E+308 | 0和2.2250738585072014E-308~1.7976931348623157E+308 | 8个字节  |
| DECIMAL(M,D) | 压缩的'严格'定点数，就是指定了小数点后几位 |                                                  |                                                    |          |



### 时间和日期类型

MySQL 中表示日期的数据类型: **YEAR**、 **TIME**、 **DATE**、 **DTAETIME**、 **TIMESTAMP**。

每一个类型都有合法的取值范围，当指定确定不合法的值时，系统将“零”值插入数据库中。

| 类型名称             | 说明     | 日期格式            | 日期范围                                          | 存储需求 |
| :------------------- | -------- | :------------------ | :------------------------------------------------ | :------- |
| YEAR                 | 年       | YYYY                | 1901 ~ 2155                                       | 1 个字节 |
| TIME                 | 时间     | HH:MM:SS            | -838:59:59 ~ 838:59:59                            | 3 个字节 |
| DATE                 | 日期     | YYYY-MM-DD          | 1000-01-01 ~ 9999-12-3                            | 3 个字节 |
| **DATETIME（常用）** | 日期时间 | YYYY-MM-DD HH:MM:SS | 1000-01-01 00:00:00 ~ 9999-12-31 23:59:59         | 8 个字节 |
| TIMESTAMP            | 日期时间 | YYYY-MM-DD HH:MM:SS | 1980-01-01 00:00:01 UTC ~ 2040-01-19 03:14:07 UTC | 4 个字节 |

TIMESTAMP 与 DATETIME 区别:

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

MySQL 支持 4 种运算符，分别是:

### 算术运算符

执行算术运算，例如:加、减、乘、除等。

| 算术运算符 | 说明               |
| :--------- | :----------------- |
| +          | 加法运算           |
| -          | 减法运算           |
| *          | 乘法运算           |
| /          | 除法运算，返回商   |
| %          | 求余运算，返回余数 |

### 比较运算符

​		包括大于、小于、等于或者不等于，等等。主要用于数值的比较、字符串的匹配等方面。例如:LIKE、IN、BETWEEN AND 和 IS NULL 等都是比较运算符，还包括正则表达式的 REGEXP 也是比较运算符。

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

## 6. Mysql约束

### 主键约束

“主键（PRIMARY KEY）即主键约束，MySQL 主键约束是**一个列**或者**列的组合**，其值能**唯一地标识表中的每一行**，**主键不能重复。**其中由多列组合的主键称为复合主键。这样的一列或多列称为表的主键，通过它可以强制表的实体完整性。

主键应该遵守下面的规则：

- 每个表只能定义一个主键。
- 主键值必须唯一标识表中的每一行，且不能为 NULL，即表中不可能存在两行数据有相同的主键值。这是唯一性原则。
- 一个列名只能在复合主键列表中出现一次。
- 复合主键不能包含不必要的多余列。当把复合主键的某一列删除后，如果剩下的列构成的主键仍然满足唯一性原则，那么这个复合主键是不正确的。这是最小化原则。

#### 1. 在创建表时设置主键约束

**定义列的同时指定主键**

<字段名> <数据类型> PRIMARY KEY [默认值]

```sql
# 在 test_db 数据库中创建 tb_emp 3 数据表，其主键为 id
CREATE TABLE tb_emp3
     (
     id INT(11) PRIMARY KEY,
     name VARCHAR(25),
     deptId INT(11),
     salary FLOAT
     );
```

**定义完所有列之后，指定主键**

[CONSTRAINT <约束名>] PRIMARY KEY [字段名]

```sql
# 在 test_db 数据库中创建 tb_emp 4 数据表，其主键为 id
CREATE TABLE tb_emp4
     (
     id INT(11),
     name VARCHAR(25),
     deptId INT(11),
     salary FLOAT,
     PRIMARY KEY(id)
     );
```

#### 2.在创建表时设置复合主键

PRIMARY KEY [字段1，字段2，…,字段n]

```sql
# 创建数据表 tb_emp5，假设表中没有主键 id，为了唯一确定一个员工，可以把 name、deptId 联合起来作为主键
CREATE TABLE tb_emp5
     (
     name VARCHAR(25),
     deptId INT(11),
     salary FLOAT,
     PRIMARY KEY(id,deptId)
     );
```

####  3.在修改表时添加主键约束

ALTER TABLE <数据表名> ADD PRIMARY KEY(<列名>);

```sql
# 修改数据表 tb_emp2，将字段 id 设置为主键
ALTER TABLE tb_emp2
     ADD PRIMARY KEY(id);
```

#### 4. 删除主键

ALTER  TABLE  <数据表名>DROP  PRIMARY  KEY;

[mysql如何删除主键？-mysql教程-PHP中文网](https://www.php.cn/mysql-tutorials-418313.html)

### 外键约束

外键约束（FOREIGN KEY）用来**在两个表的数据之间建立链接**，**它可以是一列或者多列**。**一个表可以有一个或多个外键**。

外键对应的是参照完整性，一个表的外键可以为空值。若不为空值，则**每一个外键的值必须等于另一个表中主键的某个值**。

**外键是表的一个字段，不是本表的主键，但对应另一个表的主键。**定义外键后，不允许删除另一个表中具有关联关系的行。

外键的主要作用是保持数据的一致性、完整性。例如，部门表 tb_dept 的主键是 id，在员工表 tb_emp5 中有一个键 deptId 与这个 id 关联。

- 主表（父表）：对于两个具有关联关系的表而言，相关联字段中主键所在的表就是主表。
- 从表（子表）：对于两个具有关联关系的表而言，相关联字段中外键所在的表就是从表。

**定义一个外键时，需要遵守下列规则：**

- 父表必须已经存在于数据库中，或者是当前正在创建的表。如果是后一种情况，则父表与子表是同一个表，这样的表称为自参照表，这种结构称为自参照完整性。
- 必须为父表定义主键。
- 主键不能包含空值，但允许在外键中出现空值。也就是说，只要外键的每个非空值出现在指定的主键中，这个外键的内容就是正确的。
- 在父表的表名后面指定列名或列名的组合。这个列或列的组合必须是父表的主键或候选键。
- 外键中列的数目必须和父表的主键中列的数目相同。
- 外键中列的数据类型必须和父表主键中对应列的数据类型相同。

#### 外键策略

[MySQL 外键关联策略 - chy_18883701161 - 博客园 (cnblogs.com)](https://www.cnblogs.com/chy18883701161/p/12603585.html)

![image-20211210152750428](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211210152750428.png)

#### 1. 在创建表时设置外键约束

[CONSTRAINT <外键名>] FOREIGN KEY 字段名 [，字段名2，…]
REFERENCES <主表名> 主键列1 [，主键列2，…]

外键名：为定义的外键约束的名称，一个表中不能有相同名称的外键； 

字段名：表示子表需要添加外健约束的字段列； 

主表名：即被子表外键所依赖的表的名称；

主键列：表示主表中定义的主键列或者列组合。

```sql
# 在 test_db 数据库中创建一个部门表 tb_dept1 id 设置为主键
CREATE TABLE tb_dept1
     (
     id INT(11) PRIMARY KEY,
     name VARCHAR(22) NOT NULL,
     locatiON VARCHAR(50)
     );
# 创建数据表 tb_emp6，并在表 tb_emp6 上创建外键约束，让它的键 deptId 作为外键关联到表 tb_dept1 的主键 id
CREATE TABLE tb_emp6
     (
     id INT(11) PRIMARY KEY,
     name VARCHAR(25),
     deptId INT(11),
     salary FLOAT,
     CONSTRAINT fk_emp_dept1
     FOREIGN KEY(deptId) REFERENCES tb_dept1(id)
     );
```

子表的外键必须关联父表的主键，且关联字段的数据类型必须匹配，如果类型不一样，则创建子表时会出现错误。

#### 2. 在修改表时添加外键约束

ALTER TABLE <数据表名> ADD CONSTRAINT <索引名>
FOREIGN KEY(<列名>) REFERENCES <主表名> (<列名>);

```sql
# 修改数据表 tb_emp2，将字段 deptId 设置为外键，与数据表 tb_dept1 的主键 id 进行关联
ALTER TABLE tb_emp2
     ADD CONSTRAINT fk_tb_dept1
     FOREIGN KEY(deptId)
     REFERENCES tb_dept1(id);
```

#### 3. 删除外键约束

ALTER TABLE <表名> DROP FOREIGN KEY <外键约束名>;

```sql
# 删除数据表 tb_emp2 中的外键约束 fk_tb_dept1
ALTER TABLE tb_emp2
     DROP FOREIGN KEY fk_tb_dept1;
```

### 唯一约束

唯一约束（Unique Key）要求该列唯一，允许为空，但只能出现一个空值。唯一约束可以确保一列或者几列不出现重复值。

#### UNIQUE 和 PRIMARY KEY 的区别：

一个表可以有多个字段声明为 UNIQUE，但只能有一个 PRIMARY KEY 声明；声明为 PRIMAY KEY 的列不允许有空值，但是声明为 UNIQUE 的字段允许空值的存在。

#### 1. 在创建表时设置唯一约束

在定义完列之后直接使用 **UNIQUE** 关键字指定唯一约束，语法规则如下：

<字段名> <数据类型> UNIQUE

```sql
# 创建数据表 tb_dept2，指定部门的名称唯一
 CREATE TABLE tb_dept2
     (
     id INT(11) PRIMARY KEY,
     name VARCHAR(22) UNIQUE,
     locatiON VARCHAR(50)
     );
```

#### 2. 在修改表时添加唯一约束

ALTER TABLE <数据表名> ADD CONSTRAINT <唯一约束名> UNIQUE(<列名>);

```sql
# 修改数据表 tb_dept1，指定部门的名称唯一;
ALTER TABLE tb_dept1
     ADD CONSTRAINT unique_name UNIQUE(name);
```

#### 3. 删除唯一约束

ALTER TABLE <表名> DROP INDEX <唯一约束名>;

```sql
# 删除数据表 tb_dept1 中的唯一约束 unique_name
ALTER TABLE tb_dept1
     DROP INDEX unique_name;
```

### 检查约束

检查约束（CHECK）可以通过 CREATE TABLE 或 ALTER TABLE 语句实现，根据用户实际的完整性要求来定义。它可以分别对列或表实施 CHECK 约束。

检查约束使用 **CHECK** 关键字

CHECK <表达式>

<表达式>指的就是 SQL 表达式，用于指定需要检查的限定条件。

若将 CHECK 约束子句置**于表中某个列的定义之后**，则这种约束也称为基于列的 **CHECK 约束**。

在更新表数据的时候，系统会检查更新后的数据行是否满足 CHECK 约束中的限定条件。MySQL 可以使用简单的表达式来实现 CHECK 约束，也允许使用复杂的表达式作为限定条件，例如在限定条件中加入子查询。

**注意：**

若将 CHECK 约束子句置于**所有列的定义以及主键约束和外键定义之后**，则这种约束也称为基于表的 CHECK 约束。该约束可以同时对表中多个列设置限定条件。

#### 1. 在创建表时设置检查约束

CHECK(<检查约束>)

```sql
# 在 test_db 数据库中创建 tb_emp7 数据表，要求 salary 字段值大于 0 且小于 10000
CREATE TABLE tb_emp7
     (
     id INT(11) PRIMARY KEY,
     name VARCHAR(25),
     deptId INT(11),
     salary FLOAT,
     CHECK(salary>0 AND salary<100),
     FOREIGN KEY(deptId) REFERENCES tb_dept1(id)
     );
```

#### 2. 在修改表时添加检查约束

ALTER TABLE <表名>ADD CONSTRAINT <检查约束名> CHECK(<检查约束>)

```sql
# 修改 tb_dept7 数据表，要求 id 字段值大于 0
ALTER TABLE tb_emp7
     ADD CONSTRAINT check_id
     CHECK(id>0);
```

#### 3. 删除检查约束

ALTER TABLE <数据表名> DROP CONSTRAINT <检查约束名>;

```sql
# 删除 tb_dept7 数据表，要求 id 字段值大于 0
ALTER TABLE tb_emp7
     DROP  CONSTRAINT check_id;
```

### 默认约束

“默认值（Default）”的完整称呼是“默认值约束（Default CONstraint）”。

例如女性同学较多，性别就可以默认为“女”。如果插入一条新的记录时没有为这个字段赋值，那么系统会自动为这个字段赋值为“女”。

#### 在创建表时设置默认值约束

创建表时可以使用 **DEFAULT** 关键字设置默认值约束，具体的语法规则如下：

<字段名> <数据类型> DEFAULT <默认值>;

```sql
# 创建数据表 tb_dept3，指定部门位置默认为 Beijing
CREATE TABLE tb_dept3
     (
     id INT(11) PRIMARY KEY,
     name VARCHAR(22),
     locatiON VARCHAR(50) DEFAULT 'Beijing'
     );
```

#### 在修改表时添加默认值约束

修改表时添加默认值约束的语法规则如下：

ALTER TABLE <数据表名>
CHANGE COLUMN <字段名> <数据类型> DEFAULT <默认值>;

```sql
# 修改数据表 tb_dept3，将部门位置的默认值修改为 Shanghai
ALTER TABLE tb_dept3
     CHANGE COLUMN locatiON
     locatiON VARCHAR(50) DEFAULT 'Shanghai';
```

#### 删除默认值约束

修改表时删除默认值约束的语法规则如下：

ALTER TABLE <数据表名>
CHANGE COLUMN <字段名> <字段名> <数据类型> DEFAULT NULL;

```sql
# 修改数据表 tb_dept3，将部门位置的默认值约束删除
ALTER TABLE tb_dept3
     CHANGE COLUMN locatiON
     locatiON VARCHAR(50) DEFAULT NULL;
```

### 非空约束

非空约束（NOT NULL）可以通过 CREATE TABLE 或 ALTER TABLE 语句实现。在表中某个列的定义后加上关键字 NOT NULL 作为限定词，来约束该列的取值不能为空。

非空约束（Not Null CONstraint）指字段的值不能为空。对于使用了非空约束的字段，如果用户在添加数据时没有指定值，数据库系统就会报错。

#### 1. 在创建表时设置非空约束

创建表时可以使用 **NOT NULL** 关键字设置非空约束

<字段名> <数据类型> NOT NULL;

```sql
# 创建数据表 tb_dept4，指定部门名称不能为空
CREATE TABLE tb_dept4
     (
     id INT(11) PRIMARY KEY,
     name VARCHAR(22) NOT NULL,
     locatiON VARCHAR(50)
     );
```

#### 2. 在修改表时添加非空约束

ALTER TABLE <数据表名>
CHANGE COLUMN <字段名>
<字段名> <数据类型> NOT NULL;

```sql
# 修改数据表 tb_dept4，指定部门位置不能为空
ALTER TABLE tb_dept4
     CHANGE COLUMN locatiON
     locatiON VARCHAR(50) NOT NULL;
```

#### 3. 删除非空约束

修改表时删除非空约束：

ALTER TABLE <数据表名>
CHANGE COLUMN <字段名> <字段名> <数据类型> NULL;

```sql
# 修改数据表 tb_dept4，将部门位置的非空约束删除
ALTER TABLE tb_dept4
     CHANGE COLUMN locatiON
     locatiON VARCHAR(50) NULL;
```

### 查看表中的约束

SHOW CREATE TABLE <数据表名>  \G;

```sql
# 创建数据表 tb_emp8 并指定 id 为主键约束，name 为唯一约束，deptId 为非空约束和外键约束，然后查看表中的约束
CREATE TABLE tb_emp8
     (
     id INT(11) PRIMARY KEY,
     name VARCHAR(22) UNIQUE,
     deptId INT(11) NOT NULL,
     salary FLOAT DEFAULT 0,
     CHECK(salary>0),
     FOREIGN KEY(deptId) REFERENCES tb_dept1(id)
     );
```

## 7. 索引

### 0. 学习资料

[数据库索引，终于懂了 - 文章详情 (itpub.net)](https://z.itpub.net/article/detail/F1555CC47E78A806F2D7A590A4AA2DAB)

### 1. 什么是索引

**官方定义：**一种帮助MySQL提高查询效率的数据结构

**优点：**

1. 大大加快数据的查询速度

**缺点：**

1. 索引维护需要耗费数据库资源
2. 索引需要占用磁盘空间
3. 当对表的数据进行增删改的时候，因要维护索引，速度会受到影响

### 2. 索引分类

1. 主键索引
   1. 设置为主键后数据库会自动建立索引，innodb为聚簇索引
   2. 主键所有列的值不能为null

2. 单值索引 ：又称为单列索引或普通索引
   1. 即一个索引只包含单个列，一个表可以有多个单列索引
   2. 如：  id（主键索引）  age(单值索引1)  name（单值索引2）
3. 唯一索引
   1. 索引列的值必须唯一，但允许有空值（因为唯一，所以只能有一列为null值）

4. 复合索引：即一个索引包含多个列
   1. id（主键索引）   （name，age）（复合索引）
   2. 如果为name和age单独创建索引，搜索WHERE name  AND  age  可能索引失效。如果为二者建立一个复合索引，则可以走索引。
5. 全文索引（5.7之前，只能用于MYISAM引擎）（很少用到）
   1. 全文索引类型为FULL TEXT，在定义索引的列上支持值的全文查找，允许在这些索引列中插入重复值或者空值。全文索引可以在CHAR，VARCHAR，TEXT类型列上创建。MYSQL只有MYISAM存储引擎支持全文索引。
   2. 在MySQL 5.6版本以前,只有MyISAM存储引擎支持全文引擎.在5.6版本中,InnoDB加入了对全文索引的支持,但是不支持中文全文索引.在5.7.6版本,MySQL内置了ngram全文解析器,用来支持亚洲语种的分词.


### 3. 使用索引

1. **查看索引**：SHOW INDEX FROM 表名

2. **创建索引 **

   1. **主键索引**

      ```sql
      # 创建表时创建
      CREATE TABLE t_user (id VARCHAR(20) PRIMARY KEY ,NAME VARCHAR(20))
      ```

   2. **单值索引**

      ```sql
      # 1.建表时创建   不能为索引起名
      CREATE TABLE t_user2(id VARCHAR(20) PRIMARY KEY,NAME VARCHAR(20),KEY(NAME),KEY(...))
      # 2.建表之后创建
      CREATE INDEX name_index ON t_user(NAME)
      ```

   3. **唯一索引**

      ```sql
      # 1.建表时创建   不能为索引起名
      CREATE TABLE t_user3(id VARCHAR(20) PRIMARY KEY,NAME VARCHAR(20),age INT(10),UNIQUE(age))
      
      # 2.建表之后创建
      CREATE UNIQUE INDEX name_index ON t_user3(NAME)
      ```

   4. **复合索引**

      ```sql
      # 1.建表时创建   不能为索引起名
      CREATE TABLE t_user4(id VARCHAR(20) PRIMARY KEY,NAME VARCHAR(20),age INT(10),KEY(NAME,age))
       # 2.建表之后创建
      CREATE INDEX name_age_index ON t_user4(NAME,age)
      ```

3. **删除索引：**DROP INDEX name_index ON t_user

### 4. 索引面试题

#### 复合索引面试题目

基于name，age，bir 三个字段建立复合索引

问：

1. name bir age  能否利用索引
2. name age bir  能否利用索引
3. age bir 能否利用索引
4. bir age  name  能否利用索引
5. age bir 能否利用索引

#### 复合索引生效规则

1. 最左前缀原则，查询时，第一个条件必须是复合索引最左侧定义的索引（即name，必须有name，并且为第一个条件）
2. 1. 即  name，age，bir可以。
   2. name age 可以
   3. name 可以
   4. name bir 可以
3. mysql引擎为更好利用索引，在查询中会动态调整查询字段顺序以便利用索引
   1. 带name的AND查询均可命中索引


#### 查看是否命中索引

sql语句前添加explain

```sql
EXPLAIN SELECT * FROM sys_user WHERE username="admin" AND phONe = "18888888888" AND email="201507802@qq.com"
```

![](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/%E7%B4%A2%E5%BC%95%E6%98%AF%E5%90%A6%E5%91%BD%E4%B8%AD%E6%9F%A5%E7%9C%8B.png)

1. 根据type的结果来判断是否命中索引

2. type结果值从好到坏依次是：

3. 1. system > const > eq_ref > ref > fulltext > ref_or_null > index_merge > unique_subquery > index_subquery > range > index > ALL一般来说，得保证查询至少达到range级别，最好能达到ref，否则就可能会出现性能问题。

4. possible_keys：sql所用到的索引

5. key：显示MySQL实际决定使用的键（索引）。如果没有选择索引，键是NULL

6. rows: 显示MySQL认为它执行查询时必须检查的行数。

7. partitiONs：分区https://www.jianshu.com/p/b17b62057499

### 5. 索引的底层原理

主键乱序插入一批数据

![image-20211209214912899](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211209214912899.png)

为了提高查询效率，mysql会自动帮我们按照主键的顺序进行排序。

优点：按顺序查找元素，比乱序查找元素速度更快

不足：数据量特别大时，效率还不是很高

扩展：数组复杂度O（1），根据索引直接获取值。而链表复杂度为O（n）,有几个元素就得查几次。

#### **优化**

排序后，虽然能提升查询效率，但是当数据量特别大的时候，还是需要一个挨着一个去查，Innodb引擎对此进行了分页（即一页存储几个节点，存储大小为16kb），之后再在页的基础上建立页目录。（页目录只存索引可指针。也是16kb）

![image-20211209215054180](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211209215054180.png)

**计算：**

id  int(4)  name varchar(20) age(4)      指针p(暂定8)

页：

一条数据：4+20+4+8=36字节

16kb*1024/36=455

页目录“：

一条记录：4+8=12

16kb*1024/12=1365

两层结构总量：

1365*455=621,075

三层结构总量：

1365 * 1365 * 455=847,767,375

#### **B+Tree与BTree**

区别：B+树只有叶子节点存储数据，页目录不存储数据。B树所有目录和节点都存储数据。同样的数据量，B树可能更深。

```text
B+Tree是在B-Tree(B树)基础上的一种优化，使其更适合实现外存储索引结构，InnoDB存储引擎就是用B+Tree实现其索引结构。

B-Tree每个节点中不仅包含数据的key值，还有data值。而每一个页的存储空间是有限的，如果data数据较大时将会导致每个节点(即一个页）能存储的key的数星很小，当存储的数据量很大时同样会导致B-Tree的深度较大，增大查询时的磁盘l/O次数，进而影响查询效率。在B+Tee中，所有数据记录节点都是按照键值大小顺序存放在同一层的叶子节点上，而非叶子节点上只存储key值信息，这样可以大大加大每个节点存储的key值数量降低B+Tree的高度。
```

注意：最顶层是常驻内存，查询时不需要读写IO，所以当有两层B+tree时，只需要读写一次磁盘IO。（注意：只有主键索引时，才会走常驻内存。如果是非主键索引，会先读写磁盘查询符合当前条件记录的主键，再去页目录找对应的主键。）

```text
B+Tree相对于B-Tree有几点不同:

1.非叶子节点只存储键值信息。

2.所有叶子节点之间都有一个链指针。
3.数据记录都存放在叶子节点中。

InnoDB存储引擎中页的大小为16KB，一般表的主键类型为INT(占用4个字节）或BIGINT(占用8个字节)，指针类型也一般为4或8个字节，也就是说一个页(B+Tree中的一个节点)中大概存储16KB(8B+8B)=1K个键值（因为是估值，为方便计算，这里的K取值为[10]^3)。也就是说一个深度为3的B+Tree索引可以维护10^3* 10^3* 10^3=10亿条记录。

实际情况中每个节点可能不能填充满，因此在数据库中，B+Tree的高度一般都在2~4层。mysql的innoDB存储引擎在设计时是将根节点常驻内存的，也就是说查找某—键值的行记录时最多只需要1~3次磁盘I/O操作。
```

#### **总结**

索引过程

1. 根据主键索引排序，排序时以链表相连
2. 将排完序后的链表分页，每页存储大小为16kb。页里面存储 主键+值+下一节点指针
3. 创建页目录，大小也为16kb，存储主键+当前主键的指针。
4. 三层的B+树，存储的数据量在8-10亿条
5. 三层B+树，根据主键去查会经过两次磁盘IO，如果经过非主键索引去查，要经过三次磁盘IO

### 6. 聚簇索引和非聚簇索引

#### 介绍

**聚簇索引**

将数据存储与索引放到了一起，索引结构的叶子节点保存了行数据

​                 ![img](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/JzONUNen3H9xN74Lz0K4Tw.png)        

**聚簇索引不一定是主键索引，主键索引一定是聚簇索引。**



**非聚簇索引**

将数据与索引分开存储，索引结构的叶子节点指向了数据对应的位置

**注意：**

在Innodb中，在聚簇索引之上创建的索引称之为辅助索引，非聚簇索引都是辅助索引，像复合索引，前缀索引，唯一索引。辅助索引叶子节点存储的不再是行的物理值，而是主键值，辅助索引访问数据库总是需要二次查找。

#### 聚簇索引存储结构

假如我们为name列创建了索引，此时会生成以name为基准的辅助索引树。树的页目录中存储主键以及其指向的地址。当我们查询时，他会先在辅助索引树上获取到相应的主键，再到聚簇索引树上查找对应的值。

![image-20211209220543046](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211209220543046.png)

**问题：**

为什么辅助索引上存的是主键的索引，而非地址？有了地址不是可以直接访问到对应的数据吗？

当存地址时，如果我们进行数据的增删改，那么主键对应的地址是会相应进行变化的，这样再次查询时，会浪费更多的性能。

#### Innodb和MYISAM中的区别

**InnoDB**

1. InnoDB使用的是聚簇索引，将主键组织到一棵B+树中，而行数据就储存在叶子节点上，若使用"WHERE id =14"这样的条件查找主键，则按照B+树的检索算法即可查找到对应的叶节点，之后获得行数据。
2. 若对Name列进行条件搜索，则需要两个步骤∶第一步在辅助索引B+树中检索Name，到达其叶子节点获取对应的主键。第二步使用主键在主索引|B+树种再执行一次B+树检索操作，最终到达叶子节点即可获取整行数据。(重点在于通过其他键需要建立辅助索引)
3. 聚簇索引默认是主键，如果表中没有定义主键，InnoDB会选择一个唯一且非空的索引代替。如果没有这样的索引，InnoDB会隐式定义一个主键(类似oracle中的Rowld)来作为聚簇索引。如果已经设置了主键为聚簇索引又希望再单独设置聚簇索引，必须先删除主键，然后添加我们想要的聚簇索引，最后恢复设置主键即可。

![image-20211209221015862](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211209221015862.png)

**MYISAM**

MyISAM使用的是非聚簇索引，非聚簇索引的两棵B+树看上去没什么不同，节点的结构完全一致只是存储的内容不同而已，主键索引B+树的节点存储了主键;辅助键索引B+树存储了辅助键。表数据存储在独立的地方，这两颗B+树的叶子节点都使用一个地址指向真正的装数据，对于表数据来说，这两个键没有任何差别。由于索引树是独立的，通过辅助键检索无需访问主键的索引树。

![image-20211209220929371](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211209220929371.png)

#### 使用聚簇索引的优势

```text
问题:每次使用辅助索引检索都要经过两次B+树查找，看上去聚簇索引的效率明显要低于非聚簇索引，这不是多此一举吗?聚簇索引的优势在哪?

1.由于行数据和聚簇索引的叶十子节点存储在一起，同一页中会有多条行数据，访问同一数据页不同行记录时，已经把页加载到了Buffer中(缓存器),再次访问时，会在内存中完成访问，不必访问磁盘。这样主键和行数据是一起被载入内存的，找到叶子节点就可以立刻将行数据返回了，如果按照主键id来组织数据，获得数据更快。
                                                                  
2. 辅助索引的叶子节点，存储主键值，而不是数据的存放地址。好处是当行数据放生变化时，索引树的节点也需要分裂变化﹔或者是我们需要查找的的数据，在上一次IO读写的缓存中没有，需要发生一次新的IO操作时，可以避免对辅助索引的维护工作，只需要维护聚簇索引树锁好了。另一个好处是，因为辅助索引存放的是主键值，减少了辅时索引占用的存储空间大小。
```

#### 聚簇索引注意事项

```text
问题：聚簇索引需要注意什么?

1.当使用主键为聚簇索引时，主键最好不要使用uid，因为uuid的值太过离散，不适合排序且可能出线新抓增加记录的uid，会插入在索引树中间的位置，导致索引树调整复杂度变大，消耗更多的时间和资源。

2.建议使用int类型的自增，方便排序并且默认会在索引树的末尾增加主键值，对索引树的结构影响最小。而且，键值占用的存储空间越大，辅的索引中保存的主键值
也会跟着变大，占用存储空间,也会影响到Io操作读取到的数据量。
```

#### 为什么主键使用自增id

```
问题：为什么主键通常建议使用自增id

聚簇索引的改数据的物理存放顺序与索引顺序是一致的，即:只要索引是相邻的，那么对应的数据一定也是相邻地存放在磁盘上的。如果主键不是自增id，那么可以想象，它会干些什么，不断地调整数据的物理地址、分页，当然也有其他一些措施来减少这些操作，但却无法彻底避免。但，如果是自增的，那就简单了，它只需要一页一页地写，索引结构相对紧凑，磁盘碎片少，效率也高。
```

## 8. 范式

范式程度越高，数据粒度越小。性能上也会降低。

### 建表原则

符合一范式的基础上，建立二范式。三范式根据实际要求，不一定必须符合

### 第一范式

要求：每个属性都不可以再分

![image-20211210161546003](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211210161546003.png)

例子：
![](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211210161616854.png)
存在问题：
1、 数据沉余：如姓名，系名，系主任，重复了很多次
2、 插入异常：如果我新建一个计算机系，系主任是小张，学生还没入学，那么id，姓名，课名等字段无法设置
3、 删除异常：假如小明毕业，删除小明的信息，那么小明所在系也会被删除。
4、 修改异常：如果小明转专业，那么我得把小明的系名和系主任都改了，并且得多次修改，因为小明出现了三次

### 第二范式
要求：在一范式的基础上，消除了非主属性对码的依赖。
码：在一个表中，可以决定一个元素的属性集合（id和课名一旦确定，那么所有的属性都可以确定）
![image-20211210161709517](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211210161709517.png)
主属性：码就是主属性
非主属性：除主属性之外的属性
函数依赖：y=f(x);x的值决定y，y依赖x。如：1、id确定之后，姓名，系别，系主任都可以确定了。2、系名确定了，系主任就确定。
完全函数依赖：x1，x2等决定y。如：id和课名可以决定分数。那么分数就是完全依赖于id和课名。
部分函数依赖：y依赖于x，但是y不完全依赖于x。如用id和课名去决定姓名。姓名由id就直接确定了，所以姓名不完全依赖与id和课名这两个字段

**判断是否为二范式**

数据表中是否存在非主属性对码的部分函数依赖。若存在，则数据表最高只符合一范式，若不存在，则符合二范式。
步骤;
1. 找出数据表中所有的码（id,课名）;
2. 根据第一步所得到的码，找出所有的主属性。id和课名
3. 数据表中，除去所有的主属性，剩下的就是非主属性。
4. 查看是否存在非主属性对码的部分函数依赖。
    例子：![image-20211210161736378](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211210161736378.png)
    存在问题：

1. 数据沉余：***解决***
2. 插入异常：如果我新建一个计算机系，系主任是小张，学生还没入学，那么id，姓名，课名等字段无法设置
3. 删除异常：假如小明毕业，删除小明的信息，那么小明所在系也会被删除。
4. 修改异常：如果小明转专业，那么我得把小明的系名和系主任都改了，并且得多次修改，因为小明出现了三次。***解决***

### 第三范式
在二范式的基础上，消除了非主属性对码的传递函数依赖。

![image-20211210161832266](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211210161832266.png)

传递函数依赖：y依赖于x，z又依赖于y，那么z依赖于x。如：系别依赖于学号，系主任依赖于系别，那么系主任也依赖于学号。
例子：

![image-20211210161800979](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211210161800979.png)



问题：

1. 插入异常：如果我新建一个计算机系，系主任是小张，学生还没入学，那么id，姓名，课名等字段无法设置 ***解决***
2. 删除异常：假如小明毕业，删除小明的信息，那么小明所在系也会被删除。 ***解决***

## 9. 关系

### 一对一

![image-20211210200700529](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211210200700529.png)



### 一对多

![image-20211210200705814](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211210200705814.png)

### 多对多

![image-20211210200713214](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211210200713214.png)

![image-20211211100637555](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211211100637555.png)

**建表语句**

```sql
CREATE TABLE `tb_student` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id，唯一标识一名学生。',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '学生姓名',
  `stu_card` varchar(255) NOT NULL DEFAULT '' COMMENT '学生学号',
  `sex` bit(1) NOT NULL DEFAULT b'0' COMMENT '学生性别。0:男，1：女',
  `enter_year` date DEFAULT NULL COMMENT '入学年份',
  `institute_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '学院id',
  PRIMARY KEY (`id`)
) 

CREATE TABLE `tb_institute` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '系id',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '系名称',
  `director` varchar(255) NOT NULL DEFAULT '' COMMENT '系主任',
  PRIMARY KEY (`id`)
)

CREATE TABLE `tb_lession` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '课程id',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '课程名',
  `lession_card` varchar(255) NOT NULL DEFAULT '' COMMENT '课程编号',
  `limit` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '限制人数',
  `credit` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '课程学分',
  PRIMARY KEY (`id`)
)

CREATE TABLE `tb_student_lession` (
  `stu_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '学生id',
  `les_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '课程id',
  `score` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '分数'
)


```

