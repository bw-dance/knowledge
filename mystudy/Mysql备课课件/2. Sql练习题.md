### 部门管理系列

#### 表

**dept表**

deptno：部门编号

dname：部门名称

loc：地点

![](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211210130614881.png)

**emp表**

empno：员工编号

ename：员工姓名

job：员工职务

1. MANAGER：管理者
2. CLERK：店员
3. SALEMAN：售货员
4. ANALYST: 分析员
5. PRESIDENT:总裁

mgr：经理

hiredate：入职日期

sal：薪水

comm：提成

deptno：所在部门id

![image-20211210130255348](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211210130255348.png)

**salgrate表**

grade：等级 

losal：薪水

hisal：最高工资

![image-20211210130436515](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211210130436515.png)

#### 建表语句

```sql
CREATE TABLE dept
    (deptno int(2) not null ,
    dname VARCHAR(14) ,
    loc VARCHAR(13),
    primary key (deptno)
    );
CREATE TABLE emp
    (empno int(4)  not null ,
    ename VARCHAR(10),
    job VARCHAR(9),
    mgr INT(4),
    hiredate DATE  DEFAULT NULL,
    sal DOUBLE(7,2),
    comm DOUBLE(7,2),
    primary key (empno),
    deptno INT(2) 
    )
    ;
 
CREATE TABLE salgrate
    ( gradr INT,
    losal INT,
    hisal INT );
		
		
		
 
 
INSERT INTO `dept` VALUES (10, 'ACCOUNTING', 'NEW YORK');
INSERT INTO `dept` VALUES (20, 'RESEARCH', 'DALLAS');
INSERT INTO `dept` VALUES (30, 'SALES', 'CHICAGO');
INSERT INTO `dept` VALUES (40, 'OPERATIONS', 'BOSTON');
  
INSERT INTO `emp` VALUES (7369, 'SMITH', 'CLERK', 7902, '1980-12-17', 800.00, NULL, 20);
INSERT INTO `emp` VALUES (7499, 'ALLEN', 'SALESMAN', 7698, '1981-02-20', 1600.00, 300.00, 30);
INSERT INTO `emp` VALUES (7521, 'WARD', 'SALESMAN', 7698, '1981-02-22', 1250.00, 500.00, 30);
INSERT INTO `emp` VALUES (7566, 'JONES', 'MANAGER', 7839, '1981-04-02', 2975.00, NULL, 20);
INSERT INTO `emp` VALUES (7654, 'MARTIN', 'SALESMAN', 7698, '1981-09-28', 1250.00, 1400.00, 30);
INSERT INTO `emp` VALUES (7698, 'BLAKE', 'MANAGER', 7839, '1981-05-01', 2850.00, NULL, 30);
INSERT INTO `emp` VALUES (7782, 'CLARK', 'MANAGER', 7839, '1981-06-09', 2450.00, NULL, 10);
INSERT INTO `emp` VALUES (7788, 'SCOTT', 'ANALYST', 7566, '1987-04-19', 3000.00, NULL, 20);
INSERT INTO `emp` VALUES (7839, 'KING', 'PRESIDENT', NULL, '1981-11-17', 5000.00, NULL, 10);
INSERT INTO `emp` VALUES (7844, 'TURNER', 'SALESMAN', 7698, '1981-09-08', 1500.00, 0.00, 30);
INSERT INTO `emp` VALUES (7876, 'ADAMS', 'CLERK', 7788, '1987-05-23', 1100.00, NULL, 20);
INSERT INTO `emp` VALUES (7900, 'JAMES', 'CLERK', 7698, '1981-12-03', 950.00, NULL, 30);
INSERT INTO `emp` VALUES (7902, 'FORD', 'ANALYST', 7566, '1981-12-03', 3000.00, NULL, 20);
INSERT INTO `emp` VALUES (7934, 'MILLER', 'CLERK', 7782, '1982-01-23', 1300.00, NULL, 10);


INSERT INTO `salgrade` VALUES (1, 700, 1200);
INSERT INTO `salgrade` VALUES (2, 1201, 1400);
INSERT INTO `salgrade` VALUES (3, 1401, 2000);
INSERT INTO `salgrade` VALUES (4, 2001, 3000);
INSERT INTO `salgrade` VALUES (5, 3001, 9999);
```

#### 1、取得每个部门最高薪水的人员名称

```sql
# 第一步:取得每个部门最高薪水(按照部门编号分组，找出每一组最大值)
SELECT deptno, MAX(sal) AS maxsal FROM emp GROUP BY deptno
+--------+---------+
| deptno | maxsal  |
+--------+---------+
|     10 | 5000.00 |
|     20 | 3000.00 |
|     30 | 2850.00 |
+--------+---------+
# 第二步:将以上的查询结果当做一张临时表t，t和emp表连接，条件:t.deptno = e.deptno and t.maxsal = e.sal

SELECT ename,e1.deptno,sal
FROM emp e1
INNER JOIN
(
    SELECT deptno, MAX(sal) AS maxsal 
    FROM emp 
    GROUP BY deptno 
)e2
ON e1.deptno = e2.deptno AND e1.sal=e2.maxsal

+-------+--------+---------+
| ename | deptno | maxsal  |
+-------+--------+---------+
| BLAKE |     30 | 2850.00 |
| SCOTT |     20 | 3000.00 |
| KING  |     10 | 5000.00 |
| FORD  |     20 | 3000.00 |
+-------+--------+---------+
```

