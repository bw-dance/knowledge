# 1. JavaScript

## 1. 介绍

### 介绍

1. JavaScript 是世界上最流行的语言之一，是一种运行在客户端的脚本语言 （Script 是脚本的意思）

2. 脚本语言：不需要编译，运行过程中由 js 解释器( js 引擎）逐行来进行解释并执行

3. 现在也可以基于 Node.js 技术进行服务器端编程

**作用：**

1. 表单动态校验（密码强度检测） （ JS 产生最初的目的 ）
2. 网页特效
3. 服务端开发(Node.js)
4. 桌面程序(Electron)
5. App(Cordova) 
6. 控制硬件-物联网(Ruff)
7. 游戏开发(cocos2d-js)

**浏览器分成两部分：渲染引擎和 JS 引擎**

**渲染引擎**：用来解析HTML与CSS，俗称内核，比如 chrome 浏览器的 blink ，老版本的 webkit

**JS** **引擎**：也称为 JS 解释器。 用来读取网页中的JavaScript代码，对其处理后运行，比如 chrome 浏览器的 V8

浏览器本身并不会执行JS代码，而是通过内置 JavaScript 引擎(解释器) 来执行 JS 代码 。JS 引擎执行代码时逐行解释每一句源码（转换为机器语言），然后由计算机去执行，所以 JavaScript 语言归为脚本语言，会逐行解释执行。

### 组成

![image-20211223195532298](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211223195532298.png)

1. **ECMAScript** 
   1. 是由ECMA 国际（ 原欧洲计算机制造商协会）进行标准化的一门编程语言，这种语言在万维网上应用广泛，它往往被称为 JavaScript 或 JScript，但实际上后两者是 ECMAScript 语言的实现和扩展。
   2. ECMAScript 规定了JS的编程语法和基础核心知识，是所有浏览器厂商共同遵守的一套JS语法工业标准。
2. **DOM——文档对象模型**
   1. **文档对象模型**（Document Object Model，简称DOM），是W3C组织推荐的处理可扩展标记语言的标准编程接口。通过 DOM 提供的接口可以对页面上的各种元素进行操作（大小、位置、颜色等）。
3. **BOM——浏览器对象模型** 
   1. **BOM** (Browser Object Model，简称BOM) 是指浏览器对象模型，它提供了独立于内容的、可以与浏览器窗口进行互动的对象结构。通过BOM可以操作浏览器窗口，比如弹出框、控制浏览器跳转、获取分辨率等。

### 写法

1. **行内式** **JS**

   1. ```html
      <input type="button" value="点我试试" onclick="alert('Hello World')" />
      ```

      1. 可以将单行或少量 JS 代码写在HTML标签的事件属性中（以 on 开头的属性），如：onclick
      2. 注意单双引号的使用：在HTML中我们推荐使用双引号, JS 中我们推荐使用单引号
      3. 可读性差， 在html中编写JS大量代码时，不方便阅读；
      4. 引号易错，引号多层嵌套匹配时，非常容易弄混；
      5. 特殊情况下使用

2. **内嵌JS**

   1. ```html
      <script>
          alert('Hello  World~!');
       </script>
      
      ```

      1. 利于HTML页面代码结构化，把大段 JS代码独立到 HTML 页面之外，既美观，也方便文件级别的复用
      2. 引用外部 JS文件的 script 标签中间不可以写代码
      3. 适合于JS 代码量比较大的情况

3. **外部JS文件**

   1. ```htaccess
      <script src="my.js"></script>
      ```

      1. 利于HTML页面代码结构化，把大段 JS代码独立到 HTML 页面之外，既美观，也方便文件级别的复用
      2. 引用外部 JS文件的 script 标签中间不可以写代码
      3. 适合于JS 代码量比较大的情况

## 2. 变量

### 介绍

**什么是变量**

白话：变量就是一个装东西的盒子。

通俗：变量是用于存放数据的容器。 我们通过 变量名 获取数据，甚至数据可以修改。

**使用变量**

```javascript
//  声明变量  
var age; //  声明一个 名称为age 的变量   
age = 10; // 给 age  这个变量赋值为 10   
//变量初始化
var age2  = 18;  // 声明变量同时赋值为 18     
```

1. var 是一个 JS关键字，用来声明变量( variable 变量的意思 )。使用该关键字声明变量后，计算机会自动为变量分配内存空间，不需要程序员管
2. age 是程序员定义的变量名，我们要通过变量名来访问内存中分配的空间
3. = 用来把右边的值赋给左边的变量空间中  此处代表赋值的意思
4. 变量值是程序员保存到变量空间里的值
5. 声明一个变量并赋值， 我们称之为变量的初始化。

**练习**

```javascript
<script>
        var myname = '旗木卡卡西';
        var address = '火影村';
        var age = 30;
        var email = 'kakaxi@itcast.cn';
        var gz = 2000;
        console.log(myname);
        console.log(address);
        console.log(age);
        console.log(email);
        console.log(gz);
    </script>
```

**扩展**

```javascript
<script>
        // 1. 更新变量
        var myname = '老师';
        console.log(myname);
        myname = '迪丽热巴';
        console.log(myname);
        // 2. 声明多个变量
        // var age = 18;
        // var address = '火影村';
        // var gz = 2000;
        var age = 18,
            address = '火影村',
            gz = 2000;
        // 3. 声明变量的特殊情况
        // 3.1 只声明不赋值 结果是？  程序也不知道里面存的是啥 所以结果是 undefined  未定义的
        var sex;
        console.log(sex); // undefined
        // 3.2  不声明 不赋值 直接使用某个变量会报错滴
        // console.log(tel);
        // 3.3 不声明直接赋值使用
        qq = 110;
        console.log(qq);
    </script>
```

### 变量命名规范

1. 由字母(A-Za-z)、数字(0-9)、下划线(_)、美元符号( $ )组成，如：usrAge, num01, _name
2. 严格区分大小写。var app; 和 var App; 是两个变量
3. 不能 以数字开头。 18age  是错误的
4. 不能 是关键字、保留字。例如：var、for、while
5. 变量名必须有意义。 MMD  BBD    nl  →   age 
6. 遵守驼峰命名法。首字母小写，后面单词的首字母需要大写。 myFirstName

## 3. 数据类型

### 介绍

在计算机中，不同的数据所需占用的存储空间是不同的，为了便于把数据分成所需内存大小不同的数据，充分利用存储空间，于是定义了不同的数据类型。

变量是用来存储值的所在处，它们有名字和数据类型。变量的数据类型决定了如何将代表这些值的位存储到计算机的内存中。**JavaScript** **是一种弱类型或者说动态语言。**这意味着不用提前声明变量的类型，在程序运行过程中，类型会被自动确定。

```javascript
var age = 10;        // 这是一个数字型
var areYouOk = '是的';   // 这是一个字符串   
```

在代码运行时，变量的数据类型是由 JS引擎 根据 = 右边变量值的数据类型来判断 的，运行完毕之后， 变量就确定了数据类型。

**JavaScript 拥有动态类型，同时也意味着相同的变量可用作不同的类型**

```javascript
var x = 6;           // x 为数字
var x = "Bill";      // x 为字符串    
```

### 分类

1. 简单数据类型 （Number,String,Boolean,Undefined,Null）
2. 复杂数据类型 （object)

#### 简单数据类型

![image-20211223201321865](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211223201321865.png)

##### Number

1. **数字进制**

   1. 常见的进制有二进制、八进制、十进制、十六进制。

   2. ```js
      // 1.八进制数字序列范围：0~7
       var num1 = 07;   // 对应十进制的7
       var num2 = 019;  // 对应十进制的19
       var num3 = 08;   // 对应十进制的8
        // 2.十六进制数字序列范围：0~9以及A~F
       var num = 0xA;   
      ```

   3. 在JS中八进制前面加0，十六进制前面加 0x 

2. **数字型范围**

   1. 最大值：Number.MAX_VALUE，这个值为： 1.7976931348623157e+308
   2. 最小值：Number.MIN_VALUE，这个值为：5e-32

3. **特殊值**

   1. Infinity ，代表无穷大，大于任何数值
   2. -Infinity ，代表无穷小，小于任何数值
   3. NaN ，Not a number，代表一个非数值

