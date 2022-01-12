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

**jQuery** **的顶级对象** **$**

1. **$**是 jQuery 的别称，在代码中可以使用 jQuery 代替 **$**，但一般为了方便，通常都直接使用 $ 。
2. **$**是jQuery 的顶级对象， 相当于原生JavaScript中的 window。把元素利用$包装成jQuery对象，就可以调用jQuery 的方法。

**jQuery对象和DOM 对象**

1. 用原生 JS 获取来的对象就是 DOM 对象
2. jQuery 方法获取的元素就是 jQuery 对象。
3. jQuery 对象本质是： 利用$对DOM 对象包装后产生的对象（伪数组形式存储）。

**注意**

只有 jQuery 对象才能使用 jQuery 方法，DOM 对象则使用原生的 JavaScirpt 方法。

**DOM 对象与 jQuery 对象之间是可以相互转换的。**

因为原生js 比 jQuery 更大，原生的一些属性和方法 jQuery没有给我们封装. 要想使用这些属性和方法需要把jQuery对象转换为DOM对象才能使用。

1. DOM 对象转换为 jQuery 对象： $(DOM对象)
   1. $('div')     
2. . jQuery 对象转换为 DOM 对象（两种方式）
   1. $('div') [index]    index 是索引号      
   2. $('div') .get(index)  index 是索引号   

**安装**

