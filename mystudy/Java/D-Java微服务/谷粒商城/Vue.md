# 基础

## 安装Vue

方式一：通过cdn引入

方式二:  通过npm   ：  npm isntall vue 即可

1. 创建空文件夹

2. 初始化vue项目：npm init -y

   1. 表示使用的是npm管理，此时会出现package.json文件

3. 安装vue依赖：npm install vue

4. 创建测试文件：index.html

5. 使用vue，需要引入，从nodemodules里面引入

6. ```java
       <script src="./node_modules/vue/dist/vue.js"></script>
   ```

7. 入门案例

```vue
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
</head>

<body>

    <div id="app">
        <input type="text" v-model="num">
        <button v-on:click="num++">点赞</button>
        <button v-on:click="cancle">取消</button>
        <h1> {{name}} ,非常帅，有{{num}}个人为他点赞{{hello()}}</h1>
    </div>

    <script src="./node_modules/vue/dist/vue.js"></script>

    <script>
        //1、vue声明式渲染  使用vue对象管控div app
        let vm = new Vue({
            el: "#app",//绑定元素
            data: {  //封装数据
                name: "张三",
                num: 1
            },
            methods:{  //封装方法
                cancle(){
                    this.num -- ;
                },
                hello(){
                    return "1"
                }
            }
        });

        //2、双向绑定,模型变化，视图变化。反之亦然。

        //3、事件处理

        //v-xx：指令

        //1、创建vue实例，关联页面的模板，将自己的数据（data）渲染到关联的模板，响应式的
        //2、指令来简化对dom的一些操作。
        //3、声明方法来做更复杂的操作。methods里面可以封装方法。

    </script>
</body>

</html>
```

## 指令

### 插值表达式{{}}

插值表达式为标签体赋值。

```vue
<body>
   
    <div id="app">
        {{msg}}  {{1+1}}  {{hello()}}<br/>
        <span v-html="msg"></span>
        <br/>
        <span v-text="msg"></span>

        
    </div>
   
    <script src="../node_modules/vue/dist/vue.js"></script>

    <script>
        new Vue({
            el:"#app",
            data:{
                msg:"<h1>Hello</h1>",
                link:"http://www.baidu.com"
            },
            methods:{
                hello(){
                    return "World"
                }
            }
        })
    </script>
    
</body>
```

### 属性赋值v-bind

给标签属性与vue实例绑定

```vue
<body>

    <!-- 给html标签的属性绑定 -->
    <div id="app"> 

        <a v-bind:href="link">gogogo</a>

        <!-- class,style  {class名：加上？}-->
       <!--   一个class属性对应多个值，active,text-danger。
        创建对象{}让active和text-danger分别与isActive和hasError绑定，为true时对应的属性值才生效。-->
        <span v-bind:class="{active:isActive,'text-danger':hasError}"
              <!--  font-size可以用驼峰命名-->
          :style="{color: color1,fontSize: size}">你好</span>


    </div>

    <script src="../node_modules/vue/dist/vue.js"></script>

    <script>
        let vm = new Vue({
            el:"#app",
            data:{
                link: "http://www.baidu.com",
                isActive:true,
                hasError:true,
                color1:'red',
                size:'36px'
            }
        })
    </script>

</body>
```

v-bind是单向绑定，data中的属性值改变，标签里面的属性值也随着改变。但是标签里面的属性值发生改变，data中的属性并不会发生变化。

### 双向绑定v-model

常用于表单项和自定义组件。

```vue
<body>


    <!-- 表单项，自定义组件 -->
    <div id="app">

        精通的语言：
            <input type="checkbox" v-model="language" value="Java"> java<br/>
            <input type="checkbox" v-model="language" value="PHP"> PHP<br/>
            <input type="checkbox" v-model="language" value="Python"> Python<br/>
        选中了 {{language.join(",")}}
    </div>
    
    <script src="../node_modules/vue/dist/vue.js"></script>

    <script>
        let vm = new Vue({
            el:"#app",
            data:{
                language: []
            }
        })
    </script>

</body>
```

### 事件v-on

![image-20220308153408646](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220308153408646.png)

```java
<body>
    <div id="app">
                
        <!--事件中直接写js片段-->
        <button v-on:click="num++">点赞</button>
        <!--事件指定一个回调函数，必须是Vue实例中定义的函数-->
        <button @click="cancle">取消</button>
        <!--  -->
        <h1>有{{num}}个赞</h1>


        <!-- 事件修饰符 -->
        <div style="border: 1px solid red;padding: 20px;" v-on:click.once="hello">
            大div
            <div style="border: 1px solid blue;padding: 20px;" @click.stop="hello">
                小div <br />
                <a href="http://www.baidu.com" @click.prevent.stop="hello">去百度</a>
            </div>
        </div>



        <!-- 按键修饰符： -->
        <input type="text" v-model="num" v-on:keyup.up="num+=2" @keyup.down="num-=2" @click.ctrl="num=10"><br />

        提示：

    </div>
    <script src="../node_modules/vue/dist/vue.js"></script>

    <script>
        new Vue({
            el:"#app",
            data:{
                num: 1
            },
            methods:{
                cancle(){
                    this.num--;
                },
                hello(){
                    alert("点击了")
                }
            }
        })
    </script>
</body>
```