4. **isNaN()**

   1. 用来判断一个变量是否为非数字的类型，返回 true 或者 false

   2. ![image-20211223201742098](C:\Users\DELL\AppData\Roaming\Typora\typora-user-images\image-20211223201742098.png)

   3. ```js
      var usrAge = 21;
      var isOk = isNaN(userAge);
      console.log(isNum);            // false ，21 不是一个非数字
      var usrName = "andy";
      console.log(isNaN(userName));  // true ，"andy"是一个非数字
      ```

```js
<script>
        var num = 10; // num 数字型 
        var PI = 3.14 // PI 数字型
            // 1. 八进制  0 ~ 7  我们程序里面数字前面加0 表示八进制
        var num1 = 010;
        console.log(num1); //  010  八进制 转换为 10进制 就是  8 
        var num2 = 012;
        console.log(num2);
        // 2. 十六进制  0 ~ 9  a ~ f    #ffffff  数字的前面加 0x 表示十六进制
        var num3 = 0x9;
        console.log(num3);
        var num4 = 0xa;
        console.log(num4);
        // 3. 数字型的最大值
        console.log(Number.MAX_VALUE);
        // 4. 数字型的最小值
        console.log(Number.MIN_VALUE);
        // 5. 无穷大
        console.log(Number.MAX_VALUE * 2); // Infinity 无穷大  
        // 6. 无穷小
        console.log(-Number.MAX_VALUE * 2); // -Infinity 无穷大
        // 7. 非数字
        console.log('FF老师' - 100); // NaN
    </script>
```

##### String

1. 字符串型可以是引号中的任意文本，其语法为 双引号 "" 和 单引号''

   1. ```js
      var strMsg = "我爱北京天安门~";  // 使用双引号表示字符串
      var strMsg2 = '我爱吃猪蹄~';    // 使用单引号表示字符串
      // 常见错误
      var strMsg3 = 我爱大肘子;       // 报错，没使用引号，会被认为是js代码，但js没有这些语法
      ```

   2. 因为 HTML 标签里面的属性使用的是双引号，JS 这里我们更推荐使用单引号。

2. **字符串引号嵌套**

   1. JS 可以用单引号嵌套双引号 ，或者用双引号嵌套单引号 (**外双内单，外单内双**)

   2. ```js
      var strMsg = '我是"高帅富"程序猿';   // 可以用''包含""
      var strMsg2 = "我是'高帅富'程序猿";  // 也可以用"" 包含''
      //  常见错误
      var badQuotes = 'What on earth?"; // 报错，不能 单双引号搭配
      ```

3. **字符串转义符**

   1. 类似HTML里面的特殊字符，字符串中也有特殊字符，我们称之为转义符。
      1. ![image-20211223202019697](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211223202019697.png)

4. **字符串是由若干字符组成的，这些字符的数量就是字符串的长度。通过字符串的 length 属性可以获取整个字符串的长度**

   1. ```js
      var strMsg = "我是帅气多金的程序猿！";
      alert(strMsg.length); // 显示 11
      ```

5. **字符串拼接**

   1. 多个字符串之间可以使用 + 进行拼接，其拼接方式为 字符串 + 任何类型 = 拼接之后的新字符串

   2. 拼接前会把与字符串相加的任何类型转成字符串，再拼接成一个新的字符串

   3. ```js
      //1.1 字符串 "相加"
      alert('hello' + ' ' + 'world'); // hello world
      //1.2 数值字符串 "相加"
      alert('100' + '100'); // 100100
      //1.3 数值字符串 + 数值
      alert('11' + 12);     // 1112 
      ```

6. **字符串拼接加强**

   1. ```js
      console.log('pink老师' + 18);           // 只要有字符就会相连 
      var age = 18;
      // console.log('pink老师age岁啦');       // 这样不行哦
      console.log('pink老师' + age);          // pink老师18
      console.log('pink老师' + age + '岁啦');  // pink老师18岁啦

   ##### "Boolean"

   1. **介绍**

      1. 布尔类型有两个值：true 和 false ，其中 true 表示真（对），而 false 表示假（错）。

      2. 布尔型和数字型相加的时候， true 的值为 1 ，false 的值为 0。

      3. ```js
         console.log(true + 1);  // 2
         console.log(false + 1); // 1
         ```

##### **Undefined 和 Null**

1. 一个声明后没有被赋值的变量会有一个默认值 undefined ( 如果进行相连或者相加时，注意结果）

   1. ```js
      var variable;
      console.log(variable);           // undefined
      console.log('你好' + variable);  // 你好undefined
      console.log(11 + variable);     // NaN
      console.log(true + variable);   //  NaN
      
      ```

2. 一个声明变量给 null 值，里面存的值为空

   1. ```js
      var vari = null;
      console.log('你好' + vari);  // 你好null
      console.log(11 + vari);     // 11
      console.log(true + vari);   //  1
      ```

##### 获取变量数据类型

1. typeof 可用来获取检测变量的数据类型

   ```js
   var num = 18;
   console.log(typeof num) // 结果 number      
   ```

2. 不同类型的返回值

   ![image-20211223202803028](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211223202803028.png)

##### 数据类型转换

**转换为数字型**

![image-20211223202945444](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211223202945444.png)

1. 注意 parseInt 和 parseFloat 单词的大小写，这2个是重点

2. 隐式转换是我们在进行算数运算的时候，JS 自动转换了数据类型

**案例一:**

1. 弹出一个输入框（prompt)，让用户输入出生年份 （用户输入）
2. 把用户输入的值用变量保存起来，然后用今年的年份减去变量值，结果就是现在的年龄  （程序内部处理）
3. 弹出警示框（alert) ， 把计算的结果输出 （输出结果）

```js
// 1. 弹出输入框，输入出生年份，并存储在变量中  
var year = prompt('请输入您的出生年份：');  // 用户输入
// 2. 用今年减去刚才输入的年份   
var result = 2019 - year;               // 程序内部处理
// 3. 弹出提示框  
alert('您的年龄是:' + result + '岁');     // 输出结果
```

**案例二:**

1. 先弹出第一个输入框，提示用户输入第一个值  保存起来
2. 再弹出第二个框，提示用户输入第二个值  保存起来
3. 把这两个值相加，并将结果赋给新的变量（注意数据类型转换）  
4. 弹出警示框（alert) ， 把计算的结果输出 （输出结果）

```js
// 1. 先弹出第一个输入框，提示用户输入第一个值 
 var num1 = prompt('请输入第一个值：');
// 2. 再弹出第二个框，提示用户输入第二个值 
 var num2 = prompt('请输入第二个值：');
// 3. 将输入的值转换为数字型后，把这两个值相加，并将结果赋给新的变量  
 var result = parseFloat(num1) + parseFloat(num2);
// 4. 弹出结果
 alert('结果是:' + result);
