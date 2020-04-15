> 使用springBoot + SpringData JPA搭建的企业级OA脚手架

### 目前的问题以及后期计划：

- ~~【已完成】【bug】jqGrid在前台分页模式下，搜索会有bug~~
- ~~【已完成】增加邮件服务~~
- ~~【已完成】实现忘记密码功能，配合邮件服务~~
- 【未完成】增加微信公众号框架，配合WxJava
- ~~【已完成】可配置定时任务（参考【若依】项目）~~
- 【未完成】第三方账号绑定
- 【未完成】第三方登录（QQ登录、微信登录）
- 【未完成】微信公众号配置页面（参考https://gitee.com/joolun/JooLun-wx/issues）
- ~~【已完成】各个菜单的查看权限（权限配置就和流程查看权限一样）~~
- ~~【已完成】系统管理中增加服务器信息以及项目信息（参考【若依】项目）~~
- ~~【已完成】代码自动生成（根据实体类属性）~~
- ~~【已完成】增加消息提醒~~
- 【未完成】mongodb附件前台导入导出功能
- ~~【已完成】多标签页情况下的页面排版~~
- ~~【已完成】个人中心我创建的文档、我参与过的文档~~
- 【未完成】编写Kepler项目完整开发文档

### 后台使用到的框架或插件
|  name |  description | page |
| :------------: | :------------: | :------------: |
| SpringBoot JPA | orm |  |
| Spring Security  |  安全框架 |  |
| hutool | 封装各种工具类 | https://www.hutool.club/docs/#/ |
| justauth | 第三方登陆 | https://docs.justauth.whnb.wang/#/ |
| WxJava | 微信 | https://gitee.com/binary/weixin-java-tools |
| velocity | 代码生成模板 | https://blog.csdn.net/tttzzztttzzz/article/details/90720877 |
| Quartz | 定时任务 |  |

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
