# Docker学习

# 学习资料

docker官网：[Empowering App Development for Developers | Docker](https://www.docker.com/)

docker容器：[Docker Hub](https://hub.docker.com/)

docker文档：[Docker Documentation | Docker Documentation](https://docs.docker.com/)

狂神笔记：[(61条消息) 狂神说docker(最全笔记）_烟霞畔的博客-CSDN博客_狂神说docker笔记](https://blog.csdn.net/qq_21197507/article/details/115071715)[(61条消息) 狂神说docker(最全笔记）_烟霞畔的博客-CSDN博客_狂神说docker笔记](https://blog.csdn.net/qq_21197507/article/details/115071715)

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

​                 ![img](https://qqadapt.qpic.cn/txdocpic/0/1aceba8d36fa54c748bdf2a9e0da2eb9/0?w=1724&h=701)        

第二步：获取加速器地址

​                 ![img](https://qqadapt.qpic.cn/txdocpic/0/ef1c05d0ef49af08fabe54fc826a267e/0?w=523&h=641)        

第三步：配置使用

​                 ![img](https://docimg3.docs.qq.com/image/MbUWDxN4kOW8yOrezUZQNg?w=915&h=288)        

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
2. docker run -d --name tomcat01 -p 8080:8080 /bin/bash
3. curl localhost:8080
   1. 发现404
   2. 原因：我们下载的tomcat容器是经过缩减过的，下载的容器只保障最基本的运行。
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
2. 运行mysql容器:docker run -d --name mysql01-p 3366:66 mysql
   1. 出现问题：mysql闪退，docker ps 发现没有正在运行的容器
   2. 原因：需要未mysql设置账户
      1. 查看错误原因：docker logs mysql01
         1. ![image-20211105103337034](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211105103337034.png)
      2. 卸载刚刚创建的容器：docker rm -f mysql01
      3. 重新启动容器：docker run -d --name mysql01 -e MYSQL_ROOT_PASSWORD=121156   -p 3366:3366 mysql
3. 进入mysql：mysql -u root -p
4. 查看数据库信息：show databases
5. 外部访问数据库

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

## Docker安装ES和kibana

[【狂神说Java】Docker最新超详细版教程通俗易懂_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV1og4y1q7M4?p=16)

## 端口 映射

![image-20211105095143294](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211105095143294.png)

## Docker可视化窗口（不需要使用）

[【狂神说Java】Docker最新超详细版教程通俗易懂_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV1og4y1q7M4?p=17)

# 问题汇总

## 1. docker卸载后，镜像还存在

## 2. docker run 和 docker start的区别

**docker run 只在第一次运行时使用**，将镜像放到容器中，以后再次启动这个容器时，只需要使用命令docker start即可。docker run相当于执行了两步操作：将镜像放入容器中（docker create）,然后将容器启动，使之变成运行时容器（docker start）。

而**docker start的作用是，重新启动已存在的镜像**。也就是说，如果使用这个命令，我们必须事先知道这个容器的ID，或者这个容器的名字，我们可以使用docker ps找到这个容器的信息

![image-20211105091423624](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211105091423624.png)

## 3. mysql安装后，外部无法访问



# 常用命令

1. linux命令

   1. 系统内核：uname -r
   2. 系统版本：cat /etc/os-release

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
      5. 卸载docker：[(61条消息) docker 彻底卸载_无恋-zx的博客-CSDN博客_docker卸载干净](https://blog.csdn.net/qq_29726869/article/details/113353315)

   2. 其他命令

      1. docker系统信息：docker info 
      2. 帮助命令：docker --help

   3. 操作命令

      1. 开机启动docker：systemctl  enable docker
      2. 启动docker：systemctl start docker
      3. 停止docker：systemctl stop docker

   4. 查看指令

      1. 查看已下载的docker镜像：docker images
      2. 搜索镜像：docker search mysql/redis
      3. 

1. 