```



**转换为布尔型 **

![image-20211223203038245](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211223203038245.png)

1. 代表空、否定的值会被转换为 false ，如 ''、0、NaN、null、undefined 
2. 其余值都会被转换为 true

```js
console.log(Boolean('')); // false
console.log(Boolean(0)); // false
console.log(Boolean(NaN)); // false
console.log(Boolean(null)); // false
console.log(Boolean(undefined)); // false
console.log(Boolean('小白')); // true
console.log(Boolean(12)); // true
```

## 4. 运算符

### 算术运算符

算术运算使用的符号，用于执行两个变量或值的算术运算。

![image-20211223203718777](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211223203718777.png)

**浮点数的精度问题**

不要直接判断两个浮点数是否相等 ! 

```js
var result = 0.1 + 0.2;    // 结果不是 0.3，而是：0.30000000000000004
console.log(0.07 * 100);   // 结果不是 7，  而是：7.000000000000001
```

### 递增和递减运算符

如果需要反复给数字变量添加或减去1，可以使用递增（++）和递减（-- **）**运算符来完成。

在 JavaScript 中，递增（++）和递减（ -- ）既可以放在变量前面，也可以放在变量后面。放在变量前面时，我们可以称为前置递增（递减）运算符，放在变量后面时，我们可以称为后置递增（递减）运算符。

**注意：**递增和递减运算符必须和变量配合使用。

++num 前置递增，就是自加1，类似于 num = num + 1，但是 ++num 写起来更简单。

```js
var  num = 10;
alert(++num + 10);   // 21
```

num++ 后置递增，就是自加1，类似于 num = num + 1 ，但是 num++ 写起来更简单。

```js
var  num = 10;
alert(10 + num++);  // 20
```

### 比较运算符

比较运算符（关系运算符）是两个数据进行比较时所使用的运算符，比较运算后，会返回一个布尔值（true / false）作为比较运算的结果。

![image-20211223204005865](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211223204005865.png)

![image-20211223204016647](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211223204016647.png)

### 逻辑运算符

逻辑运算符是用来进行布尔值运算的运算符，其返回值也是布尔值。

![image-20211223204050361](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211223204050361.png)

### 赋值运算符

用来把数据赋值给变量的运算符。

![image-20211223204123431](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211223204123431.png)

### 运算符优先级

![image-20211223204144229](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211223204144229.png)

## 5.流程控制

在一个程序执行的过程中，各条代码的执行顺序对程序的结果是有直接影响的。很多时候我们要通过控制代码的执行顺序来实现我们要完成的功能。

简单理解： 流程控制就是来控制我们的代码按照什么结构顺序来执行

流程控制主要有三种结构，分别是顺序结构、分支结构和循环结构，这三种结构代表三种代码执行的顺序。

![image-20211223204227154](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211223204227154.png) 

### 顺序结构

顺序结构是程序中最简单、最基本的流程控制，它没有特定的语法结构，程序会按照代码的先后顺序，依次执行，程序中大多数的代码都是这样执行的。

![image-20211223204259110](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211223204259110.png)

### 分支结构

#### if

**if**

![image-20211223204451346](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211223204451346.png)

```js
var usrAge = prompt('请输入您的年龄：');
if(usrAge >= 18){
    alert('您的年龄合法，欢迎来天际网吧享受学习的乐趣！');
}
```

**if else**

![image-20211223204358081](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211223204358081.png)

```js
   if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
      alert("这个年份是闰年");
   } else { // 剩下的是平年
      alert("这个年份是平年");
   }
```

**if else else**

![image-20211223204510400](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211223204510400.png)

```js
var score = prompt('请您输入分数:');
if (score >= 90) {
alert('宝贝，你是我的骄傲');
} else if (score >= 80) {
alert('宝贝，你已经很出色了');
} else if (score >= 70) {
alert('你要继续加油喽');
} else if (score >= 60) {
alert('孩子，你很危险');
} else {
alert('熊孩子，我不想和你说话，我只想用鞭子和你说话');
}
```

#### 三元表达式

表达式1 ? 表达式2 : 表达式3;

1. 如果表达式1为 true ，则返回表达式2的值，如果表达式1为 false，则返回表达式3的值
2.  类似于 if else （双分支） 的简写

#### **switch**

switch 语句也是多分支语句，它用于基于不同的条件来执行不同的代码。当要针对变量设置一系列的特定值的选项时，就可以使用 switch。

```js
switch( 表达式 ){ 
    case value1:
        // 表达式 等于 value1 时要执行的代码
        break;
    case value2:
        // 表达式 等于 value2 时要执行的代码
        break;
    default:
        // 表达式 不等于任何一个 value 时要执行的代码
}
```

### 循环结构

1. for 循环
2. 双重 for 循环
3. while 循环
4. do while 循环
5. continue break

## 6. 数组

### 介绍

数组是指一组数据的集合，其中的每个数据被称作元素，在数组中可以存放任意类型的元素。数组是一种将一组数据存储在单个变量名下的优雅方式。

数组中可以存放**任意类型**的数据，例如字符串，数字，布尔值等。

```js
// 普通变量一次只能存储一个值
var  num = 10; 
// 数组一次可以存储多个值
var arr = [1,2,3,4,5];
```

**数组的创建方式**

1. 利用 new 创建数组 

   1. ```js
      var 数组名 = new Array() ；
      var arr = new Array();   // 创建一个新的空数组
      
      ```

2. 利用数组字面量创建数组

   1. ```js
      //1. 使用数组字面量方式创建空的数组
      var  数组名 = []；
      //2. 使用数组字面量方式创建带初始值的数组
      var  数组名 = ['小白','小黑','大黄','瑞奇'];
      ```

### 使用

#### 数组索引

数组可以通过**索引**来访问、设置、修改对应的数组元素，我们可以通过“数组名[索引]”的形式来获取数组中的元素。

```js
// 定义数组
var arrStus = [1,2,3];
// 获取数组中的第2个元素
alert(arrStus[1]);    
```

#### 遍历数组

遍历：就是把数组中的每个元素从头到尾都访问一次

```js
var arr = ['red','green', 'blue'];
for(var i = 0; i < arr.length; i++){
    console.log(arrStus[i]);
}
```

#### 数组的长度

使用“数组名.length”可以访问数组元素的数量（数组长度）。 

```js
var arrStus = [1,2,3];
alert(arrStus.length);  // 3
```

1. 此处数组的长度是数组元素的个数 ，不要和数组的索引号混淆。

2. 当我们数组里面的元素个数发生了变化，这个 length 属性跟着一起变化。

**案例一：**

求和和平均值

```js
var arr = [2, 6, 1, 7, 4];
var sum = 0;
var average = 0;
for (var i = 0; i < arr.length; i++) {
    sum += arr[i];
}
average = sum / arr.length;
console.log('这组数的和是：' + sum);
console.log('这组数的平均值是：' + average);
```

**案例二：**

拼接字符串

```js
var arr = ['red', 'green', 'blue', 'pink'];
var str = '';
for (var i = 0; i < arr.length; i++) {
    str += arr[i];
}
console.log(str);
```

**案例三：**

数组转换为分割字符串

```js
var arr = ['red', 'green', 'blue', 'pink'];
var str = '';
var separator = '|'
for (var i = 0; i < arr.length; i++) {
   str += arr[i] + separator;
}
console.log(str);
```

#### 新增元素

通过修改 length长度新增数组元素

1. 可以通过修改 length 长度来实现数组扩容的目的

2. length 属性是可读写的

```js
var arr = ['red', 'green', 'blue', 'pink'];
arr.length = 7;
console.log(arr);
console.log(arr[4]);
console.log(arr[5]);
console.log(arr[6]);
```

其中索引号是 4，5，6 的空间没有给值，就是声明变量未给值，默认值就是 undefined。

![image-20211223205824234](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211223205824234.png)

## 7. 函数

1. 函数的概念

2. 函数的使用

3. 函数的参数

4. 函数的返回值

5. arguments的使用

6. 函数案例

7. 函数的两种声明方式

### 介绍

**函数：**就是封装了一段可被重复调用执行的**代码块**。通过此代码块可以实现大量代码的重复使用。 

### 使用

函数在使用时分为两步：声明函数和调用函数。

```js
// 声明函数
function 函数名() {
    //函数体代码
}

// 调用函数
函数名();  // 通过调用函数名来执行函数体代码
```

1. function 是声明函数的关键字,必须小写

2. 由于函数一般是为了实现某个功能才定义的， 所以通常我们将函数名命名为动词，比如 getSum  
3. 调用的时候千万不要忘记添加小括号
4. 声明函数本身并不会执行代码，只有调用函数时才会执行函数体代码。

```js
/* 
   计算1-100之间值的函数
*/
// 声明函数
function getSum(){
  var sumNum = 0;// 准备一个变量，保存数字和
  for (var i = 1; i <= 100; i++) {
    sumNum += i;// 把每个数值 都累加 到变量中
  }
  alert(sumNum);
}
// 调用函数
getSum();
```

### 函数的参数

在声明函数时，可以在函数名称后面的小括号中添加一些参数，这些参数被称为**形参**，而在调用该函数时，同样也需要传递相应的参数，这些参数被称为**实参**。

![image-20211223210243645](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211223210243645.png)

**参数的作用** : 在**函数内部**某些值不能固定，我们可以通过参数在**调用函数时传递**不同的值进去。

```js
function getSum(num1, num2) {
    console.log(num1 + num2);
}
getSum(1, 3); // 4
getSum(6, 5); // 11
```

1. 调用的时候实参值是传递给形参的

2. 形参简单理解为：**不用****声明的变量**

3. 实参和形参的多个参数之间用逗号（,）分隔

**函数形参和实参个数不匹配问题**

![image-20211223210423266](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211223210423266.png)

```js