#### 2、哪些人的薪水在部门的平均薪水之上

```sql
# 第一步:找出每个部门的平均薪水
select deptno,avg(sal) as avgsal from emp group by deptno;
+--------+-------------+
| deptno | avgsal      |
+--------+-------------+
|     10 | 2916.666667 |
|     20 | 2175.000000 |
|     30 | 1566.666667 |
+--------+-------------+
# 第二步:将以上查询结果当做t表，t和emp表连接,条件:部门编号相同，并且emp的sal大于t表的avgsal
SELECT e1.deptno,ename,sal
FROM emp e1
INNER JOIN
(
    SELECT deptno, AVG(sal) AS avgsal 
    FROM emp 
    GROUP BY deptno 
) e2
ON e1.deptno = e2.deptno AND e1.sal>e2.avgsal
+--------+-------------+-------+---------+
| deptno | avgsal      | ename | sal     |
+--------+-------------+-------+---------+
|     30 | 1566.666667 | ALLEN | 1600.00 |
|     20 | 2175.000000 | JONES | 2975.00 |
|     30 | 1566.666667 | BLAKE | 2850.00 |
|     20 | 2175.000000 | SCOTT | 3000.00 |
|     10 | 2916.666667 | KING  | 5000.00 |
|     20 | 2175.000000 | FORD  | 3000.00 |
+--------+-------------+-------+---------+
```

#### 3、取得部门中（所有人的）平均的薪水等级

平均的薪水等级:先计算每一个人薪水的等级，然后找出薪水等级的平均值。

```sql
 # 第一步:找出每个人的薪水等级,emp e和salgrade s表连接。连接条件:e.sal between s.losal and s.hisal
  SELECT 
        e.ename,e.sal,e.deptno,s.grade
    FROM
        emp e
    INNER JOIN
        salgrade s
    ON
        e.sal between s.losal and s.hisal;
         +--------+---------+--------+-------+
    | ename  | sal     | deptno | grade |
    +--------+---------+--------+-------+
    | CLARK  | 2450.00 |     10 |     4 |
    | KING   | 5000.00 |     10 |     5 |
    | MILLER | 1300.00 |     10 |     2 |
 
    | SMITH  |  800.00 |     20 |     1 |
    | ADAMS  | 1100.00 |     20 |     1 |
    | SCOTT  | 3000.00 |     20 |     4 |
    | FORD   | 3000.00 |     20 |     4 |
    | JONES  | 2975.00 |     20 |     4 |
 
    | MARTIN | 1250.00 |     30 |     2 |
    | TURNER | 1500.00 |     30 |     3 |
    | BLAKE  | 2850.00 |     30 |     4 |
    | ALLEN  | 1600.00 |     30 |     3 |
    | JAMES  |  950.00 |     30 |     1 |
    | WARD   | 1250.00 |     30 |     2 |
    +--------+---------+--------+-------+
 
 # 第二步:基于以上的结果继续按照deptno分组，求grade的平均值。
SELECT deptno,AVG(s.grade) 
FROM emp e 
INNER JOIN 
salgrade s WHERE  e.sal BETWEEN losal AND hisal
GROUP BY deptno;
      +--------+--------------+
    | deptno | avg(s.grade) |
    +--------+--------------+
    |     10 |       3.6667 |
    |     20 |       2.8000 |
    |     30 |       2.5000 |
    +--------+--------------+
```

平均薪水的等级:先计算平均薪水，然后找出每个平均薪水的等级值。

```sql
SELECT deptno,grade 
FROM salgrade s
INNER JOIN 
(
SELECT deptno, AVG(sal) AS avgsal 
FROM emp 
GROUP BY deptno ) d
ON d.avgsal BETWEEN losal AND hisal
```



#### 4、不准用函数（MAX ），取得最高薪水

```sql
# 第一种:sal降序，limit 1
SELECT ename,sal FROM emp ORDER BY sal DESC LIMIT 1;
+-------+---------+
| ename | sal     |
+-------+---------+
| KING  | 5000.00 |
+-------+---------+
# 第二种方案:
SELECT MAX(sal) FROM emp;
+---------+
| sal     |
+---------+
| 5000.00 |
+---------+
 
# 第三种方案:表的自连接
SELECT sal FROM emp WHERE sal not in(SELECT distinct a.sal FROM emp a join emp b ON a.sal < b.sal);
+---------+
| sal     |
+---------+
| 5000.00 |
+---------+

 select 
    distinct a.sal 
from 
    emp a 
join 
    emp b 
on 
    a.sal < b.sal

a表
+---------+
| sal     |
+---------+
|  800.00 |
| 1600.00 |
| 1250.00 |
| 2975.00 |
| 1250.00 |
| 2850.00 |
| 2450.00 |
| 3000.00 |
| 5000.00 |
| 1500.00 |
| 1100.00 |
|  950.00 |
| 3000.00 |
| 1300.00 |
+---------+
 
b表
+---------+
| sal     |
+---------+
|  800.00 |
| 1600.00 |
| 1250.00 |
| 2975.00 |
| 1250.00 |
| 2850.00 |
| 2450.00 |
| 3000.00 |
| 5000.00 |
| 1500.00 |
| 1100.00 |
|  950.00 |
| 3000.00 |
| 1300.00 |
+---------+
```

