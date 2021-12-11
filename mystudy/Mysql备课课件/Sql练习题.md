# 不要用战术上的勤劳来掩盖战略上的懒惰

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

#### 2、哪些人的薪水在部门的平均薪水之上

#### 3、取得部门中（所有人的）平均的薪水等级

平均的薪水等级:先计算每一个人薪水的等级，然后找出薪水等级的平均值。

平均薪水的等级:先计算平均薪水，然后找出每个平均薪水的等级值。

#### 4、不准用函数（MAX ），取得最高薪水

####  5、取得平均薪水最高的部门的部门编号

第一种方案:降序取第一个

第二种方案:MAX

#### 6、取得平均薪水最高的部门的部门名称

1. 先获取所有部门的所有薪水
2. 对这些数据进行分组

#### 7、求平均薪水的等级最低的部门的部门名称

1. 获取每个部门的平均薪水
2. 获取各个部门的平均薪水等级
3. 排序

#### 8、取得比普通员工(员工代码没有在 mgr 字段上出现的) 的最高薪水还要高的领导人姓名

1. 求出普通员工的最高工资
2. 求比普通员工的最高工资还多的人。

#### 9、取得薪水最高的前五名员工

#### 10、取得薪水最高的第六到第十名员工

#### 11、取得最后入职的 5 名员工，日期也可以降序，升序。

#### 12、取得每个薪水等级有多少员工。分组count。

#### 13、列出所有员工及领导的姓名

#### 15、列出受雇日期早于其直接上级的所有员工的编号,姓名,部门名称

#### 16、列出部门名称和这些部门的员工信息, 同时列出那些没有员工的部门

#### 17、 列出至少有 5 个员工的所有部门

#### 18、列出薪金比"SMITH" 多的所有员工信息

#### 19、列出所有"CLERK"( 办事员) 的姓名及其部门名称, 部门的人数