function sum(num1, num2) {
    console.log(num1 + num2);
}
sum(100, 200);             // 形参和实参个数相等，输出正确结果
sum(100, 400, 500, 700);   // 实参个数多于形参，只取到形参的个数
sum(200);                  // 实参个数少于形参，多的形参定义为undefined，结果为NaN
```

在JavaScript中，形参的默认值是undefined。

### **return** 语句

我们会希望函数将值返回给调用者，此时通过使用 return 语句就可以实现

1. 在使用 return 语句时，函数会停止执行，并返回指定的值
2. 如果函数没有 return ，返回的值是 undefined
3. return 语句之后的代码不被执行。

```js
//定义一个获取数组中最大数的函数
function getMaxFromArr(numArray){
    var maxNum = 0;
    for(var i =0;i < numArray.length;i++){
        if(numArray[i] > maxNum){
            maxNum = numArray[i];
        }
    }
    return maxNum;
}
var arrNum = [5,2,99,101,67,77];
var maxN = getMaxFromArr(arrNum); // 这个实参是个数组
alert('最大值为：'+ maxN);
```

**return只能返回一个值。如果用逗号隔开多个值，以最后一个为准。**

```js
function add(num1，num2){
    //函数体
    return num1，num2;
}
var resNum = add(21,6); // 调用函数，传入两个实参，并通过 resNum 接收函数返回值
alert(resNum);          // 6

```

**案例：**

```js
var a = parseFloat(prompt('请输入第一个数'));
var b = parseFloat(prompt('请输入第二个数'));
function count(a, b) {
    var arr = [a + b, a - b, a * b, a / b];
    return arr;
}
var result = count(a, b);
console.log(result);
```

### break ,continue ,return 的区别

1. break ：结束当前的循环体（如 for、while）

2. continue ：跳出本次循环，继续执行下次循环（如 for、while）

3. return ：不仅可以退出循环，还能够返回 return 语句中的值，同时还可以结束当前的函数体内的代码

### arguments

**arguments**展示形式是**一个伪数组**

当我们不确定有多少个参数传递的时候，可以用 arguments 来获取。

arguments 实际上它是当前函数的一个内置对象。所有函数都内置了一个 arguments 对象，arguments 对象中存储了传递的所有实参。

特点：

1. 具有 length 属性

2. 按索引方式储存数据

3. 不具有数组的 push , pop 等方法

```js
function maxValue() {
      var max = arguments[0];
      for (var i = 0; i < arguments.length; i++) {
         if (max < arguments[i]) {
                    max = arguments[i];
         }
      }
      return max;
}
 console.log(maxValue(2, 4, 5, 9));
 console.log(maxValue(12, 4, 9));
```

### 函数的两种声明方式

1. 自定义函数方式(命名函数)

   1. ```js
      // 声明定义方式
      function fn() {...}
      // 调用  
      fn();  
      ```

   2. 因为有名字，所以也被称为命名函数

   3. 调用函数的代码既可以放到声明函数的前面，也可以放在声明函数的后面

2. 函数表达式方式(匿名函数）

   1. ```js
      // 这是函数表达式写法，匿名函数后面跟分号结束
      var fn = function(){...}；
      // 调用的方式，函数调用必须写到函数体下面
      fn();
      ```

   2. 因为函数没有名字，所以也被称为匿名函数
   3. 这个fn 里面存储的是一个函数 
   4. 函数表达式方式原理跟声明变量方式是一致的
   5. **函数调用的代码必须写到函数体后面**

## 8. 作用域

1. 作用域
2. 变量的作用域
3. 作用域链

### 介绍

一段程序代码中所用到的名字**并不总是有效和可用的**，而限定这个名字的**可用性的代码范围**就是这个名字的**作用域**。作用域的使用提高了程序逻辑的局部性，增强了程序的可靠性，减少了名字冲突。

JavaScript（es6前）中的作用域有两种：

1. 全局作用域

2. 局部作用域（函数作用域）

**全局作用域** 

作用于所有代码执行的环境(整个 script 标签内部)或者一个独立的 js 文件。

**局部作用域（函数作用域）**

作用于函数内的代码环境，就是局部作用域。 因为跟函数有关系，所以也称为函数作用域。

### 作用域

JS 没有块级作用域

1. 块作用域由 { } 包括。

2. 在其他编程语言中（如 java、c#等），在 if 语句、循环语句中创建的变量，仅仅只能在本 if 语句、本循环语句中使用

   1. ```java
      if(true){
        int num = 123;
        system.out.print(num);  // 123
      }
      system.out.print(num);    // 报错
      
      ```

3. js没有块级作用域（es6之前）

   1. ```js
      if(true){
        var num = 123;
        console.log(123); //123
      }
      console.log(123);   //123
      ```

```js
 <script>
        // 1.JavaScript作用域 ： 就是代码名字（变量）在某个范围内起作用和效果 目的是为了提高程序的可靠性更重要的是减少命名冲突
        // 2. js的作用域（es6）之前 ： 全局作用域   局部作用域 
        // 3. 全局作用域： 整个script标签 或者是一个单独的js文件
        var num = 10;
        var num = 30;
        console.log(num);

        // 4. 局部作用域（函数作用域） 在函数内部就是局部作用域 这个代码的名字只在函数内部起效果和作用
        function fn() {
            // 局部作用域
            var num = 20;
            console.log(num);
        }
        fn();
    </script>
```

### 变量作用域

在JavaScript中，根据作用域的不同，变量可以分为两种：

1. 全局变量
2. 局部变量

**全局变量**

在全局作用域下声明的变量叫做**全局变量**（在函数外部定义的变量）。

1. 全局变量在代码的任何位置都可以使用

2. 在全局作用域下 var 声明的变量 是全局变量

3. 特殊情况下，在函数内不使用 var 声明的变量也是全局变量（不建议使用）

**局部变量**

在局部作用域下声明的变量叫做**局部变量**（在函数内部定义的变量）

1. 局部变量只能在该函数**内部**使用

2. 在函数内部 var 声明的变量是局部变量

3. 函数的**形参**实际上就是局部变量

**全局变量和局部变量的区别**

全局变量：**在任何一个地方都可以使用，只有在浏览器关闭时才会被销毁，因此比较占内存**

局部变量：**只在函数内部使用，当其所在的代码块被执行时，会被初始化；当代码块运行结束后，就会被销毁，因此更节省内存空间**

```js
<script>
        // 变量的作用域： 根据作用域的不同我们变量分为全局变量和局部变量
        // 1. 全局变量： 在全局作用域下的变量 在全局下都可以使用
        // 注意 如果在函数内部 没有声明直接赋值的变量也属于全局变量
        var num = 10; // num就是一个全局变量
        console.log(num);

        function fn() {
            console.log(num);

        }
        fn();
        // console.log(aru);

        // 2. 局部变量   在局部作用域下的变量   后者在函数内部的变量就是 局部变量
        // 注意： 函数的形参也可以看做是局部变量
        function fun(aru) {
            var num1 = 10; // num1就是局部变量 只能在函数内部使用
            num2 = 20;
        }
        fun();
        // console.log(num1);
        // console.log(num2);
        // 3. 从执行效率来看全局变量和局部变量
        // (1) 全局变量只有浏览器关闭的时候才会销毁，比较占内存资源
        // (2) 局部变量 当我们程序执行完毕就会销毁， 比较节约内存资源
    </script>
```

### 作用域链

1. 只要是代码，就至少有一个作用域
2. 写在函数内部的局部作用域
3. 如果函数中还有函数，那么在这个作用域中就又可以诞生一个作用域
4. 根据在内部函数可以访问外部函数变量的这种机制，用链式查找决定哪些数据能被内部函数访问，就称作作用域链
5. 作用域链  ： 内部函数访问外部函数的变量，采取的是链式查找的方式来决定取那个值 这种结构我们称为作用域链

```js
function f1() {
    var num = 123;
    function f2() {
        console.log( num );
    }
    f2();
}
var num = 456;
f1();
```

![image-20211223212216063](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211223212216063.png)

作用域链：**采取就近原则的方式来查找变量最终的值。**

```js
var a = 1;
function fn1() {
    var a = 2;
    var b = '22';
    fn2();
    function fn2() {
        var a = 3;
        fn3();
        function fn3() {
            var a = 4;
            console.log(a); //a的值 ?  4
            console.log(b); //b的值 ?  22
        }
    }
}
fn1();