####  5、取得平均薪水最高的部门的部门编号

第一种方案:降序取第一个

```	sql
#第一步:找出每个部门的平均薪水
SELECT deptno,avg(sal) AS avgsal FROM emp GROUP BY deptno;
        +--------+-------------+
        | deptno | avgsal      |
        +--------+-------------+
        |     10 | 2916.666667 |
        |     20 | 2175.000000 |
        |     30 | 1566.666667 |
        +--------+-------------+
#第二步:降序选第一个。
SELECT deptno,avg(sal) AS avgsal FROM emp GROUP BY deptno ORDER BY avgsal DESC limit 1;
        +--------+-------------+
        | deptno | avgsal      |
        +--------+-------------+
        |     10 | 2916.666667 |
        +--------+-------------+
```

第二种方案:MAX

```sql
#第一步:找出每个部门的平均薪水
SELECT deptno,avg(sal) AS avgsal FROM emp GROUP BY deptno;
    +--------+-------------+
    | deptno | avgsal      |
    +--------+-------------+
    |     10 | 2916.666667 |
    |     20 | 2175.000000 |
    |     30 | 1566.666667 |
    +--------+-------------+
 
#第二步:找出以上结果中avgsal最大的值。
SELECT MAX(t.avgsal) FROM (SELECT avg(sal) AS avgsal FROM emp GROUP BY deptno) t;
    +---------------+
    | MAX(t.avgsal) |
    +---------------+
    |   2916.666667 |
    +---------------+
 
#第三步:
    SELECT 
        deptno,avg(sal) AS avgsal 
    FROM 
        emp 
    GROUP BY 
        deptno
    having
        avgsal = (SELECT MAX(t.avgsal) FROM (SELECT avg(sal) AS avgsal FROM emp GROUP BY deptno) t);
     
    +--------+-------------+
    | deptno | avgsal      |
    +--------+-------------+
    |     10 | 2916.666667 |
    +--------+-------------+
 
```

#### 6、取得平均薪水最高的部门的部门名称

1. 先获取所有部门的所有薪水
2. 对这些数据进行分组

```sql
SELECT 
    d.dname,avg(e.sal) AS avgsal 
FROM 
    emp e
INNER JOIN
    dept d
ON
    e.deptno = d.deptno
GROUP BY 
    d.dname
ORDER BY 
    avgsal DESC 
limit 
    1;
 
+------------+-------------+
| dname      | avgsal      |
+------------+-------------+
| ACCOUNTING | 2916.666667 |
+------------+-------------+
```

#### 7、求平均薪水的等级最低的部门的部门名称

1. 获取每个部门的平均薪水
2. 获取各个部门的平均薪水等级
3. 排序

```sql
#平均薪水是800
#平均薪水是900
#那么他俩都是1级别。
 
#第一步:找出每个部门的平均薪水
SELECT deptno,avg(sal) AS avgsal FROM emp GROUP BY deptno;
+--------+-------------+
| deptno | avgsal      |
+--------+-------------+
|     10 | 2916.666667 |
|     20 | 2175.000000 |
|     30 | 1566.666667 |
+--------+-------------+
 
#第二步:找出每个部门的平均薪水的等级
#以上t表和salgrade表连接，条件:t.avgsal BETWEEN s.losal AND s.hisal
 
SELECT 
    t.*,s.grade
FROM
    (SELECT d.dname,avg(sal) AS avgsal FROM emp e join dept d ON e.deptno = d.deptno GROUP BY d.dname) t
join
    salgrade s
ON
    t.avgsal BETWEEN s.losal AND s.hisal;
 
+------------+-------------+-------+
| dname      | avgsal      | grade |
+------------+-------------+-------+
| SALES      | 1566.666667 |     3 |
| ACCOUNTING | 2916.666667 |     4 |
| RESEARCH   | 2175.000000 |     4 |
+------------+-------------+-------+
 
SELECT 
    t.*,s.grade
FROM
    (SELECT d.dname,AVG(sal) AS avgsal FROM emp e JOIN dept d ON e.deptno = d.deptno GROUP BY d.dname) t
JOIN
    salgrade s
ON
    t.avgsal BETWEEN s.losal AND s.hisal ORDER BY grade ASC LIMIT 1;
 
+-------+-------------+-------+
| dname | avgsal      | grade |
+-------+-------------+-------+
| SALES | 1566.666667 |     3 |
+-------+-------------+-------+
 
 附：
 
平均薪水最低的对应的等级一定是最低的.
    SELECT avg(sal) AS avgsal FROM emp GROUP BY deptno ORDER BY avgsal ASC limit 1;
    +-------------+
    | avgsal      |
    +-------------+
    | 1566.666667 |
    +-------------+
 
    SELECT grade FROM salgrade WHERE (SELECT avg(sal) AS avgsal FROM emp GROUP BY deptno ORDER BY avgsal ASC limit 1) BETWEEN losal AND hisal;
    +-------+
    | grade |
    +-------+
    |     3 |
    +-------+
```

