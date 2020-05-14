> 使用springBoot + SpringData JPA搭建的企业级OA脚手架

本项目为单应用版本，项目采用springboot2 + SpringData JPA搭建的企业OA脚手架，springSecurity做安全认证，内置基本组织结构、菜单管理、系统字典、流程控制、全局文档管理。使用Quartz实现定时任务（包括静态定时任务和可配置定时任务）。使用velocity模板实现代码生成工具，减少重复代码开发量。
前台使用Thymeleaf模板引擎，jquery+bootstrap

###  代码说明以及部署环境

JDK1.8

MySql5.5+

Redis

Mongodb

1. idea安装lombok插件（否则会报没有get set方法错误）
1. 将代码导入到idea中并等待maven导入jar
1. 创建数据库、并修改application.yml中的数据库信息，数据库字符集：utf8；排序规则：utf8_general_ci
1. 一些特殊表需要手动创建，导入sql文件夹下的文件（springSecurity记住我、定时任务）。
1. 项目使用的是hibernate自动建表，所以业务类的表不需要手动创建。
1. 找到主启动类keper-web/src/main/java/com/hanqian/kepler/KeplerApplication.JAVA启动
1. 启动成功后会再数据库中自动创建响应业务表，第一次启动会创建一个系统管理员【admin/password】

### 目录

- kepler-common --------------------- 公共代码（工具类、公共代码）
- kepler-core ----------------------- 主要业务类代码（entity、dao、service）
- kepler-flow ----------------------- 流程引擎部分
- kepler-generator ------------------ 代码生成部分
- kepler-quartz --------------------- 定时任务（定时任务实例代码）
- kepler-web ------------------------ 控制层和前台页面（controller、html）
- sql ------------------------------- 需要手动导入的sql语句


### 后台使用到的框架或插件
|  name |  description | page |
| :------------: | :------------: | :------------: |
| SpringBoot JPA | orm |  |
| Spring Security  |  安全框架 |  |
| Quartz | 定时任务 |  |
| hutool | 封装各种工具类 | https://www.hutool.club/docs/#/ |
| justauth | 第三方登陆 | https://docs.justauth.whnb.wang/#/ |
| WxJava | 微信 | https://gitee.com/binary/weixin-java-tools |
| velocity | 代码生成模板 | https://blog.csdn.net/tttzzztttzzz/article/details/90720877 |
| kkFileView | 附件预览 | https://gitee.com/kekingcn/file-online-preview |

### 前台使用到的框架或插件
|  name |  description | page |
| :------------: | :------------: | :------------: |
| bootstrap |  |  |
| jquery |  |  |
| jqGrid | 视图表格分页 | https://blog.mn886.net/jqGrid/ |
| Thymeleaf | 前台模板引擎 | https://blog.csdn.net/pdw2009/article/details/44700897 |
| ztree | 树插件 |  http://www.treejs.cn/v3/api.php |
| summernote | 编辑器 | https://summernote.org |
| city-picker | 地域选择 | https://www.npmjs.com/package/city-picker-pc |
| dropzone | 附件上传 | https://www.dropzonejs.com |
| laydate | 时间选择器 | https://www.layui.com/doc/modules/laydate.html |
| jquery-tmpl | 模板引擎 | https://blog.csdn.net/u010142437/article/details/84399222 |
| layX | 弹窗插件 | https://gitee.com/monksoul/LayX |
| mui | 手机端前台框架 | https://dev.dcloud.net.cn/mui/ui/ |

### 本项目的搭建参考了以下开源项目
|  name | page |
| :------------: | :------------: |
| RuoYi | https://gitee.com/y_project/RuoYi |
| weixin-java-mp-demo-springboot | https://gitee.com/binary/weixin-java-mp-demo-springboot |
| spring-boot-demo | https://github.com/xkcoding/spring-boot-demo |
| SmartWx | https://gitee.com/qingfengtaizi/wxmp |

### 截图
![输入图片说明](https://images.gitee.com/uploads/images/2020/0514/155037_0bae3324_1226268.png "QQ截图20200514154819.png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/0514/155118_f8e8adb4_1226268.png "QQ截图20200514154854.png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/0514/155140_0bd42054_1226268.png "QQ截图20200514154927.png")

### 目前的问题以及后期计划：

- ~~【已完成】增加邮件服务~~
- ~~【已完成】实现忘记密码功能，配合邮件服务~~
- ~~【已完成】增加微信公众号框架，配合WxJava~~
- ~~【已完成】可配置定时任务（参考【若依】项目）~~
- 【未完成】第三方账号绑定
- 【未完成】第三方登录（QQ登录、微信登录）
- ~~【已完成】微信公众号配置页面（参考https://gitee.com/qingfengtaizi/wxmp~~
- ~~【已完成】各个菜单的查看权限（权限配置就和流程查看权限一样）~~
- ~~【已完成】系统管理中增加服务器信息以及项目信息（参考【若依】项目）~~
- ~~【已完成】代码自动生成（根据实体类属性）~~
- ~~【已完成】增加消息提醒~~
- ~~【已完成】mysql、mongodb数据定时备份~~
- ~~【已完成】多标签页情况下的页面排版~~
- ~~【已完成】个人中心我创建的文档、我参与过的文档~~
- ~~【已完成】代码生成增加数据导入导出功能~~
- ~~【已完成】excel导入前台增加进度信息~~
- ~~【已完成】流程中增加撤回功能~~
- ~~【已完成】流程审批增加附件上传~~
- 【未完成】微信端页面排版
- 【未完成】Kepler项目完整开发文档