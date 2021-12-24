# JQuery

参考文档：[jQuery HTML / CSS 方法 | 菜鸟教程 (runoob.com)](https://www.runoob.com/jquery/jquery-ref-html.html)

## 1. 介绍

jQuery，就是为了快速方便的操作DOM，里面基本都是函数（方法）。

jQuery 是一个快速、简洁的 JavaScript 库，其设计的宗旨是“write Less，Do More”，即倡导写更少的代码，做更多的事情。

**入口函数**

```js
$(function () {   
    ...  // 此处是页面 DOM 加载完成的入口
 }) ;         
$(document).ready(function(){
   ...  //  此处是页面DOM加载完成的入口
});       
```

1. 等着 DOM 结构渲染完毕即可执行内部代码，不必等到所有外部资源加载完成，jQuery 帮我们完成了封装。
2. 相当于原生 js 中的 DOMContentLoaded。
3. 不同于原生 js 中的 load 事件是等页面文档、外部的 js 文件、css文件、图片加载完毕才执行内部代码。
4. 更推荐使用第一种方式。

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
    <script src="jquery.min.js"></script>
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
        // 1. $ 是jQuery的别称（另外的名字）
        // $(function() {
        //     alert(11)
        // });
        jQuery(function() {
            // alert(11)
            // $('div').hide();
            jQuery('div').hide();
        });
        // 2. $同时也是jQuery的 顶级对象
    </script>
</body>

</html>
```

**jQuery** **的顶级对象** **$**

1. **$**是 jQuery 的别称，在代码中可以使用 jQuery 代替 **$**，但一般为了方便，通常都直接使用 $ 。
2. **$**是jQuery 的顶级对象， 相当于原生JavaScript中的 window。把元素利用$包装成jQuery对象，就可以调用jQuery 的方法。

**jQuery对象和DOM 对象**

1. 用原生 JS 获取来的对象就是 DOM 对象
2. jQuery 方法获取的元素就是 jQuery 对象。
3. jQuery 对象本质是： 利用$对DOM 对象包装后产生的对象（伪数组形式存储）。

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
    <script src="jquery.min.js"></script>
    <style>
        div {
            width: 100px;
            height: 100px;
            background-color: pink;
        }
    </style>
</head>

<body>
    <div></div>
    <span></span>
    <script>
        // 1. DOM 对象：  用原生js获取过来的对象就是DOM对象
        var myDiv = document.querySelector('div'); // myDiv 是DOM对象
        var mySpan = document.querySelector('span'); // mySpan 是DOM对象
        console.dir(myDiv);
        // 2. jQuery对象： 用jquery方式获取过来的对象是jQuery对象。 本质：通过$把DOM元素进行了包装
        $('div'); // $('div')是一个jQuery 对象
        $('span'); // $('span')是一个jQuery 对象
        console.dir($('div'));
        // 3. jQuery 对象只能使用 jQuery 方法，DOM 对象则使用原生的 JavaScirpt 属性和方法
        // myDiv.style.display = 'none';
        // myDiv.hide(); myDiv是一个dom对象不能使用 jquery里面的hide方法
        // $('div').style.display = 'none'; 这个$('div')是一个jQuery对象不能使用原生js 的属性和方法
    </script>
</body>

</html>
```

**注意**

只有 jQuery 对象才能使用 jQuery 方法，DOM 对象则使用原生的 JavaScirpt 方法。

**DOM 对象与 jQuery 对象之间是可以相互转换的。**

因为原生js 比 jQuery 更大，原生的一些属性和方法 jQuery没有给我们封装. 要想使用这些属性和方法需要把jQuery对象转换为DOM对象才能使用。

1. DOM 对象转换为 jQuery 对象： $(DOM对象)
   1. $('div')     
2. . jQuery 对象转换为 DOM 对象（两种方式）
   1. $('div') [index]    index 是索引号      
   2. $('div') .get(index)  index 是索引号   

## 2. **jQuery** 基础选择器

原生 JS 获取元素方式很多，很杂，而且兼容性情况不一致，因此 jQuery 给我们做了封装，使获取元素统一标准。

$(“选择器”)  // 里面选择器直接写 CSS 选择器即可，但是要加引号  

**基础选择器** 

![image-20211223234158269](C:\Users\DELL\AppData\Roaming\Typora\typora-user-images\image-20211223234158269.png)

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
    <script src="jquery.min.js"></script>
</head>

<body>
    <div>我是div</div>
    <div class="nav">我是nav div</div>
    <p>我是p</p>
    <ol>
        <li>我是ol 的</li>
        <li>我是ol 的</li>
        <li>我是ol 的</li>
        <li>我是ol 的</li>
    </ol>
    <ul>
        <li>我是ul 的</li>
        <li>我是ul 的</li>
        <li>我是ul 的</li>
        <li>我是ul 的</li>
    </ul>
    <script>
        $(function() {
            console.log($(".nav"));
            console.log($("ul li"));

        })
    </script>
</body>

</html>
```



## 3. **jQuery** 样式操作

jQuery 可以使用 css 方法来修改简单元素样式； 也可以操作类，修改多个样式。

1. 参数只写属性名，则是返回属性值
   1. $(this).css(''color'');
2. 参数是属性名，属性值，逗号分隔，是设置一组样式，属性必须加引号，值如果是数字可以不用跟单位和引号
   1. $(this).css(''color'', ''red'');
3. 参数可以是对象形式，方便设置多组样式。属性名和属性值用冒号隔开， 属性可以不用加引号
   1. $(this).css({ "color":"white","font-size":"20px"});
4. 添加类
   1. $(“div”).addClass(''current'');
5. 移除类
   1. $(“div”).removeClass(''current'');
6. 切换类
   1. $(“div”).toggleClass(''current'');

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
    <script src="jquery.min.js"></script>
</head>

<body>
    <div>惊喜不，意外不</div>
    <div>惊喜不，意外不</div>
    <div>惊喜不，意外不</div>
    <div>惊喜不，意外不</div>
    <ul>
        <li>相同的操作</li>
        <li>相同的操作</li>
        <li>相同的操作</li>
    </ul>
    <script>
        // 1. 获取四个div元素 
        console.log($("div"));

        // 2. 给四个div设置背景颜色为粉色 jquery对象不能使用style
        $("div").css("background", "pink");
        // 3. 隐式迭代就是把匹配的所有元素内部进行遍历循环，给每一个元素添加css这个方法
        $("ul li").css("color", "red");
    </script>
</body>

</html>
```

## 4. **设置或获取元素固有属性值** prop()

1. 获取属性语法
   1. prop(''属性'')
2. 设置属性语法
   1. prop(''属性'', ''属性值'')
3. 获取属性语法
   1. attr(''属性'')   // 类似原生 getAttribute()
   2. attr(''属性'', ''属性值'')  // 类似原生 setAttribute()

```html

<script>
$(document).ready(function(){
	$("button").click(function(){
		$("img").attr("width","500");
	});
});
</script>

<body>

<img src="img_pulpitrock.jpg" alt="Pulpit Rock" width="284" height="213">
<br>
<button>为图片设置width属性</button>

</body>

```

## 5. **jQuery** **内容文本值**

1. **普通元素内容** **html()（ 相当于原生inner HTML)**
   1. html()       // 获取元素的内容

   2. html(''内容'')  // 设置元素的内容

   3. ```html
      <script>
      $(document).ready(function(){
      	$("button").click(function(){
      		$("p").html("Hello <b>world!</b>");
      	});
      });
      </script>
      
      <body>
      
      <button>修改所有P元素的内容</button>
      <p>这是一个段落。</p>
      <p>这是另一个段落。</p>
      
      </body>
      ```