[jQuery 安装 | 菜鸟教程 (runoob.com)](https://www.runoob.com/jquery/jquery-install.html)

1. 使用国内cdn

   1. ```html
      <head>
       <script src="https://cdn.jsdelivr.net/npm/jquery@3.5.1/dist/jquery.slim.min.js"
              integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj"
              crossorigin="anonymous"></script>
      <script>
          
      //方法一    
      $(document).ready(function(){
        $("button").click(function(){
          $("p").hide();
        });
      });
      //方法二
      $(function () {   
         $("button").click(function(){
          $("p").hide();
        });
       }) ; 
      </script>
      </head>
      <body>
          <p>123456</p>
          <button>按钮</button>
      </body>
      ```

2. 使用本地安装包


## 2. **jQuey** 基础选择器

[jQuery 选择器 | 菜鸟教程 (runoob.com)](https://www.runoob.com/jquery/jquery-selectors.html)

原生 JS 获取元素方式很多，很杂，而且兼容性情况不一致，因此 jQuery 给我们做了封装，使获取元素统一标准。

$(“选择器”)  // 里面选择器直接写 CSS 选择器即可，但是要加引号  

**基础选择器** 

[jQuery 选择器 | 菜鸟教程 (runoob.com)](https://www.runoob.com/jquery/jquery-ref-selectors.html)

![image-20211223234158269](C:\Users\DELL\AppData\Roaming\Typora\typora-user-images\image-20211223234158269.png)

1. [jQuery #id 选择器 | 菜鸟教程 (runoob.com)](https://www.runoob.com/jquery/jq-sel-id.html)
2. [jQuery * 选择器 | 菜鸟教程 (runoob.com)](https://www.runoob.com/jquery/jq-sel-all.html)
3. [jQuery .class 选择器 | 菜鸟教程 (runoob.com)](https://www.runoob.com/jquery/jq-sel-class.html)

## 3. **jQuery** 样式操作

[jQuery 获取并设置 CSS 类 | 菜鸟教程 (runoob.com)](https://www.runoob.com/jquery/jquery-css-classes.html)

jQuery 可以使用 css 方法来修改简单元素样式； 也可以操作类，修改多个样式。

1. 参数只写属性名，则是返回属性值
   1. $(this).css(''color'');
2. 参数是属性名，属性值，逗号分隔，是设置一组样式，属性必须加引号，值如果是数字可以不用跟单位和引号
   1. $(this).css(''color'', ''red'');
3. 参数可以是对象形式，方便设置多组样式。属性名和属性值用冒号隔开， 属性可以不用加引号
   1. $(this).css({ "color":"white","font-size":"20px"});
4. https://www.runoob.com/jquery/css-css.html

1. 添加类
   1. 向被选元素添加一个或多个类名
   2. $(“div”).addClass(''current'');
   3. https://www.runoob.com/jquery/html-addclass.html
2. 移除类
   1. 从被选元素移除一个或多个类
   2. $(“div”).removeClass(''current'');
   3. [removeClass()](https://www.runoob.com/jquery/html-removeclass.html)
3. 切换类
   1. 在被选元素中添加/移除一个或多个类之间切换
   2. $(“div”).toggleClass(''current'');
   3. [toggleClass()](https://www.runoob.com/jquery/html-toggleclass.html)

## 4. **设置或获取元素固有属性值** prop()

1. 获取属性语法
   1. prop(''属性'')
   2. prop(''属性'', ''属性值'')
   3. [prop()](https://www.runoob.com/jquery/html-prop.html)
   4. [removeProp()](https://www.runoob.com/jquery/html-removeprop.html)
2. 获取属性语法
   1. attr(''属性'')   // 类似原生 getAttribute()
   2. attr(''属性'', ''属性值'')  // 类似原生 setAttribute()
   2. [attr()](https://www.runoob.com/jquery/html-attr.html)
   2. [removeAttr()](https://www.runoob.com/jquery/html-removeattr.html)

## 5. **jQuery** **内容文本值**

获取内容

[jQuery 获取内容和属性 | 菜鸟教程 (runoob.com)](https://www.runoob.com/jquery/jquery-dom-get.html)

1. **普通元素内容** **html()（ 相当于原生inner HTML)**
   
   1. html()       // 获取元素的内容
   
   2. html(''内容'')  // 设置元素的内容
   
   3. [html()](https://www.runoob.com/jquery/html-html.html)
   
2. **普通元素文本内容** **text()  (相当与原生 innerText)**
   1. text()           // 获取元素的文本内容
   2. text(''文本内容'')  // 设置元素的文本内容
   3. [text()](https://www.runoob.com/jquery/html-text.html)

3. **表单的值val()（ 相当于原生value)**
   1. val()       // 获取表单的值

   2. val(''内容'')  // 设置表单的值

   3. [val()](https://www.runoob.com/jquery/html-val.html)
   
   4. ```html
      <!DOCTYPE html>
      <html>
      <head>
      <meta charset="utf-8">
      <title>菜鸟教程(runoob.com)</title>
      <script src="https://cdn.staticfile.org/jquery/1.10.2/jquery.min.js">
      </script>
      <script>
      $(document).ready(function(){
      	$("button").click(function(){
      		$(this).text($("input:text").val())
      	});
      });
      </script>
      </head>
      <body>
      
      <p>名称: <input type="text" name="user"></p>
      	<p class="pl"></p>
      <button>设置输入字段的值</button>
      
      </body>
      </html>
      ```

## 6. **jQuery** **元素操作**

jQuery 隐式迭代是对同一类元素做了同样的操作。 如果想要给同一类元素做不同操作，就需要用到遍历。

1. **遍历元素：**
   1. $("div").each(function (index, domEle) { xxx; }）    
   
   1. [each()](https://www.runoob.com/jquery/traversing-each.html)
   
   1. ```js
      <!DOCTYPE html>
      <html>
      <head>
      <meta charset="utf-8">
      <title>菜鸟教程(runoob.com)</title>
      <script src="https://cdn.staticfile.org/jquery/1.10.2/jquery.min.js">
      </script>
      <script>
      $(document).ready(function(){
      
      	$("li").each(function(){
      		$(this).click(function(){
      		alert($(this).text())
      		})
      		});
      });
      </script>
      </head>
      <body>
      
      <button>输出每个列表项的值</button>
      <ul>
      <li>Coffee</li>
      <li>Milk</li>
      <li>Soda</li>
      </ul>
      
      </body>
      </html>
      ```
   
2. **内部添加：**
   1. element.append(''内容'')   //把内容放入匹配元素内部最后面，类似原生 appendChild。
   
   2. element.prepend(''内容'')   //把内容放入匹配元素内部最前面。
   
   3. 内部添加元素，生成之后，它们是父子关系
   
   4. [jQuery 添加元素 | 菜鸟教程 (runoob.com)](https://www.runoob.com/jquery/jquery-dom-add.html)
   
   5. ```js
      <!DOCTYPE html>
      <html>
      <head>
      <meta charset="utf-8">
      <script src="https://cdn.staticfile.org/jquery/1.10.2/jquery.min.js">
      </script>
      <script>
      $(document).ready(function(){
        $("#btn1").click(function(){
          $("p").append("jdhfkjsdkfsdk");
        });
      
        $("#btn2").click(function(){
          $("ol").append("<li>追加列表项<button>这是按钮</button></li>");
        });
      });
      </script>
      </head>
      
      <body>
      <p>这是一个段落。</p>
      <p>这是另外一个段落。</p>
      <ol>
      <li>List item 1</li>
      <li>List item 2</li>
      <li>List item 3</li>
      </ol>
      <button id="btn1">添加文本</button>
      <button id="btn2">添加列表项</button>
      </body>
      </html>
      ```
   
3. **外部添加**
   
   1. element.after(''内容'')    // 把内容放入目标元素后面
   2. element.before(''内容'')   // 把内容放入目标元素前面 
   3. 外部添加元素，生成之后，他们是兄弟关系
   3. [jQuery 添加元素 | 菜鸟教程 (runoob.com)](https://www.runoob.com/jquery/jquery-dom-add.html)
   
4. **删除元素**
   1. element.remove()  // 删除匹配的元素（本身）
   2. element.empty()  //  删除匹配的元素集合中所有的子节点
   3. element.html('''')  //  清空匹配的元素内容
   4. remove 删除元素本身。
   5. empt() 和 html('''') 作用等价，都可以删除元素里面的内容，只不过 html 还可以设置内容。
   5. [jQuery 删除元素 | 菜鸟教程 (runoob.com)](https://www.runoob.com/jquery/jquery-dom-remove.html)

# BootStrap

1. 安装：[简介 · Bootstrap v4 中文文档 v4.6 | Bootstrap 中文网 (bootcss.com)](https://v4.bootcss.com/docs/getting-started/introduction/)

   1. ```javascript
                                     jquery
      <script src="https://cdn.jsdelivr.net/npm/jquery@3.5.1/dist/jquery.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
      
                                     bootstrap
      <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-fQybjgWLrvvRgtW6bFlB7jaZrFsaBXjsOMm/tB9LTS58ONXgqbR9W8oWht/amnpF" crossorigin="anonymous"></script>
      
                                     css
      <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css" integrity="sha384-zCbKRCUGaJDkqS1kPbPd7TveP5iyJE0EjAuZQTgFLD2ylzuqKfdKlfG/eSrtxUkn" crossorigin="anonymous">
      
      ```

      1. 方便，但是太慢

   2. 本地下载

         1. ```js
                   <link rel="stylesheet" href="../resource/bootstrap-4.6.1-dist/css/bootstrap.min.css">
                   </script>
               
                   <script src="../resource/jquery-3.4.1.min压缩文档.js"></script>
               
                   <script src="../resource/bootstrap-4.6.1-dist/js/bootstrap.min.js"></script>
               ```

2. 表格：[Tables · Bootstrap v4 中文文档 v4.6 | Bootstrap 中文网 (bootcss.com)](https://v4.bootcss.com/docs/content/tables/)

3. 按钮：[按钮（Buttons） · Bootstrap v4 中文文档 v4.6 | Bootstrap 中文网 (bootcss.com)](https://v4.bootcss.com/docs/components/buttons/)

4. 下拉菜单：[Dropdowns · Bootstrap v4 中文文档 v4.6 | Bootstrap 中文网 (bootcss.com)](https://v4.bootcss.com/docs/components/dropdowns/)

5. 表单：[Forms · Bootstrap v4 中文文档 v4.6 | Bootstrap 中文网 (bootcss.com)](https://v4.bootcss.com/docs/components/forms/)

6. 分页：[Pagination · Bootstrap v4 中文文档 v4.6 | Bootstrap 中文网 (bootcss.com)](https://v4.bootcss.com/docs/components/pagination/)

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
                  $(this).attr("src","/blog_system/checkCodeServlet?date="+date);
              })
              $(".identifying-change").click(function () {
                  let date = new Date().getTime();
                  $("img").attr("src","/blog_system/checkCodeServlet?date="+date);
              })
          }
      })
      ```

2. 用户登录

3. 跳转到主页