```

## 9. 预解析

### 预解析

```js
<script>
        // 1问  
        console.log(num);

        // 2问
        console.log(num); // undefined  坑 1
        var num = 10;
        // 相当于执行了以下代码
        // var num;
        // console.log(num);
        // num = 10;



        // 3问  
        function fn() {
            console.log(11);
        }
        fn();




        // 4问
        fun(); // 报错  坑2 
        var fun = function() {
                console.log(22);

            }
            // 函数表达式 调用必须写在函数表达式的下面
            // 相当于执行了以下代码
            // var fun;
            // fun();
            // fun = function() {
            //         console.log(22);

        //     }

        // 1. 我们js引擎运行js 分为两步：  预解析  代码执行
        // (1). 预解析 js引擎会把js 里面所有的 var  还有 function 提升到当前作用域的最前面
        // (2). 代码执行  按照代码书写的顺序从上往下执行
        // 2. 预解析分为 变量预解析（变量提升） 和 函数预解析（函数提升）
        // (1) 变量提升 就是把所有的变量声明提升到当前的作用域最前面  不提升赋值操作
        // (2) 函数提升 就是把所有的函数声明提升到当前作用域的最前面  不调用函数
    </script>
```

1. JavaScript 代码是由浏览器中的 JavaScript 解析器来执行的。JavaScript 解析器在运行 JavaScript 代码的时候分为两步：预解析和代码执行。
   1. **预解析：**在当前作用域下, JS 代码执行之前，浏览器会默认把带有 var 和 function 声明的变量在内存中进行提前声明或者定义。
   2. **代码执行：**从上到下执行JS语句。
2. 预解析只会发生在通过 var 定义的变量和 function 上。学习预解析能够让我们知道为什么在变量声明之前访问变量的值是 undefined，为什么在函数声明之前就可以调用函数。

### 变量预解析和函数预解析

**变量提升**

预解析也叫做变量、函数提升。

**变量提升：** **变量的声明会被提升到当前作用域的最上面，变量的赋值不会提升。**

```js
console.log(num);  // 结果是多少？
var num = 10;      // ？
```

**函数预解析（函数提升）**

**函数提升：** 函数的声明会被提升到当前作用域的最上面，但是不会调用函数。

```js
fn();
function fn() {
    console.log('打印');
}
```

**解决函数表达式声明调用问题**

```js
fn();
var  fn = function() {
    console.log('想不到吧');
}
```

### 预解析案例

案例一：

```js
var num = 10;
fun();
function fun() {
  console.log(num);
  var num = 20;
}

```

案例二：

```js
// 案例2
var num = 10;
function fn(){
    console.log(num);
    var num = 20;
    console.log(num);
} 
fn();
```

案例三：

```js
// 案例3
var a = 18;
f1();
function f1() {
  var b = 9;
  console.log(a);
  console.log(b);
  var a = '123';
}
```

案例四：

```js
f1();
console.log(c);
console.log(b);
console.log(a);
function f1() {
  var a = b = c = 9;
  console.log(a);
  console.log(b);
  console.log(c);
}
```

## 10. 创建对象的三种方式

### 创建对象

1. 利用字面量创建对象 

   1. ```js
      var star = {
          name : 'pink',
          age : 18,
          sex : '男',
          sayHi : function(){
              alert('大家好啊~');
          }
      };
      console.log(star.name)     // 调用名字属性
      console.log(star['name'])  // 调用名字属性
      star.sayHi();              // 调用 sayHi 方法,注意，一定不要忘记带后面的括号
      ```

   2. 对象里面的属性调用 : 对象.属性名 ，这个小点 . 就理解为“ 的 **”** 
   3. 对象里面属性的另一种调用方式 : 对象[‘属性名’]**，**注意方括号里面的属性必须加引号，我们后面会用   
   4. 对象里面的方法调用：对象.方法名() **，**注意这个方法名字后面一定加括号 

2. 利用 new Object 创建对象 

   1. ```js
      var andy = new Obect();
      andy.name = 'pink';
      andy.age = 18;
      andy.sex = '男';
      andy.sayHi = function(){
          alert('大家好啊~');
      }
      ```

   2. Object() ：第一个字母大写  

   3. new Object() ：需要 new 关键字

   4. 使用的格式：对象.属性 = 值;   

3. 利用构造函数创建对象 

   1. **构造函数 ：**是一种特殊的函数，主要用来初始化对象，即为对象成员变量赋初始值，它总与 new 运算符一起使用。我们可以把对象中一些公共的属性和方法抽取出来，然后封装到这个函数里面。

   2. 构造函数用于创建某一类对象，其首字母要大写

   3. ```js
      function Person(name, age, sex) {
           this.name = name;
           this.age = age;
           this.sex = sex;
           this.sayHi = function() {
            alert('我的名字叫：' + this.name + '，年龄：' + this.age + '，性别：' + this.sex);
          }
      }
      var bigbai = new Person('大白', 100, '男');
      var smallbai = new Person('小白', 21, '男');
      console.log(bigbai.name);
      console.log(smallbai.name);
      ```

  5. 注意

     		1. 构造函数约定首字母大写。

     2. 函数内的属性和方法前面需要添加 this ，表示当前对象的属性和方法。

     3. 构造函数中不需要 return 返回结果。

     4. 当我们创建对象的时候，必须用 new 来调用构造函数。

### 遍历对象属性

for...in 语句用于对数组或者对象的属性进行循环操作

```js
for (var k in obj) {
    console.log(k);      // 这里的 k 是属性名
    console.log(obj[k]); // 这里的 obj[k] 是属性值
}
```

## 11. Dom

1. DOM 简介
2. 获取元素
3. 事件基础
4. 操作元素
5. 节点操作

### Dom简介

文档对象模型（Document Object Model，简称 DOM），是 W3C 组织推荐的处理可扩展标记语言（HTML或者XML）的标准编程接口。

W3C 已经定义了一系列的 DOM 接口，通过这些 DOM 接口可以改变网页的内容、结构和样式。

![image-20211223220439663](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211223220439663.png)

1. 文档：一个页面就是一个文档，DOM 中使用 document 表示
2. 元素：页面中的所有标签都是元素，DOM 中使用 element 表示
3. 节点：网页中的所有内容都是节点（标签、属性、文本、注释等），DOM 中使用 node 表示

### 获取元素

获取页面中的元素可以使用以下几种方式:

1. 根据 ID 获取

   1.  document.getElementById('id');

   2. ```html
       <div id="time">2019-9-9</div>
          <script>
              // 1. 因为我们文档页面从上往下加载，所以先得有标签 所以我们script写到标签的下面
              // 2. get 获得 element 元素 by 通过 驼峰命名法 
              // 3. 参数 id是大小写敏感的字符串
              // 4. 返回的是一个元素对象
              var timer = document.getElementById('time');
              console.log(timer);
              console.log(typeof timer);
              // 5. console.dir 打印我们返回的元素对象 更好的查看里面的属性和方法
              console.dir(timer);
          </script>
      ```

2. 根据标签名获取

   1.  document.getElementsByTagName('标签名');

      1. 因为得到的是一个对象的集合，所以我们想要操作里面的元素就需要遍历。

      2. 得到元素对象是动态的

      3. ```html
         <body>
             <ul>
                 <li>知否知否，应是等你好久11</li>
                 <li>知否知否，应是等你好久11</li>
                 <li>知否知否，应是等你好久11</li>
                 <li>知否知否，应是等你好久11</li>
         
             </ul>
             <ol id="ol">
                 <li>生僻字</li>
                 <li>生僻字</li>
                 <li>生僻字</li>
                 <li>生僻字</li>
         
             </ol>
         
             <script>
                 // 1.返回的是 获取过来元素对象的集合 以伪数组的形式存储的
                 var lis = document.getElementsByTagName('li');
                 console.log(lis);
                 console.log(lis[0]);
                 // 2. 我们想要依次打印里面的元素对象我们可以采取遍历的方式
                 for (var i = 0; i < lis.length; i++) {
                     console.log(lis[i]);
         
                 }
                 // 3. 如果页面中只有一个li 返回的还是伪数组的形式 
                 // 4. 如果页面中没有这个元素 返回的是空的伪数组的形式
                 // 5. element.getElementsByTagName('标签名'); 父元素必须是指定的单个元素
                 // var ol = document.getElementsByTagName('ol'); // [ol]
                 // console.log(ol[0].getElementsByTagName('li'));
                 var ol = document.getElementById('ol');
                 console.log(ol.getElementsByTagName('li'));
             </script>
         </body>
         
         ```

3. 通过 HTML5 新增的方法获取

   1.  document.**getElementsByClassName(‘类名’)**；// 根据类名返回元素对象集合

   2. document.**querySelector('选择器')**;    // 根据指定选择器返回第一个元素对象

   3. document.**querySelectorAll('选择器')**;//根据指定选择器返回

   4. 注意：

      1. querySelector 和 querySelectorAll里面的选择器需要加符号,比如:document.querySelector('#nav'); 

   5. ```html
      <body>
          <div class="box">盒子1</div>
          <div class="box">盒子2</div>
          <div id="nav">
              <ul>
                  <li>首页</li>
                  <li>产品</li>
              </ul>
          </div>
          <script>
              // 1. getElementsByClassName 根据类名获得某些元素集合
              var boxs = document.getElementsByClassName('box');
              console.log(boxs);
              // 2. querySelector 返回指定选择器的第一个元素对象  切记 里面的选择器需要加符号 .box  #nav
              var firstBox = document.querySelector('.box');
              console.log(firstBox);
              var nav = document.querySelector('#nav');
              console.log(nav);
              var li = document.querySelector('li');
              console.log(li);
              // 3. querySelectorAll()返回指定选择器的所有元素对象集合
              var allBox = document.querySelectorAll('.box');
              console.log(allBox);
              var lis = document.querySelectorAll('li');
              console.log(lis);
          </script>
      </body>
      ```

4. 特殊元素获取（body，html）

   1. 获取body元素：doucumnet.body // 返回body元素对象

   2. 获取html元素：document.documentElement // 返回html元素对象

   3. ```js
      <body>
          <script>
              // 1.获取body 元素
              var bodyEle = document.body;
              console.log(bodyEle);
              console.dir(bodyEle);
              // 2.获取html 元素
              // var htmlEle = document.html;
              var htmlEle = document.documentElement;
              console.log(htmlEle);
          </script>
      </body>
      ```

### 事件基础

JavaScript 使我们有能力创建动态页面，而事件是可以被 JavaScript 侦测到的行为。

简单理解： 触发--- 响应机制。

网页中的每个元素都可以产生某些可以触发 JavaScript 的事件，例如，我们可以在用户点击某按钮时产生一个 事件，然后去执行某些操作。

1. 事件源 （谁）

2. 事件类型 （什么事件）

3. 事件处理程序 （做啥）

```html
<body>
    <button id="btn">唐伯虎</button>
    <script>
        // 点击一个按钮，弹出对话框
        // 1. 事件是有三部分组成  事件源  事件类型  事件处理程序   我们也称为事件三要素
        //(1) 事件源 事件被触发的对象   谁  按钮
        var btn = document.getElementById('btn');
        //(2) 事件类型  如何触发 什么事件 比如鼠标点击(onclick) 还是鼠标经过 还是键盘按下
        //(3) 事件处理程序  通过一个函数赋值的方式 完成
        btn.onclick = function() {
            alert('点秋香');
        }
    </script>