2. **普通元素文本内容** **text()  (相当与原生 innerText)**
   1. text()           // 获取元素的文本内容

   2. text(''文本内容'')  // 设置元素的文本内容

   3. ```html
      <script>
      $(document).ready(function(){
      	$("button").click(function(){
      		$("p").text("Hello world!");
      	});
      });
      </script>
      
      <body>
      
      <button>设置所有p元素的文本内容</button>
      <p>这是一个段落。</p>
      <p>这是另一个段落。</p>
      
      </body>
      ```

3. **表单的值val()（ 相当于原生value)**
   1. val()       // 获取表单的值

   2. val(''内容'')  // 设置表单的值

   3. ```html
      <script>
      $(document).ready(function(){
      	$("button").click(function(){
      		$("input:text").val("Glenn Quagmire");
      	});
      });
      </script>
      
      <body>
      
      <p>名称: <input type="text" name="user"></p>
      <button>设置输入字段的值</button>
      
      </body>
      ```

## 6. **jQuery** **元素操作**

jQuery 隐式迭代是对同一类元素做了同样的操作。 如果想要给同一类元素做不同操作，就需要用到遍历。

1. **遍历元素：**
   1. $("div").each(function (index, domEle) { xxx; }）    
   
2. **内部添加：**

   1. element.append(''内容'')   //把内容放入匹配元素内部最后面，类似原生 appendChild。
   2. element.prepend(''内容'')   //把内容放入匹配元素内部最前面。
   3. 内部添加元素，生成之后，它们是父子关系

3. **外部添加**

   1. element.after(''内容'')    // 把内容放入目标元素后面
   2. element.before(''内容'')   // 把内容放入目标元素前面 

   3. 外部添加元素，生成之后，他们是兄弟关系

4. **删除元素**
   1. element.remove()  // 删除匹配的元素（本身）
   2. element.empty()  //  删除匹配的元素集合中所有的子节点
   3. element.html('''')  //  清空匹配的元素内容
   4. remove 删除元素本身。
   5. empt() 和 html('''') 作用等价，都可以删除元素里面的内容，只不过 html 还可以设置内容。

## 实践

### Blog框架搭建

1. 创建WEB项目
2. 数据库建表建库
3. 登录模块

### Blog登录

1. 获取验证码

   1. ```js
      $(function () {
          window.onload = function () {
              //点击图片切换
              //获取图片
              $("img").click(function () {
                  let date = new Date().getTime();
                  $(this).attr("src","/aishangboke/checkCodeServlet?date="+date);
              })
              $(".identifying-change").click(function () {
                  let date = new Date().getTime();
                  $("img").attr("src","/aishangboke/checkCodeServlet?date="+date);
              })
          }
      })
      ```

2. 用户登录

3. 跳转到主页