#### 8、取得比普通员工(员工代码没有在 mgr 字段上出现的) 的最高薪水还要高的领导人姓名

1. 求出普通员工的最高工资
2. 求比普通员工的最高工资还多的人。

```sql
mysql> SELECT distinct mgr FROM emp WHERE mgr is not null;
+------+
| mgr  |
+------+
| 7902 |
| 7698 |
| 7839 |
| 7566 |
| 7788 |
| 7782 |
+------+

#员工编号没有在以上范围内的都是普通员工。
#第一步:找出普通员工的最高薪水
#not in在使用的时候，后面小括号中记得排除NULL。
SELECT MAX(sal) FROM emp WHERE empno not in(SELECT distinct mgr FROM emp WHERE mgr is not null);
+----------+
| MAX(sal) |
+----------+
|  1600.00 |
+----------+
 
#第二步:找出高于1600的
SELECT ename,sal FROM emp WHERE sal > (SELECT MAX(sal) FROM emp WHERE empno not in(SELECT distinct mgr FROM emp WHERE mgr is not null));
+-------+---------+
| ename | sal     |
+-------+---------+
| JONES | 2975.00 |
| BLAKE | 2850.00 |
| CLARK | 2450.00 |
| SCOTT | 3000.00 |
| KING  | 5000.00 |
| FORD  | 3000.00 |
+-------+---------+
```

#### 9、取得薪水最高的前五名员工

```sql
SELECT ename,sal FROM emp ORDER BY sal DESC limit 5;
+-------+---------+
| ename | sal     |
+-------+---------+
| KING  | 5000.00 |
| SCOTT | 3000.00 |
| FORD  | 3000.00 |
| JONES | 2975.00 |
| BLAKE | 2850.00 |
+-------+---------+
```

#### 10、取得薪水最高的第六到第十名员工

```sql
SELECT ename,sal FROM emp ORDER BY sal DESC limit 5, 5;
+--------+---------+
| ename  | sal     |
+--------+---------+
| CLARK  | 2450.00 |
| ALLEN  | 1600.00 |
| TURNER | 1500.00 |
| MILLER | 1300.00 |
| MARTIN | 1250.00 |
+--------+---------+
```

#### 11、取得最后入职的 5 名员工，日期也可以降序，升序。

```sql
 SELECT ename,hiredate FROM emp ORDER BY hiredate DESC limit 5;
 
    +--------+------------+
    | ename  | hiredate   |
    +--------+------------+
    | ADAMS  | 1987-05-23 |
    | SCOTT  | 1987-04-19 |
    | MILLER | 1982-01-23 |
    | FORD   | 1981-12-03 |
    | JAMES  | 1981-12-03 |
    +--------+------------+
```

#### 12、取得每个薪水等级有多少员工。分组count

```sql
#第一步:找出每个员工的薪水等级
SELECT 
    e.ename,e.sal,s.grade 
FROM 
    emp e 
join 
    salgrade s 
ON 
    e.sal BETWEEN s.losal AND s.hisal;
+--------+---------+-------+
| ename  | sal     | grade |
+--------+---------+-------+
| SMITH  |  800.00 |     1 |
| ALLEN  | 1600.00 |     3 |
| WARD   | 1250.00 |     2 |
| JONES  | 2975.00 |     4 |
| MARTIN | 1250.00 |     2 |
| BLAKE  | 2850.00 |     4 |
| CLARK  | 2450.00 |     4 |
| SCOTT  | 3000.00 |     4 |
| KING   | 5000.00 |     5 |
| TURNER | 1500.00 |     3 |
| ADAMS  | 1100.00 |     1 |
| JAMES  |  950.00 |     1 |
| FORD   | 3000.00 |     4 |
| MILLER | 1300.00 |     2 |
+--------+---------+-------+
 
#第二步:继续按照grade分组统计数量
SELECT 
    s.grade ,count(*)
FROM 
    emp e 
join 
    salgrade s 
ON 
    e.sal BETWEEN s.losal AND s.hisal
GROUP BY
    s.grade;
 
+-------+----------+
| grade | count(*) |
+-------+----------+
|     1 |        3 |
|     2 |        3 |
|     3 |        2 |
|     4 |        5 |
|     5 |        1 |
+-------+----------+
```

#### 13、面试题:

有 3 个表 S(学生表)，C（课程表），SC（学生选课表）
S（SNO，SNAME）代表（学号，姓名）
C（CNO，CNAME，CTEACHER）代表（课号，课名，教师）
SC（SNO，CNO，SCGRADE）代表（学号，课号，成绩）

