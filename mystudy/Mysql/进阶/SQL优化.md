# SQL优化最干货总结 - MySQL（2020最新版）

[SQL优化最干货总结 - MySQL（2020最新版） - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/265852739)



![img](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/v2-73ba0253f1b087d615b10a6372c43d4f_1440w.webp)



- 优化成本：硬件>系统配置>数据库表结构>SQL及索引。
- 优化效果：硬件<系统配置<数据库表结构<SQL及索引。

```java
String result = "嗯，不错，";
 
if ("SQL优化经验足") {
    if ("熟悉事务锁") {
        if ("并发场景处理666") {
            if ("会打王者荣耀") {
                result += "明天入职" 
            }
        }
    }
} else {
    result += "先回去等消息吧";
} 
 
Logger.info("面试官：" + result );
```

别看了，上面这是一道送命题。



![img](https://pic4.zhimg.com/80/v2-a00bb47c2947c9605095a8756928ec7b_1440w.webp)



好了我们言归正传，首先，对于MySQL层优化我一般遵从五个原则：

1. 减少数据访问： 设置合理的字段类型，启用压缩，通过索引访问等减少磁盘IO
2. 返回更少的数据： 只返回需要的字段和数据分页处理 减少磁盘io及网络io
3. 减少交互次数： 批量DML操作，函数存储等减少数据连接次数
4. 减少服务器CPU开销： 尽量减少数据库排序操作以及全表查询，减少cpu 内存占用
5. 利用更多资源： 使用表分区，可以增加并行操作，更大限度利用cpu资源

总结到SQL优化中，就三点:

- 最大化利用索引；
- 尽可能避免全表扫描；
- 减少无效数据的查询；

理解SQL优化原理 ，首先要搞清楚SQL执行顺序：

## SELECT语句 - 语法顺序：

```java
1. SELECT 
2. DISTINCT <select_list>
3. FROM <left_table>
4. <join_type> JOIN <right_table>
5. ON <join_condition>
6. WHERE <where_condition>
7. GROUP BY <group_by_list>
8. HAVING <having_condition>
9. ORDER BY <order_by_condition>
10.LIMIT <limit_number>
```

## SELECT语句 - 执行顺序：

> **FROM**
> <表名> # 选取表，将多个表数据通过笛卡尔积变成一个表。
> **ON**
> <筛选条件> # 对笛卡尔积的虚表进行筛选
> **JOIN**<join, left join, right join...>
> <join表> # 指定join，用于添加数据到on之后的虚表中，例如left join会将左表的剩余数据添加到虚表中
> **WHERE**
> <where条件> # 对上述虚表进行筛选
> **GROUP BY**
> <分组条件> # 分组
> <SUM()等聚合函数> # 用于having子句进行判断，在书写上这类聚合函数是写在having判断里面的
> **HAVING**
> <分组筛选> # 对分组后的结果进行聚合筛选
> **SELECT**
> <返回数据列表> # 返回的单列必须在group by子句中，聚合函数除外
> **DISTINCT**
> \# 数据除重
> **ORDER BY**
> <排序条件> # 排序
> **LIMIT**
> <行数限制>



## SQL优化策略

> 声明：以下SQL优化策略适用于数据量较大的场景下，如果数据量较小，没必要以此为准，以免画蛇添足。

## 一、避免不走索引的场景

**1. 尽量避免在字段开头模糊查询，会导致数据库引擎放弃索引进行全表扫描。**如下：

**2. 尽量避免使用in 和not in，会导致引擎走全表扫描。**如下：

**3. 尽量避免使用 or，会导致数据库引擎放弃索引进行全表扫描。**如下：

**4. 尽量避免进行null值的判断，会导致数据库引擎放弃索引进行全表扫描。**如下：

**5.尽量避免在where条件中等号的左侧进行表达式、函数操作，会导致数据库引擎放弃索引进行全表扫描。**

**6. 当数据量大时，避免使用where 1=1的条件。通常为了方便拼装查询条件，我们会默认使用该条件，数据库引擎会放弃索引进行全表扫描。**如下：
**7. 查询条件不能用 <> 或者 !=**

**8. where条件仅包含复合索引非前置列**
**9. 隐式类型转换造成不使用索引**


**10. order by 条件要与where中条件一致，否则order by不会利用索引进行排序**

```java
-- 不走age索引
SELECT * FROM t order by age;

-- 走age索引
SELECT * FROM t where age > 0 order by age;
```



对于上面的语句，数据库的处理顺序是：

- 第一步：根据where条件和统计信息生成执行计划，得到数据。
- 第二步：将得到的数据排序。当执行处理数据（order by）时，数据库会先查看第一步的执行计划，看order by 的字段是否在执行计划中利用了索引。如果是，则可以利用索引顺序而直接取得已经排好序的数据。如果不是，则重新进行排序操作。
- 第三步：返回排序后的数据。

当order by 中的字段出现在where条件中时，才会利用索引而不再二次排序，更准确的说，order by 中的字段在执行计划中利用了索引时，不用排序操作。

这个结论不仅对order by有效，对其他需要排序的操作也有效。比如group by 、union 、distinct等。

**11. 正确使用hint优化语句**

MySQL中可以使用hint指定优化器在执行时选择或忽略特定的索引。一般而言，处于版本变更带来的表结构索引变化，更建议避免使用hint，而是通过Analyze table多收集统计信息。但在特定场合下，指定hint可以排除其他索引干扰而指定更优的执行计划。

1. USE INDEX 在你查询语句中表名的后面，添加 USE INDEX 来提供希望 MySQL 去参考的索引列表，就可以让 MySQL 不再考虑其他可用的索引。例子: SELECT col1 FROM table USE INDEX (mod_time, name)...
2. IGNORE INDEX 如果只是单纯的想让 MySQL 忽略一个或者多个索引，可以使用 IGNORE INDEX 作为 Hint。例子: SELECT col1 FROM table IGNORE INDEX (priority) ...
3. FORCE INDEX 为强制 MySQL 使用一个特定的索引，可在查询中使用FORCE INDEX 作为Hint。例子: SELECT col1 FROM table FORCE INDEX (mod_time) ...

在查询的时候，数据库系统会自动分析查询语句，并选择一个最合适的索引。但是很多时候，数据库系统的查询优化器并不一定总是能使用最优索引。如果我们知道如何选择索引，可以使用FORCE INDEX强制查询使用指定的索引。

例如：

```java
SELECT * FROM students FORCE INDEX (idx_class_id) WHERE class_id = 1 ORDER BY id DESC;
```

## 二、SELECT语句其他优化

### **1. 避免出现select \***


### **2. 避免出现不确定结果的函数**


### **3.多表关联查询时，小表在前，大表在后。**

### **4. 使用表的别名**


### **5. 用where字句替换HAVING字句**

### **6.调整Where字句中的连接顺序**

##  三、增删改 DML 语句优化

### **1. 大批量插入数据**

### **2. 适当使用commit**

### **3. 避免重复查询更新的数据**
### **4.查询优先还是更新（insert、update、delete）优先**

##  四、查询条件优化

### **1. 对于复杂的查询，可以使用中间临时表 暂存数据；**

### **2. 优化group by语句**

默认情况下，MySQL 会对GROUP BY分组的所有值进行排序，如 “GROUP BY col1，col2，....;” 查询的方法如同在查询中指定 “ORDER BY col1，col2，...;” 如果显式包括一个包含相同的列的 ORDER BY子句，MySQL 可以毫不减速地对它进行优化，尽管仍然进行排序。

因此，如果查询包括 GROUP BY 但你并不想对分组的值进行排序，你可以指定 ORDER BY NULL禁止排序。例如：

```text
SELECT col1, col2, COUNT(*) FROM table GROUP BY col1, col2 ORDER BY NULL ;
```

### **3. 优化join语句**

MySQL中可以通过子查询来使用 SELECT 语句来创建一个单列的查询结果，然后把这个结果作为过滤条件用在另一个查询中。使用子查询可以一次性的完成很多逻辑上需要多个步骤才能完成的 SQL 操作，同时也可以避免事务或者表锁死，并且写起来也很容易。但是，有些情况下，子查询可以被更有效率的连接(JOIN)..替代。


例子：假设要将所有没有订单记录的用户取出来，可以用下面这个查询完成：

```java
SELECT col1 FROM customerinfo WHERE CustomerID NOT in (SELECT CustomerID FROM salesinfo )
```


如果使用连接(JOIN).. 来完成这个查询工作，速度将会有所提升。尤其是当 salesinfo表中对 CustomerID 建有索引的话，性能将会更好，查询如下：

```java
SELECT col1 FROM customerinfo 
LEFT JOIN salesinfoON customerinfo.CustomerID=salesinfo.CustomerID 
WHERE salesinfo.CustomerID IS NULL 
```



连接(JOIN).. 之所以更有效率一些，是因为 MySQL 不需要在内存中创建临时表来完成这个逻辑上的需要两个步骤的查询工作。

### **4. 优化union查询**

MySQL通过创建并填充临时表的方式来执行union查询。除非确实要消除重复的行，否则建议使用union all。原因在于如果没有all这个关键词，MySQL会给临时表加上distinct选项，这会导致对整个临时表的数据做唯一性校验，这样做的消耗相当高。

高效：

```java
SELECT COL1, COL2, COL3 FROM TABLE WHERE COL1 = 10 
UNION ALL 
SELECT COL1, COL2, COL3 FROM TABLE WHERE COL3= 'TEST'; 
```



低效：

```text
SELECT COL1, COL2, COL3 FROM TABLE WHERE COL1 = 10 
UNION 
SELECT COL1, COL2, COL3 FROM TABLE WHERE COL3= 'TEST';
```

### **5.拆分复杂SQL为多个小SQL，避免大事务**

- 简单的SQL容易使用到MySQL的QUERY CACHE；
- 减少锁表时间特别是使用MyISAM存储引擎的表；
- 可以使用多核CPU。

### **6. 使用truncate代替delete**

当删除全表中记录时，使用delete语句的操作会被记录到undo块中，删除记录也记录binlog，当确认需要删除全表时，会产生很大量的binlog并占用大量的undo数据块，此时既没有很好的效率也占用了大量的资源。

使用truncate替代，不会记录可恢复的信息，数据不能被恢复。也因此使用truncate操作有其极少的资源占用与极快的时间。另外，使用truncate可以回收表的水位，使自增字段值归零。

### **7. 使用合理的分页方式以提高分页效率**

使用合理的分页方式以提高分页效率 针对展现等分页需求，合适的分页方式能够提高分页的效率。

案例1：

```java
select * from t where thread_id = 10000 and deleted = 0 
order by gmt_create asc limit 0, 15;
```


上述例子通过一次性根据过滤条件取出所有字段进行排序返回。数据访问开销=索引IO+索引全部记录结果对应的表数据IO。因此，该种写法越翻到后面执行效率越差，时间越长，尤其表数据量很大的时候。

适用场景：当中间结果集很小（10000行以下）或者查询条件复杂（指涉及多个不同查询字段或者多表连接）时适用。


案例2：

```java
select t.* from (select id from t where thread_id = 10000 and deleted = 0
order by gmt_create asc limit 0, 15) a, t 
where a.id = t.id;
```



上述例子必须满足t表主键是id列，且有覆盖索引secondary key:(thread_id, deleted, gmt_create)。通过先根据过滤条件利用覆盖索引取出主键id进行排序，再进行join操作取出其他字段。数据访问开销=索引IO+索引分页后结果（例子中是15行）对应的表数据IO。因此，该写法每次翻页消耗的资源和时间都基本相同，就像翻第一页一样。

适用场景：当查询和排序字段（即where子句和order by子句涉及的字段）有对应覆盖索引时，且中间结果集很大的情况时适用。

## 五、建表优化

### 1. 在表中建立索引，优先考虑where、order by使用到的字段。

### 2. 尽量使用数字型字段（如性别，男：1 女：2），若只含数值信息的字段尽量不要设计为字符型，这会降低查询和连接的性能，并会增加存储开销。
这是因为引擎在处理查询和连接时会 逐个比较字符串中每一个字符，而对于数字型而言只需要比较一次就够了。

3. 查询数据量大的表 会造成查询缓慢。主要的原因是扫描行数过多。这个时候可以通过程序，分段分页进行查询，循环遍历，将结果合并处理进行展示。要查询100000到100050的数据，如下：

```java
SELECT * FROM (SELECT ROW_NUMBER() OVER(ORDER BY ID ASC) AS rowid,* 
FROM infoTab)t WHERE t.rowid > 100000 AND t.rowid <= 100050
```



4. 用varchar/nvarchar 代替 char/nchar

尽可能的使用 varchar/nvarchar 代替 char/nchar ，因为首先变长字段存储空间小，可以节省存储空间，其次对于查询来说，在一个相对较小的字段内搜索效率显然要高些。
不要以为 NULL 不需要空间，比如：char(100) 型，在字段建立时，空间就固定了， 不管是否插入值（NULL也包含在内），都是占用 100个字符的空间的，如果是varchar这样的变长字段， null 不占用空间。