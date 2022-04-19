# Docker基础篇

# 学习资料

docker官网：[Empowering App Development for Developers | Docker](https://www.docker.com/)

docker容器：[Docker Hub](https://hub.docker.com/)

docker文档：[Docker Documentation | Docker Documentation](https://docs.docker.com/)

docker教程：[docker教程_docker使用快速入门-php中文网](https://www.php.cn/docker/)

狂神笔记：[(61条消息) 狂神说docker(最全笔记）_烟霞畔的博客-CSDN博客_狂神说docker笔记](https://blog.csdn.net/qq_21197507/article/details/115071715)[(61条消息) 狂神说docker(最全笔记）_烟霞畔的博客-CSDN博客_狂神说docker笔记](https://blog.csdn.net/qq_21197507/article/details/115071715)

docker容器常见故障：https://blog.csdn.net/qq_41958579/article/details/107927140

学习规划(https://www.processon.com/mindmap/61872e0b0791293fb6c57d95)](https://www.processon.com/mindmap/61872e0b0791293fb6c57d95)

# 学习内容

1. Docker概述
2. Docker安装
3. Docker命令
   1. 操作命令
   2. 容器命令
   3. 操作命令
   4. .....
4. Dcoker镜像！
5. 容器数据卷！
6. DockerFile
7. Docker网络原理
8. Idea整合Docker
9. Docker Compose
10. Docker Swarm（简化版k8s）
11. CI\CD  Jenkins 

# Docker概述

## Docker出现的原因

1. 部署项目

   1. 之前部署项目：
      1. 开发人员生成jar包，运维人员部署环境（redis，mysql，tomcat等等），项目不能带上环境打包。
      2. 缺点：
         1. 环境配置复杂，耗时长，稍有不慎，甚至需要重新部署
         2. 在不同的系统上部署相同的项目，每个项目都需要配置一套环境，麻烦

   2. 利用docker部署项目：
      1. 开发人员打jar包时带上环境，直接生成jar包和环境的镜像，运维人员直接下载使用即可。
      2. 优点：
         1. 项目的开发和运行使用同一套环境（都是开发人员配置的），不会出现在我电脑上能用，在你电脑上不能使用的情况。
         2. 打包后的镜像就像应用商店一样，运维人员直接下载部署集合，不需要再进行复杂的环境配置。
         3. 在不同的服务器上部署项目，只需要配好一套镜像，在所有服务器上直接下载使用即可，不需要在每个服务器上都重新部署一套
         4. docker隔离机制，发挥服务器的最大性能
            1. 如果直接在linux上部署项目，如果有多个项目，很容易出现部署冲突（如端口占用等等），使用docker，将linux分成一个个小的linux的docker容器，每一个docker都是迷你版的linux，每一个docker里面部署各自的项目，将linux服务器的资源发挥到最大，并且可以减少部署冲突的问题。

## Docker 历史

docker基于go语言开发。

虚拟机：在window中安装一个vw，通过这个软件我们可以虚拟出来一台或者多台电脑！笨重！

虚拟机也是徐继华技术，Docker容器技术，也是一种虚拟化技术。

vm： linux+centos原生镜像（相当于一台电脑）  使用隔离，需要开启多个虚拟机！  一个虚拟机几个G，开启需要几分钟。

docker：使用隔离时，采用镜像（小的linux，只包含最核心功能，只有及M）小巧，运行镜像就可以！

## Docker能干什么

### **传统虚拟机技术**

模拟了一台电脑，内存非常大。

![image-20211104155003201](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211104155003201.png)

**缺点：**

1. 资源占用多
2. 冗余步骤多
3. 开多个服务很多



### **容器化技术：**

![image-20211104155321506](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211104155321506.png)

**比较docker与虚拟机**

1. 传统虚拟机，虚拟出一条硬件，运行一个完整的操作系统，然后在这个系统上安装和运行软件。
2. 容器内的应用直接运行在宿主机的内核，容器灭有自己的内容，也没有虚拟我们的硬件，比较轻便。
3. 每个容器间相互隔离，每个容器内都有一个数据自己的文件系统，互不影响。

### Docker 的优势

1. **应用更快速的交付和部署**
   1. 传统：一堆帮助文档，安装程序
   2. docker：打包镜像，发布测试和运行。测试环境和运行环境都是用一套配置

2. **更便捷的升级和扩缩容**
   1. 假如说要进行mysql，redis，springboot的升级，我们只需要将打包的jar和相应环境下载运行即可，而不需要针对各个环境进行升级。
   2. 如果我们一个服务器不够用，要用多台服务器部署我们的项目，我们直接下载使用同一套docker配置即可。
3. **更简单的系统运维**
   1. 在容器化之后，我们的开发，测试环境都是高度一致
4. **更高效的计算资源利用**
   1. Dcoker是内核级别的虚拟化，可以在一个物理机上运行很多容器示例，服务器的性能可以压榨到极致。

# Docker安装

## Docker的基本组成

![image-20211104161349242](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211104161349242.png)

client去启动容器，容器去远程仓库中下载相关静像，在容器中创建这些镜像的示例（可以理解成配置静像）

**静像（Image）：**

docker静像就好比一个模板，通过模板来创建容器服务。  tomcat静像---run --- tomcat1容器（提供服务器），通过这个静像可以创建多个容器（最终服务运行或者项目运行就在这个容器中）。

**容器：**

docker利用容器技术，独立运行一个或者一组应用，通过静像来创建的。

包含启动，停止，删除，等基本命令。

这个容器可以理解成一个简易的linux系统

**仓库：**

仓库就是存放静像的地方！分为公有仓库（DockerHub国外的）和私有仓库（私人仓库）。

阿里云，华为云等都有容器服务器（配置镜像下载加速，因为dockerhub是国外的，下载慢）。

**docker主机：**

安装了Docker程序的机器（Docker直接安装在了操作系统之上）

**docker客户端：**

链接docker主机进行操作

## Docker安装

### 系统配置

```yml
# 系统内核 要求linux系统版本高于3.10
[root@ZHQ ~]# uname -r
3.10.0-1127.19.1.el7.x86_64
# 系统版本
[root@ZHQ ~]# cat /etc/os-release
NAME="CentOS Linux"
VERSION="7 (Core)"
ID="centos"
ID_LIKE="rhel fedora"
VERSION_ID="7"
PRETTY_NAME="CentOS Linux 7 (Core)"
ANSI_COLOR="0;31"
CPE_NAME="cpe:/o:centos:centos:7"
HOME_URL="https://www.centos.org/"
BUG_REPORT_URL="https://bugs.centos.org/"

CENTOS_MANTISBT_PROJECT="CentOS-7"
CENTOS_MANTISBT_PROJECT_VERSION="7"
REDHAT_SUPPORT_PRODUCT="centos"
REDHAT_SUPPORT_PRODUCT_VERSION="7"

```

### 安装步骤

[CentOS Docker 安装 | 菜鸟教程 (runoob.com)](https://www.runoob.com/docker/centos-docker-install.html)

使用docker：

![image-20211104211520552](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211104211520552.png)

查看已安装的docker镜像

docker images

刚刚安装的hello world 镜像

![image-20211104211628815](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211104211628815.png)

### 安装阿里云静像

如果没有阿里云镜像，我们下载相关配置的驱动时会从外网上下载，速度非常的慢。

[https://cr.console.aliyun.com/cn-hangzhou/instances/mirrorshttps://cr.console.aliyun.com/cn-hangzhou/instances/mirrors](https://cr.console.aliyun.com/cn-hangzhou/instances/mirrors)

第一步：登陆阿里云，找到容器镜像服务，进入到控制台

​        ![image-20211107150557255](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211107150557255.png)        

第二步：获取加速器地址

​           ![image-20211107150634403](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211107150634403.png)        

第三步：配置使用

​          ![](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211107150649879.png)        

将EOF前的内容（包含EOF）在控制台输入，之后再分别执行下面两句话。

# Docker原理

## Docker Run原理

使用docker run hello-world指令后所进行的操作。

![image-20211104212016151](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211104212016151.png)

## Docker 是如何工作的

Docker是一个Client-Server架构的系统，Docker的守护进程运行在主机上。通过Scoket从客户端进行访问！

DockerServer接收到Docker client的指令，就会执行这个命令1

![image-20211104212929841](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211104212929841.png)

## Docker 为什么比VM快

1. Docker有着比虚拟机更少的抽象层。
2. Docker利用的是宿主机的内核，vm需要时Guest os（比较大，有他才能运行）

![image-20211104213052301](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211104213052301.png)

新建容器的时候，docker不需要像虚拟机一样新建一个操作系统内核。虚拟机加载Guest os，分钟级别。而docker使用了宿主机的操作系统，秒级！

# Docker常用命令解析

![image-20211105091047321](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211105091047321.png)

## 帮助命令

```` yml
docker version  # docker版本信息
docker info     # 系统级别的信息，包括镜像和容器的数量
docker 命令 --help 
````

- [**帮助文档**](https://docs.docker.com/engine/reference/commandline/docker/)

## 镜像命令

**docker images 查看所有本地主机上的镜像**

``` yml
[root@iZ2zeg4ytp0whqtmxbsqiiZ ~]# docker images
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
hello-world         latest              bf756fb1ae65        7 months ago        13.3kB
 
# 解释
REPOSITORY      # 镜像的仓库
TAG             # 镜像的标签
IMAGE ID        # 镜像的ID
CREATED         # 镜像的创建时间
SIZE            # 镜像的大小
 
# 可选项
--all , -a      # 列出所有镜像
--quiet , -q    # 只显示镜像的id
```

**docker search 查找镜像**

``` yml
NAME                              DESCRIPTION                                     STARS               OFFICIAL         AUTOMATED
mysql                             MySQL is a widely used, open-source relation…   9822                [OK]                
mariadb                           MariaDB is a community-developed fork of MyS…   3586                [OK]                
mysql/mysql-server                Optimized MySQL Server Docker images. Create…   719                                     [OK]
 
# 可选项
--filter=STARS=3000     # 搜素出来的镜像就是STARS大于3000的
 
[root@iZ2zeg4ytp0whqtmxbsqiiZ ~]# docker search mysql --filter=STARS=3000
NAME                DESCRIPTION                                     STARS               OFFICIAL            AUTOMATED
mysql               MySQL is a widely used, open-source relation…   9822                [OK]                
mariadb             MariaDB is a community-developed fork of MyS…   3586    
```

**docker pull 下拉镜像**

``` yml
# 下载镜像，docker pull 镜像名[:tag]
[root@iZ2zeg4ytp0whqtmxbsqiiZ ~]# docker pull mysql
Using default tag: latest           # 如果不写tag，默认就是latest
latest: Pulling from library/mysql
bf5952930446: Pull complete         # 分层下载，dockerimages的核心，联合文件系统
8254623a9871: Pull complete 
938e3e06dac4: Pull complete 
ea28ebf28884: Pull complete 
f3cef38785c2: Pull complete 
894f9792565a: Pull complete 
1d8a57523420: Pull complete 
6c676912929f: Pull complete 
ff39fdb566b4: Pull complete 
fff872988aba: Pull complete 
4d34e365ae68: Pull complete 
7886ee20621e: Pull complete 
Digest: sha256:c358e72e100ab493a0304bda35e6f239db2ec8c9bb836d8a427ac34307d074ed     # 签名
Status: Downloaded newer image for mysql:latest
docker.io/library/mysql:latest      # 真实地址
 
# 等价于
docker pull mysql
docker pull docker.io/library/mysql:latest
 
# 指定版本下载
[root@iZ2zeg4ytp0whqtmxbsqiiZ ~]# docker pull mysql:5.7
5.7: Pulling from library/mysql
bf5952930446: Already exists 
8254623a9871: Already exists 
938e3e06dac4: Already exists 
ea28ebf28884: Already exists 
f3cef38785c2: Already exists 
894f9792565a: Already exists 
1d8a57523420: Already exists 
5f09bf1d31c1: Pull complete 
1b6ff254abe7: Pull complete 
74310a0bf42d: Pull complete 
d398726627fd: Pull complete 
Digest: sha256:da58f943b94721d46e87d5de208dc07302a8b13e638cd1d24285d222376d6d84
Status: Downloaded newer image for mysql:5.7
docker.io/library/mysql:5.7
 
# 查看本地镜像
[root@iZ2zeg4ytp0whqtmxbsqiiZ ~]# docker images
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
mysql               5.7                 718a6da099d8        6 days ago          448MB
mysql               latest              0d64f46acfd1        6 days ago          544MB
hello-world         latest              bf756fb1ae65        7 months ago        13.3kB
```



**docker rmi 删除镜像**

``` yml
[root@iZ2zeg4ytp0whqtmxbsqiiZ ~]# docker rmi -f IMAGE ID                        # 删除指定镜像
[root@iZ2zeg4ytp0whqtmxbsqiiZ ~]# docker rmi -f IMAGE ID1 IMAGE ID2 IMAGE ID3   # 删除多个镜像
[root@iZ2zeg4ytp0whqtmxbsqiiZ ~]#  docker rmi -f $(docker images -aq)           # 删除所有镜像
```

## 容器命令

**说明： 我们有了镜像才可创建容器，linux，下载一个centos镜像来测试学习**

```
docker pull centos
```

**新建容器并启动**

``` yml
docker run [可选参数] image
 
# 参数说明
--name=“Name”   容器名字    tomcat01    tomcat02    用来区分容器
-d      后台方式运行
-it     使用交互方式运行，进入容器查看内容
-p      指定容器的端口     -p 8080:8080   
    -p  ip:主机端口：容器端口
    -p  主机端口：容器端口（常用）
    -p  容器端口
    容器端口
-p      随机指定端口
 
 
# 测试，启动并进入容器
[root@iZ2zeg4ytp0whqtmxbsqiiZ ~]# docker run -it centos /bin/bash zhuo
[root@74e82b7980e7 /]# ls   # 查看容器内的centos，基础版本，很多命令是不完善的
bin  etc   lib    lost+found  mnt  proc  run   srv  tmp  var
dev  home  lib64  media       opt  root  sbin  sys  usr
 
# 从容器中退回主机
[root@77969f5dcbf9 /]# exit
exit
[root@iZ2zeg4ytp0whqtmxbsqiiZ /]# ls
bin   dev  fanfan  lib    lost+found  mnt  proc  run   srv  tmp  var
boot  etc  home    lib64  media       opt  root  sbin  sys  usr
```

**列出所有的运行的容器**

``` yml
# docker ps 命令
        # 列出当前正在运行的容器
-a      # 列出正在运行的容器包括历史容器
-n=?    # 显示最近创建的容器
-q      # 只显示当前容器的编号
 
[root@iZ2zeg4ytp0whqtmxbsqiiZ /]# docker ps
CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS              PORTS               NAMES
[root@iZ2zeg4ytp0whqtmxbsqiiZ /]# docker ps -a
CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS                     PORTS               NAMES
77969f5dcbf9        centos              "/bin/bash"         5 minutes ago       Exited (0) 5 minutes ago                       xenodochial_bose
74e82b7980e7        centos              "/bin/bash"         16 minutes ago      Exited (0) 6 minutes ago                       silly_cori
a57250395804        bf756fb1ae65        "/hello"            7 hours ago         Exited (0) 7 hours ago                         elated_nash
392d674f4f18        bf756fb1ae65        "/hello"            8 hours ago         Exited (0) 8 hours ago                         distracted_mcnulty
571d1bc0e8e8        bf756fb1ae65        "/hello"            23 hours ago        Exited (0) 23 hours ago                        magical_burnell
 
[root@iZ2zeg4ytp0whqtmxbsqiiZ /]# docker ps -qa
77969f5dcbf9
74e82b7980e7
a57250395804
392d674f4f18
571d1bc0e8e8
```

**退出容器**

``` yml
exit            # 直接退出容器并关闭
Ctrl + P + Q    # 容器不关闭退出
```

**删除容器**

``` yml
docker rm -f 容器id                       # 删除指定容器，不能删除正在运行的容器
docker rm -f $(docker ps -aq)       # 删除所有容器
docker ps -a -q|xargs docker rm -f  # 删除所有的容器
```

**启动和停止容器的操作**

``` yml
docker start 容器id           # 启动容器
docker restart 容器id         # 重启容器
docker stop 容器id            # 停止当前正在运行的容器
docker kill 容器id            # 强制停止当前的容器
```

## 常用其它命令(常用)

**后台启动容器**

``` yml

# 命令 docker run -d 镜像名
[root@iZ2zeg4ytp0whqtmxbsqiiZ /]# docker run -d centos
 
# 问题 docker ps， 发现centos停止了
 
# 常见的坑， docker 容器使用后台运行， 就必须要有一个前台进程，docker发现没有应用，就会自动停止
# nginx， 容器启动后，发现自己没有提供服务，就会立即停止，就是没有程序了
```

**查看日志**

```yml
docker logs -tf --tail number 容器id
 
[root@iZ2zeg4ytp0whqtmxbsqiiZ /]# docker logs -tf --tail 1 8d1621e09bff
2020-08-11T10:53:15.987702897Z [root@8d1621e09bff /]# exit      # 日志输出
 
# 自己编写一段shell脚本
[root@iZ2zeg4ytp0whqtmxbsqiiZ /]# docker run -d centos /bin/sh -c "while true;do echo xiaofan;sleep 1;done"
a0d580a21251da97bc050763cf2d5692a455c228fa2a711c3609872008e654c2
 
[root@iZ2zeg4ytp0whqtmxbsqiiZ /]# docker ps
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS               NAMES
a0d580a21251        centos              "/bin/sh -c 'while t…"   3 seconds ago       Up 1 second                             lucid_black
 
# 显示日志
-tf                 # 显示日志
--tail number       # 显示日志条数
[root@iZ2zeg4ytp0whqtmxbsqiiZ /]# docker logs -tf --tail 10 a0d580a21251
```

**查看容器中进程信息ps**

``` yml
# 命令 docker top 容器id
[root@iZ2zeg4ytp0whqtmxbsqiiZ /]# docker top df358bc06b17
UID                 PID                 PPID                C                   STIME               TTY     
root                28498               28482               0                   19:38               ?      
```

**查看容器的元数据**

```yml
# 命令
docker inspect 容器id
 
[root@iZ2zeg4ytp0whqtmxbsqiiZ /]# docker inspect df358bc06b17
[
    {
        "Id": "df358bc06b17ef44f215d35d9f46336b28981853069a3739edfc6bd400f99bf3",
        "Created": "2020-08-11T11:38:34.935048603Z",
        "Path": "/bin/bash",
        "Args": [],
        "State": {
            "Status": "running",
            "Running": true,
            "Paused": false,
            "Restarting": false,
            "OOMKilled": false,
            "Dead": false,
            "Pid": 28498,
            "ExitCode": 0,
            "Error": "",
            "StartedAt": "2020-08-11T11:38:35.216616071Z",
            "FinishedAt": "0001-01-01T00:00:00Z"
        },
        "Image": "sha256:0d120b6ccaa8c5e149176798b3501d4dd1885f961922497cd0abef155c869566",
        "ResolvConfPath": "/var/lib/docker/containers/df358bc06b17ef44f215d35d9f46336b28981853069a3739edfc6bd400f99bf3/resolv.conf",
        "HostnamePath": "/var/lib/docker/containers/df358bc06b17ef44f215d35d9f46336b28981853069a3739edfc6bd400f99bf3/hostname",
        "HostsPath": "/var/lib/docker/containers/df358bc06b17ef44f215d35d9f46336b28981853069a3739edfc6bd400f99bf3/hosts",
        "LogPath": "/var/lib/docker/containers/df358bc06b17ef44f215d35d9f46336b28981853069a3739edfc6bd400f99bf3/df358bc06b17ef44f215d35d9f46336b28981853069a3739edfc6bd400f99bf3-json.log",
        "Name": "/hungry_heisenberg",
        "RestartCount": 0,
        "Driver": "overlay2",
        "Platform": "linux",
        "MountLabel": "",
        "ProcessLabel": "",
        "AppArmorProfile": "",
        "ExecIDs": null,
        "HostConfig": {
            "Binds": null,
            "ContainerIDFile": "",
            "LogConfig": {
                "Type": "json-file",
                "Config": {}
            },
            "NetworkMode": "default",
            "PortBindings": {},
            "RestartPolicy": {
                "Name": "no",
                "MaximumRetryCount": 0
            },
            "AutoRemove": false,
            "VolumeDriver": "",
            "VolumesFrom": null,
            "CapAdd": null,
            "CapDrop": null,
            "Capabilities": null,
            "Dns": [],
            "DnsOptions": [],
            "DnsSearch": [],
            "ExtraHosts": null,
            "GroupAdd": null,
            "IpcMode": "private",
            "Cgroup": "",
            "Links": null,
            "OomScoreAdj": 0,
            "PidMode": "",
            "Privileged": false,
            "PublishAllPorts": false,
            "ReadonlyRootfs": false,
            "SecurityOpt": null,
            "UTSMode": "",
            "UsernsMode": "",
            "ShmSize": 67108864,
            "Runtime": "runc",
            "ConsoleSize": [
                0,
                0
            ],
            "Isolation": "",
            "CpuShares": 0,
            "Memory": 0,
            "NanoCpus": 0,
            "CgroupParent": "",
            "BlkioWeight": 0,
            "BlkioWeightDevice": [],
            "BlkioDeviceReadBps": null,
            "BlkioDeviceWriteBps": null,
            "BlkioDeviceReadIOps": null,
            "BlkioDeviceWriteIOps": null,
            "CpuPeriod": 0,
            "CpuQuota": 0,
            "CpuRealtimePeriod": 0,
            "CpuRealtimeRuntime": 0,
            "CpusetCpus": "",
            "CpusetMems": "",
            "Devices": [],
            "DeviceCgroupRules": null,
            "DeviceRequests": null,
            "KernelMemory": 0,
            "KernelMemoryTCP": 0,
            "MemoryReservation": 0,
            "MemorySwap": 0,
            "MemorySwappiness": null,
            "OomKillDisable": false,
            "PidsLimit": null,
            "Ulimits": null,
            "CpuCount": 0,
            "CpuPercent": 0,
            "IOMaximumIOps": 0,
            "IOMaximumBandwidth": 0,
            "MaskedPaths": [
                "/proc/asound",
                "/proc/acpi",
                "/proc/kcore",
                "/proc/keys",
                "/proc/latency_stats",
                "/proc/timer_list",
                "/proc/timer_stats",
                "/proc/sched_debug",
                "/proc/scsi",
                "/sys/firmware"
            ],
            "ReadonlyPaths": [
                "/proc/bus",
                "/proc/fs",
                "/proc/irq",
                "/proc/sys",
                "/proc/sysrq-trigger"
            ]
        },
        "GraphDriver": {
            "Data": {
                "LowerDir": "/var/lib/docker/overlay2/5af8a2aadbdba9e1e066331ff4bce56398617710a22ef906f9ce4d58bde2d360-init/diff:/var/lib/docker/overlay2/62926d498bd9d1a6684bb2f9920fb77a2f88896098e66ef93c4b74fcb19f29b6/diff",
                "MergedDir": "/var/lib/docker/overlay2/5af8a2aadbdba9e1e066331ff4bce56398617710a22ef906f9ce4d58bde2d360/merged",
                "UpperDir": "/var/lib/docker/overlay2/5af8a2aadbdba9e1e066331ff4bce56398617710a22ef906f9ce4d58bde2d360/diff",
                "WorkDir": "/var/lib/docker/overlay2/5af8a2aadbdba9e1e066331ff4bce56398617710a22ef906f9ce4d58bde2d360/work"
            },
            "Name": "overlay2"
        },
        "Mounts": [],
        "Config": {
            "Hostname": "df358bc06b17",
            "Domainname": "",
            "User": "",
            "AttachStdin": true,
            "AttachStdout": true,
            "AttachStderr": true,
            "Tty": true,
            "OpenStdin": true,
            "StdinOnce": true,
            "Env": [
                "PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin"
            ],
            "Cmd": [
                "/bin/bash"
            ],
            "Image": "centos",
            "Volumes": null,
            "WorkingDir": "",
            "Entrypoint": null,
            "OnBuild": null,
            "Labels": {
                "org.label-schema.build-date": "20200809",
                "org.label-schema.license": "GPLv2",
                "org.label-schema.name": "CentOS Base Image",
                "org.label-schema.schema-version": "1.0",
                "org.label-schema.vendor": "CentOS"
            }
        },
        "NetworkSettings": {
            "Bridge": "",
            "SandboxID": "4822f9ac2058e8415ebefbfa73f05424fe20cc8280a5720ad3708fa6e80cdb08",
            "HairpinMode": false,
            "LinkLocalIPv6Address": "",
            "LinkLocalIPv6PrefixLen": 0,
            "Ports": {},
            "SandboxKey": "/var/run/docker/netns/4822f9ac2058",
            "SecondaryIPAddresses": null,
            "SecondaryIPv6Addresses": null,
            "EndpointID": "5fd269c0a28227241e40cd30658e3ffe8ad6cc3e6514917c867d89d36a31d605",
            "Gateway": "172.17.0.1",
            "GlobalIPv6Address": "",
            "GlobalIPv6PrefixLen": 0,
            "IPAddress": "172.17.0.2",
            "IPPrefixLen": 16,
            "IPv6Gateway": "",
            "MacAddress": "02:42:ac:11:00:02",
            "Networks": {
                "bridge": {
                    "IPAMConfig": null,
                    "Links": null,
                    "Aliases": null,
                    "NetworkID": "30d6017888627cb565618b1639fecf8fc97e1ae4df5a9fd5ddb046d8fb02b565",
                    "EndpointID": "5fd269c0a28227241e40cd30658e3ffe8ad6cc3e6514917c867d89d36a31d605",
                    "Gateway": "172.17.0.1",
                    "IPAddress": "172.17.0.2",
                    "IPPrefixLen": 16,
                    "IPv6Gateway": "",
                    "GlobalIPv6Address": "",
                    "GlobalIPv6PrefixLen": 0,
                    "MacAddress": "02:42:ac:11:00:02",
                    "DriverOpts": null
                }
            }
        }
    }
]
[root@iZ2zeg4ytp0whqtmxbsqiiZ /]# 

```

**进入当前正在运行的容器**

```yml
 
进入当前正在运行的容器

# 我们通常容器使用后台方式运行的， 需要进入容器，修改一些配置
 
# 命令
docker exec -it 容器id /bin/bash
 
# 测试
[root@iZ2zeg4ytp0whqtmxbsqiiZ /]# docker exec -it df358bc06b17 /bin/bash
[root@df358bc06b17 /]# ls       
bin  etc   lib    lost+found  mnt  proc  run   srv  tmp  var
dev  home  lib64  media       opt  root  sbin  sys  usr
[root@df358bc06b17 /]# ps -ef
UID        PID  PPID  C STIME TTY          TIME CMD
root         1     0  0 Aug11 pts/0    00:00:00 /bin/bash
root        29     0  0 01:06 pts/1    00:00:00 /bin/bash
root        43    29  0 01:06 pts/1    00:00:00 ps -ef
 
# 方式二
docker attach 容器id
 
# docker exec       # 进入容器后开启一个新的终端，可以在里面操作
# docker attach     # 进入容器正在执行的终端，不会启动新的进程
```

**从容器中拷贝文件到主机**

```yml
docker cp 容器id：容器内路径(/home/Test.java)    目的地主机路径(/home)
 
[root@iZ2zeg4ytp0whqtmxbsqiiZ /]# docker cp 7af535f807e0:/home/Test.java /home
```

# Docker部署

## Docker安装nginx

**安装步骤**

1. 搜索镜像：docker search nginx
2. 下载镜像：docker pull nginx
3. 启动镜像：docker run -d --name nginx01 -p 3344:80 nginx  （访问本机3344，映射到容器里面的80端口）
   1. -d 后台运行
   2. --name 命名
   3. -p 端口设置
4. 本机自测：curl localhost:3344
5. 外部访问：注意阿里云端口的开放
6. 进入nginx容器:docker  exec -it nginx01 /bin/bash
7. 进入容器里面的nginx文件夹：
   1. 查看文件路径：whereis nginx
   2. 进入文件：cd /etc/nginx
   3. 查看里面内容：ls
   4. 查看nginx配置文件：cat nginx.conf
8. 退出容器：exit
9. 停止容器：docker stop 容器id

![image-20211105094658440](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211105094658440.png)

![image-20211105100012547](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211105100012547.png)

**注意：如果3344阿里云未开放此端口，是访问不到的**

![image-20211105100115274](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211105100115274.png)

## Docker安装Tomcat

**安装步骤**

1. docker pull tomcat:8.5.57
   1. 直接运行（本地没有镜像会直接下载）:docker run -it --rm tomcat:8.5.57
      1. 之前启动没有--rm，启动都是容器后台，容器退出后还可以查到。 而 docker run -it --rm tomcat:8.5.57推出容器后，即把docker容器删除，用于测试使用。通常不使用。
2. docker run -d --name tomcat01 -p 8080:8080 tomc
3. curl localhost:8080
   1. 发现404
   2. 原因：我们下载的tomcat容器是经过缩减过的，下载的容器只保障最基本的运行。
      1. 原来是在docker安装的tomcat在8.0.52版本之后，默认移除了默认显示的页面（webapps下的文件是空的），我们可以安装8.0.51版本.
   3. 解决方法：
      1. 进入容器内部：docker exec -it tomcat01 /bin/bash
      2. ls，发现有webapps和webapps.dist目录
         1. cd webapps:发现里面是空的
         2. cd webapps.dist:发现里面有tomcat的相关文件（可以理解为tomcat的启动文件）
         3. 我们需要把webapps.dist中的所有文件放在webapps中，即可成功运行。
      3. 拷贝文件：在docker exec -it tomcat01 /bin/bash进去的文件夹下
         1. 执行命令:cp -r webapps.dist/* webapps
      4. 测试：curl localhost:8080
   4. 开放阿里云端口

![image-20211105112518298](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211105112518298.png)

## Docker安装mysql

**安装步骤**

1. 下载mysql最新版:docker pull mysql
2. 运行mysql容器:docker run -d --name mysql01-p 3366:3366 mysql
   1. 出现问题：mysql闪退，docker ps 发现没有正在运行的容器
   2. 原因：需要未mysql设置账户
      1. 查看错误原因：docker logs mysql01
         1. ![image-20211105103337034](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211105103337034.png)
      2. 卸载刚刚创建的容器：docker rm -f mysql01
      3. 重新启动容器：docker run -d --name mysql01 -e MYSQL_ROOT_PASSWORD=111111   -p 3366:3366 mysql
3. 进入mysql：mysql -u root -p
4. 查看数据库信息：show databases
5. 外部访问数据库
   1. 出现问题，nativecat连接mysql一直报错。
   2. 原因：我是用的端口映射是3366:3366，外部端口3366没有问题，但是容器内部端口3366并不是mysql的默认端口，mysql的默认端口是3306，所以就需要修改mysql的默认端口。
   3. 修改方式：[docker安装mysql后如何修改默认端口_aaa6202341的博客-CSDN博客](https://blog.csdn.net/aaa6202341/article/details/107415723)

```shell
[root@ZHQ /]# docker run -p 3366:3366 --name mysql01 -e MYSQL_ROOT_PASSWORD=1111111 -d mysql
c3ac777788557126d16be3c05866aa146a15e0e6e8a9f63ce4dc101218eab376
[root@ZHQ /]# docker ps
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                                         NAMES
c3ac77778855        mysql               "docker-entrypoint..."   6 seconds ago       Up 5 seconds        3306/tcp, 33060/tcp, 0.0.0.0:3366->3366/tcp   mysql01
[root@ZHQ /]# docker exec -it mysql01 /bin/bash
root@c3ac77778855:/# ls
bin  boot  dev  docker-entrypoint-initdb.d  entrypoint.sh  etc  home  lib  lib64  media  mnt  opt  proc  root  run  sbin  srv  sys  tmp  usr  var
root@c3ac77778855:/# whereis mysql
mysql: /usr/bin/mysql /usr/lib/mysql /etc/mysql
root@c3ac77778855:/# cd /etc/mysql
root@c3ac77778855:/etc/mysql# ls
conf.d  my.cnf  my.cnf.fallback
root@c3ac77778855:/etc/mysql# mysql -u root -p
Enter password: 
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 8
Server version: 8.0.27 MySQL Community Server - GPL

Copyright (c) 2000, 2021, Oracle and/or its affiliates.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql> exit

```

## Docker安装ES和kibana（未学）

[【狂神说Java】Docker最新超详细版教程通俗易懂_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV1og4y1q7M4?p=16)

## 端口 映射

![image-20211105095143294](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211105095143294.png)

## Docker可视化窗口（不需要使用）

[【狂神说Java】Docker最新超详细版教程通俗易懂_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV1og4y1q7M4?p=17)

# Docker镜像

## 镜像是什么

镜像是一种轻量级、可执行的独立软件保，用来打包软件运行环境和基于运行环境开发的软件，他包含运行某个软件所需的所有内容，包括代码、运行时库、环境变量和配置文件。

所有的应用，直接打包docker镜像，就可以直接跑起来。

如何获取镜像：

1. 远程仓库下载
2. 别人拷贝
3. 自己制作一个镜像 DockerFile

## Docker镜像加载原理

关于docker下载为什么是分层下载的解答！

安装centos  第一层

安装docker  第二层

安装jdk         第三层。

联合文件系统：

![image-20211105145911706](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211105145911706.png)

**UnionFs （联合文件系统）**

UnionFs（联合文件系统）：Union文件系统（UnionFs）是一种**分层**、轻量级并且高性能的文件系统，他支持对**文件系统的修改作为一次提交来一层层的叠加，**同时可以将不同目录挂载到同一个虚拟文件系统下（ unite several directories into a single virtual filesystem)。Union文件系统是 Docker镜像的基础。镜像可以通过分层来进行继承，基于基础镜像（没有父镜像），可以制作各种具体的应用镜像
特性：一次同时加载多个文件系统，但从外面看起来，只能看到一个文件系统，联合加载会把各层文件系统叠加起来，这样最终的文件系统会包含所有底层的文件和目录

**Docker镜像加载原理**

docker的镜像实际上由一层一层的文件系统组成，这种层级的文件系统UnionFS。

boots(boot file system）主要包含 **bootloader和 Kernel**, bootloader主要是引导加载 kernel, Linux刚启动时会加载bootfs文件系统，在 Docker镜像的最底层是 boots。这一层与我们典型的Linux/Unix系统是一样的，包含boot加載器和内核。当boot加载完成之后整个内核就都在内存中了，此时内存的使用权已由 bootfs转交给内核，此时系统也会卸载bootfs。

rootfs（root file system),在 bootfs之上。包含的就是典型 Linux系统中的/dev,/proc,/bin,/etc等标准目录和文件。 rootfs就是各种不同的操作系统发行版，比如 Ubuntu, Centos等等。

![image-20211105150332150](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211105150332150.png)



平时我们安装进虚拟机的CentOS都是好几个G，为什么Docker这里才200M？

![image-20211105144649595](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211105144649595.png)

对于个精简的OS,rootfs可以很小，只需要包合最基本的命令，工具和程序库就可以了，因为**底层直接用Host的kernel，**自己只需要提供rootfs就可以了。由此可见对于不同的Linux发行版， boots基本是一致的， rootfs会有差別，因此不同的发行版可以公用bootfs.

虚拟机是分钟级别，容器是秒级！

## 分层理解

**分层的镜像**

我们可以去下载一个镜像，注意观察下载的日志输出，可以看到是一层层的在下载

其中id为54的已经存在，就不需要下载，只需要下载没有的。

![image-20211105144727523](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211105144727523.png)

思考：为什么Docker镜像要采用这种分层的结构呢？

最大的好处，我觉得莫过于资源共享了！比如有多个镜像都从相同的Base镜像构建而来，那么宿主机只需在磁盘上保留一份base镜像，同时内存中也只需要加载一份base镜像，这样就可以为所有的容器服务了，而且镜像的每一层都可以被共享。

查看镜像分层的方式可以通过docker image inspect 命令

```shell
➜  / docker image inspect redis          
[
    {
        "Id": "sha256:f9b9909726890b00d2098081642edf32e5211b7ab53563929a47f250bcdc1d7c",
        "RepoTags": [
            "redis:latest"
        ],
        "RepoDigests": [
            "redis@sha256:399a9b17b8522e24fbe2fd3b42474d4bb668d3994153c4b5d38c3dafd5903e32"
        ],
        "Parent": "",
        "Comment": "",
        "Created": "2020-05-02T01:40:19.112130797Z",
        "Container": "d30c0bcea88561bc5139821227d2199bb027eeba9083f90c701891b4affce3bc",
        "ContainerConfig": {
            "Hostname": "d30c0bcea885",
            "Domainname": "",
            "User": "",
            "AttachStdin": false,
            "AttachStdout": false,
            "AttachStderr": false,
            "ExposedPorts": {
                "6379/tcp": {}
            },
            "Tty": false,
            "OpenStdin": false,
            "StdinOnce": false,
            "Env": [
                "PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin",
                "GOSU_VERSION=1.12",
                "REDIS_VERSION=6.0.1",
                "REDIS_DOWNLOAD_URL=http://download.redis.io/releases/redis-6.0.1.tar.gz",
                "REDIS_DOWNLOAD_SHA=b8756e430479edc162ba9c44dc89ac394316cd482f2dc6b91bcd5fe12593f273"
            ],
            "Cmd": [
                "/bin/sh",
                "-c",
                "#(nop) ",
                "CMD [\"redis-server\"]"
            ],
            "ArgsEscaped": true,
            "Image": "sha256:704c602fa36f41a6d2d08e49bd2319ccd6915418f545c838416318b3c29811e0",
            "Volumes": {
                "/data": {}
            },
            "WorkingDir": "/data",
            "Entrypoint": [
                "docker-entrypoint.sh"
            ],
            "OnBuild": null,
            "Labels": {}
        },
        "DockerVersion": "18.09.7",
        "Author": "",
        "Config": {
            "Hostname": "",
            "Domainname": "",
            "User": "",
            "AttachStdin": false,
            "AttachStdout": false,
            "AttachStderr": false,
            "ExposedPorts": {
                "6379/tcp": {}
            },
            "Tty": false,
            "OpenStdin": false,
            "StdinOnce": false,
            "Env": [
                "PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin",
                "GOSU_VERSION=1.12",
                "REDIS_VERSION=6.0.1",
                "REDIS_DOWNLOAD_URL=http://download.redis.io/releases/redis-6.0.1.tar.gz",
                "REDIS_DOWNLOAD_SHA=b8756e430479edc162ba9c44dc89ac394316cd482f2dc6b91bcd5fe12593f273"
            ],
            "Cmd": [
                "redis-server"
            ],
            "ArgsEscaped": true,
            "Image": "sha256:704c602fa36f41a6d2d08e49bd2319ccd6915418f545c838416318b3c29811e0",
            "Volumes": {
                "/data": {}
            },
            "WorkingDir": "/data",
            "Entrypoint": [
                "docker-entrypoint.sh"
            ],
            "OnBuild": null,
            "Labels": null
        },
        "Architecture": "amd64",
        "Os": "linux",
        "Size": 104101893,
        "VirtualSize": 104101893,
        "GraphDriver": {
            "Data": {
                "LowerDir": "/var/lib/docker/overlay2/adea96bbe6518657dc2d4c6331a807eea70567144abda686588ef6c3bb0d778a/diff:/var/lib/docker/overlay2/66abd822d34dc6446e6bebe73721dfd1dc497c2c8063c43ffb8cf8140e2caeb6/diff:/var/lib/docker/overlay2/d19d24fb6a24801c5fa639c1d979d19f3f17196b3c6dde96d3b69cd2ad07ba8a/diff:/var/lib/docker/overlay2/a1e95aae5e09ca6df4f71b542c86c677b884f5280c1d3e3a1111b13644b221f9/diff:/var/lib/docker/overlay2/cd90f7a9cd0227c1db29ea992e889e4e6af057d9ab2835dd18a67a019c18bab4/diff",
                "MergedDir": "/var/lib/docker/overlay2/afa1de233453b60686a3847854624ef191d7bc317fb01e015b4f06671139fb11/merged",
                "UpperDir": "/var/lib/docker/overlay2/afa1de233453b60686a3847854624ef191d7bc317fb01e015b4f06671139fb11/diff",
                "WorkDir": "/var/lib/docker/overlay2/afa1de233453b60686a3847854624ef191d7bc317fb01e015b4f06671139fb11/work"
            },
            "Name": "overlay2"
        },
        "RootFS": {
            "Type": "layers",
            "Layers": [
                "sha256:c2adabaecedbda0af72b153c6499a0555f3a769d52370469d8f6bd6328af9b13",
                "sha256:744315296a49be711c312dfa1b3a80516116f78c437367ff0bc678da1123e990",
                "sha256:379ef5d5cb402a5538413d7285b21aa58a560882d15f1f553f7868dc4b66afa8",
                "sha256:d00fd460effb7b066760f97447c071492d471c5176d05b8af1751806a1f905f8",
                "sha256:4d0c196331523cfed7bf5bafd616ecb3855256838d850b6f3d5fba911f6c4123",
                "sha256:98b4a6242af2536383425ba2d6de033a510e049d9ca07ff501b95052da76e894"
            ]
        },
        "Metadata": {
            "LastTagTime": "0001-01-01T00:00:00Z"
        }
    }
]
```

![image-20211105150859797](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211105150859797.png)

**问题：这些已下载的层是如何记录的呢？**

**理解：**

所有的 Docker镜像都起始于一个基础镜像层，当进行修改或培加新的内容时，就会在当前镜像层之上，创建新的镜像层。

举一个简单的例子，假如基于 Ubuntu Linux16.04创建一个新的镜像，这就是新镜像的第一层；如果在该镜像中添加 Python包，
就会在基础镜像层之上创建第二个镜像层；如果继续添加一个安全补丁，就会创健第三个镜像层该像当前已经包含3个镜像层，如下图所示（这只是一个用于演示的很简单的例子）。

我们如果再加一个jdk，那么上面就会为我们再加一个第四层

![image-20211105144909349](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211105144909349.png)

在添加额外的镜像层的同时，镜像始终保持是当前所有镜像的组合，理解这一点非常重要。下图中举了一个简单的例子，每个镜像层包含3个文件，而镜像包含了来自两个镜像层的**6个文件。**

![image-20211105144937051](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211105144937051.png)

上图中的镜像层跟之前图中的略有区別，主要目的是便于展示文件

下图中展示了一个稍微复杂的三层镜像，在外部看来整个镜像只有6个文件，这是因为最上层中的文件7是文件5的一个更新版

![image-20211105152143766](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211105152143766.png)

文种情況下，上层镜像层中的文件覆盖了底层镜像层中的文件。这样就使得文件的更新版本作为一个新镜像层添加到镜像当中

Docker通过存储引擎（新版本采用快照机制）的方式来实现镜像层堆栈，并保证多镜像层对外展示为统一的文件系统

Linux上可用的存储引撃有AUFS、 Overlay2、 Device Mapper、Btrfs以及ZFS。顾名思义，每种存储引擎都基于 Linux中对应的
件系统或者块设备技术，井且每种存储引擎都有其独有的性能特点。

Docker在 Windows上仅支持 windowsfilter 一种存储引擎，该引擎基于NTFS文件系统之上实现了分层和CoW [1]。

下图展示了与系统显示相同的三层镜像。所有镜像层堆并合井，对外提供统一的视图
![image-20211105145054358](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211105145054358.png)

**特点**

Docker 镜像都是只读的，当容器启动时，一个新的可写层加载到镜像的顶部！

这一层就是我们通常说的容器层，容器之下的都叫镜像层！

![image-20211105145127770](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211105145127770.png)

理解：我们pull下来一个镜像之后，假如这个镜像有6层，这6层都是只可读的，并不能修改。当我们运行这个镜像的容器时，我们就可以对它进行操作了，但是我们的操作，都是在这六层之上的容器层（那六层是不会变得），我们操作之后，如果想把我们操作后的容器发给他人，我们可以直接将这7层打包成一个镜像，然后发给别人。

![image-20211106085056268](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211106085056268.png)

## **commit镜像**

```shell
docker commit 提交容器成为一个新的副本
 
# 命令和git原理类似
docker commit -m="描述信息" -a="作者" 容器id 目标镜像名:[TAG]
```

实战检测

我们下载tomcat时，运行成功后会出现404，原因时tomcat里面的webapps下面没有任何内容，需要把web apps.dist下的文件拷贝到webapps中。我们可以生成一个这样的镜像，webapps里面包含了启动tomcat的内容，我们再次使用直接创建这个镜像的容器就行，而不需要再从webapps.dist中复制。

```shell
# 1、启动一个默认的tomcat
docker run -d -p 8080:8080 tomcat

# 2、发现这个默认的tomcat 是没有webapps应用，官方的镜像默认webapps下面是没有文件的！
docker exec -it 容器id

# 3、拷贝文件进去
cp -r webapps.dist/* webapps
 
# 4、将操作过的容器通过commit调教为一个镜像！我们以后就使用我们修改过的镜像即可，这就是我们自己的一个修改的镜像。
docker commit -m="描述信息" -a="作者" 容器id 目标镜像名:[TAG]
docker commit -m "tomcat add webapp app" -a "zhq"  8ca6316887f1 tomcat10:1.0

```

如果你想要保存当前容器的状态，就可以通过commit来提交，获得一个镜像，就好比我们我们使用虚拟机的快照。

![image-20211105154619509](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211105154619509.png)

# Docker数据卷

## 问题

**docker的理念回顾**

将应用和环境打包成一个镜像！

数据？如果数据都在容器中，那么我们**容器删除，数据就会丢失**！**需求：数据可以持久化**

MySQL，容器删除了，删库跑路！**需求：MySQL数据可以存储在本地！**

容器之间可以有一个数据共享的技术！**Docker容器中产生的数据，同步到本地！**

这就是卷技术！**目录的挂载，将我们容器内的目录，挂载到Linux上面**！
![image-20211105161130440](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211105161130440.png)

**总结一句话：容器的持久化和同步操作！容器间也是可以数据共享的！**

## 使用数据卷

使用方式：直接使用容器挂在 -v

```shell

-v, --volume list                    Bind mount a volume
 
docker run -it -v 主机目录:容器内目录  -p 主机端口:容器内端口
将主机的host下的home下的cesh文件与docker容器中的home目录绑定
docker run -it -v /home/ceshi:/home centos /bin/bash
```

要求：将本机home文件下的test目录与centosdocker镜像中home目录绑定

1. 下载centos镜像 docker pull centos
2. 运行镜像 docker run -it centos /bin/bash
3. 在home目录下创建文件testdata.java： touch testdata.java
4. 退出容器： exit
5. 进行目录映射：docker run -it -v /home/test:/home centos /bin/bash
   1. 查看绑定：docker inspect 容器号
   2. ![image-20211105164854893](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211105164854893.png)
6. 在本机中出现目录test，修改文件:vim testdata.java
   1. ![image-20211105164600735](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211105164600735.png)
   2. ![image-20211105164648438](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211105164648438.png)
7. 进入容器中查看效果，发现容器中testdata.java的内容和主机一致。
   1. ![image-20211105164724603](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211105164724603.png)

我们双向绑定后，其中一方进行修改，另一方也会跟着修改。并且，就算停止容器，本机修改后，容器再次启动时也会是最新的数据。可以理解成本机和docker容器共同指向了一个地址。

## 实战：挂载mysql

创建容器时，直接进行挂载

1. docker pull mysql:5.7

1. docker run -d -p 3307:3306 -v /home/mysql/conf:/etc/mysql/conf.d -v /home/mysql/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 --name mysql01 mysql:5.7
   1. /etc/mysql/conf.d  mysql的相关配置文件
      1. ![image-20211105195852199](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211105195852199.png)
   2. /var/lib/mysql    mysql存储的表信息
      1. ![image-20211105195810894](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211105195810894.png)
2. 此时我们删除容器，容器中的表以及配置信息都还是有的。

## 具名挂载和匿名挂载

``` shell
# 匿名挂载   没有指定挂载卷的名称和宿主机的挂载路径
-v 容器内路径!   
docker run -d -P --name nginx01 -v /etc/nginx nginx
 
# 查看所有的volume（数据卷）的情况
➜  ~ docker volume ls    
DRIVER              VOLUME NAME
local               33ae588fae6d34f511a769948f0d3d123c9d45c442ac7728cb85599c2657e50d
local            
# 这里发现，这种就是匿名挂载，我们在 -v只写了容器内的路径，没有写容器外的路劲！
 
# 具名挂载     指定了挂在卷名称juming-nigix，但是没有主机路径
➜  ~ docker run -d -P --name nginx02 -v juming-nginx:/etc/nginx nginx
➜  ~ docker volume ls                  
DRIVER              VOLUME NAME
local               juming-nginx
 
# 通过 -v 卷名：容器内路径
# 查看一下这个卷

# 三种挂载： 匿名挂载、具名挂载、指定路径挂载
-v 容器内路径			#匿名挂载
-v 卷名：容器内路径		#具名挂载
-v /宿主机路径：容器内路径 #指定路径挂载 docker volume ls 是查看不到的
```

**具名挂载的卷信息**

![image-20211105205808350](C:\Users\DELL\AppData\Roaming\Typora\typora-user-images\image-20211105205808350.png)

所有的docker容器内的卷，没有指定目录的情况下都是在`/var/lib/docker/volumes/xxxx/_data`下
如果指定了目录，docker volume ls 是查看不到的

![image-20211105205920686](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211105205920686.png)

## 挂载权限设置

```shell
# 通过 -v 容器内路径： ro rw 改变读写权限
ro #readonly 只读
rw #readwrite 可读可写  （默认）
docker run -d -P --name nginx05 -v juming:/etc/nginx:ro nginx
docker run -d -P --name nginx05 -v juming:/etc/nginx:rw nginx
 
# ro 只要看到ro就说明这个路径只能通过宿主机来操作，容器内部是无法操作！
```

## 数据卷之DockerFile

DockerFile用于构建docker镜像的够建文件。是命令脚本！

**创建步骤：**

1. 创建一个dockerfile文件。名字随机，建议dockerfile
2. 文件中的内容 指令（大写）
3. 每个命令都是一层

**镜像脚本**

```shell
FROM centos

VOLUME [ "volume01","volume02"]

CMD echo "----end----"
CMD /bin/bash

```

1. FROM centos 以centos为基础
2. VOLUME ["volume01","volume02"]在生成的时候就挂载卷
3. CMD echo "----end-----"   够建完给我们发的消息 ----end-----
4. CMD /bin/bash   构建完默认走/bin/bash 控制台

**生成镜像**

docker build -f /home/docker-test-volume/dockerfile1 -t kuangshen/centos .

​                                       镜像脚本地址                                                       镜像名       .表示在当前路径下

```shell
[root@ZHQ docker-test-volume]# docker build -f /home/docker-test-volume/dockerfile1 -t kuangshen/centos .
Sending build context to Docker daemon 2.048 kB
Step 1/4 : FROM centos   
 ---> 5d0da3dc9764      #  生成镜像
Step 2/4 : VOLUME volume01 volume02
 ---> Running in 0593446e65a0
 ---> 4fdb7fac091d       # 进行挂载
Removing intermediate container 0593446e65a0
Step 3/4 : CMD echo "----end----"
 ---> Running in d9e9d1d5870f
 ---> ae6f9bdab4ff
Removing intermediate container d9e9d1d5870f
Step 4/4 : CMD /bin/bash   
 ---> Running in 6d2d2330b36e
 ---> 4d563c27c3ed
Removing intermediate container 6d2d2330b36e
Successfully built 4d563c27c3ed

```

此时已经生成我们自己的镜像

![image-20211105214302489](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211105214302489.png)

**启动我们自己的容器**

 docker run -it  4d563c27c3ed /bin/bash

![image-20211105215558738](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211105215558738.png)

volume01和volume02我们创建时只指定了容器内路径，因此是匿名挂载。

查看容器信息：docker inspect optimistic_sammet

![image-20211105220523164](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211105220523164.png)

我们在volume01这个文件夹创建一个文件

```shell
[root@bb20967099d9 /]# cd volume01
[root@bb20967099d9 volume01]# touch test.java
[root@bb20967099d9 volume01]# ls
test.java
```

查看宿主机的挂载目录

![image-20211105220856340](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211105220856340.png)

这是我们之后经常使用的方式。

假设构建镜像时我们没有挂载卷，那么我们创建容器的时候需要手动镜像挂载  -v 卷名：容器内路径！

## 数据卷容器

容器内数据共享，多个容器之间数据同步。如多个mysql同步数据。

![image-20211105221452771](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211105221452771.png)

利用一个父容器，去给别的容器共享数据。

``` shell
# 创建docker01为父容器
docker run -it --name docker01 kuangshen/centos
# 创建docker02为子容器，继承docker01
docker run -it --name docker02 --volumes-from docker01 kuangshen/centos
# 创建docker03为子容器，继承docker01
docker run -it --name docker03 --volumes-from docker01 kuangshen/centos
# 在docker01下的volume的文件夹下创建docker01file
 touch docker01file
# 发现docker02和docker03中都包含此文件
```

**说明：**在宿主机中，docker01-03有一个共同的文件存储空间，他们之间实现了数据共享，即使退出或者删除某个容器，这些共享数据还是存储的。如果删除宿主机下的这个文件，那么这些绑定的数据也将消失。

数据卷操作：

1. 查看所有数据卷
   1. 方式一：docker volume ls
   2. 方式二：进入指定文件夹查看![image-20211106082742835](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211106082742835.png)
2. 查看所有不在使用的数据卷，使用--filter dangling=true 过滤 不在使用的数据卷
   1. docker volume ls --filter dangling=true
3. 创建一个数据卷，可以设置，--name，--path，--mode。 也可以不用
   1. 拥有一切自动生成的参数: docker volume create
4. 删除数据卷
   1. docker volume rm 数据卷名称
5. 删除所有数据卷
   1. docker volume ls --filter dangling=true | grep local |awk '{print $2}'|xargs docker volume rm
   2. 注意：正在使用的不能删除
      1. Error response from daemon: Unable to remove volume, **volume still in use:** remove 02fdaf3c2190237871d79e86f30df34a00a8d1bf67322c01313b241cc0ece207: volume is in use - [75ee5c98a2b9e67f25d41d8465474dbd25a0a82ec2a05b3a5b368fa79e533c6d]

## 数据库同步

```shell
docker run -d -p 3310:3306 -v /etc/mysql/conf.d -v var/lib/mysql --name mysql01 -e MYSQL_ROOT_PASSWORD=123456 mysql:5.7

docker run -d -p 3311:3306 -e MYSQL_ROOT_PASSWORD=123456 --name mysql02 --volumes-from mysql01 mysql:5.7

注意：两个数据库的端口号不一样。
```

# DockerFile

dockerfile是用来构建docker镜像！是命令参数脚本！

构建步骤：

1. 编写一个dockerfile文件
2. docker build 构建成一个镜像
3. docker run 运行镜像
4. docker push 发布镜像（DockerHub，阿里云镜像仓库）

**官方dockerfile**

![img](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/20200813141504113.png)

![在这里插入图片描述](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/20200813141235209.png)

很多官方镜像都像是基础包，很多功能都不具备，我们通常会自己搭建自己的镜像！

官方既然可以制作镜像，能我们一样可以！

我们可以自己制作一个镜像，centos+mysql+jdk+redis等等

## DockerFile的构建过程

**基础知识：**

1. 每个保留关键字（指令）都是必须大写字母
2. 执行从上到下顺序执行
3. `#` 表示注释
4. 每个指令都会创建提交一个新的镜像层，并提交！

![image-20211106085056268](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211106085056268.png)

dockerFile是面向开发的， 我们以后要发布项目， 做镜像， 就需要编写dockefile文件， 这个文件十分简单！

Docker镜像逐渐成为企业的交互标准，必须要掌握！

步骤：开发，部署， 运维..... 缺一不可！

DockerFile： 构建文件， 定义了一切的步骤，源代码

DockerImages： 通过DockerFile构建生成的镜像， 最终发布和运行的产品！

Docker容器：容器就是镜像运行起来提供服务器

## DockerFile指令

![img](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/20200813144806291.png)

```shell
FROM            # 基础镜像，一切从这里开始构建
MAINTAINER      # 镜像是谁写的， 姓名+邮箱
RUN             # 镜像构建的时候需要运行的命令
ADD             # 步骤， tomcat镜像， 这个tomcat压缩包！添加内容
WORKDIR         # 镜像的工作目录
VOLUME          # 挂载的目录
EXPOSE          # 保留端口配置
CMD             # 指定这个容器启动的时候要运行的命令，只有最后一个会生效可被替代
ENTRYPOINT      # 指定这个容器启动的时候要运行的命令， 可以追加命令
ONBUILD         # 当构建一个被继承DockerFile 这个时候就会运行 ONBUILD 的指令，触发指令
COPY            # 类似ADD, 将我们文件拷贝到镜像中
ENV             # 构建的时候设置环境变量！
```

## 创建一个自己的centos

1. 在home目录下创建dockerfile1文件

2. 编写脚本内容：安装一个centos，包含一些常用指令。

   1. ```shell
      # 编写dockerfile文件
      # 编写dockerfile文件
      #基础镜像
      FROM centos
      # 作者信息
      MAINTAINER zhq<1427421650@qq.com>
      # 构建时创建的环境变量  MYPATH 启动时默认访问的目录
      ENV MYPATH /user/local
      # 镜像的工作目录
      WORKDIR $MYPATH
      
      # 安装常用指令
      RUN yum -y install vim
      RUN yum -y install net-tools
      
      # 暴露80端口
      EXPOSE 80
      CMD echo $MYPATH
      CMD echo "----end----"
      CMD /bin/bash
      
      ```

3. 运行脚本：docker build -f dockerfile1 -t zhq/centos .

   1. ![image-20211106093717750](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211106093717750.png)
   2. ![](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211106093748008.png)
   3. 通过安装流程我们发现，我们一共10条指令，就分成了10层进行下载。

4. 镜像下载完成

   1. ![image-20211106094010014](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211106094010014.png)

5. 创建镜像的容器并运行 
   1. docker run -it zhq/centos
   2. ![image-20211106094631204](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211106094631204.png)

6. 安装完毕

## 查看镜像历史

可以看到镜像的构建过程。

步骤：

1. docker images
2. docker history 镜像id
3. ![image-20211106094947966](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211106094947966.png)

## CMD和ENTRYPOINT区别

CMD					# 指定这个容器启动的时候要运行的命令，只有最后一个会生效，可被替代。
ENTRYPOINT			# 指定这个容器启动的时候要运行的命令，可以追加命令

**测试CMD**

```shell
# 编写dockerfile文件
$ vim dockerfile-test-cmd
FROM centos
CMD ["ls","-a"]
# 构建镜像
$ docker build  -f dockerfile-test-cmd -t cmd-test:0.1 .
# 运行镜像
$ docker run cmd-test:0.1
.
..
.dockerenv
bin
dev

# 想追加一个命令  -l 成为ls -al
$ docker run cmd-test:0.1 -l
docker: Error response from daemon: OCI runtime create failed: container_linux.go:349: starting container process caused "exec: \"-l\":
 executable file not found in $PATH": unknown.
ERRO[0000] error waiting for container: context canceled 
# cmd的情况下 -l 替换了CMD["ls","-l"]。 -l  不是命令所有报错

```

**测试ENTRYPOINT**

```shell
# 编写dockerfile文件
$ vim dockerfile-test-entrypoint
FROM centos
ENTRYPOINT ["ls","-a"]
# 构建镜像
$ docker build  -f dockerfile-test-entrypoint -t entrypoint-test:0.1
$ docker run entrypoint-test:0.1
.
..
.dockerenv
bin
dev
etc
home
lib
lib64
lost+found ...
# 我们的命令，是直接拼接在我们得ENTRYPOINT命令后面的
$ docker run entrypoint-test:0.1 -l
total 56
drwxr-xr-x   1 root root 4096 May 16 06:32 .
drwxr-xr-x   1 root root 4096 May 16 06:32 ..
-rwxr-xr-x   1 root root    0 May 16 06:32 .dockerenv
lrwxrwxrwx   1 root root    7 May 11  2019 bin -> usr/bin
drwxr-xr-x   5 root root  340 May 16 06:32 dev
drwxr-xr-x   1 root root 4096 May 16 06:32 etc
drwxr-xr-x   2 root root 4096 May 11  2019 home
lrwxrwxrwx   1 root root    7 May 11  2019 lib -> usr/lib
lrwxrwxrwx   1 root root    9 May 11  2019 lib64 -> usr/lib64 ....

```

## 实战：Tomcat镜像

### **操作步骤**

1. 准备镜像文件 tomcat压缩包（需要jdk环境），jdk压缩包

2. 进入/home/zhq/tomcat

3. 将上述两个压缩包上传到该目录下

   1. 上传文件：
      1. 方式1：**lrzsz方式上传文件**
         1. 安装lrzsz包：yum install -y lrzsz
         2. 上传文件：rz
   2. ![image-20211106113131214](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211106113131214.png)

4. 创建readme.txt说明文档:rouch readme.txt

5. 创建DockerFile脚本: vim Dockerfile

   1. Dockerfile是官方命名，我们也可以自定义，通常使用官方命名即可

      ```shell
      FROM centos
      MAINTAINER zhq<1427421650@qq.com>
      
      # 拷贝文件  将当前目录下的readme.txt文件拷贝到容器中/usr/local目录
      COPY readme.txt /usr/local/readme.txt
      
      # 将压缩包添加到容器中/usr/local目录 (通过ADD 会自动解压我们的压缩包)
      ADD apache-tomcat-8.5.72.tar.gz /usr/local/
      ADD jdk-8u181-linux-x64.tar.gz /usr/local/
      
      # 安装vim
      
      RUN yum -y install vim
      
      # 进入容器，默认走到/usr/local目录
      ENV MYPATH /usr/local
      # 工作目录
      WORKDIR  /usr/local
      
      # 配置java环境变量
      ENV JAVA_HOME /usr/local/jdk1.8.0_181
      ENV CLASSPATH $JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
      # tomcat配置
      ENV CATALINA_HOME /usr/local/apache-tomcat-8.5.72
      ENV CATALINA_BASH /usr/local/apache-tomcat-8.5.72
      # path配置
      ENV PATH $PATH:$JAVA_HOME/bin:$CATALINA_HOME/lib:$CATALINA_HOME/bin
      
      # 暴漏tomcat的8080端口
      EXPOSE 8080
      
      # 启动容器时，就执行tomcat
      CMD /usr/local/apache-tomcat-8.5.72/bin/startup.sh && tail -F /usr/local/apache-tomcat-8.5.72/bin/logs/catalina.out
      ```

6. 创建镜像： docker build -t mytomcat .    

   1. 因为我们dockerfile使用的是官方命名Dockerfile，所以构建镜像时会自动读取dockerfile，不需要指定脚本名称（非官方名称运行：$ docker build  -f dockerfile-test-entrypoint -t entrypoint-test:0.1）
   2. ![image-20211106120523141](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211106120523141.png)
   3. ![image-20211106120622484](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211106120622484.png)

7. 创建容器

   1. docker run -d -p 8080:8080 --name tomcat01 -v /home/zhq/tomcat/test:/usr/local/apache-tomcat-8.5.72/webapps/test -v /home/zhq/tomcat/tomcatlogs/:/usr/local/apache-tomcat-8.5.72/logs mytomcat
   2. ![image-20211106151119042](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211106151119042.png)
   3. curl localhost:8080 成功访问到tomcat页面

8. 发布项目

   1. 在test目录下创建一个WEB-INF目录，并写一个web.xml文件放入里面

      1. ```java
         <web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
                               http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
           version="3.1"
           metadata-complete="true">
         
           <display-name>Welcome to Tomcat</display-name>
           <description>
              Welcome to Tomcat
           </description>
         <welcome-file-list>
          <welcome-file>index2.jsp</welcome-file>
           <welcome-file>index.jsp</welcome-file>
         </welcome-file-list>
         </web-app>
         
         ```

   2. 在test目录下写一个index.jsp文件进行访问

      1. ```java
         <%@ page language="java" contentType="text/html; charset=UTF-8"
             pageEncoding="UTF-8"%>
         <html>
         <head>
         <title>life.jsp</title>
         </head>
         <body>
         
         <h1>Hello Tomcat</h1>
         
         </body>
         </html>
         ```

   3. 进行访问

      1. ![image-20211106152744665](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211106152744665.png)

      2. 查看日志

         1. cat catalina.out

         2. ```java
            [root@ZHQ tomcat]# cd test
            [root@ZHQ test]# ls
            index.jsp  WEB-INF
            [root@ZHQ test]# cd ../
            [root@ZHQ tomcat]# cd tomcatlogs
            [root@ZHQ tomcatlogs]# ls
            catalina.2021-11-06.log  host-manager.2021-11-06.log  localhost_access_log.2021-11-06.txt
            catalina.out             localhost.2021-11-06.log     manager.2021-11-06.log
            [root@ZHQ tomcatlogs]# cat catalina.out
            06-Nov-2021 04:56:06.194 INFO [main] org.apache.catalina.startup.VersionLoggerListener.log Server version name:   Apache Tomcat/8.5.72
            ```

### 发布镜像到dockerhub

### 发布镜像到阿里云

https://www.bilibili.com/video/BV1og4y1q7M4?p=31

## 小结

![image-20200516171155667](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9yYXcuZ2l0aHVidXNlcmNvbnRlbnQuY29tL2NoZW5nY29kZXgvY2xvdWRpbWcvbWFzdGVyL2ltZy9pbWFnZS0yMDIwMDUxNjE3MTE1NTY2Ny5wbmc?x-oss-process=image/format,png)

# Docker网络

## Docker0

```shell
# 删除全部容器
$ docker rm -f $(docker ps -aq)

# 删除全部镜像
$ docker rmi -f $(docker images -aq)
```



![image-20211106164701814](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211106164701814.png)

**问题1：docker是如何处理容器网络的访问的？**

![image-20211106164745082](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211106164745082.png)

1. **运行一个tomcat01**

   1.  docker run -d -P --name tomcat01 mytomcat
   2. ip addr
      1. ![image-20211106204310768](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211106204310768.png)
   3. docker exec tomcat01 ip addr
      1. ![image-20211106204416241](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211106204416241.png)

2. **运行一个tomcat02**

   1. ip addr

      1. ![](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211106204450873.png)

   2. docker exec tomcat02 ip addr

      1. ![image-20211106204608091](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211106204608091.png)

      2. ```shell
         # 我们发现这个容器带来网卡，都是一对对的
         # veth-pair 就是一对的虚拟设备接口，他们都是成对出现的，一端连着协议，一端彼此相连
         # 正因为有这个特性 veth-pair 充当一个桥梁，连接各种虚拟网络设备的
         # OpenStac,Docker容器之间的连接，OVS的连接，都是使用evth-pair技术
         ```

      3. 

   3. 使用主机ping docker1

      1.  ping 172.18.0.3
      2. ![image-20211106204900631](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211106204900631.png)

   4. 使用docker1 ping docker2

      1.  docker exec -it tomcat01 ping 172.18.0.3
      2. ![image-20211106204818938](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211106204818938.png)
      3. 容器与容器之间和容器与主机之间是可以相互ping通的

   5. **网络模型图**：容器与容器之间的通信是通过路由进行的，而非直接通信

      ![img](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9yYXcuZ2l0aHVidXNlcmNvbnRlbnQuY29tL2NoZW5nY29kZXgvY2xvdWRpbWcvbWFzdGVyL2ltZy9pbWFnZS0yMDIwMDUxNjE3NDI0ODYyNi5wbmc?x-oss-process=image/format,png)

   6. 结论：tomcat01和tomcat02公用一个路由器，docker0。所有的容器**不指定网络的情况下，都是docker0路由的**，docker会给我们的容器分配一个默认的可用ip。

Docker使用的是Linux的桥接，宿主机是一个Docker容器的网桥 docker0

![img](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9yYXcuZ2l0aHVidXNlcmNvbnRlbnQuY29tL2NoZW5nY29kZXgvY2xvdWRpbWcvbWFzdGVyL2ltZy9pbWFnZS0yMDIwMDUxNjE3NDcwMTA2My5wbmc?x-oss-process=image/format,png)

Docker中所有网络接口都是虚拟的，虚拟的转发效率高（内网传递文件）

只要容器删除，对应的网桥一对就没了！

![image-20211106211311450](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211106211311450.png)

## 思考：数据库的容器ip切换

**思考一个场景：我们编写了一个微服务，database url=ip: 项目不重启，数据ip换了，我们希望可以处理这个问题，可以通过名字来进行访问容器**？

可以通过link来实现。

**使用名字来连通两个容器**

1. 使用ip地址ping通（见上方笔记）
2. 使用名字ping通
   1. 进行ping：docker exec -it tomcat02 ping tomcat01
      1. 出错：ping: tomcat01: Name or service not known
   2. 进行link连接
      1. docker run -d -P --name tomcat03 --link tomcat01 mytomcat
   3. 进行ping：docker tomcat03 ping tomcat01
      1. 成功![image-20211106210834378](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211106210834378.png)
   4. 反向ping（tomcat01  ping  tomcat03）：
      1. 出错：ping: tomcat01: Name or service not known
   5. 问题：只可以单向ping
3. 双向ping通
   1. 查看我们docker中的网络
      1. docker network ls
         1. ![image-20211106212310655](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211106212310655.png)
      2. 查看docker主机
         1. docker network inspect 5bc
         2. ![image-20211106212353673](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211106212353673.png)
         3. 里面包含他的绑定
            1. ![image-20211106212426505](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211106212426505.png)
   2. 查看我们的tomcat03的配置
      1. docker inspect tomcat03
         1. ![image-20211106212559455](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211106212559455.png)
      2. 原理剖析，其实他是在我们docker03的本地的host文件中添加了tomcat01的地址映射
         1. 进入查看： docker exec -it tomcat03 cat /etc/hosts
         2. ![image-20211106212801885](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211106212801885.png)
      3. 我们进入tomcat01进行查看配置
         1. ![image-20211106212838016](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211106212838016.png)
         2. 并没有tomcat03的映射。我们可以在其中添加。（没有必要，因为link的方式已经淘汰）
4. 本质探究：--link 就是我们在hosts配置中增加了一个 172.18.0.2  tomcat01 dca6bd83479e，因此当我们访问tomcat01的时候，就相当于访问ip地址 172.18.0.2  
5. 因未--link太过麻烦，已经不使用，现在使用自定义网络。不适用docker0
   1. docker0问题：不支持容器名连接访问。

## 自定义网络

**容器互联：**容器与容器之间进行连接。

查看所有的docker网络：

![image-20211106213413832](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211106213413832.png)

**网络模式**

bridge：桥接（默认，自己创建也是用bridge 模式）

none：不配置网络

host：和宿主机共享网络

container：容器网络联通（容器之间可以直接互联，局限很大，很少用）

**测试**

1. **查看网络的相关命令**

   1. docker network ls

      1. ```java
         [root@ZHQ tomcat]# docker network ls
         NETWORK ID          NAME                   DRIVER              SCOPE
         5bc56f92ce56        bridge                 bridge              local
         5be40764e6dc        docker_smart_network   bridge              local
         81f37c0badc9        host                   host                local
         cf4142c5dd51        none                   null                local
         
         ```

2. **创建docker容器**

   1. 使用docker0网络

      ```shell
      docker run -d -P --name tomcat01 tomcat
      等价于 => 
      docker run -d -P --name tomcat01 --net bridge tomcat
      # 我们直接启动的命令 --net bridge,而这个就是我们得docker0
      # bridge就是docker0
      # docker0，特点：默认，域名不能访问。 --link可以打通连接，但是很麻烦！
      ```

   2. 使用自定义网络

      ```shell
      # 我们可以 自定义一个网络
      $ docker network create --driver bridge --subnet 192.168.0.0/16 --gateway 192.168.0.1 mynet
      --driver 设置连接方式
      --subnet 子网
      --gateway 网关
      mynet 自定义网络名称
      ```

      1. ![image-20211106223742074](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211106223742074.png)

      2. docker network inspect mynet;

         ![image-20211106223824070](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211106223824070.png)

   3. **使用自定义网络启动tomcat**

      1.  docker run -d -P --name tomcat-net-01 --net mynet mytomcat

      2.  docker run -d -P --name tomcat-net-02 --net mynet mytomcat

      3. 查看  mynetwork

         1. docker network inspect mynet;
         2. 我们创建的两个容器

         ![image-20211106224240706](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211106224240706.png)

   4. **在自定义网络下，进行容器ping**

      1. docker exec tomcat-net-02 ping tomcat-net-01
      2. ![image-20211106224351981](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211106224351981.png)
      3. docker exec tomcat-net-01 ping tomcat-net-02
         1. ![image-20211106224418925](C:\Users\DELL\AppData\Roaming\Typora\typora-user-images\image-20211106224418925.png)
      4. 我们发现，容器是可以通过名称互相ping通的。推荐使用。

   5. **好处：**

      1. redis -不同的集群使用不同的网络，保证集群是安全和健康的
      2. mysql-不同的集群使用不同的网络，保证集群是安全和健康的

   6. 问题：假如我们的mysql服务和redis服务在不同的网络区段下，能否进行相互连接呢？

      1. ![image-20211106224640423](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211106224640423.png)
      2. 使用网络联通解决。

## 网络联通

1. 在docker0上创建tomcat01,tomcat02，在mynet上创建两个tomcat-net-01,tomcat-net-02

   1. ```shell
      docker run -d -P --name tomcat01 tomcat
      docker run -d -P --name tomcat02 tomcat
      docker run -d -P --name tomcat-net-01 --net mynet mytomcat
      docker run -d -P --name tomcat-net-02 --net mynet mytomcat
      ```

2. 让 tomcat01 ping tomcat-net-02

   1. ```shell
      [root@ZHQ tomcat]# docker exec  tomcat01 ping tomcat-net-01
      rpc error: code = 2 desc = oci runtime error: exec failed: container_linux.go:235: starting container process caused "exec: \"ping\": executable file not found in $PATH"
      
      ```

   2. 失败：两个容器并没有联通。docker0和mynet不在同一个网路区段，不可能联通。

3. 虽然两个网络区段不可能联通，但是却可以让一个容器联通另一个网络区段

   1. 让容器tomcat01联通mynet网络

   2. docker network COMMAND

      1. ```shell
         Usage:  docker network COMMAND
         
         Manage networks
         
         Options:
               --help   Print usage
         
         Commands:
           connect     Connect a container to a network
           create      Create a network
           disconnect  Disconnect a container from a network
           inspect     Display detailed information on one or more networks
           ls          List networks
           prune       Remove all unused networks
           rm          Remove one or more networks
         
         Run 'docker network COMMAND --help' for more information on a command.
         ```

      2. docker network connect --help

         1. ```shell
            
            Usage:  docker network connect [OPTIONS] NETWORK CONTAINER
            
            Connect a container to a network
            
            Options:
                  --alias stringSlice           Add network-scoped alias for the container
                  --help                        Print usage
                  --ip string                   IP Address
                  --ip6 string                  IPv6 Address
                  --link list                   Add link to another container (default [])
                  --link-local-ip stringSlice   Add a link-local address for the container
            
            ```

      3. 将mynet 与 tomcat01 联通

         1.  docker network connect mynet tomcat01
         2. tomcat-net-01  ping  tomcat01
            1.  docker exec -it tomcat-net-01 ping tomcat01
            2. ![image-20211107082716468](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211107082716468.png)
         3. 反向ping  tomcat01 ping tomcat-net-01(不可以)
            1. docker exec -it tomcat01 ping tomcat-net-01
            2. ![image-20211107082857606](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211107082857606.png)
         4. tomcat-net-01  ping  tomcat02（没有联通，所以不行）
            1.  docker exec -it tomcat-net-01 ping tomcat02
            2. ![image-20211107083014636](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211107083014636.png)

4. 结论：假设要跨网络操作别人，就需要使用docker network connect 连通！

## 搭建redis集群（未学）

原生redis集群：[【狂神说Java】Redis最新超详细版教程通俗易懂_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV1S54y1R7SB?from=search&seid=2994884944114730596&spm_id_from=333.337.0.0)

docker创建集群：[【狂神说Java】Docker最新超详细版教程通俗易懂_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV1og4y1q7M4?p=38)

实现（3个主机，3个从机，主机挂掉之后，进行相应的操作可以直接在从机上进行）高可用。

# Docker部署SpringBoot项目

**部署流程**

1. 打包   mvn package

2. 黑窗口运行jar包

   1. 进入到jar包所在目录打开黑窗口
   2. 执行：java -jar smpe-system-1.0.0-RELEASE.jar
   3. 成功![image-20211107100443029](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211107100443029.png)

3. 使用docker 部署

   1. /home/zhq/spring

   2. 上传jar包

   3. 运行Dockerfile文件

      ```shell
      FROM java:8
      COPY *.jar /app.jar
      CMD ["--server.port=8080"]
      EXPOSE 8080
      ENTRYPOINT ["java","-jar","app.jar"]
      ```

   4. docker build -t safeedu .

   ![image-20211107101602567](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211107101602567.png)

   5. 此时我们的镜像已经打包完成，之后直接发给别人即可。别人拿到下载即可运行环境。

      ![image-20211107102307336](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211107102307336.png)

   6. docker run -d -P --name safeedu safeedu

   7. 访问成功

      ![image-20211107102221634](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211107102221634.png)

   



# 问题汇总

## 1. docker卸载后，镜像还存在

## 2. docker run 和 docker start的区别

**docker run 只在第一次运行时使用**，将镜像放到容器中，以后再次启动这个容器时，只需要使用命令docker start即可。docker run相当于执行了两步操作：将镜像放入容器中（docker create）,然后将容器启动，使之变成运行时容器（docker start）。

而**docker start的作用是，重新启动已存在的镜像**。也就是说，如果使用这个命令，我们必须事先知道这个容器的ID，或者这个容器的名字，我们可以使用docker ps找到这个容器的信息

![image-20211105091423624](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211105091423624.png)

## 3. mysql安装后，外部无法访问

docker run -d --name mysql01 -e MYSQL_ROOT_PASSWORD=111111   -p 3366:3366 mysql

1. 出现问题，nativecat连接mysql一直报错。
   1. 原因：我是用的端口映射是3366:3366，外部端口3366没有问题，但是容器内部端口3366并不是mysql的默认端口，mysql的默认端口是3306，所以就需要修改mysql的默认端口。
   2. 修改方式：[docker安装mysql后如何修改默认端口_aaa6202341的博客-CSDN博客](https://blog.csdn.net/aaa6202341/article/details/107415723)

## 4. ls ls-a ls -l  ls -al区别

1. ls 查看所有未隐藏命令
2. ls -a 查看所有命令
3. ls -l 查看所有未隐藏命令以及 他们的信息（创建时间等）
4. ls -al 查看所有命令以及他们的信息

## 5. 容器启动闪退问题

有时候，当我们docker run -d 容器时，docker ps -a 显示这个容器闪退。主要原因是：当我们使用docker run -d启动容器时，-d设置的是后台运行，如果后台运行的话，容器中的服务要保证开启，否则的话docker就会认为该容器没有运行，就会关掉该容器。

我们使用mysql进行测试

1. docker run -d --name mysql01 -e MYSQL_ROOT_PASSWORD=111111   -p 3306:3306 mysql
2. docker exec -it mysql01 /bash/bin
3. mysql -u root -p  并输入密码,此时我们就进入了数据库中。说明我们创建docker容器时，mysql服务就已经自动开启了。
4. 此时我们停掉mysql服务（注意，是停掉mysql服务，并不是docker容器）
5. 停止指令：mysqladmin -uroot -p shutdown
   Enter password: 
6. 此时我们使用docker ps会发现，mysql所在的容器docker01 已经被自动停止掉了（我们并没有手动关闭docker，而是关闭了docker中的mysql服务）。
   1. 问题：此时我如果重新启动docker容器，会显示启动失败，原因是需要设置密码。
      1. 针对问题，目前没有找到启动原容器的方案，重新docker run 一下。
   2. 如果是自定义的dockerfile出现上述问题，可能是dockerfile中的启动命令有误。

[(66条消息) docker run 的 -i -t -d参数_Huifeng Tang 的博客-CSDN博客](https://blog.csdn.net/qq_19381989/article/details/102781663)

解决方案：启动命令不用-d  使用 -t -i

```
docker run -t -i  /bin/bash tomcat:8.5.57
```

## 6. 容器创建后如何进行文件挂载

[(60条消息) 修改docker容器的挂载目录_jun-CSDN博客](https://blog.csdn.net/Doudou_Mylove/article/details/117691550?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_title~default-0.no_search_link&spm=1001.2101.3001.4242.1)

## 7.文件挂载失败

1. 执行将nginx的default.conf文件挂载到宿主机的default.cnof文件

   1. ```shell
       docker run -d -P --privileged=true --name nginx15 -v /home/zhq/web/nginx15/default.conf:/etc/nginx/conf.d/default.conf nginx
      ```

2. 报错

   ```shell
   "container init exited prematurely".
   ```

   ![image-20211108110950511](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211108110950511.png)

2. 原因：**docker 禁止用主机上不存在的文件挂载到container中已经存在的文件**

[Docker volume 挂载时文件或文件夹不存在_weixin_33953249的博客-CSDN博客](https://blog.csdn.net/weixin_33953249/article/details/88759709)

## 8.容器挂载后，容器内资源消失

挂载：即将宿主机与容器内的资源建立共享。注意：挂载时，宿主机里面的文件会覆盖容器内的文件。因此当宿主机文件是空的时候，会覆盖掉容器内已有的文件。

解决方式：先创建容器，将容器内的文件拷贝到宿主机，删除容器，再新建容器与宿主机进行挂载。

**mysql好像给我们提供了相关机制，使我们挂载时，容器内的文件不会被覆盖掉**

**mysql挂载**

```shell
[root@ZHQ /]# docker run -d -p 3307:3306 -v /home/mysql/conf:/etc/mysql/conf.d -v /home/mysql/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 --name mysql01 mysql
22df4a2635ff2859e40199355a4a770824ac1dc33b9a492b554b0968f8f62e58
[root@ZHQ ~]# docker exec -t mysql01 bash
root@22df4a2635ff:/# ls
^H^H^H^C
[root@ZHQ ~]# docker exec -it mysql01 /bin/bash
root@22df4a2635ff:/# ls
bin   dev                         entrypoint.sh  home  lib64  mnt  proc  run   srv  tmp  var
boot  docker-entrypoint-initdb.d  etc            lib   media  opt  root  sbin  sys  usr
root@22df4a2635ff:/# cd var 
root@22df4a2635ff:/var# ls
backups  cache  lib  local  lock  log  mail  opt  run  spool  tmp
root@22df4a2635ff:/var# cd lib
root@22df4a2635ff:/var/lib# ls
apt  dpkg  mecab  misc  mysql  pam  systemd
root@22df4a2635ff:/var/lib# cd mysql
root@22df4a2635ff:/var/lib/mysql# ls     # 默认的配置没有被fu'gai
'#ib_16384_0.dblwr'   binlog.000001   ca.pem            ib_logfile0   mysql                public_key.pem    undo_001
'#ib_16384_1.dblwr'   binlog.000002   client-cert.pem   ib_logfile1   mysql.ibd            server-cert.pem   undo_002
'#innodb_temp'        binlog.index    client-key.pem    ibdata1       performance_schema   server-key.pem
 auto.cnf             ca-key.pem      ib_buffer_pool    ibtmp1        private_key.pem      sys

```

**nginx挂载**

```shell
[root@ZHQ ~]#  docker run -d -P --privileged=true --name nginx30 -v /home/zhq/web/nginx30/default:/etc/nginx/conf.d nginx
ba374c6c870dd97b8224d78696b9223156e3a4500748a31f859144c31b1f6b75
[root@ZHQ ~]# docker exec -it nginx30 /bin/bash
root@ba374c6c870d:/# ls
bin   dev                  docker-entrypoint.sh  home  lib64  mnt  proc  run   srv  tmp  var
boot  docker-entrypoint.d  etc                   lib   media  opt  root  sbin  sys  usr
root@ba374c6c870d:/# cd /etc/nginx
root@ba374c6c870d:/etc/nginx# ls
conf.d  fastcgi_params  mime.types  modules  nginx.conf  scgi_params  uwsgi_params
root@ba374c6c870d:/etc/nginx# cd conf.d
root@ba374c6c870d:/etc/nginx/conf.d# ls
root@ba374c6c870d:/etc/nginx/conf.d#        # 默认的配置被覆盖
```



## 9.为什么不能直接访问服务器上面的文件，而必须使用代理。

服务器的资源是通过是http访问的，而本地的HTML文件在浏览器中是通过file协议打开的，他们之间不能跨域访问。

如果需要访问服务器上面的静态资源，需要使用文件传输协议：

中文释义：本地[文件传输协议](https://baike.baidu.com/item/文件传输协议)

注解：File协议主要用于**访问本地计算机中的文件**，就如同在Windows[资源管理器](https://baike.baidu.com/item/资源管理器)中打开文件一样。

**windows主机：**

**file:**///G:/ComputerStudy/%E8%BD%AF%E8%80%83/%E8%BD%AF%E8%80%83%E8%B5%84%E6%BA%90/4-%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84.pdf

**服务器:**

 curl file:////home/zhq/web/index.html

![image-20211108144124369](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211108144124369.png)

## 10. nginx刷新后页面显示404

[(66条消息) nginx代理后刷新显示404_maple的博客-CSDN博客](https://blog.csdn.net/xu622/article/details/87348848)

## 11.启动dockerfile，报错：no permisson

docker build -f /home/docker-test-volume/dockerfile1 -t kuangshen/centos .

原因：. 表示使用当前目录构建上下文。

解决方案：在dockerfile所在目录下执行创建。

# 常用命令

1. linux命令

   1. 系统内核：uname -r
   2. 系统版本：cat /etc/os-release
   3. 当前目录：pwd
   4. 移动文件：mv 文件  目标位置
      1. 批量移动文件：待整理
   5. 创建目录：mkdir 文件夹名
   6. 删除目录：
      1. (只能删除空目录)rmdir  文件夹名
      2. （删除文件夹下所有文件）rm -rf(不要用，千万不要用。。。。。) 文件夹
      2. -i 删除前逐一询问确认。
      4. -f 即使原档案属性设为唯读，亦直接删除，无需逐一确认。
      5. -r 将目录及以下之档案亦逐一删除（并询问）。
   7. 创建文件：touch 文件名
   8. 查看文件：cat 文件名称
   9. 创建/修改文件：vim 文件名称
   10. 将一个名为abc的文件重命名为1234：mv abc 1234
   11. 上传文件：
       1. 方式1：**lrzsz方式上传文件**
          1. 安装lrzsz包：yum install -y lrzsz
          2. 上传文件：rz
       2. 方式2：**ftp方式上传文件**
          1. https://www.linuxprobe.com/windows-linux-ftp.html

2. docker命令

   1. 安装卸载命令

      1. 卸载docker:

      ```java
      sudo yum remove docker \
                        docker-client \
                        docker-client-latest \
                        docker-common \
                        docker-latest \
                        docker-latest-logrotate \
                        docker-logrotate \
                        docker-engine
      ```

      1. 安装docker：yum install docker
      2. 查看docker版本： docker -v
      3. 删除docker安装包：yum remove docker-ce
      4. 删除静像、容器、配置文件：rm -rf /var/lib/docker
      5. 删除镜像
         1. docker rmi 镜像名
      6. 删除所有容器
         1. （包含正在运行的容器）：docker rm -f $(docker ps -aq)
         2. （不包含正在运行的容器）:docker rm $(docker ps -aq)
      7. 卸载docker：[(61条消息) docker 彻底卸载_无恋-zx的博客-CSDN博客_docker卸载干净](https://blog.csdn.net/qq_29726869/article/details/113353315)
      8. docker start 容器id           # 启动容器
         docker restart 容器id         # 重启容器
         docker stop 容器id            # 停止当前正在运行的容器
         docker kill 容器id            # 强制停止当前的容器
      9. docker rm -f 容器id                       # 删除指定容器，不能删除正在运行的容器
         docker rm -f $(docker ps -aq)       # 删除所有容器
         docker ps -a -q|xargs docker rm -f  # 删除所有的容器
      10. 修改容器名称  docker rename 旧 新
      10. 将一个名为abc的文件重命名为1234：mv abc 1234

   2. 其他命令

      1. docker系统信息：docker info 
      2. 帮助命令：docker --help

   3. 操作命令

      1. 开机启动docker：systemctl  enable docker
      2. 启动docker：systemctl start docker
      3. 停止docker：systemctl stop docker
      4. 创建容器：
      5. 进入容器：
      6. 启动容器：docker start 容器名称
      7. 关闭容器：docker stop 容器名称
      8. 退出容器（后台运行）：ctrl+p+q
      9. 退出容器（退出并关闭）：exit

   4. 查看指令

      1. 查看已下载的docker镜像：docker images
      2. 搜索镜像：docker search mysql/redis

   5. 指令后缀

      1. -d 后台运行
      2. -p 端口映射
      3. -P  随机端口映射（大写P）
      4. -v 卷挂载
      5. -e 环境配置

   6. 获取容器的ip地址：

      1. docker inspect 容器id/名称 | grep Mounts -A 20 *//查看容器与服务器的映射目录*
      2. docker inspect 容器id/名称 | grep IPAddress *//查看容器的ip地址*
      3. 获取一个容器：docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' 容器id/名称
      4. [(60条消息) 如何获取 docker 容器(container)的 ip 地址_sannerlittle的博客-CSDN博客_docker 获取容器ip](https://blog.csdn.net/sannerlittle/article/details/77063800)
      4. 查看容器的进程id:docker inspect --format '{{.State.Pid}}' 容器id

   7. 参数：

      1. --redstart = always  重启docker容器时，redis容器也重启
      2. --privileged=true   
      3. 使用该参数，container内的root拥有真正的root权限。否则，container内的root只是外部的一个普通用户权限。

3. docker 安装vim [(60条消息) docker容器中安装vim_人在码途-CSDN博客_docker vim](https://blog.csdn.net/huangjinao/article/details/101099081)