问题:

1. 找出没选过“黎明”老师的所有学生姓名。
2. 列出 2 门以上（含2 门）不及格学生姓名及平均成绩。
3. 即学过 1 号课程又学过 2 号课所有学生的姓名。

#### 14、列出所有员工及领导的姓名

```sql
SELECT 
    a.ename '员工', b.ename '领导'
FROM
    emp a
left join
    emp b
ON
    a.mgr = b.empno;
 
+--------+-------+
| 员工   | 领导    |
+--------+-------+
| SMITH  | FORD  |
| ALLEN  | BLAKE |
| WARD   | BLAKE |
| JONES  | KING  |
| MARTIN | BLAKE |
| BLAKE  | KING  |
| CLARK  | KING  |
| SCOTT  | JONES |
| KING   | NULL  |
| TURNER | BLAKE |
| ADAMS  | SCOTT |
| JAMES  | BLAKE |
| FORD   | JONES |
| MILLER | CLARK |
+--------+-------+
```

#### 15、列出受雇日期早于其直接上级的所有员工的编号,姓名,部门名称

emp a 员工表  ;  emp b 领导表  ;  a.mgr = b.empno AND a.hiredate < b.hiredate

```sql
SELECT 
    a.ename '员工', a.hiredate, b.ename '领导', b.hiredate, d.dname
FROM
    emp a
join
    emp b
ON
    a.mgr = b.empno
join
    dept d
ON
    a.deptno = d.deptno
WHERE
     a.hiredate < b.hiredate;
 
+-------+------------+-------+------------+------------+
| 员工     | hiredate   | 领导    | hiredate   | dname      |
+-------+------------+-------+------------+------------+
| CLARK | 1981-06-09 | KING  | 1981-11-17 | ACCOUNTING |
| SMITH | 1980-12-17 | FORD  | 1981-12-03 | RESEARCH   |
| JONES | 1981-04-02 | KING  | 1981-11-17 | RESEARCH   |
| ALLEN | 1981-02-20 | BLAKE | 1981-05-01 | SALES      |
| WARD  | 1981-02-22 | BLAKE | 1981-05-01 | SALES      |
| BLAKE | 1981-05-01 | KING  | 1981-11-17 | SALES      |
+-------+------------+-------+------------+------------+
```

#### 16、 列出部门名称和这些部门的员工信息, 同时列出那些没有员工的部门

```sql
SELECT 
    e.*,d.dname
FROM
    emp e
right join
    dept d
ON
    e.deptno = d.deptno;
 
+-------+--------+-----------+------+------------+---------+---------+--------+------------+
| EMPNO | ENAME  | JOB       | MGR  | HIREDATE   | SAL     | COMM    | DEPTNO | dname      |
+-------+--------+-----------+------+------------+---------+---------+--------+------------+
|  7782 | CLARK  | MANAGER   | 7839 | 1981-06-09 | 2450.00 |    NULL |     10 | ACCOUNTING |
|  7839 | KING   | PRESIDENT | NULL | 1981-11-17 | 5000.00 |    NULL |     10 | ACCOUNTING |
|  7934 | MILLER | CLERK     | 7782 | 1982-01-23 | 1300.00 |    NULL |     10 | ACCOUNTING |
|  7369 | SMITH  | CLERK     | 7902 | 1980-12-17 |  800.00 |    NULL |     20 | RESEARCH   |
|  7566 | JONES  | MANAGER   | 7839 | 1981-04-02 | 2975.00 |    NULL |     20 | RESEARCH   |
|  7788 | SCOTT  | ANALYST   | 7566 | 1987-04-19 | 3000.00 |    NULL |     20 | RESEARCH   |
|  7876 | ADAMS  | CLERK     | 7788 | 1987-05-23 | 1100.00 |    NULL |     20 | RESEARCH   |
|  7902 | FORD   | ANALYST   | 7566 | 1981-12-03 | 3000.00 |    NULL |     20 | RESEARCH   |
|  7499 | ALLEN  | SALESMAN  | 7698 | 1981-02-20 | 1600.00 |  300.00 |     30 | SALES      |
|  7521 | WARD   | SALESMAN  | 7698 | 1981-02-22 | 1250.00 |  500.00 |     30 | SALES      |
|  7654 | MARTIN | SALESMAN  | 7698 | 1981-09-28 | 1250.00 | 1400.00 |     30 | SALES      |
|  7698 | BLAKE  | MANAGER   | 7839 | 1981-05-01 | 2850.00 |    NULL |     30 | SALES      |
|  7844 | TURNER | SALESMAN  | 7698 | 1981-09-08 | 1500.00 |    0.00 |     30 | SALES      |
|  7900 | JAMES  | CLERK     | 7698 | 1981-12-03 |  950.00 |    NULL |     30 | SALES      |
|  NULL | NULL   | NULL      | NULL | NULL       |    NULL |    NULL |   NULL | OPERATIONS |
+-------+--------+-----------+------+------------+---------+---------+--------+------------+
```

