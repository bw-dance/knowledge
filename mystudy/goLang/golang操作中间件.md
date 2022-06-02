# Golang操作常用中间件
## 操作redis

[Go操作Redis实战 - itbsl - 博客园 (cnblogs.com)](https://www.cnblogs.com/itbsl/p/14198111.html)

### 链接redis
1. 安装redis库
   go get github.com/garyburd/redigo/redis
2. 链接redis
   ```go
    package main
   
    import (
        "fmt"
        "github.com/garyburd/redigo/redis"
    )
   
    func main() {
        func main() {
	//1.链接redis
	c, err := redis.Dial("tcp", "39.96.41.35:6379")
	if err != nil {
		fmt.Println("conn redis failed,", err)
		return
	}
	defer c.Close()
	//2.验证密码
	if _, err = c.Do("AUTH", "123456");err!=nil{
		c.Close()
	}
	//3.存入数据
	fmt.Println("redis conn success")
	_, err = c.Do("Set", "abc", 100)
	if err != nil {
		fmt.Println(err)
		return
	}
	//4.获取数据
	r, err := redis.Int(c.Do("Get", "abc"))
	if err != nil {
		fmt.Println("get abc failed,", err)
		return
	}
   
	fmt.Println(r)
    }
   ```
### String操作
## 操作数据库

### 链接mysql

1. 安装mysql驱动

   ```java
       go get github.com/go-sql-driver/mysql 
       go get github.com/jmoiron/sqlx
   ```

2. 引入mysql和sql包

   ```sql
   	"database/sql"
   	_ "github.com/go-sql-driver/mysql"
   ```

   3. [(132条消息) Go 的database/sql 和 sqlx_YSL_ALI的博客-CSDN博客](https://blog.csdn.net/YSL_ALI/article/details/105748300#:~:text=关于 Go 的标准库 database%2Fsql 和,sqlx database%2Fsql 是 Go 操作数据库的标准库之一，它提供了一系列接口方法，用于访问数据库（mysql，sqllite，oralce，postgresql），它并不会提供数据库特有的方法，那些特有的方法交给数据库驱动去实现)

3. 进行数据库连接

   ```java
   import (
   	"fmt"
   	_ "github.com/go-sql-driver/mysql"
   	"github.com/jmoiron/sqlx"
   )
   
   func main() {
   	database, err := sqlx.Open("mysql", "root:123456@tcp(39.96.41.35:3306)/binlog_test")
   	if err!=nil {
   		fmt.Println(err)
   	}
   	fmt.Println(database)
   }
   
   ```

### 添加数据

```go
import (
	"fmt"
	_ "github.com/go-sql-driver/mysql"
	"github.com/jmoiron/sqlx"
)

type Person struct {
	UserId   int    `db:"user_id"`
	Username string `db:"username"`
	Sex      string `db:"sex"`
	Email    string `db:"email"`
}


var Db *sqlx.DB

func init() {
	database, err := sqlx.Open("mysql", "root:123456@tcp(39.96.41.35:3306)/binlog_test")
	if err!=nil {
		fmt.Println(err)
	}
	Db = database
	//defer Db.Close()  // 注意这行代码要写在上面err判断的下面
}

func main() {
	r, err := Db.Exec("insert into student(id,name,age,gender)values(?, ?, ?,?)", nil, "张三炮", 18,1)
	if err != nil {
		fmt.Println("exec failed, ", err)
		return
	}
	id, err := r.LastInsertId()
	if err != nil {
		fmt.Println("exec failed, ", err)
		return
	}
	fmt.Println("insert succ:", id)
	defer Db.Close()
}
```

### 删除数据

```go
package main

import (
	"fmt"

	_ "github.com/go-sql-driver/mysql"
	"github.com/jmoiron/sqlx"
)




var DD *sqlx.DB

func init() {

	database, err := sqlx.Open("mysql", "root:121156@tcp(39.96.41.35:3306)/binlog_test")
	if err!=nil {
		fmt.Println(err)
	}

	DD = database

}

func main() {

	/*
	   _, err := Db.Exec("delete from person where user_id=?", 1)
	   if err != nil {
	       fmt.Println("exec failed, ", err)
	       return
	   }
	*/
	defer DD.Close()  // 注意这行代码要写在上面err判断的下面
	res, err := DD.Exec("delete from student where id=?", 1)
	if err != nil {
		fmt.Println("exec failed, ", err)
		return
	}

	row,err := res.RowsAffected()
	if err != nil {
		fmt.Println("rows failed, ",err)
	}

	fmt.Println("delete succ: ",row)
}
```

### 更新数据

```go
import (
	"fmt"

	_ "github.com/go-sql-driver/mysql"
	"github.com/jmoiron/sqlx"
)

type Student struct {
	Id   int    `db:"id"`
	Name string `db:"name"`
	Age      string `db:"age"`
	Gender    []uint8 `db:"gender"`
}

var RB *sqlx.DB

func init() {

	database, err := sqlx.Open("mysql", "root:123456@tcp(39.96.41.35:3306)/binlog_test")
	if err != nil {
		fmt.Println("open mysql failed,", err)
		return
	}


	RB = database

}

func main() {
	res, err := RB.Exec("update student set name=? where id=?", "stu0001", 1)
	if err != nil {
		fmt.Println("exec failed, ", err)
		return
	}
	row, err := res.RowsAffected()
	if err != nil {
		fmt.Println("rows failed, ",err)
	}
	fmt.Println("update succ:",row)
	defer RB.Close()  // 注意这行代码要写在上面err判断的下面

}
```



### 查询数据

```go

package main

import (
	"fmt"

	_ "github.com/go-sql-driver/mysql"
	"github.com/jmoiron/sqlx"
)

type Student struct {
	Id   int    `db:"id"`
	Name string `db:"name"`
	Age      string `db:"age"`
	Gender    []uint8 `db:"gender"`
}

type Place struct {
	Country string `db:"country"`
	City    string `db:"city"`
	TelCode int    `db:"telcode"`
}

var Eb *sqlx.DB

func init() {

	database, err := sqlx.Open("mysql", "root:123456@tcp(39.96.41.35:3306)/binlog_test")
	if err != nil {
		fmt.Println("open mysql failed,", err)
		return
	}

	Eb = database
	//defer Eb.Close()  // 注意这行代码要写在上面err判断的下面
}

func main() {

	var student []Student
	err := Eb.Select(&student, "select id, name, age, gender from student where id=?", 2)
	if err != nil {
		fmt.Println("exec failed, ", err)
		return
	}

	fmt.Println("select succ:", student)
	defer Eb.Close()
}

```

### 事务操作

```go
package main

import (
	"fmt"

	_ "github.com/go-sql-driver/mysql"
	"github.com/jmoiron/sqlx"
)


var Tb *sqlx.DB

func init() {
	database, err := sqlx.Open("mysql", "root:121156@tcp(39.96.41.35:3306)/binlog_test")
	if err != nil {
		fmt.Println("open mysql failed,", err)
		return
	}

	Tb = database
	//defer Eb.Close()  // 注意这行代码要写在上面err判断的下面
}

func main() {
	conn, err := Tb.Begin()
	if err != nil {
		fmt.Println("begin failed :", err)
		return
	}

	r, err := Tb.Exec("insert into student(id,name,age,gender)values(?, ?, ?,?)", nil, "张三炮44444", 18,0)
	if err != nil {
		fmt.Println("exec failed, ", err)
		conn.Rollback()
		return
	}
	id, err := r.LastInsertId()
	if err != nil {
		fmt.Println("exec failed, ", err)
		conn.Rollback()
		return
	}
	fmt.Println("insert succ:", id)

	r, err = conn.Exec("insert into student(id,name,age,gender)values(?, ?, ?,?)", nil, "张三炮55555", 18,1)
	if err != nil {
		fmt.Println("exec failed, ", err)
		conn.Rollback()
		return
	}
	id, err = r.LastInsertId()
	if err != nil {
		fmt.Println("exec failed, ", err)
		conn.Rollback()
		return
	}
	fmt.Println("insert succ:", id)

	conn.Commit()
}
```



### 数据类型对照

![image-20220531152124966](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220531152124966.png)

1. 接收mysql的bit类型，使用[]uint8

   1. ```go
      
      type Student struct {
      	Id   int    `db:"id"`
      	Name string `db:"name"`
      	Age      string `db:"age"`
      	Gender    []uint8 `db:"gender"`
      }
      
      ```

   2. ![image-20220531152739640](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220531152739640.png)