</body>
```

**步骤：**

1. 获取事件源（按钮）

2. 注册事件（绑定事件），使用 onclick

3. 编写事件处理程序，写一个函数弹出 alert 警示框

```js
var btn = document.getElementById('btn');
btn.onclick = function() {
  alert('你好吗');  
};
```

**常见鼠标事件**

![image-20211223221247913](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211223221247913.png)

### 操作元素

JavaScript 的 DOM 操作可以改变网页内容、结构和样式，我们可以利用 DOM 操作元素来改变元素里面的内容 、属性等。

#### 1. 改变元素

element.innerText

从起始位置到终止位置的内容, 但它去除 html 标签， 同时空格和换行也会去掉: 

element.innerHTML

起始位置到终止位置的全部内容，包括 html 标签，同时保留空格和换行

```js
<body>
    <button>显示当前系统时间</button>
    <div>某个时间</div>
    <p>1123</p>
    <script>
        // 当我们点击了按钮，  div里面的文字会发生变化
        // 1. 获取元素 
        var btn = document.querySelector('button');
        var div = document.querySelector('div');
        // 2.注册事件
        btn.onclick = function() {
            // div.innerText = '2019-6-6';
            div.innerHTML = getDate();
        }

        function getDate() {
            var date = new Date();
            // 我们写一个 2019年 5月 1日 星期三
            var year = date.getFullYear();
            var month = date.getMonth() + 1;
            var dates = date.getDate();
            var arr = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'];
            var day = date.getDay();
            return '今天是：' + year + '年' + month + '月' + dates + '日 ' + arr[day];
        }
        // 我们元素可以不用添加事件
        var p = document.querySelector('p');
        p.innerHTML = getDate();
    </script>
</body>
```

**二者区别：**

1. innerText 不识别html标签 非标准  去除空格和换行
2. innerHTML 识别html标签 W3C标准 保留空格和换行的
3. 这两个属性是可读写的  可以获取元素里面的内容

```js
<body>
    <div></div>
    <p>
        我是文字
        <span>123</span>
    </p>
    <script>
        // innerText 和 innerHTML的区别 
        // 1. innerText 不识别html标签 非标准  去除空格和换行
        var div = document.querySelector('div');
        // div.innerText = '<strong>今天是：</strong> 2019';
        // 2. innerHTML 识别html标签 W3C标准 保留空格和换行的
        div.innerHTML = '<strong>今天是：</strong> 2019';
        // 这两个属性是可读写的  可以获取元素里面的内容
        var p = document.querySelector('p');
        console.log(p.innerText);
        console.log(p.innerHTML);
    </script>
</body>
```

#### 2. 改变元素属性

1. innerText、innerHTML 改变元素内容

2. src、href

3. id、alt、title

**操作元素并修改元素属性**

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
    <style>
        img {
            width: 300px;
        }
    </style>
</head>

<body>
    <button id="ldh">刘德华</button>
    <button id="zxy">张学友</button> <br>
    <img src="images/ldh.jpg" alt="" title="刘德华">

    <script>
        // 修改元素属性  src
        // 1. 获取元素
        var ldh = document.getElementById('ldh');
        var zxy = document.getElementById('zxy');
        var img = document.querySelector('img');
        // 2. 注册事件  处理程序
        zxy.onclick = function() {
            img.src = 'images/zxy.jpg';
            img.title = '张学友思密达';
        }
        ldh.onclick = function() {
            img.src = 'images/ldh.jpg';
            img.title = '刘德华';
        }
    </script>
</body>

</html>
```

#### 3. 表单元素的属性操作 

利用 DOM 可以操作如下表单元素的属性：

type、value、checked、selected、disabled

```js
<body>
    <button>按钮</button>
    <input type="text" value="输入内容">
    <script>
        // 1. 获取元素
        var btn = document.querySelector('button');
        var input = document.querySelector('input');
        // 2. 注册事件 处理程序
        btn.onclick = function() {
            // input.innerHTML = '点击了';  这个是 普通盒子 比如 div 标签里面的内容
            // 表单里面的值 文字内容是通过 value 来修改的
            input.value = '被点击了';
            // 如果想要某个表单被禁用 不能再点击 disabled  我们想要这个按钮 button禁用
            // btn.disabled = true;
            this.disabled = true;
            // this 指向的是事件函数的调用者 btn
        }
    </script>
</body>
```

#### 4. 样式属性操作

我们可以通过 JS 修改元素的大小、颜色、位置等样式。

1. element.style   行内样式操作

2. element.className 类名样式操作

**注意**：

1. JS 里面的样式采取驼峰命名法 比如 fontSize、 backgroundColor

2. JS 修改 style 样式操作，产生的是行内样式，CSS 权重比较高
3. 如果样式修改较多，可以采取操作类名方式更改元素样式。
4.  class因为是个保留字，因此使用className来操作元素类名属性
5. className 会直接更改元素的类名，会覆盖原先的类名。

```js
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
    <style>
        div {
            width: 200px;
            height: 200px;
            background-color: pink;
        }
    </style>
</head>

<body>
    <div></div>
    <script>
        // 1. 获取元素
        var div = document.querySelector('div');
        // 2. 注册事件 处理程序
        div.onclick = function() {
            // div.style里面的属性 采取驼峰命名法 
            this.style.backgroundColor = 'purple';
            this.style.width = '250px';
        }
    </script>
</body>

</html>
```

#### 操作总结

![image-20211223223415763](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211223223415763.png)

#### 5. 案例