#### 17、列出至少有 5 个员工的所有部门

按照部门编号分组，计数，筛选出 >= 5

```sql
SELECT 
    deptno
FROM
    emp
GROUP BY
    deptno
having
    count(*) >= 5;
 
+--------+
| deptno |
+--------+
|     20 |
|     30 |
+--------+
```

#### 18、列出薪金比"SMITH" 多的所有员工信息

```sql
SELECT ename,sal FROM emp WHERE sal > (SELECT sal FROM emp WHERE ename = 'SMITH');
+--------+---------+
| ename  | sal     |
+--------+---------+
| ALLEN  | 1600.00 |
| WARD   | 1250.00 |
| JONES  | 2975.00 |
| MARTIN | 1250.00 |
| BLAKE  | 2850.00 |
| CLARK  | 2450.00 |
| SCOTT  | 3000.00 |
| KING   | 5000.00 |
| TURNER | 1500.00 |
| ADAMS  | 1100.00 |
| JAMES  |  950.00 |
| FORD   | 3000.00 |
| MILLER | 1300.00 |
+--------+---------+
```

#### 19、 列出所有"CLERK"( 办事员) 的姓名及其部门名称, 部门的人数

```sql
SELECT ename,job FROM emp WHERE job = 'CLERK';
+--------+-------+
| ename  | job   |
+--------+-------+
| SMITH  | CLERK |
| ADAMS  | CLERK |
| JAMES  | CLERK |
| MILLER | CLERK |
+--------+-------+
 
SELECT 
    e.ename,e.job,d.dname
FROM 
    emp e
join
    dept d
ON
    e.deptno = d.deptno
WHERE 
    e.job = 'CLERK';
 
+--------+-------+------------+
| ename  | job   | dname      |
+--------+-------+------------+
| MILLER | CLERK | ACCOUNTING |
| SMITH  | CLERK | RESEARCH   |
| ADAMS  | CLERK | RESEARCH   |
| JAMES  | CLERK | SALES      |
+--------+-------+------------+
 
SELECT 
    e.ename,e.job,d.dname,d.deptno
FROM 
    emp e
join
    dept d
ON
    e.deptno = d.deptno
WHERE 
    e.job = 'CLERK';
 
+--------+-------+------------+--------+
| ename  | job   | dname      | deptno |
+--------+-------+------------+--------+
| MILLER | CLERK | ACCOUNTING |     10 |
| SMITH  | CLERK | RESEARCH   |     20 |
| ADAMS  | CLERK | RESEARCH   |     20 |
| JAMES  | CLERK | SALES      |     30 |
+--------+-------+------------+--------+
 
 
#每个部门的人数？
SELECT deptno, count(*) AS deptcount FROM emp GROUP BY deptno;
+--------+-----------+
| deptno | deptcount |
+--------+-----------+
|     10 |         3 |
|     20 |         5 |
|     30 |         6 |
+--------+-----------+
 
SELECT 
    t1.*,t2.deptcount
FROM
    (SELECT 
        e.ename,e.job,d.dname,d.deptno
    FROM 
        emp e
    join
        dept d
    ON
        e.deptno = d.deptno
    WHERE 
        e.job = 'CLERK') t1
join
    (SELECT deptno, count(*) AS deptcount FROM emp GROUP BY deptno) t2
ON
    t1.deptno = t2.deptno;
 
+--------+-------+------------+--------+-----------+
| ename  | job   | dname      | deptno | deptcount |
+--------+-------+------------+--------+-----------+
| MILLER | CLERK | ACCOUNTING |     10 |         3 |
| SMITH  | CLERK | RESEARCH   |     20 |         5 |
| ADAMS  | CLERK | RESEARCH   |     20 |         5 |
| JAMES  | CLERK | SALES      |     30 |         6 |
+--------+-------+------------+--------+-----------+
```

#### 20、列出最低薪金大于 1500 的各种工作及从事此工作的全部雇员人数,按照工作岗位分组求最小值。

```sql
SELECT job,count(*) FROM emp GROUP BY job having min(sal) > 1500;

+-----------+----------+
| job       | count(*) |
+-----------+----------+
| ANALYST   |        2 |
| MANAGER   |        3 |
| PRESIDENT |        1 |
+-----------+----------+
```

#### 21、列出在部门"SALES"< 销售部> 工作的员工的姓名, 假定不知道销售部的部门编号

```sql
SELECT ename FROM emp WHERE deptno = (SELECT deptno FROM dept WHERE dname = 'SALES');
 
+--------+
| ename  |
+--------+
| ALLEN  |
| WARD   |
| MARTIN |
| BLAKE  |
| TURNER |
| JAMES  |
+--------+
```

#### 22、列出薪金高于公司平均薪金的所有员工, 所在部门, 上级领导, 雇员的工资等级.

