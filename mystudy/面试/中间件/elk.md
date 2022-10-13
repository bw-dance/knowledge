# 参考资料

本文只是配置ELK的基本使用，其扩展功能可以查看以下这些BLOG。

## 1. 基本概念，以及监控

如基本监控mysql，redis，linux，docker等。

[(115条消息) Elastic Stack_benjamin55555的博客-CSDN博客](https://blog.csdn.net/qq_24950043/category_11491856.html)

## 2. 安装及各种案例

ELK各项功能安装，配合消息队列使用，或者直接通过filebeat输入文件到es等

宿主机：[ELK (skynemo.cn)](https://www.skynemo.cn/categories/elk)

使用docker:[还在用命令行看日志？快用Kibana吧，可视化日志分析YYDS！ | 夜风博客 (homedt.net)](https://www.homedt.net/225511.html#:~:text=安装并运行Kibana容器，使用如下命令即可； docker run --name kibana -p 5601%3A5601,--link elasticsearch%3Aes -e "elasticsearch.hosts%3Dhttp%3A%2F%2Fes%3A9200" -d kibana%3A7.17.3 ELK日志收集系统启动完成后，就可以访问Kibana的界面了，访问地址：http%3A%2F%2F192.168.3.105%3A5601)

elastic官方blog：[(103条消息) Logstash：用 Filebeat 把数据传入到 Logstash_Elastic 中国社区官方博客的博客-CSDN博客_filebeat 到logstash](https://blog.csdn.net/UbuntuTouch/article/details/100675502)

基于8.1的优秀案例blog：[ELK 运维入门（6）- Filebeat 8.1 简介及收集日志演示 - 基于CentOS 7.9 (skynemo.cn)](https://www.skynemo.cn/archives/base-on-centos79-filebeat81-config-and-usage#简介及安装)

[ELK日志平台（elasticsearch +logstash+kibana）原理和实操（史上最全） - 疯狂创客圈 - 博客园 (cnblogs.com)](https://www.cnblogs.com/crazymakercircle/p/16732034.html#autoid-h2-5-0-1)

## 3. 视频推荐

尚硅谷：https://www.bilibili.com/video/BV1hh411D7sb?p=63

黑马：[day5-18-项目一：ELK用于日志分析3_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV1Nt4y1m7qL?p=100&vd_source=383642096a66f0385ac22b0096523696)

# 基本环境

![image-20220818103809188](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220818103809188.png)

## 版本

版本参照：[支持矩阵 | Elastic](https://www.elastic.co/cn/support/matrix#matrix_compatibility)

jdk版本参照：[支持矩阵 | Elastic](https://www.elastic.co/cn/support/matrix#matrix_jvm)

| Elasticsearch | Kibana | Beats^*（Filebeat） |   Logstash^*   |
| :-----------: | :----: | :-----------------: | :------------: |
|    7.10.x     | 7.10.x |    6.8.x-7.17.x     |  6.8.x-7.17.x  |
|    7.11.x     | 7.11.x |    6.8.x-7.17.x     |  6.8.x-7.17.x  |
|    7.12.x     | 7.12.x |    6.8.x-7.17.x     |  6.8.x-7.17.x  |
|    7.13.x     | 7.13.x |    6.8.x-7.17.x     |  6.8.x-7.17.x  |
|    7.14.x     | 7.14.x |    6.8.x-7.17.x     |  6.8.x-7.17.x  |
|    7.15.x     | 7.15.x |    6.8.x-7.17.x     |  6.8.x-7.17.x  |
|    7.16.x     | 7.16.x |    6.8.x-7.17.x     |  6.8.x-7.17.x  |
|    7.17.x     | 7.17.x |    6.8.x-7.17.x     |  6.8.x-7.17.x  |
|     8.0.x     | 8.0.x  |   7.17.x - 8.3.x    | 7.17.x - 8.3.x |
|     8.1.x     | 8.1.x  |   7.17.x - 8.3.x    | 7.17.x - 8.3.x |
|     8.2.x     | 8.2.x  |   7.17.x - 8.3.x    | 7.17.x - 8.3.x |
|     8.3.x     | 8.3.x  |   7.17.x - 8.3.x    | 7.17.x - 8.3.x |

本文为了保守一下，怕最新版的不稳定，因此使用的是8.10版本的ES，需要主要的是8.0之后的ES要求jdk为java17

**版本选择：**

都下载这个版本：![image-20220809162922721](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220809162922721.png)

1. Elasticsearch：8.10：[Elasticsearch 8.1.0 | Elastic](https://www.elastic.co/cn/downloads/past-releases/elasticsearch-8-1-0)
2. Kibana：[Kibana 8.1.0 | Elastic](https://www.elastic.co/cn/downloads/past-releases/kibana-8-1-0)
3. Logstash：8.10：[Logstash 8.1.0 | Elastic](https://www.elastic.co/cn/downloads/past-releases/logstash-8-1-0)
4. Filebeat：8.10：[Filebeat 8.1.0 | Elastic](https://www.elastic.co/cn/downloads/past-releases/filebeat-8-1-0)
5. jdk：17:[Java Archive Downloads - Java SE 17 (oracle.com)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

## 环境

![image-20220819151045968](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220819151045968.png)

| 服务器        | 服务                                     |
| ------------- | ---------------------------------------- |
| 192.168.41.34 | elasticsearch+kibana+logstash+nginx      |
| 192.168.41.31 | filebeat + 微服务1+ 微服务2+微服务3..... |

Elastic8.0之后，默认开启了XPACK认证，开启这个认证过程中坑比较多，新手的话建议不开启，本文提供了两套方案。

我在安装时，ES和Kibana开启了XPACK认证，logstash没有开启，因为在内网，也没有必要开启。

# 部署使用

## 1. JDK安装

![image-20220811084031204](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220811084031204.png)

注意：使用系统配置的JDK时，启动的时候报错日志有问题，就没有使用系统配置的，使用的时捆绑的jdk

1. tar -zvxf jdk-17.0.4_linux-x64_bin.tar.gz               

2. 配置jdk路径   vi  /etc/profile

   ```java
   export ES_JAVA_HOME=/var/elk/package/jdk-17.0.4
   export JAVA_HOME=/var/elk/package/jdk-17.0.4 # jdk安装路径
   export PATH=$JAVA_HOME/bin:$PATH
   export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
   ```

3. 重新加载文件  source /etc/profile

4. java -version

5. javac

![image-20220809165213546](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220809165213546.png)

## 2. 部署ES

elasticsearch8.0+在第一次使用时，默认开启xpack安全认证之后, 直接通过curl 访问接口会报错，并且配置xpack门槛较高,对新手很不友好，因此我们推荐关闭他

[elasticsearch-7-x使用xpack进行安全认证 | 二丫讲梵 (eryajf.net)](https://wiki.eryajf.net/pages/3500.html#_1、x-pack演变)

### 1. 部署ES(关闭xpack)

1. tar -zxvf elasticsearch-8.1.0-linux-x86_64.tar.gz       

2. 创建data和log路径，修改配置文件

   注意：这里路径是我自己创建的，不是默认的，其他配置先默认

   ![image-20220809165932828](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220809165932828.png)

   es配置详解：[(97条消息) elasticsearch配置文件详解_wangchewen的博客-CSDN博客_es配置文件](https://blog.csdn.net/wangchewen/article/details/121142336)

   ```yml
   cluster.name: es-cluster
   node.name: es-node-1
   path.data: /var/elk/package/elasticsearch-8.1.0/env/data
   path.logs: /var/elk/package/elasticsearch-8.1.0/env/log
   network.host: 127.0.0.1
   http.port: 9200
   discovery.seed_hosts: ["127.0.0.1"]
   cluster.initial_master_nodes: ["es-node-1"]
   http.host: [_local_,_site_]
   ingest.geoip.downloader.enabled: false
   xpack.security.enabled: false
   ```

   注意：elasticsearch8.0在第一次使用时，默认开启xpack安全认证之后, 直接通过curl 访问接口会报错，并且配置xpack门槛较高,对新手很不友好，因此我们关闭他xpack.security.enabled: false

3. 启动ES：`./bin/elasticsearch -d`

   ![image-20220809172101036](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220809172101036.png)

   解决方式：[(101条消息) java.lang.RuntimeException: can not run elasticsearch as root_wkCaeser_的博客-CSDN博客](https://blog.csdn.net/qq_36666651/article/details/81285543)创建子账号。

4. ES不允许root账号（linux的root）启动，因此创建一个子账号

   1. adduser elastic
   2. passwd elastic
   3. 输入密码即可：elastic1457

5. 赋予elastic操作es权限

   1. 设置ES文件所有者为elastic：chown -R elastic:elastic elasticsearch-8.1.0       
   2. 赋予权限：chmod 770 elasticsearch-8.1.0  
   3. 命令解释：
      1. [Linux chown 命令 | 菜鸟教程 (runoob.com)](https://www.runoob.com/linux/linux-comm-chown.html)
      2. [Linux chmod 命令 | 菜鸟教程 (runoob.com)](https://www.runoob.com/linux/linux-comm-chmod.html)

6. 重新启动ES

   1. 使用elastic帐号登录服务器或者直接用root登录后，使用 su elastic切换用户
   2. 进入es安装目录，启动ES：./bin/elasticsearch -d

7. 访问ES

   ```java
    curl -u elastic:elastic1457 localhost:9200
   {
     "name" : "es-node-1",
     "cluster_name" : "es-cluster",
     "cluster_uuid" : "5WMg9WzkTAeSV5Us_bj3hw",
     "version" : {
       "number" : "8.1.0",
       "build_flavor" : "default",
       "build_type" : "tar",
       "build_hash" : "3700f7679f7d95e36da0b43762189bab189bc53a",
       "build_date" : "2022-03-03T14:20:00.690422633Z",
       "build_snapshot" : false,
       "lucene_version" : "9.0.0",
       "minimum_wire_compatibility_version" : "7.17.0",
       "minimum_index_compatibility_version" : "7.0.0"
     },
     "tagline" : "You Know, for Search"
   }
   ```

### 2. 部署ES（开启xpack，更安全）

推荐视频：https://www.bilibili.com/video/BV1hh411D7sb?p=63

**1—5步操作和不开启xpack的流程一样**

1. tar -zxvf elasticsearch-8.1.0-linux-x86_64.tar.gz       

2. 创建data和log路径，修改配置文件

   注意：这里路径是我自己创建的，不是默认的，其他配置先默认

   ![image-20220809165932828](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220809165932828.png)

   es配置详解：[(97条消息) elasticsearch配置文件详解_wangchewen的博客-CSDN博客_es配置文件](https://blog.csdn.net/wangchewen/article/details/121142336)

3. 启动ES：`./bin/elasticsearch -d`

   ![image-20220809172101036](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220809172101036.png)

   解决方式：[(101条消息) java.lang.RuntimeException: can not run elasticsearch as root_wkCaeser_的博客-CSDN博客](https://blog.csdn.net/qq_36666651/article/details/81285543)创建子账号。

4. ES不允许root账号（linux的root）启动，因此创建一个子账号

   1. adduser elastic
   2. passwd elastic
   3. 输入密码即可：elastic1457

5. 赋予elastic操作es权限

   1. 设置ES文件所有者为elastic：chown -R elastic:elastic elasticsearch-8.1.0       
   2. 赋予权限：chmod 770 elasticsearch-8.1.0  
   3. 命令解释：
      1. [Linux chown 命令 | 菜鸟教程 (runoob.com)](https://www.runoob.com/linux/linux-comm-chown.html)
      2. [Linux chmod 命令 | 菜鸟教程 (runoob.com)](https://www.runoob.com/linux/linux-comm-chmod.html)

6. 重新启动ES

   1. 使用elastic帐号登录服务器或者直接用root登录后，使用 su elastic切换用户

   2. 进入es安装目录，启动ES：./bin/elasticsearch -d

   3. 测试：

      ```shell
      [elastic@k8s-node3 elasticsearch-8.1.0]$ curl localhost:9200
      {"error":{"root_cause":[{"type":"security_exception","reason":"missing authentication credentials for REST request [/]","header":{"WWW-Authenticate":["Basic realm=\"security\" charset=\"UTF-8\"","ApiKey"]}}],"type":"security_exception","reason":"missing authentication credentials for REST request [/]","header":{"WWW-Authenticate":["Basic realm=\"security\" charset=\"UTF-8\"","ApiKey"]}},"status":401}[elastic@k8s-node3 elasticsearch-8.1.0]$ 
      ```

   4. 原因：elasticsearch8.0在第一次使用时，默认开启xpack安全认证之后, 直接通过curl 访问接口会报错

   5. 解决方式：

      1. 创建安全认证证书：在config目录下创建cert文件。mkdir /var/elk/package/elasticsearch-8.1.0/config/certs

      2. 签发ca证书：

         1. bin/elasticsearch-certutil ca    

         2. 回车，填写证书密码 elastic1457

         3. 新生成的证书文件![image-20220809205456704](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220809205456704.png)

         4. 使用ca证书签发节点证书，节点就可以通过安全认证方式访问：bin/elasticsearch-certutil cert --ca elastic-stack-ca.p12

            输入密码，回车，再次输入密码即可。

         5. 将生成的两个证书移动到config/certs中：mv elastic-stack-ca.p12 config/certs   mv elastic-certificates.p12 config/certs            

      3. 配置HTTP证书：

         1. bin/elasticsearch-certutil http     

         2. ![image-20220809211934664](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220809211934664.png)：N

         3. ![image-20220809212013559](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220809212013559.png)

            路径：/var/elk/package/elasticsearch-8.1.0/config/certs/elastic-stack-ca.p12

         4. 输入密码：elastic1457

         5. 证书有效时间：5y

         6. 是否需要每个节点都生成证书：n![image-20220809213651696](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220809213651696.png)

         7. 输入主机名称：所有节点的主机名称，每行一个

         8. 输入ip地址：每行一个

         9. 修改证书配置：n

         10. 输入密码：elastic1457

         11. 确定名称：回车

         12. 生成zip文件并解压缩： yum install unzip   unzip 文件名![image-20220809213943615](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220809213943615.png)

         13. 移动解压文件到config/certs中：mv elasticsearch/http.p12 kibana/elasticsearch-ca.pem config/certs         

         14. 总流程记录

             ```shell
             [elastic@k8s-node3 elasticsearch-8.1.0]$ bin/elasticsearch-certutil http
             warning: ignoring JAVA_HOME=/var/elk/package/jdk-17.0.4; using bundled JDK
             
             ## Elasticsearch HTTP Certificate Utility
             
             The 'http' command guides you through the process of generating certificates
             for use on the HTTP (Rest) interface for Elasticsearch.
             
             This tool will ask you a number of questions in order to generate the right
             set of files for your needs.
             
             ## Do you wish to generate a Certificate Signing Request (CSR)?
             
             A CSR is used when you want your certificate to be created by an existing
             Certificate Authority (CA) that you do not control (that is, you don't have
             access to the keys for that CA). 
             
             If you are in a corporate environment with a central security team, then you
             may have an existing Corporate CA that can generate your certificate for you.
             Infrastructure within your organisation may already be configured to trust this
             CA, so it may be easier for clients to connect to Elasticsearch if you use a
             CSR and send that request to the team that controls your CA.
             
             If you choose not to generate a CSR, this tool will generate a new certificate
             for you. That certificate will be signed by a CA under your control. This is a
             quick and easy way to secure your cluster with TLS, but you will need to
             configure all your clients to trust that custom CA.
             
             Generate a CSR? [y/N]n\
             Did not understand answer 'n\'
             Generate a CSR? [y/N]n
             
             ## Do you have an existing Certificate Authority (CA) key-pair that you wish to use to sign your certificate?
             
             If you have an existing CA certificate and key, then you can use that CA to
             sign your new http certificate. This allows you to use the same CA across
             multiple Elasticsearch clusters which can make it easier to configure clients,
             and may be easier for you to manage.
             
             If you do not have an existing CA, one will be generated for you.
             
             Use an existing CA? [y/N]y
             
             ## What is the path to your CA?
             
             Please enter the full pathname to the Certificate Authority that you wish to
             use for signing your new http certificate. This can be in PKCS#12 (.p12), JKS
             (.jks) or PEM (.crt, .key, .pem) format.
             CA Path: /var/elk/package/elasticsearch-8.1.0/config/certselastic-stack-ca.p12
             The file /var/elk/package/elasticsearch-8.1.0/config/certselastic-stack-ca.p12 does not exist
             CA Path: /var/elk/package/elasticsearch-8.1.0/config/certselastic-stack-ca.p12
             The file /var/elk/package/elasticsearch-8.1.0/config/certselastic-stack-ca.p12 does not exist
             CA Path: /var/elk/package/elasticsearch-8.1.0/config/certs/elastic-stack-ca.p12
             Reading a PKCS12 keystore requires a password.
             It is possible for the keystore's password to be blank,
             in which case you can simply press <ENTER> at the prompt
             Password for elastic-stack-ca.p12:
             
             ## How long should your certificates be valid?
             
             Every certificate has an expiry date. When the expiry date is reached clients
             will stop trusting your certificate and TLS connections will fail.
             
             Best practice suggests that you should either:
             (a) set this to a short duration (90 - 120 days) and have automatic processes
             to generate a new certificate before the old one expires, or
             (b) set it to a longer duration (3 - 5 years) and then perform a manual update
             a few months before it expires.
             
             You may enter the validity period in years (e.g. 3Y), months (e.g. 18M), or days (e.g. 90D)
             
             For how long should your certificate be valid? [5y] 5y
             
             ## Do you wish to generate one certificate per node?
             
             If you have multiple nodes in your cluster, then you may choose to generate a
             separate certificate for each of these nodes. Each certificate will have its
             own private key, and will be issued for a specific hostname or IP address.
             
             Alternatively, you may wish to generate a single certificate that is valid
             across all the hostnames or addresses in your cluster.
             
             If all of your nodes will be accessed through a single domain
             (e.g. node01.es.example.com, node02.es.example.com, etc) then you may find it
             simpler to generate one certificate with a wildcard hostname (*.es.example.com)
             and use that across all of your nodes.
             
             However, if you do not have a common domain name, and you expect to add
             additional nodes to your cluster in the future, then you should generate a
             certificate per node so that you can more easily generate new certificates when
             you provision new nodes.
             
             Generate a certificate per node? [y/N]n
             
             ## Which hostnames will be used to connect to your nodes?
             
             These hostnames will be added as "DNS" names in the "Subject Alternative Name"
             (SAN) field in your certificate.
             
             You should list every hostname and variant that people will use to connect to
             your cluster over http.
             Do not list IP addresses here, you will be asked to enter them later.
             
             If you wish to use a wildcard certificate (for example *.es.example.com) you
             can enter that here.
             
             Enter all the hostnames that you need, one per line.
             When you are done, press <ENTER> once more to move on to the next step.
             
             k8s-node3 
             
             You entered the following hostnames.
             
              - k8s-node3
             
             Is this correct [Y/n]y
             
             ## Which IP addresses will be used to connect to your nodes?
             
             If your clients will ever connect to your nodes by numeric IP address, then you
             can list these as valid IP "Subject Alternative Name" (SAN) fields in your
             certificate.
             
             If you do not have fixed IP addresses, or not wish to support direct IP access
             to your cluster then you can just press <ENTER> to skip this step.
             
             Enter all the IP addresses that you need, one per line.
             When you are done, press <ENTER> once more to move on to the next step.
             
             192.168.41.34
             
             You entered the following IP addresses.
             
              - 192.168.41.34
             
             Is this correct [Y/n]y
             
             ## Other certificate options
             
             The generated certificate will have the following additional configuration
             values. These values have been selected based on a combination of the
             information you have provided above and secure defaults. You should not need to
             change these values unless you have specific requirements.
             
             Key Name: k8s-node3
             Subject DN: CN=k8s-node3
             Key Size: 2048
             
             Do you wish to change any of these options? [y/N]n
             
             ## What password do you want for your private key(s)?
             
             Your private key(s) will be stored in a PKCS#12 keystore file named "http.p12".
             This type of keystore is always password protected, but it is possible to use a
             blank password.
             
             If you wish to use a blank password, simply press <enter> at the prompt below.
             Provide a password for the "http.p12" file:  [<ENTER> for none]
             Repeat password to confirm: 
             
             ## Where should we save the generated files?
             
             A number of files will be generated including your private key(s),
             public certificate(s), and sample configuration options for Elastic Stack products.
             
             These files will be included in a single zip archive.
             
             What filename should be used for the output zip file? [/var/elk/package/elasticsearch-8.1.0/elasticsearch-ssl-http.zip] 
             
             Zip file written to /var/elk/package/elasticsearch-8.1.0/elasticsearch-ssl-http.zip
             [elastic@k8s-node3 elasticsearch-8.1.0]$ ls
             bin  config  data  elasticsearch-ssl-http.zip  env  jdk  lib  LICENSE.txt  logs  modules  NOTICE.txt  plugins  README.asciidoc
             [elastic@k8s-node3 elasticsearch-8.1.0]$ unzip elasticsearch-ssl-http.zip
             Archive:  elasticsearch-ssl-http.zip
                creating: elasticsearch/
               inflating: elasticsearch/README.txt  
               inflating: elasticsearch/http.p12  
               inflating: elasticsearch/sample-elasticsearch.yml  
                creating: kibana/
               inflating: kibana/README.txt       
               inflating: kibana/elasticsearch-ca.pem  
               inflating: kibana/sample-kibana.yml  
             [elastic@k8s-node3 elasticsearch-8.1.0]$ ls
             bin  config  data  elasticsearch  elasticsearch-ssl-http.zip  env  jdk  kibana  lib  LICENSE.txt  logs  modules  NOTICE.txt  plugins  README.asciidoc
             [elastic@k8s-node3 elasticsearch-8.1.0]$ mv elasticsearch/http.p12 kibana/elasticsearch-ca.pem config/certs
             [elastic@k8s-node3 elasticsearch-8.1.0]$ ls
             bin  config  data  elasticsearch  elasticsearch-ssl-http.zip  env  jdk  kibana  lib  LICENSE.txt  logs  modules  NOTICE.txt  plugins  README.asciidoc
             [elastic@k8s-node3 elasticsearch-8.1.0]$ cd ../
             [elastic@k8s-node3 package]$ ls
             ```

      4. 修改ES配置文件

         ```yml
         cluster.name: es-cluster
         node.name: es-node-1
         path.data: /var/elk/package/elasticsearch-8.1.0/env/data
         path.logs: /var/elk/package/elasticsearch-8.1.0/env/log
         network.host: 127.0.0.1
         http.port: 9200
         discovery.seed_hosts: ["127.0.0.1"]
         cluster.initial_master_nodes: ["es-node-1"]
         http.host: [_local_,_site_]
         ingest.geoip.downloader.enabled: false
         xpack.security.enabled: true
         xpack.security.enrollment.enabled: true
         xpack.security.http.ssl:
           enabled: true
           keystore.password: elastic1457
           keystore.path: /var/elk/package/elasticsearch-8.1.0/config/certs/http.p12
           truststore.password: elastic1457
           truststore.path: /var/elk/package/elasticsearch-8.1.0/config/certs/http.p12
         xpack.security.transport.ssl:
           enabled: true
           verification_mode: certificate
           keystore.password: elastic1457
           keystore.path: /var/elk/package/elasticsearch-8.1.0/config/certs/elastic-certificates.p12
           truststore.password: elastic1457
           truststore.path: /var/elk/package/elasticsearch-8.1.0/config/certs/elastic-certificates.p12
         xpack.security.http.ssl.client_authentication: none
         ```

      5. 启动ES服务:`./bin/elasticsearch`

         1. 期间可能会出现报错，只要出现这个信息，就不需要理会.

            ![image-20220810102657106](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220810102657106.png)

            ```java
            ✅ Elasticsearch security features have been automatically configured!
            ✅ Authentication is enabled and cluster connections are encrypted.
            
            ℹ️  Password for the elastic user (reset with `bin/elasticsearch-reset-password -u elastic`):
             密码xxxxx
            
            
            
            ❌ Unable to generate an enrollment token for Kibana instances, try invoking `bin/elasticsearch-create-enrollment-token -s kibana`.
            
            ℹ️  Configure other nodes to join this cluster:
            • On this node:
              ⁃ Create an enrollment token with `bin/elasticsearch-create-enrollment-token -s node`.
              ⁃ Uncomment the transport.host setting at the end of config/elasticsearch.yml.
              ⁃ Restart Elasticsearch.
            • On other nodes:
              ⁃ Start Elasticsearch with `bin/elasticsearch --enrollment-token <token>`, using the enrollment token that you generated.
            ```

      6. 注意：因为我们配置了https，因此需要通过https进行访问。curl -k --user elastic:qqBWWqJ70g_7EVdkw=Af -X GET "https://192.168.41.34:9200"    

         ![image-20220810103206821](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220810103206821.png)

         1. **此处的密码为第一次启动服务时，生成的密码，这个密码只有在第一次启动服务时展示。**

         2. curl无法直接访问https，需要添加-k参数，如果直接访问，会出现下列提示。

            ![image-20220810103154977](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220810103154977.png)

      7. 注意，启动服务时，有可能出现这些警告，导致服务不能正常访问，出现这个报错：![image-20220810103500059](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220810103500059.png)

         查看启动服务时，发现有三处警告：

         ![image-20220810103527898](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220810103527898.png)

         解决方式：https://blog.csdn.net/educast/article/details/90647309

         ![image-20220810103535611](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220810103535611.png)

         解决方式（虚拟内存过小）：https://blog.csdn.net/Nicolege678/article/details/125280585

## 3. 部署Kibana

### 1. 部署Kibana(开启xpack)

推荐视频：[070-集群环境安装-Kibana软件安装_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV1hh411D7sb?p=70&vd_source=383642096a66f0385ac22b0096523696)

1. 解压：tar -zxvf kibana-8.1.0-linux-x86_64.tar.gz

2. 生成kibna证书

   1. 进入ES的安装目录，没错，是ES，在ES服务器中生成证书，不是Kinbana：/var/elk/package/elasticsearch-8.1.0

   2. 证书名称为kibana ：bin/elasticsearch-certutil csr -name kibana 

      ![image-20220810154443808](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220810154443808.png)

      生成压缩包，解压缩 unzip csr-bundle.zip

      移动压缩文件： 

      ![image-20220810154720037](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220810154720037.png)

      cd kibana

      mv kibana.csr kibana.key /var/elk/package/kibana-8.1.0/config/     

   3. 进入kibana安装目录，生成crt证书

      1. 进入目录：/var/elk/package/kibana-8.1.0/config
      2. 执行命令：openssl x509 -req -in kibana.csr -signkey kibana.key -out kibana.crt 
      3. 生成证书文件：![image-20220810155219612](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220810155219612.png)

3. 修改配置文件：cd /var/elk/package/kibana-8.1.0/config/kibana.yml

   ```java
   server.port: 5601
   server.host: "192.168.41.34"
   i18n.locale: "zh-CN"
   elasticsearch.hosts: ["https://192.168.41.34:9200"]
   elasticsearch.username: "kibana"
   elasticsearch.password: "jFZ1Jz5Ng+raYOpi6dX1"
   elasticsearch.ssl.verificationMode: none
   elasticsearch.ssl.certificateAuthorities: ["/var/elk/package/elasticsearch-8.1.0/config/certs/elasticsearch-ca.pem"]
   server.ssl.enabled: true
   server.ssl.certificate: /var/elk/package/kibana-8.1.0/config/kibana.crt
   server.ssl.key: /var/elk/package/kibana-8.1.0/config/kibana.key
   ```

4. kibana的密码需要生成

   ```java
   在ES的安装目录下执行该指令 
   ./bin/elasticsearch-reset-password -u kibana  --url "https://192.168.41.34:9200"
       
   21:32:57.988 [main] WARN  org.elasticsearch.deprecation.common.settings.Settings - data_stream.dataset="deprecation.elasticsearch" data_stream.namespace="default" data_stream.type="logs" elasticsearch.event.category="settings" event.code="keystore.password" message="[keystore.password] setting was deprecated in Elasticsearch and will be removed in a future release."
   21:32:57.989 [main] WARN  org.elasticsearch.deprecation.common.settings.Settings - data_stream.dataset="deprecation.elasticsearch" data_stream.namespace="default" data_stream.type="logs" elasticsearch.event.category="settings" event.code="truststore.password" message="[truststore.password] setting was deprecated in Elasticsearch and will be removed in a future release."
   21:32:57.990 [main] WARN  org.elasticsearch.deprecation.common.settings.Settings - data_stream.dataset="deprecation.elasticsearch" data_stream.namespace="default" data_stream.type="logs" elasticsearch.event.category="settings" event.code="keystore.password" message="[keystore.password] setting was deprecated in Elasticsearch and will be removed in a future release."
   21:32:57.991 [main] WARN  org.elasticsearch.deprecation.common.settings.Settings - data_stream.dataset="deprecation.elasticsearch" data_stream.namespace="default" data_stream.type="logs" elasticsearch.event.category="settings" event.code="truststore.password" message="[truststore.password] setting was deprecated in Elasticsearch and will be removed in a future release."
   Password for the [kibana] user successfully reset.
   New value: jFZ1Jz5Ng+raYOpi6dX1
   ```

5. 赋予elastic用户操作kibana权限

   chown -R elastic:elastic /var/elk/package/kibana-8.1.0  

   切换用户：su elastic

   启动kibana服务：./bin/kibana 

   问题：kibana启动成功，但是启动的过程中有报错，提示链接不上ES.访问部署好的kibana页面也出现503访问超时

   问题：kibana端口号通过配置文件直接修改成了8080，但是没有起作用，访问503，后重新改成5601，即可正常访问。

   访问路径：curl -k --user elastic:qqBWWqJ70g_7EVdkw=Af -X GET "https://192.168.41.34:5601"     

### 2. 部署Kibana(关闭xpack)

只需要将server.ssl.enabled: false即可

## 4. 使用nginx代理kibana

默认情况下，kibana是没有登录页面的，使用xpack配置后，即可生成登录页面。

**nginx代理开启xpack**

因为服务在内网服务器，无法直接通过互联网访问，需要把接口暴露到公网服务中

kibana的配置文件中添加配置

```xml
server.basePath: "/kibana"
```

nginx代理

```json
 location /kibana/ {
    proxy_pass https://192.168.41.34:5601/;
    rewrite ^/kibabna/(.*)$ /$1 break;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header Host $host:$server_port;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_http_version 1.1;
  }
```

如果kibana没有开启xpack，默认是没有登录界面的，外界可以直接进入kibana服务，可以通过nginx配置一个kibana登录

[Kibana——通过Nginx代理Kibana并实现登陆认证 - 曹伟雄 - 博客园 (cnblogs.com)](https://www.cnblogs.com/caoweixiong/p/14874997.html)

## 5. 安装Logstash

Logstash也可以配置XPack，后嫌太麻烦，况且在内网环境，就没有配置，问题不大。

Logstash 是数据（流式事件）收集引擎，它就像一根管道，可以动态统一不同来源的数据，并将数据标准化到选择的目标输出。Logstash 提供了大量的输入、过滤和输出插件，可以收集、过滤、输出各种类型的数据，并且提供了各种编解码器插件，方便解析转换各种格式的数据。

logstash需要做三件事情：

1. 收集日志
   1. 直接从文件收集
   2. 创建监听端口（配合filebeat使用）
2. 过滤日志
3. 输出日志到ES

### 1. 部署Logstash（关闭xpack）

1. 解压压缩包：tar -zxvf logstash-8.1.0-linux-x86_64.tar.gz

2. 因为logstash需要链接ES，ES开启了XPACK认证，因此链接时需要提供证书

   1. 将配置ES时，生成的elasticsearch-ca.pem证书复制到改目录下/var/elk/package/logstash-8.1.0/config/certs/elasticsearch-ca.pem

3. 进入config目录，修改配置文件

   1. logstash本身提供了一个logstash.yml,可以在里面直接改配置，也可以自定义创建文件配置，我这里选择自定义配置

   2. 在config目录下创建logstash-server.conf

      ```yml
      # beats传入的端口，默认5044
      input {
        beats {
          port => 5044
        }
      }
      
      # filter
      filter {
              grok {
                      match => {
                              "message" => "%{GREEDYDATA:contextName}\ - %{DATA:datetime}\ \[%{DATA:thead}\]\ %{DATA:level}\ \ %{DATA:class}\ - \[%{DATA:method}\] - %{GREEDYDATA:LOGGER}"
                      }
                      remove_field => ["message"]
              }
      
              date {
                      match => ["datetime", "yyyy-MM-dd HH:mm:ss.SSS"]
              }
      #       if "_grokparsefailure" in [tags] {
      #               drop {}
      #       }
      
      }
      
      
      # logstash输出
      output {
      
              #按照日志标签对日志进行分类处理， 日志标签后续会在filebeat中定义
              # 标签以模块为单位
              if "auth" in [tags] {
                      elasticsearch {
                              hosts => ["https://192.168.41.34:9200"]       # ES服务地址
                              index => "[auth]-%{+YYYY.MM.dd}"              # 索引名称：以模块名和天为单位建立
                              cacert => "/var/elk/package/logstash-8.1.0/config/certs/elasticsearch-ca.pem" # ES证书位置
                              user => "elastic" # ES账户
                              password => "qqBWWqJ70g_7EVdkw=Af"
                      }
              }
      
              if "gateway" in [tags] {
                      elasticsearch {
                              hosts => ["https://192.168.41.34:9200"]
                              index => "[gateway]-%{+YYYY.MM.dd}"
                              cacert => "/var/elk/package/logstash-8.1.0/config/certs/elasticsearch-ca.pem"
                              user => "elastic"
                              password => "qqBWWqJ70g_7EVdkw=Af"
                      }
              }
      
              if "system" in [tags] {
                      elasticsearch {
                              hosts => ["https://192.168.41.34:9200"]
                              index => "[system]-%{+YYYY.MM.dd}"
                              cacert => "/var/elk/package/logstash-8.1.0/config/certs/elasticsearch-ca.pem"
                              user => "elastic"
                              password => "qqBWWqJ70g_7EVdkw=Af"
                      }
              }
      
      
              if "algorithm" in [tags] {
                      elasticsearch {
                              hosts => ["https://192.168.41.34:9200"]
                              index => "[algorithm]-%{+YYYY.MM.dd}"
                              cacert => "/var/elk/package/logstash-8.1.0/config/certs/elasticsearch-ca.pem"
                              user => "elastic"
                              password => "qqBWWqJ70g_7EVdkw=Af"
                      }
              }
              if "project" in [tags] {
                      elasticsearch {
                              hosts => ["https://192.168.41.34:9200"]
                              index => "[project]-%{+YYYY.MM.dd}"
                              cacert => "/var/elk/package/logstash-8.1.0/config/certs/elasticsearch-ca.pem"
                              user => "elastic"
                              password => "qqBWWqJ70g_7EVdkw=Af"
                      }
              }
              if "course" in [tags] {
                      elasticsearch {
                              hosts => ["https://192.168.41.34:9200"]
                              index => "[course]-%{+YYYY.MM.dd}"
                              cacert => "/var/elk/package/logstash-8.1.0/config/certs/elasticsearch-ca.pem"
                              user => "elastic"
                              password => "qqBWWqJ70g_7EVdkw=Af"
                      }
              }
      
      }
      ```

4. 启动logstash`nohup bin/logstash -f config/logstash-sample.conf  > logstash.log 2>&1 &    `，logstash默认占用9600端口

   ![image-20220817215355502](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220817215355502.png)

   logstash会一直今天5044端口，监听该端口的数据流，然后转换成日志

   ![image-20220817215444309](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220817215444309.png)

### 问题

#### 1. 新建用户提示未授权

新建了一个elastic用户专门用于logstash的操作，结果logstash启动的时候提示未授权。

logstash报错：

```shell
[2022-08-16T16:56:34,032][INFO ][logstash.outputs.elasticsearch][main][1e101a43e1fd6cb1ca1fdee8d63b87289c142a2196dc3e33b0e80917af81948c] Retrying failed action {:status=>403, :action=>["index", {:_id=>nil, :_index=>"[user-log]-2022.08.16", :routing=>nil}, {"event"=>{"original"=>"\e[0;39m\e[30mTRAIN-PLATFORM-\e[0;39m \e[31m2022-08-10 21:05:52\e[0;39m \e[32m[http-nio-9201-exec-9]\e[0;39m \e[34mINFO \e[0;39m \e[1;35mc.t.c.l.apilog.LogHandlerInterceptor\e[0;39m - \e[1;30m【GET /user/info/admin】操作人id：0,  请求参数：null, 请求体： "}, "@timestamp"=>2022-08-16T08:42:42.428Z, "message"=>"\e[0;39m\e[30mTRAIN-PLATFORM-\e[0;39m \e[31m2022-08-10 21:05:52\e[0;39m \e[32m[http-nio-9201-exec-9]\e[0;39m \e[34mINFO \e[0;39m \e[1;35mc.t.c.l.apilog.LogHandlerInterceptor\e[0;39m - \e[1;30m【GET /user/info/admin】操作人id：0,  请求参数：null, 请求体： ", "log"=>{"file"=>{"path"=>"/var/www/api/logs/train-platform-system/2022-08/redis.2022-08-10-21.log"}, "offset"=>30111}, "ecs"=>{"version"=>"8.0.0"}, "tags"=>["user-log", "beats_input_codec_plain_applied"], "@version"=>"1", "agent"=>{"version"=>"8.1.0", "name"=>"localhost.localdomain", "ephemeral_id"=>"1e9f46b6-edba-49c9-8da9-67ea9483069c", "id"=>"e5f30cf6-3efe-44e5-b09d-84906ca9fe84", "type"=>"filebeat"}, "input"=>{"type"=>"log"}, "host"=>{"name"=>"localhost.localdomain", "id"=>"57415fb6605a4acab717e696eec3c55d", "hostname"=>"localhost.localdomain", "architecture"=>"x86_64", "containerized"=>false, "ip"=>["192.168.41.31", "fe80::1236:97e4:5b27:e528"], "mac"=>["00:50:56:95:ab:5a"], "os"=>{"version"=>"7 (Core)", "name"=>"CentOS Linux", "kernel"=>"3.10.0-1160.el7.x86_64", "platform"=>"centos", "family"=>"redhat", "type"=>"linux", "codename"=>"Core"}}}], :error=>{"type"=>"security_exception", "reason"=>"action [indices:admin/auto_create] is unauthorized for user [logstash_internal] with roles [logstash_write] on indices [[user-log]-2022.08.16], this action is granted by the index privileges [auto_configure,create_index,manage,all]"}}
```

奇怪的知识又增加了，我明明授权了啊，我授权的是all，最后发现，这样是无效的。详细解决方案参考这篇文章：[ELK 运维入门（4）- Logstash 8.1 安装配置 - 基于CentOS 7.9 (skynemo.cn)](https://www.skynemo.cn/archives/base-on-centos79-install-logstash81#安装)

![image-20220816165959451](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220816165959451.png)

![image-20220816170119992](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220816170119992.png)



#### 2. output的if判断

output处我们进行了大量的if判断，这样严重降低了效率。优化方案：[ELK - 优化 index patterns 和 Kibana 中显示的多余字段 - unchch - 博客园 (cnblogs.com)](https://www.cnblogs.com/unchch/p/12061380.html)

## 6. 安装FileBeat

FileBeat主要用于读取日志文件，并将日志文件输出给logstash。

FileBeat安装在服务所在的服务器，用于读取服务产生的日志文件。

如果服务不在同一台服务器上，则需要在每一台服务器上都安装FileBeat进行读取。

如果多个服务都在一台服务器上，那么只需要安装一个FileBeat即可收集所有服务的日志。（本文采用这种情况）

### 1. 部署FileBeat

1. 安装Filebeat压缩包到后台服务所在的服务器 

2. 解压Filebeat：tar -zxvf  /var/filebeat/filebeat-8.1.0-linux-x86_64.jar

3. 配置FileBeat(默认的配置文件filebeat.yaml，这里我自己创建一个配置文件filebeat-myconf.yml)

   ```yml
   # 从日志文件输入日志
   filebeat.inputs:
   - type: log               
     enabled: true
     paths:
       - /var/www/api/logs/train-platform-auth/*/server.*.log   # auth模块日志文件所在位置
     tags: ["auth"]          # 标签，该标签对应logstash配置文件中的if判断
     multiline:
       type: pattern
       pattern: 'TRAIN-PLATFORM-AUTH'
       negate: true
       match: after
   - type: log
     enabled: true
     paths:
       - /var/www/api/logs/train-platform-gateway/*/server.*.log
     tags: ["gateway"]
     multiline:
       type: pattern
       pattern: 'TRAIN-PLATFORM-GATEWAY'
       negate: true
       match: after
   - type: log
     enabled: true
     paths:
       - /var/www/api/logs/train-platform-system/*/server.*.log
     tags: ["system"]
     multiline:
       type: pattern
       pattern: 'TRAIN-PLATFORM-SYSTEM'
       negate: true
       match: after
   - type: log
     enabled: true
     paths:
       - /var/www/api/logs/train-platform-algorithm/*/server.*.log
     tags: ["algorithm"]
     multiline:
       type: pattern
       pattern: 'TRAIN-PLATFORM-ALG'
       negate: true
       match: after
   - type: log
     enabled: true
     paths:
       - /var/www/api/logs/train-platform-project/*/server.*.log
     tags: ["project"]
     multiline:
       type: pattern
       pattern: 'TRAIN-PLATFORM-PROJECT'
       negate: true
       match: after
   - type: log
     enabled: true
     paths:
       - /var/www/api/logs/train-platform-course/*/server.*.log
     tags: ["course"]
     multiline:
       type: pattern
       pattern: 'TRAIN-PLATFORM-COURSE'
       negate: true
       match: after
   setup.template.settings:
   # 设置主分片数
     index.number_of_shards: 1
   # 因为测试环境只有一个es节点，所以将副本分片设置为0，否则集群会报黄
     index.number_of_replicas: 0
   # 输出到logstash
   
   output.logstash:
   # logstash所在服务器的ip和端口
     hosts: ["192.168.41.34:5044"]
   # 默认配置，不做改动
   processors:
     - add_host_metadata:
         when.not.contains.tags: forwarded
     - add_cloud_metadata: ~
     - add_docker_metadata: ~
     - add_kubernetes_metadata: ~
     - drop_fields:        # 配置删除的字段
         fields: ["_id","_index","_score","version","host","version","agent.name","event.original","input_type", "log.offset", "host.name", "input.type", "agent.hostname", "agent.type", "ecs.version", "agent.ephemeral_id", "agent.id", "agent.version", "fields.ics", "log.file.path", "log.flags" ]
   ```

4. 启动filebeat`nohup ./filebeat -e -c filebeat-myconf.yml > filebeat.log 2>&1 & `

   filebeat启动的时候，会读取一大堆日志。

### 问题

#### 1. 设置副本分片无效，索引爆黄

```shell
# 因为测试环境只有一个es节点，所以将副本分片设置为0，否则集群会报黄
  index.number_of_replicas: 0
```

原因：此配置只适合直接将日志输入到ES，而我们是先将数据输入到了logstash进行过滤。

解决方式：既然我们将数据输入到了LOGSTASH，那么就可以在logstash中解决这个问题。

##### 解决方式一：简单，但是索引多了很麻烦。

进入kibana，找到报黄的索引，直接修改其副本分片的数量

![image-20220819095136696](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220819095136696.png)

##### 解决方式二：配置索引模板，源头解决问题。

索引模板是ES创建索引时，配置索引的一些配置以及字段的映射。详细说明见文档：[索引模板|弹性搜索指南 [8.1\] |弹性的 (elastic.co)](https://www.elastic.co/guide/en/elasticsearch/reference/8.1/index-templates.html)

这里需要说明一下，ES的版本更替比较快，网上的很多文章都过时了，建议查看官方文档，少走弯路。

索引模板主要配置一下内容：

1. 索引的匹配规则（即什么样的索引使用该规则）
2. setting（设置一些分片数量等），这也是索引报黄问题的关键
3. mapping，字段映射，跟grok的处理结果一样，即日志中的那些内容可以用于筛选条件
4. 别名

**匹配规则**

我配置的匹配规则是，以**train-platform-\***开头的索引，默认使用该模板，模板名称是[train-platform-log-template](https://sxpt.sjzc.edu.cn/kibana/app/management/data/index_management/templates/train-platform-log-template) 

**setting的规则是：**

```json
  "settings": {
    "index": {
  "blocks.read_only_allow_delete": "false",
  "priority": "1",
  "query.default_field": [
    "*"
  ],
  "refresh_interval": "1s",
  "write.wait_for_active_shards": "1",
  "routing.allocation.include._tier_preference": "data_content",
  "number_of_replicas": "0"
    }
  },
```

此规则我是直接在已创建好的索引中copy的。

![image-20220819100229005](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220819100229005.png)

**mapping的规则**

mapping的规则同样也是copy之前爆黄索引的规则。

**别名：没有配置**

您可以随时更改别名的数据流或索引。如果您在应用程序的 Elasticsearch 请求中使用别名，则可以在不停机或更改应用程序代码的情况下对数据重新编制索引。

我理解的是，假如你要想索引名称为A的索引输入数据，但是此时程序配置的输入索引名称是A-ALG，那么你想要的效果是向A-ALG索引数据，但是数据是进入到了A。这样你只需要给A起个别名就可以，而不需要将A-ALG再改成A了。

**生成模板**

模板名称是：train-platform-log-template （不能大写）

匹配规则是： "train-platform-*",(创建模板时能大写，但是通过logstash向ES创建索引时，大写报错，因此还是不能大写)

```json
PUT _index_template/train-platform-log-template 
{
  "index_patterns": "train-platform-*",
  "template":{
    "settings": {
    "index": {
  "blocks.read_only_allow_delete": "false",
  "priority": "1",
  "query.default_field": [
    "*"
  ],
  "refresh_interval": "1s",
  "write.wait_for_active_shards": "1",
  "routing.allocation.include._tier_preference": "data_content",
  "number_of_replicas": "0"
    }
  },
   
  "mappings": {
    "properties": {
      "@timestamp": {
        "type": "date"
      },
      "@version": {
        "type": "text",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
          }
        }
      },
      "LOGGER": {
        "type": "text",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
          }
        }
      },
      "agent": {
        "type": "object"
      },
      "class": {
        "type": "text",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
          }
        }
      },
      "contextName": {
        "type": "text",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
          }
        }
      },
      "datetime": {
        "type": "text",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
          }
        }
      },
      "ecs": {
        "type": "object"
      },
      "event": {
        "properties": {
          "original": {
            "type": "text",
            "fields": {
              "keyword": {
                "type": "keyword",
                "ignore_above": 256
              }
            }
          }
        }
      },
      "input": {
        "type": "object"
      },
      "level": {
        "type": "text",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
          }
        }
      },
      "log": {
        "properties": {
          "file": {
            "type": "object"
          }
        }
      },
      "message": {
        "type": "text",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
          }
        }
      },
      "method": {
        "type": "text",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
          }
        }
      },
      "tags": {
        "type": "text",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
          }
        }
      },
      "thead": {
        "type": "text",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
          }
        }
      }
    }
  }
  }
    
  }
```

执行完毕后，查看索引模板

![image-20220819100629028](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220819100629028.png)

配置logstash的配置文件

```json
 if "algorithm" in [tags] {
                elasticsearch {
                        hosts => ["https://192.168.41.34:9200"]
                        index => "train-platform-[alg1]-%{+YYYY.MM.dd}"
                        cacert => "/var/elk/package/logstash-8.1.0/config/certs/elasticsearch-ca.pem"
                        user => "elastic"
                        password => "qqBWWqJ70g_7EVdkw=Af"
                }
        }
        if "project" in [tags] {
                elasticsearch {
                        hosts => ["https://192.168.41.34:9200"]
```

  index => "train-platform-[alg]-%{+YYYY.MM.dd}"添加前缀是为了匹配对应的模板。

重启logstash，重新操作后，查看最新创建的索引。此时索引默认就是绿色。

![image-20220819100855912](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220819100855912.png)

#####解决方式三：配置索引模板文件

这种方式指的是在服务器编写自定义模板文件，在logstash输入到ES的时候指定使用的模板，原理和方式二一样。

参考文章：

[(120条消息) 如何为logstash+elasticsearch配置索引模板?_三劫散仙的博客-CSDN博客](https://blog.csdn.net/u010454030/article/details/84776640?spm=1001.2101.3001.6661.1&utm_medium=distribute.pc_relevant_t0.none-task-blog-2~default~CTRLIST~Rate-1-84776640-blog-108385252.pc_relevant_multi_platform_whitelistv3&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-2~default~CTRLIST~Rate-1-84776640-blog-108385252.pc_relevant_multi_platform_whitelistv3&utm_relevant_index=1)

[(119条消息) 【ELK7.4】 logstash模版_平凡似水的人生的博客-CSDN博客_logstash 模板](https://xingning.blog.csdn.net/article/details/108385252?spm=1001.2101.3001.6650.1&utm_medium=distribute.pc_relevant.none-task-blog-2~default~CTRLIST~Rate-1-108385252-blog-120188827.pc_relevant_multi_platform_whitelistv4eslandingctr&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2~default~CTRLIST~Rate-1-108385252-blog-120188827.pc_relevant_multi_platform_whitelistv4eslandingctr&utm_relevant_index=2)

该文中的template.json就是方式二中put提交的json

## 7. 使用Kibana操作日志

最终效果：

![image-20220818100834274](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220818100834274.png)



![image-20220818101407606](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220818101407606.png)



### 1. 查看日志

#### 流程

1. 登陆Kibana

2. 在logstash的配置中，我们将每个模块，每天的日志，都建立一个索引 如：[gateway]-%{+YYYY.MM.dd}

   1. 查看索引：`GET _cat/indices`

      ![image-20220817221201008](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220817221201008.png)

   2. ![image-20220817221229414](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220817221229414.png)

   3. 发现运行状况都是yellow

      ![image-20220817221317846](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220817221317846.png)

      在logstash的配置了，但是没有起效果

      ![image-20220817221912180](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220817221912180.png)

   4. 创建数据视图

      ![image-20220817221956451](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220817221956451.png)

      匹配所有的alg，其他模块一样

      ![image-20220817222038443](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220817222038443.png)

   5. 点击Discover，找到该索引。即可查看日志

      ![image-20220818084314847](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220818084314847.png)

      ![image-20220817222116977](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220817222116977.png)

#### 问题

##### 1. Java中的一行日志被ELK解析为了多行

![image-20220817091643548](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220817091643548.png)

出现的问题是一个视图中出现了多条日志，或者是多个视图打印了一条日志，主要原因在于日志的换行。

查看程序中的日志：![image-20220818085253773](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220818085253773.png)

在logback的配置中，每条日志都是以配置的contextName开头的。

![image-20220818085347968](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220818085347968.png)

因此，我们可以配置，以TRAIN-PLATFORM-ALG 为 换行标识符，来分割每一条日志.

filebeat为我们提供了multiline关键字，用于分割日志.pattern属性表示以X为分隔符，使用的是正则匹配。我这里直接以每个模块在logback中配置的contextName为分割。这样，只要日志中碰到TRAIN-PLATFORM-ALG，日志则会被拆分成一行。

关于该关键字的详细用法，参考该文档：[管理多行消息|文件节拍参考 [7.13\] |弹性的 (elastic.co)](https://www.elastic.co/guide/en/beats/filebeat/7.13/multiline-examples.html)

[(104条消息) logstash multiline插件想要使用pattern匹配多个指定字符串_QYHuiiQ的博客-CSDN博客_multiline.pattern](https://blog.csdn.net/QYHuiiQ/article/details/104802371)

```json
- type: log
  enabled: true
  paths:
    - /var/www/api/logs/train-platform-algorithm/*/server.*.log
  tags: ["alg"]
  multiline:
    type: pattern
    pattern: 'TRAIN-PLATFORM-ALG'
    negate: true
    match: after
    
```

最终效果：

![image-20220818090625205](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220818090625205.png)

同时，fileabeat也支持我们收集指定日志，比如我想只收集error等级的日志，就可以在写配置文件时这样写

```json
- type: log
  enabled: true
  paths:
    - /var/www/api/logs/train-platform-algorithm/*/server.*.log
  include_lines: ['error']  # 表示只收集带有error关键字的行
```

日志以TRAIN-PLATFORM-ALG拆分，日志详情：

![image-20220818090716471](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220818090716471.png)

##### 2. 冗余字段过多

![image-20220817103251941](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220817103251941.png)

这些是filebeat自带的属性，filebeat添加这些属性可以帮我们更好的筛选和展示数据，但是部分属性不需要，多了还影响性能，因此我们可以将不需要的排除掉。排除方法：

在filebeat的配置文件中配置

```shell
processors:
  - add_host_metadata:
      when.not.contains.tags: forwarded
  - add_cloud_metadata: ~
  - add_docker_metadata: ~
  - add_kubernetes_metadata: ~
  - drop_fields:
      fields: ["timestamp","version","agent.name","event.original","input_type", "log.offset", "host.name", "input.type", "agent.hostname", "agent.type", "ecs.version", "agent.ephemeral_id", "agent.id", "agent.version", "fields.ics", "log.file.path", "log.flags" ]
```

需要注意的是，"timestamp","version","event.original"这三者是排除不了的。

![image-20220817110632813](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220817110632813.png)

![image-20220817110721918](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220817110721918.png)

如果我只想关注具体的日志信息，不想看其他内容，可以通过kibana左侧进行筛选

![image-20220817165251705](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220817165251705.png)



##### 3. 日志中出现一些乱码

![image-20220817110745294](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220817110745294.png)

查询得知，这是Kibana无法识别带色彩的日志。系统日志通过logback配置的使用，为了方便查看，我是用的是彩色日志。

```java
logback的彩色日志配置
<property name="log.pattern"
              value="%black(%contextName-) %red(%d{yyyy-MM-dd HH:mm:ss}) %green([%thread]) %highlight(%-5level) %boldMagenta(%logger{36}) - %gray(%msg%n)"/>
    
logback的普通日志配置   
    <property name="log.pattern"
              value="%contextName - %d{HH:mm:ss.SSS} [%thread] %-5level %logger{20} - [%method,%line] - %msg%n"/>

```

![image-20220817110843019](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220817110843019.png)

暂无找到解决方式，只能在生产/测试环境运行时，将彩色日志的配置换成普通日志。

### 2. 分割日志Grok

我打印的日志由以下几部分组成

```xml
TRAIN-PLATFORM-ALG - 16:56:43.385 [http-nio-9208-exec-1] INFO  jdbc.sqltiming - [sqlTimingOccurred,373] - SELECT * FROM alg_task_files WHERE is_deleted=0

模块名称    时间    线程    日志等级    日志产生类    日志产生方法以及行数   日志内容
```

如果我想以上述这七部分日志进行分词，生成统计表，比如我想看日志等级的分布情况，应该如何统计呢？

此时可以使用logstash提供的grok，将我们的日志重写并且分词

**使用方法**

参考视频：[day5-17-项目一：ELK用于日志分析2_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV1Nt4y1m7qL?p=99&vd_source=383642096a66f0385ac22b0096523696)

配置的日志格式

```java
  <property name="log.pattern"
              value="%contextName - %d{HH:mm:ss.SSS} [%thread] %-5level %logger{20} - [%method,%line] - %msg%n"/>
```

打印效果

```java
       %contextName - %d{HH:mm:ss.SSS} [%thread]      %-5level %logger{20}     - [%method,%line]         - %msg%n
            模块名称         时间           线程          日志等级    日志产生类    日志产生方法以及行数         日志内容       
TRAIN-PLATFORM-ALG - 16:56:43.385 [http-nio-9208-exec-1] INFO  jdbc.sqltiming - [sqlTimingOccurred,373] - SELECT * FROM alg_task_files WHERE is_deleted=0
```

使用logstash提供的grok重写打印的日志。

目的：

1. 忽略无效日志
2. 在日志中筛选关键字，便于后续查询和图标生成

生成的grok

```java
# 最早的配置，打印日志的时候会有问题，下面有问题解析
%{GREEDYDATA:contextName}\ - %{DATA:datetime}\ \[%{DATA:thead}\]\ %{DATA:level}\ \ %{DATA:class}\ - \[%{DATA:method}\] - %{GREEDYDATA:LOGGER}

# 使用这个配置
%{GREEDYDATA:contextName}\ - %{DATA:datetime}\ \[%{DATA:thead}\]\ %{DATA:level}\ %{DATA:class}\ - \[%{DATA:method}\] - %{GREEDYDATA:LOGGER}


大致的grok思路：
表示文本数据：%{GREEDYDATA:名称}     
表示空格：\ (\后面还有一个空格)   \ \  表示有两个空格
文本在中括号中：\[%{DATA:名称}\]
时间： %{DATA:datetime}
```

分词结果

```java
{
  "contextName": "TRAIN-PLATFORM-ALG",
  "datetime": "16:56:43.385",
  "method": "sqlTimingOccurred,373",
  "level": "INFO",
  "thead": "http-nio-9208-exec-1",
  "LOGGER": "SELECT * FROM alg_task_files WHERE is_deleted=0",
  "class": "jdbc.sqltiming"
}
```

grok调试

![image-20220817210005703](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220817210005703.png)

在logstash中配置

```shell
# filter
filter {
        # 重写日志信息
        grok {
                match => {
                        "message" => "%{GREEDYDATA:contextName}\ - %{DATA:datetime}\ \[%{DATA:thead}\]\ %{DATA:level}\ \ %{DATA:class}\ - \[%{DATA:method}\] - %{GREEDYDATA:LOGGER}"
                }
                remove_field => ["message"]
        }
        # 时间重写
        date {
                match => ["datetime", "yyyy-MM-dd HH:mm:ss.SSS"]
        }
        # 如果日志格式和grok配置的不一致，则删除该日志（不建议配置，配置后某些异常日志可能被删除） 
#       if "_grokparsefailure" in [tags] {
#               drop {}
#       }

}
```

效果：

我们可以发现，通过grok拆分的几个字段，就已经变成了几个筛选条件，我们可以这样更方便的查看日志。

![image-20220818100959705](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220818100959705.png)

![image-20220818092940782](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220818092940782.png)

![image-20220818094854370](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220818094854370.png)

此时发现了一个问题，INFO和WARN的日志LEVEL都可以筛选到，但是ERROR的没有筛选到，查看原因是因为grok表达式的时候%{DATA:level}\ \  有两个斜杠空格，表示日志等级后有两个空格，INFO和WARN都是两个空格，但是ERROR只有一个。

![image-20220818095208675](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220818095208675.png)

换成一个/ 即可解析，这样同样也适配INFO和WARN，因此进行修改。

```json
%{GREEDYDATA:contextName}\ - %{DATA:datetime}\ \[%{DATA:thead}\]\ %{DATA:level}\ %{DATA:class}\ - \[%{DATA:method}\] - %{GREEDYDATA:LOGGER}
```

### 3. 图表展示

可查看该视频

[day5-18-项目一：ELK用于日志分析3_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV1Nt4y1m7qL?p=100&vd_source=383642096a66f0385ac22b0096523696)



## 8. 定时删除过期日志

服务运行时，每天会产生大量日志，这些日志会占用大量的磁盘空间，可以定时删除无用日志，节省磁盘空间。

查看索引所占的磁盘空间

![image-20220818144751475](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220818144751475.png)

```java
# 查看所有索引
GET _cat/indices
    
# 删除某个索引
DELETE /索引名称
```

删除索引命令：[删除一个索引 | Elasticsearch: 权威指南 | Elastic](https://www.elastic.co/guide/cn/elasticsearch/guide/current/_deleting_an_index.html)

命令行删除指令：`curl -k -g --user elastic:qqBWWqJ70g_7EVdkw=Af -X DELETE "https://192.168.41.34:9200/[course]-2022.08.16"`

### 1. 使用定时任务+脚本

使用脚本批量删除：del.sh

```json
#只保留7天内的日志索引
LAST_DATA=`date -d "-7 days" "+%Y.%m.%d"`
#删除一天前所有的索引
curl -k -g --user elastic:qqBWWqJ70g_7EVdkw=Af -X DELETE "https://192.168.41.34:9200/*-${LAST_DATA}"
```

出现错误

```json
此处我使用了通配符，进行删除时，报了这个错误
{"error":{"root_cause":[{"type":"illegal_argument_exception","reason":"Wildcard expressions or all indices are not allowed"}],"type":"illegal_argument_exception","reason":"Wildcard expressions or all indices are not allowed"},"status":400}[elastic@k8s-node3 elasticsearch-8.1.0]$ cat del.sh 
```

原因：ES的保护机制，默认情况下不能批量删除索引。如果想要可以批量删除，在ES的elasticSearch的配置文件中添加：action.destructive_requires_name: false(默认为true)，配置完成后，需要重启ES，否则无效。

sh del.sh  {"acknowledged":true}

删除一天前的所有日志，今天是8.18

![image-20220818162012215](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220818162012215.png)

设置定时任务，每天执行一次，linux中使用crontab设置定时任务

1. 赋予elastic用户del.sh文件的权限 chown elastic:elastic del.sh 

2. 创建定时任务

   1. 设置一个每天凌晨一点执行此脚本的定时任务

   2. ```shell
       crontab -e -u elastic
       0 1 * * * /var/elk/package/elasticsearch-8.1.0/del.sh
      ```

   3. 切换为elastic用户，查看定时任务：

      ![image-20220818164422409](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220818164422409.png)

3. 定时任务详解：[linux中使用crontab设置定时任务-Java架构师必看 (javajgs.com)](https://javajgs.com/archives/149546#:~:text=linux中使用crontab设置定时任务1.crontab简介,crontab命令常见于Unix和类Unix的操作系统之中，用于设置周期性被执行的指令。)

### 2. 使用生命周期

ES为我们提供了生命周期策略，详细见文章：[索引生命周期|弹性搜索指南 [8.1\] |弹性的 (elastic.co)](https://www.elastic.co/guide/en/elasticsearch/reference/8.1/ilm-index-lifecycle.html#ilm-phase-transitions)

我们可以配置以下三个策略

![image-20220819110521157](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220819110521157.png)

我们可以三个阶段都配置，也可以只配置一个阶段。每个阶段都有一个最大时间，超过最大时间后，可以选择进入下一个阶段，可以将数据永久保留此阶段，也可以选择删除。

我这里只配置了热阶段，热阶段结束后，删除索引。

![image-20220819111626316](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220819111626316.png)



![image-20220819111725098](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220819111725098.png)

索引使用该策略后，一天后，索引即可自动删除。

**索引应用策略**

##### 方式一：手动配置，简单

![image-20220819113101458](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220819113101458.png)

##### 方式二：在indextemplate配置，方便

在indextemlate的settings中添加

```json
"lifecycle": {
	"name": "del-log-everyday",
	"rollover_alias": ""
}
```

最终的配置indextemplate

PUT _index_template/train-platform-log-template 

```json
{
	"index_patterns": "train-platform-*",
	"template": {
		"settings": {
			"index": {
				"blocks.read_only_allow_delete": "false",
				"priority": "1",
				"query.default_field": [
					"*"
				],
				"refresh_interval": "1s",
				"write.wait_for_active_shards": "1",
				"routing.allocation.include._tier_preference": "data_content",
				"number_of_replicas": "0",
				"lifecycle": {
					"name": "del-log-everyday",
					"rollover_alias": ""
				}
			}
		},

		"mappings": {
			"properties": {
				"@timestamp": {
					"type": "date"
				},
				"@version": {
					"type": "text",
					"fields": {
						"keyword": {
							"type": "keyword",
							"ignore_above": 256
						}
					}
				},
				"LOGGER": {
					"type": "text",
					"fields": {
						"keyword": {
							"type": "keyword",
							"ignore_above": 256
						}
					}
				},
				"agent": {
					"type": "object"
				},
				"class": {
					"type": "text",
					"fields": {
						"keyword": {
							"type": "keyword",
							"ignore_above": 256
						}
					}
				},
				"contextName": {
					"type": "text",
					"fields": {
						"keyword": {
							"type": "keyword",
							"ignore_above": 256
						}
					}
				},
				"datetime": {
					"type": "text",
					"fields": {
						"keyword": {
							"type": "keyword",
							"ignore_above": 256
						}
					}
				},
				"ecs": {
					"type": "object"
				},
				"event": {
					"properties": {
						"original": {
							"type": "text",
							"fields": {
								"keyword": {
									"type": "keyword",
									"ignore_above": 256
								}
							}
						}
					}
				},
				"input": {
					"type": "object"
				},
				"level": {
					"type": "text",
					"fields": {
						"keyword": {
							"type": "keyword",
							"ignore_above": 256
						}
					}
				},
				"log": {
					"properties": {
						"file": {
							"type": "object"
						}
					}
				},
				"message": {
					"type": "text",
					"fields": {
						"keyword": {
							"type": "keyword",
							"ignore_above": 256
						}
					}
				},
				"method": {
					"type": "text",
					"fields": {
						"keyword": {
							"type": "keyword",
							"ignore_above": 256
						}
					}
				},
				"tags": {
					"type": "text",
					"fields": {
						"keyword": {
							"type": "keyword",
							"ignore_above": 256
						}
					}
				},
				"thead": {
					"type": "text",
					"fields": {
						"keyword": {
							"type": "keyword",
							"ignore_above": 256
						}
					}
				}
			}
		}
	}
}
```

根据模板创建index查看效果,已绑定生命周期

![image-20220819114020745](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220819114020745.png)

#####  问题

索引配置好生命周期后出现报错:`illegal_argument_exception: setting [index.lifecycle.rollover_alias] for index [[algor1]-2022.08.19] is empty or not defined`

![image-20220819115425942](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220819115425942.png)

原因：我在热阶段设置了回滚，如果达到我的条件时，将创建新的索引，要求必须给索引建立别名。

具体查看文档：

别名文档：[别名 |弹性搜索指南 [8.1\] |弹性的 (elastic.co)](https://www.elastic.co/guide/en/elasticsearch/reference/8.1//aliases.html)

热滚动要求：[展期|弹性搜索指南 [8.1\] |弹性的 (elastic.co)](https://www.elastic.co/guide/en/elasticsearch/reference/8.1/ilm-rollover.html)

如何配置：[(104条消息) elasticsearch7.8索引生命周期报错：index.lifecycle.rollover_alias does not point to index_zhangpfly的博客-CSDN博客_index.lifecycle.rollover_alias](https://blog.csdn.net/zhangpfly/article/details/109238869)

配置过程稍微有点麻烦，还有一种最简单的方式，就是直接将热滚动的开关关闭，不限制文件数量和文件空间的大小，固定时间删除索引即可。

![image-20220819115116590](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220819115116590.png)



​                                                                                                                                                                                                                                                                                                                                                                                                                

