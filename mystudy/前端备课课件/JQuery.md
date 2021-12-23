# JQuery

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

## 2. **jQuery** 基础选择器

原生 JS 获取元素方式很多，很杂，而且兼容性情况不一致，因此 jQuery 给我们做了封装，使获取元素统一标准。

$(“选择器”)  // 里面选择器直接写 CSS 选择器即可，但是要加引号  

**基础选择器** 

![image-20211223234158269](C:\Users\DELL\AppData\Roaming\Typora\typora-user-images\image-20211223234158269.png)

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

## 4. **设置或获取元素固有属性值** prop()

1. 获取属性语法
   1. prop(''属性'')
2. 设置属性语法
   1. prop(''属性'', ''属性值'')
3. 获取属性语法
   1. attr(''属性'')   // 类似原生 getAttribute()
   2. attr(''属性'', ''属性值'')  // 类似原生 setAttribute()

## 5. **jQuery** **内容文本值**

1. **普通元素内容** **html()（ 相当于原生inner HTML)**
   1. html()       // 获取元素的内容
   2. html(''内容'')  // 设置元素的内容
2. **普通元素文本内容** **text()  (相当与原生 innerText)**
   1. text()           // 获取元素的文本内容
   2. text(''文本内容'')  // 设置元素的文本内容

3. **表单的值val()（ 相当于原生value)**
   1. val()       // 获取表单的值
   2. val(''内容'')  // 设置表单的值

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