```sql
SELECT 
    e.ename '员工',d.dname,l.ename '领导',s.grade
FROM
    emp e
join
    dept d
ON
    e.deptno = d.deptno
left join
    emp l
ON
    e.mgr = l.empno
join
    salgrade s
ON
    e.sal BETWEEN s.losal AND s.hisal
WHERE
    e.sal > (SELECT avg(sal) FROM emp);
 
 
+-------+------------+-------+-------+
| 员工     | dname      | 领导    | grade |
+-------+------------+-------+-------+
| JONES | RESEARCH   | KING  |     4 |
| BLAKE | SALES      | KING  |     4 |
| CLARK | ACCOUNTING | KING  |     4 |
| SCOTT | RESEARCH   | JONES |     4 |
| KING  | ACCOUNTING | NULL  |     5 |
| FORD  | RESEARCH   | JONES |     4 |
+-------+------------+-------+-------+
```

#### 23、 列出与"SCOTT" 从事相同工作的所有员工及部门名称

```sql
SELECT job FROM emp WHERE ename = 'SCOTT';
+---------+
| job     |
+---------+
| ANALYST |
+---------+
 
SELECT 
    e.ename,e.job,d.dname
FROM
    emp e
join
    dept d
ON
    e.deptno = d.deptno
WHERE
    e.job = (SELECT job FROM emp WHERE ename = 'SCOTT')
AND
    e.ename <> 'SCOTT';
 
+-------+---------+----------+
| ename | job     | dname    |
+-------+---------+----------+
| FORD  | ANALYST | RESEARCH |
+-------+---------+----------+
```

#### 24、列出薪金等于部门 30 中员工的薪金的其他员工的姓名和薪金

```sql
SELECT distinct sal FROM emp WHERE deptno = 30;
+---------+
| sal     |
+---------+
| 1600.00 |
| 1250.00 |
| 2850.00 |
| 1500.00 |
|  950.00 |
+---------+
 
SELECT 
    ename,sal 
FROM 
    emp 
WHERE 
    sal in(SELECT distinct sal FROM emp WHERE deptno = 30) 
AND 
    deptno <> 30;
 
Empty SET (0.00 sec)
```

#### 25、列出薪金高于在部门 30 工作的所有员工的薪金的员工姓名和薪金. 部门名称

```sql
SELECT MAX(sal) FROM emp WHERE deptno = 30;
+----------+
| MAX(sal) |
+----------+
|  2850.00 |
+----------+
 
SELECT
    e.ename,e.sal,d.dname
FROM
    emp e
join
    dept d
ON
    e.deptno = d.deptno
WHERE
    e.sal > (SELECT MAX(sal) FROM emp WHERE deptno = 30);
 
+-------+---------+------------+
| ename | sal     | dname      |
+-------+---------+------------+
| KING  | 5000.00 | ACCOUNTING |
| JONES | 2975.00 | RESEARCH   |
| SCOTT | 3000.00 | RESEARCH   |
| FORD  | 3000.00 | RESEARCH   |
+-------+---------+------------+
```

#### 26、列出在每个部门工作的员工数量, 平均工资和平均服务期限,没有员工的部门，部门人数是0

```sql
SELECT 
    d.deptno, count(e.ename) ecount,ifnull(avg(e.sal),0) AS avgsal, ifnull(avg(timestampdiff(YEAR, hiredate, now())), 0) AS avgservicetime
FROM
    emp e
right join
    dept d
ON
    e.deptno = d.deptno
GROUP BY
    d.deptno;
 
+--------+--------+-------------+----------------+
| deptno | ecount | avgsal      | avgservicetime |
+--------+--------+-------------+----------------+
|     10 |      3 | 2916.666667 |        38.0000 |
|     20 |      5 | 2175.000000 |        35.8000 |
|     30 |      6 | 1566.666667 |        38.3333 |
|     40 |      0 |    0.000000 |         0.0000 |
+--------+--------+-------------+----------------+
 
#在mysql当中怎么计算两个日期的“年差”，差了多少年？
#TimeStampDiff(间隔类型, 前一个日期, 后一个日期)
     
    timestampdiff(YEAR, hiredate, now())
 
    # 间隔类型:
    #    SECOND   秒，
    #    MINUTE   分钟，
    #    HOUR   小时，
    #    DAY   天，
    #    WEEK   星期
    #    MONTH   月，
    #    QUARTER   季度，
    #    YEAR   年
 
```

#### 27、 列出所有员工的姓名、部门名称和工资。

```sql
SELECT 
    e.ename,d.dname,e.sal
FROM
    emp e
join 
    dept d
ON
    e.deptno = d.deptno;
 
+--------+------------+---------+
| ename  | dname      | sal     |
+--------+------------+---------+
| CLARK  | ACCOUNTING | 2450.00 |
| KING   | ACCOUNTING | 5000.00 |
| MILLER | ACCOUNTING | 1300.00 |
| SMITH  | RESEARCH   |  800.00 |
| JONES  | RESEARCH   | 2975.00 |
| SCOTT  | RESEARCH   | 3000.00 |
| ADAMS  | RESEARCH   | 1100.00 |
| FORD   | RESEARCH   | 3000.00 |
| ALLEN  | SALES      | 1600.00 |
| WARD   | SALES      | 1250.00 |
| MARTIN | SALES      | 1250.00 |
| BLAKE  | SALES      | 2850.00 |
| TURNER | SALES      | 1500.00 |
| JAMES  | SALES      |  950.00 |
+--------+------------+---------+
```

