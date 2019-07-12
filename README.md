# browser_linux 当前版本 v1.2
###### 已完成功能：远程登陆linux系统、执行大部分linux命令、文件上传下载、登录信息加密
###### 计划未完成功能：文件加密、命令响应信息加密
###### 备注：Private Key登陆功能先不做、websocket也可传输文件

#### 使用提示
###### 1.当前有多个命令窗口时，文件传输时一定要确定当前传输所连接的服务器，避免文件传输错乱
###### 2.当输入top、clear等命令时提示:'bash': unknown terminal type或者vi、vim命令显示数据错乱时，执行命令export TERM=vt100;export TEMCAP=$INFORMIXDIR/etc/termcap;即可

#### 试想：
###### 1.如果通过js可以直接连接到linux，就可以实现局域网内linux远程连接
###### 2.数据安全问题，明文传输风险太大，不可用--已解决，采用RSA非对称加密
###### 3.连接信息数据存储问题，不能每次进来都需要输入登录信息


#### 疑问：如何实现服务器加密、浏览器js解密并且不易被破解。