##### 排他思想

如果有同一组元素，我们想要某一个元素实现某种样式， 需要用到循环的排他思想算法：

![image-20211223223630578](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211223223630578.png)

1. 所有元素全部清除样式（干掉其他人）

2. 给当前元素设置样式 （留下我自己）

3. 注意顺序不能颠倒，首先干掉其他人，再设置自己

```js
<body>
    <button>按钮1</button>
    <button>按钮2</button>
    <button>按钮3</button>
    <button>按钮4</button>
    <button>按钮5</button>
    <script>
        // 1. 获取所有按钮元素
        var btns = document.getElementsByTagName('button');
        // btns得到的是伪数组  里面的每一个元素 btns[i]
        for (var i = 0; i < btns.length; i++) {
            btns[i].onclick = function() {
                // (1) 我们先把所有的按钮背景颜色去掉  干掉所有人
                for (var i = 0; i < btns.length; i++) {
                    btns[i].style.backgroundColor = '';
                }
                // (2) 然后才让当前的元素背景颜色为pink 留下我自己
                this.style.backgroundColor = 'pink';
            }
        }
        //2. 首先先排除其他人，然后才设置自己的样式 这种排除其他人的思想我们成为排他思想
    </script>
</body>
```

**表单全选取消全选案例**

**业务需求：**

1. 点击上面全选复选框，下面所有的复选框都选中（全选）

2. 再次点击全选复选框，下面所有的复选框都不中选（取消全选）

3. 如果下面复选框全部选中，上面全选按钮就自动选中

4. 如果下面复选框有一个没有选中，上面全选按钮就不选中
5. 所有复选框一开始默认都没选中状态

![image-20211223223803761](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211223223803761.png)

### 节点操作

#### 介绍

网页中的所有内容都是节点（标签、属性、文本、注释等），在DOM 中，节点使用 node 来表示。

HTML DOM 树中的所有节点均可通过 JavaScript 进行访问，所有 HTML 元素（节点）均可被修改，也可以创建或删除。

获取元素通常使用两种方式：

1. **利用DOM提供的方法获取元素**
   1. document.getElementById()
   2. document.getElementsByTagName()
   3. document.querySelector 等
   4. 逻辑性不强、繁琐
2. **利用节点层级关系获取元素**
   1. 利用父子兄节点关系获取元素
   2. 逻辑性强， 但是兼容性稍差

利用 DOM 树可以把节点划分为不同的层级关系，常见的是**父子兄****层级****关系**。

![image-20211223224230865](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211223224230865.png)

#### 节点层级

利用 DOM 树可以把节点划分为不同的层级关系，常见的是**父子兄层级**关系。

##### 父级节点 

node.**parentNode** 

1. parentNode 属性可返回某节点的父节点，注意是最近的一个父节点

2. 如果指定的节点没有父节点则返回 null 

3. ```js
   <body>
       <!-- 节点的优点 -->
       <div>我是div</div>
       <span>我是span</span>
       <ul>
           <li>我是li</li>
           <li>我是li</li>
           <li>我是li</li>
           <li>我是li</li>
       </ul>
       <div class="demo">
           <div class="box">
               <span class="erweima">×</span>
           </div>
       </div>
   
       <script>
           // 1. 父节点 parentNode
           var erweima = document.querySelector('.erweima');
           // var box = document.querySelector('.box');
           // 得到的是离元素最近的父级节点(亲爸爸) 如果找不到父节点就返回为 null
           console.log(erweima.parentNode);
       </script>
   </body>
   ```

##### 子节点 

parentNode.**childNodes（标准）**  

1. parentNode.childNodes 返回包含指定节点的子节点的集合，该集合为即时更新的集合。

2. **注意：**

   1. 返回值里面包含了所有的子节点，包括元素节点，文本节点等。
   2. 如果只想要获得里面的元素节点，则需要专门处理。 所以我们**一般不提倡使用childNodes**

3. ```js
   <body>
       <!-- 节点的优点 -->
       <div>我是div</div>
       <span>我是span</span>
       <ul>
           <li>我是li</li>
           <li>我是li</li>
           <li>我是li</li>
           <li>我是li</li>
   
       </ul>
       <ol>
           <li>我是li</li>
           <li>我是li</li>
           <li>我是li</li>
           <li>我是li</li>
       </ol>
   
       <div class="demo">
           <div class="box">
               <span class="erweima">×</span>
           </div>
       </div>
   
       <script>
           // DOM 提供的方法（API）获取
           var ul = document.querySelector('ul');
           var lis = ul.querySelectorAll('li');
           // 1. 子节点  childNodes 所有的子节点 包含 元素节点 文本节点等等
           console.log(ul.childNodes);
           console.log(ul.childNodes[0].nodeType);
           console.log(ul.childNodes[1].nodeType);
           // 2. children 获取所有的子元素节点 也是我们实际开发常用的
           console.log(ul.children);
       </script>
   </body>
   ```

##### 子节点（重要）

parentNode.**children（非标准）** 

arentNode.children 是一个只读属性，返回所有的子元素节点。它只返回子元素节点，其余节点不返回 （**这个是我们重点掌握的**）。

虽然children 是一个非标准，但是得到了各个浏览器的支持，因此我们可以放心使用

parentNode.**firstChild**

firstChild 返回第一个子节点，找不到则返回null。同样，也是包含所有的节点。

parentNode.**lastChild**   

lastChild 返回最后一个子节点，找不到则返回null。同样，也是包含所有的节点。

parentNode.**firstElementChild**  

firstElementChild  返回第一个子元素节点，找不到则返回null。

parentNode.**lastElementChild**   

lastElementChild 返回最后一个子元素节点，找不到则返回null。 

```js
<body>
    <ol>
        <li>我是li1</li>
        <li>我是li2</li>
        <li>我是li3</li>
        <li>我是li4</li>
        <li>我是li5</li>
    </ol>
    <script>
        var ol = document.querySelector('ol');
        // 1. firstChild 第一个子节点 不管是文本节点还是元素节点
        console.log(ol.firstChild);
        console.log(ol.lastChild);
        // 2. firstElementChild 返回第一个子元素节点 ie9才支持
        console.log(ol.firstElementChild);
        console.log(ol.lastElementChild);
        // 3. 实际开发的写法  既没有兼容性问题又返回第一个子元素
        console.log(ol.children[0]);
        console.log(ol.children[ol.children.length - 1]);
    </script>
</body>
```

实际开发中，firstChild 和 lastChild 包含其他节点，操作不方便，而 firstElementChild 和 lastElementChild 又有兼容性问题，那么我们如何获取第一个子元素节点或最后一个子元素节点呢？

**解决方案：**

1. 如果想要第一个子元素节点，可以使用 parentNode.chilren[0] 