### 循环v-for

```vue
<body>

    <div id="app">
        <ul>
            <li v-for="(user,index) in users" :key="user.name" v-if="user.gender == '女'">
                <!-- 1、显示user信息：v-for="item in items" -->
               当前索引：{{index}} ==> {{user.name}}  ==>   {{user.gender}} ==>{{user.age}} <br>
                <!-- 2、获取数组下标：v-for="(item,index) in items" -->
                <!-- 3、遍历对象：
                        v-for="value in object"
                        v-for="(value,key) in object"
                        v-for="(value,key,index) in object" 
                -->
                对象信息：
                <span v-for="(v,k,i) in user">{{k}}=={{v}}=={{i}}；</span>
                <!-- 4、遍历的时候都加上:key来区分不同数据，提高vue渲染效率 -->
            </li>

            
        </ul>

        <ul>
            <li v-for="(num,index) in nums" :key="index"></li>
        </ul>
    </div>
    <script src="../node_modules/vue/dist/vue.js"></script>
    <script>         
        let app = new Vue({
            el: "#app",
            data: {
                users: [{ name: '柳岩', gender: '女', age: 21 },
                { name: '张三', gender: '男', age: 18 },
                { name: '范冰冰', gender: '女', age: 24 },
                { name: '刘亦菲', gender: '女', age: 18 },
                { name: '古力娜扎', gender: '女', age: 25 }],
                nums: [1,2,3,4,4]
            },
        })
    </script>
</body>
```

### 判断v-if

```java
<body>
    <!-- 
        v-if，顾名思义，条件判断。当得到结果为true时，所在的元素才会被渲染。
        v-show，当得到结果为true时，所在的元素才会被显示。 
    -->
    <div id="app">
        <button v-on:click="show = !show">点我呀</button>
        <!-- 1、使用v-if显示 -->
        <h1 v-if="show">if=看到我....</h1>
        <!-- 2、使用v-show显示 -->
        <h1 v-show="show">show=看到我</h1>
    </div>

    <script src="../node_modules/vue/dist/vue.js"></script>
        
    <script>
        let app = new Vue({
            el: "#app",
            data: {
                show: true
            }
        })
    </script>

</body>
```

### 判断v-else

```java
<body>
    <div id="app">
        <button v-on:click="random=Math.random()">点我呀</button>
        <span>{{random}}</span>

        <h1 v-if="random>=0.75">
            看到我啦？！ &gt;= 0.75
        </h1>

        <h1 v-else-if="random>=0.5">
            看到我啦？！ &gt;= 0.5
        </h1>

        <h1 v-else-if="random>=0.2">
            看到我啦？！ &gt;= 0.2
        </h1>

        <h1 v-else>
            看到我啦？！ &lt; 0.2
        </h1>

    </div>


    <script src="../node_modules/vue/dist/vue.js"></script>
        
    <script>         
        let app = new Vue({
            el: "#app",
            data: { random: 1 }
        })     
    </script>
</body>
```

## 计算属性和侦听器

计算属性

需求：计算总价。

西游记和水浒传的数量是会时刻变化的。此时要想实时计算总价，可以使用计算属性。

侦听器

需求：当需求量超过库存时会进行提示库存超出限制。

```vue
<body>
    <div id="app">
        <!-- 某些结果是基于之前数据实时计算出来的，我们可以利用计算属性。来完成 -->
        <ul>
            <li>西游记； 价格：{{xyjPrice}}，数量：<input type="number" v-model="xyjNum"> </li>
            <li>水浒传； 价格：{{shzPrice}}，数量：<input type="number" v-model="shzNum"> </li>
            <li>总价：{{totalPrice}}</li>
            {{msg}}
        </ul>
    </div>
    <script src="../node_modules/vue/dist/vue.js"></script>

    <script>
        //watch可以让我们监控一个值的变化。从而做出相应的反应。
        new Vue({
            el: "#app",
            data: {
                xyjPrice: 99.98,
                shzPrice: 98.00,
                xyjNum: 1,
                shzNum: 1,
                msg: ""
            },
            computed: {
                totalPrice(){
                    return this.xyjPrice*this.xyjNum + this.shzPrice*this.shzNum
                }
            },
            watch: {
                //旧值和新值
                xyjNum(newVal,oldVal){
                    if(newVal>=3){
                        this.msg = "库存超出限制";
                        this.xyjNum = 3
                    }else{
                        this.msg = "";
                    }
                }
            },
        })
    </script>

</body>
```

效果：随着数量的变化，水浒传的总价也会发生变化。

![image-20220308161921709](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220308161921709.png)



### 过滤器Filter

