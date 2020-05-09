> 使用springBoot + SpringData JPA搭建的企业级OA脚手架

###  代码说明以及部署环境

JDK1.8

MySql5.5+

Redis

Mongodb

- 直接git导入到idea中，idea要提前装好lombok插件。
- 使用maven自动导包。
- 一些特殊表需要手动创建，导入sql文件夹下的文件（springSecurity记住我、定时任务）。
- 项目使用的是hibernate自动建表，所以业务类的表不需要手动创建。
- 启动项目会自动在数据库中创建表，第一次启动会自动创建一个系统管理员（admin|password）。

### 目前的问题以及后期计划：

- ~~【已完成】【bug】jqGrid在前台分页模式下，搜索会有bug~~
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
- 【未完成】编写Kepler项目完整开发文档

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