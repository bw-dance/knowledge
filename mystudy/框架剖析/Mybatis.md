## 操作流程

1. 读取mybatis配置文件进内存
2. 获取SqlSessionFactory对象（单例）
3. 通过SqlSessionFactory获取对应的SqlSession对象（每次会话，创建一个SqlSession对象）
   1. SqlSessionFactory
      1. openSession()：外界提供SqlSession对象
4. 通过SqlSession对象完成对应的数据库操作  namespace + “.” +id

**整体架构**

![image-20211225204017997](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211225204017997.png)