2. 如果想要最后一个子元素节点，可以使用 parentNode.chilren[parentNode.chilren.length - 

##### 兄弟节点

node.**nextSibling**

nextSibling 返回当前元素的下一个兄弟元素节点，找不到则返回null。同样，也是包含所有的节点。

node.**previousSibling**

previousSibling 返回当前元素上一个兄弟元素节点，找不到则返回null。同样，也是包含所有的节点。

node.**nextElementSibling** 

nextElementSibling 返回当前元素下一个兄弟元素节点，找不到则返回null。

node.**previousElementSibling**  

previousElementSibling 返回当前元素上一个兄弟节点，找不到则返回null。

```js
<body>
    <div>我是div</div>
    <span>我是span</span>
    <script>
        var div = document.querySelector('div');
        // 1.nextSibling 下一个兄弟节点 包含元素节点或者 文本节点等等
        console.log(div.nextSibling);
        console.log(div.previousSibling);
        // 2. nextElementSibling 得到下一个兄弟元素节点
        console.log(div.nextElementSibling);
        console.log(div.previousElementSibling);
    </script>
</body>
```

##### 创建节点

document.createElement('tagName')

document.createElement() 方法创建由 tagName 指定的 HTML 元素。因为这些元素原先不存在，是根据我们的需求动态生成的，所以我们也称为动态创建元素节点。

node.appendChild(child）

node.appendChild() 方法将一个节点添加到指定父节点的子节点列表末尾。类似于 CSS 里面的 after 伪元素。

node.insertBefore(child, 指定元素)

node.insertBefore() 方法将一个节点添加到父节点的指定子节点前面。类似于 CSS 里面的 before 伪元素。

```js
<body>
    <ul>
        <li>123</li>
    </ul>
    <script>
        // 1. 创建节点元素节点
        var li = document.createElement('li');
        // 2. 添加节点 node.appendChild(child)  node 父级  child 是子级 后面追加元素  类似于数组中的push
        var ul = document.querySelector('ul');
        ul.appendChild(li);
        // 3. 添加节点 node.insertBefore(child, 指定元素);
        var lili = document.createElement('li');
        ul.insertBefore(lili, ul.children[0]);
        // 4. 我们想要页面添加一个新的元素 ： 1. 创建元素 2. 添加元素
    </script>
</body>
```

###### 案例

发布留言：

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
    <style>
        * {
            margin: 0;
            padding: 0;
        }
        
        body {
            padding: 100px;
        }
        
        textarea {
            width: 200px;
            height: 100px;
            border: 1px solid pink;
            outline: none;
            resize: none;
        }
        
        ul {
            margin-top: 50px;
        }
        
        li {
            width: 300px;
            padding: 5px;
            background-color: rgb(245, 209, 243);
            color: red;
            font-size: 14px;
            margin: 15px 0;
        }
    </style>
</head>

<body>
    <textarea name="" id=""></textarea>
    <button>发布</button>
    <ul>

    </ul>
    <script>
        // 1. 获取元素
        var btn = document.querySelector('button');
        var text = document.querySelector('textarea');
        var ul = document.querySelector('ul');
        // 2. 注册事件
        btn.onclick = function() {
            if (text.value == '') {
                alert('您没有输入内容');
                return false;
            } else {
                // console.log(text.value);
                // (1) 创建元素
                var li = document.createElement('li');
                // 先有li 才能赋值
                li.innerHTML = text.value;
                // (2) 添加元素
                // ul.appendChild(li);
                ul.insertBefore(li, ul.children[0]);
            }
        }
    </script>
</body>

</html>
```

##### 删除节点

 node.removeChild(child) 

node.removeChild() 方法从 DOM 中删除一个子节点，返回删除的节点。

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
</head>

<body>
    <button>删除</button>
    <ul>
        <li>熊大</li>
        <li>熊二</li>
        <li>光头强</li>
    </ul>
    <script>
        // 1.获取元素
        var ul = document.querySelector('ul');
        var btn = document.querySelector('button');
        // 2. 删除元素  node.removeChild(child)
        // ul.removeChild(ul.children[0]);
        // 3. 点击按钮依次删除里面的孩子
        btn.onclick = function() {
            if (ul.children.length == 0) {
                this.disabled = true;
            } else {
                ul.removeChild(ul.children[0]);
            }
        }
    </script>
</body>

</html>
```

**案例**

###### 删除留言

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
    <style>
        * {
            margin: 0;
            padding: 0;
        }
        
        body {
            padding: 100px;
        }
        
        textarea {
            width: 200px;
            height: 100px;
            border: 1px solid pink;
            outline: none;
            resize: none;
        }
        
        ul {
            margin-top: 50px;
        }
        
        li {
            width: 300px;
            padding: 5px;
            background-color: rgb(245, 209, 243);
            color: red;
            font-size: 14px;
            margin: 15px 0;
        }
        
        li a {
            float: right;
        }
    </style>
</head>

<body>
    <textarea name="" id=""></textarea>
    <button>发布</button>
    <ul>

    </ul>
    <script>
        // 1. 获取元素
        var btn = document.querySelector('button');
        var text = document.querySelector('textarea');
        var ul = document.querySelector('ul');
        // 2. 注册事件
        btn.onclick = function() {
            if (text.value == '') {
                alert('您没有输入内容');
                return false;
            } else {
                // console.log(text.value);
                // (1) 创建元素
                var li = document.createElement('li');
                // 先有li 才能赋值
                li.innerHTML = text.value + "<a href='javascript:;'>删除</a>";
                // (2) 添加元素
                // ul.appendChild(li);
                ul.insertBefore(li, ul.children[0]);
                // (3) 删除元素 删除的是当前链接的li  它的父亲
                var as = document.querySelectorAll('a');
                for (var i = 0; i < as.length; i++) {
                    as[i].onclick = function() {
                        // node.removeChild(child); 删除的是 li 当前a所在的li  this.parentNode;
                        ul.removeChild(this.parentNode);
                    }
                }
            }
        }
    </script>
</body>

</html>
```

### 三种动态创建元素区别

1. document.write()

2. element.innerHTML

3. document.createElement()

**区别**

1.  document.write 是直接将内容写入页面的内容流，但是文档流执行完毕，则它会导致页面全部重绘

2. innerHTML 是将内容写入某个 DOM 节点，不会导致页面全部重绘

3. innerHTML 创建多个元素效率更高（不要拼接字符串，采取数组形式拼接），结构稍微复杂

4. createElement() 创建多个元素效率稍低一点点，但是结构更清晰

**总结**：不同浏览器下，innerHTML 效率要比 creatElement 高

### 汇总

**创建**

1.document.write

2.innerHTML

3.createElement

**增**

1. appendChild

2. insertBefore

**删**

1. removeChild

**改**

1. 修改元素属性： src、href、title等

2. 修改普通元素内容： innerHTML 、innerText

3. 修改表单元素： value、type、disabled等

4. 修改元素样式： style、className

**查**

1. DOM提供的API 方法： getElementById、getElementsByTagName 古老用法 不太推荐 

2. H5提供的新方法： querySelector、querySelectorAll  提倡

3. 利用节点操作获取元素： 父(parentNode)、子(children)、兄(previousElementSibling、nextElementSibling) 提倡

**属性操作**

1. setAttribute：设置dom的属性值

2. getAttribute：得到dom的属性值

3. removeAttribute移除属性

**事件操作**

![image-20211223230349161](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211223230349161.png)

## 12. Bom

### 窗口加载事件 

```js
window.onload = function(){}
或者 
window.addEventListener("load",function(){});
```

window.onload 是窗口 (页面）加载事件,当文档内容完全加载完成会触发该事件(包括图像、脚本文件、CSS 文件等), 就调用的处理函数。

**注意：**

1. 有了 window.onload 就可以把 JS 代码写到页面元素的上方，因为 onload 是等页面内容全部加载完毕，再去执行处理函数。

2. window.onload 传统注册事件方式 只能写一次，如果有多个，会以最后一个 window.onload 为准。

3. 如果使用 addEventListener 则没有限制

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
    <script>
        // window.onload = function() {
        //     var btn = document.querySelector('button');
        //     btn.addEventListener('click', function() {
        //         alert('点击我');
        //     })
        // }
        // window.onload = function() {
        //     alert(22);
        // }
        window.addEventListener('load', function() {
            var btn = document.querySelector('button');
            btn.addEventListener('click', function() {
                alert('点击我');
            })
        })
        window.addEventListener('load', function() {

            alert(22);
        })
        document.addEventListener('DOMContentLoaded', function() {
                alert(33);
            })
            // load 等页面内容全部加载完毕，包含页面dom元素 图片 flash  css 等等
            // DOMContentLoaded 是DOM 加载完毕，不包含图片 falsh css 等就可以执行 加载速度比 load更快一些
    </script>
</head>

<body>

    <button>点击</button>

</body>

</html>
```

### location 对象

window 对象给我们提供了一个 location 属性用于获取或设置窗体的 URL，并且可以用于解析 URL 。 因为这个属性返回的是一个对象，所以我们将这个属性也称为 location 对象。

统一资源定位符 (Uniform Resource Locator, URL) 是互联网上标准资源的地址。互联网上的每个文件都有一个唯一的 URL，它包含的信息指出文件的位置以及浏览器应该怎么处理它。

URL 的一般语法格式为：

```html
 protocol://host[:port]/path/[?query]#fragment

 http://www.itcast.cn/index.html?name=andy&age=18#link
```

![image-20211223233132731](C:\Users\DELL\AppData\Roaming\Typora\typora-user-images\image-20211223233132731.png)

**location** **对象的属性**

![image-20211223233147704](C:\Users\DELL\AppData\Roaming\Typora\typora-user-images\image-20211223233147704.png)

重点记住： href 和 search

跳转指定接口或者页面

```js
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
</head>

<body>
    <button>点击</button>
    <script>
        location.href="https://www.cnblogs.com/top-housekeeper/p/11865399.html"
    </script>
</body>

</html>
```

