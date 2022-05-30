# Golang操作常用中间件
## 操作redis
### 链接redis
1. 安装redis库
   go get github.com/garyburd/redigo/redis
2. 链接redis
   ```java
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
	if _, err = c.Do("AUTH", "121156");err!=nil{
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
```java

```