```java
<body>
    <!-- 过滤器常用来处理文本格式化的操作。过滤器可以用在两个地方：双花括号插值和 v-bind 表达式 -->
    <div id="app">
        <ul>
            <li v-for="user in userList">
                {{user.id}} ==> {{user.name}} ==> {{user.gender == 1?"男":"女"}} ==>
                {{user.gender | genderFilter}} ==> {{user.gender | gFilter}}
            </li>
        </ul>
    </div>
    <script src="../node_modules/vue/dist/vue.js"></script>

    <script>
        //全局过滤器，可以在任何实例里面使用
        Vue.filter("gFilter", function (val) {
            if (val == 1) {
                return "男~~~";
            } else {
                return "女~~~";
            }
        })

        let vm = new Vue({
            el: "#app",
            data: {
                userList: [
                    { id: 1, name: 'jacky', gender: 1 },
                    { id: 2, name: 'peter', gender: 0 }
                ]
            },
            filters: {
                //// filters 定义局部过滤器，只可以在当前vue实例中使用
                genderFilter(val) {
                    if (val == 1) {
                        return "男";
                    } else {
                        return "女";
                    }
                }
            }
        })
    </script>
</body>
```

# 组件化

![image-20220308162920173](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220308162920173.png)

### 全局组件

```java
<body>

    <div id="app">
        <button v-on:click="count++">我被点击了 {{count}} 次</button>
         //2. 使用组件
        <counter></counter>
        <counter></counter>
        <counter></counter>
        <counter></counter>
        <counter></counter>
      
    </div>
    <script src="../node_modules/vue/dist/vue.js"></script>


    <script>
        //1、全局声明注册一个组件
        Vue.component("counter", {
            //template中填写代码模板
            template: `<button v-on:click="count++">我被点击了 {{count}} 次</button>`,
            data() {
                return {
                    //count里面为对应的属性值
                    count: 1
                }
            }
        });


        new Vue({
            el: "#app",
            data: {
                count: 1
            }
        })
    </script>
</body>
```

![image-20220308163647051](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220308163647051.png)

### 局部组件

```java
<body>

    <div id="app">
        <button v-on:click="count++">我被点击了 {{count}} 次</button>
		//3、使用组件
        <button-counter></button-counter>
    </div>
    <script src="../node_modules/vue/dist/vue.js"></script>


    <script>

        //1、局部声明一个组件
        const buttonCounter = {
            template: `<button v-on:click="count++">我被点击了 {{count}} 次~~~</button>`,
            data() {
                return {
                    count: 1
                }
            }
        };

        new Vue({
            el: "#app",
            data: {
                count: 1
            },
            //2.在当前vue实例中引入组件
            components: {
                'button-counter': buttonCounter
            }
        })
    </script>
</body>
```

### 声明周期

对象从创建到消亡的过程。

![image-20220308163709994](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220308163709994.png)

​                 ![img](https://docimg1.docs.qq.com/image/H5tTYK3Ixu7P-EiWLZrVxQ?w=2498&h=2798)        

```java
<body>
    <div id="app">
        <span id="num">{{num}}</span>
        <button @click="num++">赞！</button>
        <h2>{{name}}，有{{num}}个人点赞</h2>
    </div>

    <script src="../node_modules/vue/dist/vue.js"></script>
    
    <script>
        let app = new Vue({
            el: "#app",
            data: {
                name: "张三",
                num: 100
            },
            methods: {
                show() {
                    return this.name;
                },
                add() {
                    this.num++;
                }
            },
            beforeCreate() {
                console.log("=========beforeCreate=============");
                console.log("数据模型未加载：" + this.name, this.num);
                console.log("方法未加载：" + this.show());
                console.log("html模板未加载：" + document.getElementById("num"));
            },
            created: function () {
                console.log("=========created=============");
                console.log("数据模型已加载：" + this.name, this.num);
                console.log("方法已加载：" + this.show());
                console.log("html模板已加载：" + document.getElementById("num"));
                console.log("html模板未渲染：" + document.getElementById("num").innerText);
            },
            beforeMount() {
                console.log("=========beforeMount=============");
                console.log("html模板未渲染：" + document.getElementById("num").innerText);
            },
            mounted() {
                console.log("=========mounted=============");
                console.log("html模板已渲染：" + document.getElementById("num").innerText);
            },
            beforeUpdate() {
                console.log("=========beforeUpdate=============");
                console.log("数据模型已更新：" + this.num);
                console.log("html模板未更新：" + document.getElementById("num").innerText);
            },
            updated() {
                console.log("=========updated=============");
                console.log("数据模型已更新：" + this.num);
                console.log("html模板已更新：" + document.getElementById("num").innerText);
            }
        });
    </script>
</body>

```

# Vue模块化开发

![image-20220308164620422](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220308164620422.png)

![image-20220308170025590](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220308170025590.png)

![image-20220308170112834](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220308170112834.png)

## 使用EleUI

1. 安装Eleui
   1. npm i element-ui
   2. main.js导入
   3. vue使用

![image-20220308170232947](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220308170232947.png)