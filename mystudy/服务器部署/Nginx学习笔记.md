# Nginx学习

# 学习资料

# Nginx安装

**安装步骤**

1. 查看可用的nginx文件
   1. yum list /grep nginx
   2. ![image-20211107114623712](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211107114623712.png)
2. 安装nginx
   1. yum install nginx
   2. ![image-20211107114655290](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211107114655290.png)
3. 查看版本
   1. nginx -v
   2. ![image-20211107114721761](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211107114721761.png)

## 问题

## 1. default.d和nginx.conf的关系

default.d是nginx.conf的子配置，可以针对不同的项目设置不同的xxx.d，然后使用同一nginx代理到不同的地方。