#### 28、列出所有部门的详细信息和人数

```sql
SELECT 
    d.deptno,d.dname,d.loc,count(e.ename)
FROM
    emp e
right join
    dept d
ON
    e.deptno = d.deptno
GROUP BY
    d.deptno,d.dname,d.loc;
 
+--------+------------+----------+----------------+
| deptno | dname      | loc      | count(e.ename) |
+--------+------------+----------+----------------+
|     10 | ACCOUNTING | NEW YORK |              3 |
|     20 | RESEARCH   | DALLAS   |              5 |
|     30 | SALES      | CHICAGO  |              6 |
|     40 | OPERATIONS | BOSTON   |              0 |
+--------+------------+----------+----------------+
```

#### 29、列出各种工作的最低工资及从事此工作的雇员姓名

```sql
SELECT 
    job,min(sal) AS minsal
FROM
    emp
GROUP BY
    job;
 
+-----------+----------+
| job       | minsal        |
+-----------+----------+
| ANALYST   |  3000.00 |
| CLERK     |   800.00 |
| MANAGER   |  2450.00 |
| PRESIDENT |  5000.00 |
| SALESMAN  |  1250.00 |
+-----------+----------+
 
# emp e和以上t连接
 
SELECT 
    e.ename,t.*
FROM
    emp e
join
    (SELECT 
        job,min(sal) AS minsal
    FROM
        emp
    GROUP BY
        job) t
ON
    e.job = t.job AND e.sal = t.minsal;
 
+--------+-----------+---------+
| ename  | job       | minsal  |
+--------+-----------+---------+
| SMITH  | CLERK     |  800.00 |
| WARD   | SALESMAN  | 1250.00 |
| MARTIN | SALESMAN  | 1250.00 |
| CLARK  | MANAGER   | 2450.00 |
| SCOTT  | ANALYST   | 3000.00 |
| KING   | PRESIDENT | 5000.00 |
| FORD   | ANALYST   | 3000.00 |
+--------+-----------+---------+
```

#### 30、列出各个部门的 MANAGER( 领导) 的最低薪金

```sql
SELECT 
    deptno, min(sal)
FROM
    emp
WHERE
    job = 'MANAGER'
GROUP BY
    deptno;

+--------+----------+
| deptno | min(sal) |
+--------+----------+
|     10 |  2450.00 |
|     20 |  2975.00 |
|     30 |  2850.00 |
+--------+----------+
```

#### 31、列出所有员工的 年工资, 按 年薪从低到高排序

```sql
SELECT 
    ename,(sal + ifnull(comm,0)) * 12 AS yearsal
FROM
    emp
ORDER BY
    yearsal ASC;
 
+--------+----------+
| ename  | yearsal  |
+--------+----------+
| SMITH  |  9600.00 |
| JAMES  | 11400.00 |
| ADAMS  | 13200.00 |
| MILLER | 15600.00 |
| TURNER | 18000.00 |
| WARD   | 21000.00 |
| ALLEN  | 22800.00 |
| CLARK  | 29400.00 |
| MARTIN | 31800.00 |
| BLAKE  | 34200.00 |
| JONES  | 35700.00 |
| FORD   | 36000.00 |
| SCOTT  | 36000.00 |
| KING   | 60000.00 |
+--------+----------+
 
```

#### 32、求出员工领导的薪水超过3000的员工名称与领导

```sql
SELECT 
    a.ename '员工',b.ename '领导'
FROM
    emp a
join
    emp b
ON
    a.mgr = b.empno
WHERE
    b.sal > 3000;
 
+-------+------+
| 员工  | 领导   |
+-------+------+
| JONES | KING |
| BLAKE | KING |
| CLARK | KING |
+-------+------+
 
```

#### 33、求出部门名称中, 带'S'字符的部门员工的工资合计、部门人数

```sql
SELECT 
d.deptno,d.dname,d.loc,count(e.ename),ifnull(sum(e.sal),0) AS sumsal
FROM
    emp e
right join
    dept d
ON
    e.deptno = d.deptno
WHERE
    d.dname like '%S%'
GROUP BY
    d.deptno,d.dname,d.loc;
 
+--------+------------+---------+----------------+----------+
| deptno | dname      | loc     | count(e.ename) | sumsal   |
+--------+------------+---------+----------------+----------+
|     20 | RESEARCH   | DALLAS  |              5 | 10875.00 |
|     30 | SALES      | CHICAGO |              6 |  9400.00 |
|     40 | OPERATIONS | BOSTON  |              0 |     0.00 |
+--------+------------+---------+----------------+----------+
```

#### 34、给任职日期超过 30 年的员工加薪 10%.

```sql
update emp SET sal = sal * 1.1 WHERE timestampdiff(YEAR, hiredate, now()) > 30;
```

