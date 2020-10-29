/**
 * 通用 js
 * ============================================================================
 * 版权所有 2016 。
 *
 * @author fallenpanda
 *
 * @version 1.0 2016-10-25 。
 * ============================================================================
 */
// 为空判断
function isNotNull(data){
    return data || data === 0 || data === false;
}
function isNull(data){
    return !isNotNull(data);
}

// 滚动
function scrollOffset($element) {
    $('html,body').animate({scrollTop:$element.offset().top}, 1000);
}


/* ajax 封装 */
/*======================================*/
// 异步 load content 页面
function loadURL(url, ajax_container) {
    if($("#MinimalTip").size()>0){
        $("#MinimalTip").remove();
    }
    $.ajax({
        type : 'GET',
        url : url,
        dataType : 'html',
        contentType: 'application/x-www-form-urlencoded; charset=utf-8',
        cache : false, // (warning: this will cause a timestamp and will call the request twice)
        async : true,
        beforeSend : function() {
            // loading
            var loadPage =
                '<div class="page-content" style="height: 100%">'+
                '<div class="cls-container" style="height: 100%">'+
                '<div class="cls-content">'+
                '<div class="spiner-example">'+
                '<div class="sk-spinner sk-spinner-three-bounce">'+
                '<div class="sk-bounce1"></div>'+
                '<div class="sk-bounce2"></div>'+
                '<div class="sk-bounce3"></div>'+
                '</div>'+
                '</div>'+
                '</div>'+
                '</div>'+
                '</div>';
            ajax_container.html(loadPage);

            // only draw breadcrumb if it is content material
            // TODO: check if document title injection refreshes in IE...
            // TODO: see the framerate for the animation in touch devices
            if (ajax_container[0] == $('#container')[0]) {
                // scroll up
                $('html, body').animate({
                    scrollTop : 0
                }, 'fast');
            } else {
                ajax_container.animate({
                    scrollTop : 0
                }, 'fast');
            }
        },
        complete: function(){
            // Handle the complete event
        },
        success : function(data) {
            // end loading...
            ajax_container.css({
                opacity : '0.0'
            }).html(data).delay(50).animate({
                opacity : '1.0'
            }, 300);

            //解决重复page-content导致padding过大
            setTimeout(function(){
                if($("div[id='page-content']").length>=2){
                    $("div[id='page-content']").each(function(i){
                        if(i!=0){
                            $(this).attr("id", "page-content"+i);
                        }
                    })
                }
            }, 350)

        },
        error : function(xhr, ajaxOptions, thrownError) {
            // error
            var errorPage =
                '<div id="page-content">'+
                '<div class="cls-container">'+
                '<div class="cls-content">'+
                '<h1 class="error-code text-info">404</h1>'+
                '<p class="text-main text-semibold text-lg text-uppercase">页面没有找到!</p>'+
                '<div class="pad-btm text-muted">'+
                '抱歉，你访问的页面地址有误，或者该页面不存在。'+
                '</div>'+
                '<div class="pad-top"><a class="btn-link" href="index">返回首页</a></div>'+
                '</div>'+
                '</div>'+
                '</div>';
            ajax_container.html(errorPage);
        }
    });
}

// 发送ajax请求
function __ajax_get(url, params, success, opt){
    var defaults = {
        cache: false,
        url: url,
        timeout : 300000,
        type:'GET',
        data: params,
        dataType: 'json',
        beforeSend: null,
        complete: null,
        success: function (data) {
            if(typeof success === 'function'){
                success(data, params);
            }
        },
        error: function (data) {
            if(typeof success === 'function'){
                success(data, params);
            }
        }
    };
    var options = $.extend({}, defaults, opt);

    $.ajax(options);
}

// 发送ajax请求
function __ajax_post(url, params, success, opt){

    var defaults = {
        cache: false,
        timeout : 300000,
        url: url,
        type:'POST',
        data: params,
        dataType: 'json',
        beforeSend: null,
        complete: null,
        success: function (data) {
            if(typeof success === 'function'){
                success(data, params);
            }
        },
        error: function (data) {
            if(typeof success === 'function'){
                success(data, params);
            }
        }

    };
    var options = $.extend({}, defaults, opt);

    $.ajax(options);
}

// 保存方法
function __form_save(form_id, url, success, opt) {
    var defaults = {
        dialogId: null,
        showMessage: true
    };
    var options = $.extend({}, defaults, opt);

    $('#'+form_id+' input:disabled').removeAttr('disabled');
    var params = $('#'+form_id).serialize();
    __ajax_post(url, params, function(data, params){
        if(options.showMessage){
            __toastr(data);
        }
        if(typeof success === 'function'){
            var params_save = $.extend({}, params, {
                dialogId: options.dialogId
            });
            success(data, params_save);
        }

    });
}



/* toastr 封装 */
/*======================================*/

function __toastr_primary(title, message){
    $.niftyNoty({
        type: 'primary',
        container : "floating",
        title : title,
        message : message,
        closeBtn : true,
        timer : 3000
    });
}

function __toastr_success(message){
    $.niftyNoty({
        type: 'success',
        container : "floating",
       // title : '成功',
        message : message,
        closeBtn : true,
        timer : 3000
    });
}

function __toastr_info(message){
    $.niftyNoty({
        type: 'info',
        container : "floating",
        title : '消息',
        message : message,
        closeBtn : true,
        timer : 3000
    });
}

function __toastr_warning(message){
    $.niftyNoty({
        type: 'warning',
        container : "floating",
        title : '提示',
        message : message,
        closeBtn : true,
        timer : 5000
    });
}

function __toastr_error(message){
    $.niftyNoty({
        type: 'danger',
        container : "floating",
        title : '错误',
        message : message,
        closeBtn : true,
        timer : 5000
    });
}

function __toastr(data){
    if (data) {
        if(data.state === 1){
            __toastr_success(data.msg);
        }else{
            __toastr_error(data.msg);
        }
    }
}

/*======================================*/

/*loading按钮*/
function __button_loading(buttonId){
    $("#"+buttonId).button("loading");
}
function __button_reset(buttonId){
    $("#"+buttonId).button("reset");
}


/* dialog 封装 */
/*======================================*/

var Admin_Package_BootstrapDialog = function (dialog) {
    this.setDialog(dialog)
};

Admin_Package_BootstrapDialog.prototype = {
    getDialog: function () {
        return this.dialog;
    },
    setDialog: function (dialog) {
        this.dialog = dialog;

        return this;
    },
    id : function () {
        return this.getDialog().getId()
    },
    enableButtons : function () {
        this.getDialog().enableButtons(true);

        return this;
    },
    disenableButtons : function () {
        this.getDialog().enableButtons(false);

        return this;
    },
    close : function () {
        this.getDialog().close();

        return this;
    }
};

// 弹出框
function __open_dialog(size, title, url, buttons, opt) {
    var defaults = {
        size: size,
        title: title,
        message: function(dialog) {
            var $message = $('<div class="form-read"></div>');
            var pageToLoad = dialog.getData('pageToLoad');
            $message.load(pageToLoad);
            return $message;
        },
        data: {
            'pageToLoad': url
        },
        closable: true,
        buttons: buttons,
        draggable:true,
        resizable: true
    };

    var options = $.extend({}, defaults, opt);

    var dialog = BootstrapDialog.show(options);

     return new Admin_Package_BootstrapDialog(dialog);
}

// 提示框
function __alert_dialog(type, size, title, message, buttons, opt) {
    var defaults = {
        type: type,
        size: size,
        title: title,
        message: message,
        buttons: buttons
    };
    var options = $.extend({}, defaults, opt);

    var dialog = BootstrapDialog.show(options);
    return new Admin_Package_BootstrapDialog(dialog);
}

// 表单 dialog
function __open_dialog_form(title, url, confirm, opt) {
    var dialogRef = __open_dialog(BootstrapDialog.SIZE_WIDE, title, url,
        [{
            icon: 'ti-check',
            label: '保存',
            cssClass: 'btn-primary',
            action: function () {
                // 禁用按钮
                dialogRef.disenableButtons();
                // 执行方法
                if(typeof confirm === 'function'){
                    confirm(dialogRef);
                }
            }
        }, {
            icon: 'ti-close',
            label: '关闭',
            cssClass: 'btn-default',
            action: function () {
                // 关闭 dialog
                dialogRef.close();
            }
        }],
        opt
    );
    return dialogRef;
}

// 只读表单 dialog
function __open_dialog_form_read(title, url, opt) {
    var dialogRef = __open_dialog(BootstrapDialog.SIZE_WIDE, title, url,
        [{
            icon: 'ti-close',
            label: '关闭',
            cssClass: 'btn-default',
            action: function () {
                // 关闭 dialog
                dialogRef.close();
            }
        }],
        opt
    );
    return dialogRef;
}

// 选择框 dialog
function __open_dialog_select(title, url, confirm, opt) {
    var dialogRef =  __open_dialog(BootstrapDialog.SIZE_NORMAL, title, url,
        [{
            label: '确定',
            cssClass: 'btn-primary',
            action: function () {
                // 禁用按钮
                dialogRef.disenableButtons();
                // 执行方法
                if(typeof confirm === 'function'){
                    confirm(dialogRef);
                }
            }
        }, {
            label: '取消',
            action: function () {
                // 关闭 dialog
                dialogRef.close();
            }
        }],
        opt
    );
    return dialogRef;
}

// 确认 提示框
function __confirm_dialog(title, message, confirm, cancel, opt) {
    var dialogRef = __alert_dialog(BootstrapDialog.TYPE_PRIMARY, BootstrapDialog.SIZE_SMALL, title, message,
        [{
            label: '取消',
            cssClass: 'btn-default',
            action: function () {
                // 关闭 dialog
                dialogRef.close();
                // 执行方法
                if(typeof cancel === 'function'){
                    cancel(dialogRef);
                }
            }
        }, {
            label: '确定',
            cssClass: 'btn-primary',
            action: function () {
                // 关闭 dialog
                dialogRef.close();
                // 执行方法
                if(typeof confirm === 'function'){
                    confirm(dialogRef);
                }
            }
        }],
        opt
    );
    return dialogRef;
}

// 确认 提示框
function __confirm_dialog_input(title, hint, value, confirm, cancel, opt) {
    var $input = $('<input type="text" class="form-control" value="'+value+'" placeholder="'+hint+'" autofocus>');

    var dialogRef = __alert_dialog(BootstrapDialog.TYPE_PRIMARY, BootstrapDialog.SIZE_SMALL, title,
        function () {
            var $message = $('<div class="form-group"></div>');
            $message.append($input);
            return $message;
        },
        [{
            label: '取消',
            cssClass: 'btn-default',
            action: function () {
                // 关闭 dialog
                dialogRef.close();
                // 执行方法
                if(typeof cancel === 'function'){
                    cancel(dialogRef);
                }
            }
        }, {
            label: '确定',
            cssClass: 'btn-primary',
            action: function () {
                // 禁用按钮
                dialogRef.disenableButtons();
                // 输入框
                var inputValue = $input.val();
                // 执行方法
                if(typeof confirm === 'function'){
                    confirm(dialogRef, inputValue);
                }
            }
        }],
        opt
    );
    return dialogRef;
}


/**
 * 通用数据单选选择框
 * @param id
 * @param title 标题
 * @param dataUrl 获取数据地址
 * @param colNames jqgrid表头
 * @param colModel jqgrid表体
 * @param selectedId
 * @param selectedName
 * @param callback
 * @param opt
 */
function __common_dialog_select(id, title, dataUrl, colNames, colModel, selectedId, selectedName, callback, opt){
    setCookie("jqGrid_common_layX_id", id);
    setCookie("jqGrid_common_selectIds", selectedId);
    setCookie("jqGrid_common_selectNames", selectedName);
    setCookie("jqGrid_common_url", dataUrl);
    setCookie("jqGrid_common_colNames", JSON.stringify(colNames));
    setCookie("jqGrid_common_colModel", JSON.stringify(colModel));
    setCookie("jqGrid_common_width", "");
    __layX_html_select(id, title, "../common/dialog/selectDialog", function(id, button, event){
        fn_common_dialog_select(callback);
    });
}

/**
 * 通用数据多选选择框
 * @param id 自定义layX dialog id
 * @param title 标题
 * @param dataUrl 获取数据地址
 * @param colNames jqgrid表头
 * @param colModel jqgrid表体
 * @param selectIds 默认选中的ids（英文逗号隔开）
 * @param selectedNames
 * @param callback
 * @param opt
 */
function __common_dialog_selects(id, title, dataUrl, colNames, colModel, selectIds, selectedNames, callback, opt){
    setCookie("jqGrid_common_layX_id", id);
    setCookie("jqGrid_common_selectIds", selectIds);
    setCookie("jqGrid_common_selectedNames", selectedNames);
    setCookie("jqGrid_common_url", dataUrl);
    setCookie("jqGrid_common_colNames", JSON.stringify(colNames));
    setCookie("jqGrid_common_colModel", JSON.stringify(colModel));
    setCookie("jqGrid_common_width", "");
    // __open_dialog_select(title, "../common/dialog/selectDialogs", function(dialogRef){
    //     dialogRef.enableButtons();
    //     dialogRef.close();
    //     fn_common_dialogs_select(callback);
    // }, opt);
    __layX_html_select(id, title, "../common/dialog/selectDialogs", function(id, button, event){
        fn_common_dialogs_select(callback);
    });
}

/**
 * 选择成员多选
 */
function __users_dialog_select(dialogId, selectedIds, selectedNames, callback, opt){
    var dataUrl = "/main/member/list";
    var colNames = ["姓名","id"];
    var colModel = [
        {name: 'name', index: 'name', width: 100, sortable: false, searchoptions: {sopt: ['cn']}},
        {name: 'id', index: 'id', key: true, hidden: true}
    ];
    __common_dialog_selects(dialogId, "成员选择", dataUrl, colNames, colModel, selectedIds, selectedNames, callback, opt);
}

/**
 * 选择成员单选
 */
function __user_dialog_select(dialogId, selectedId, selectedName, callback, opt){
    var dataUrl = "/main/member/list";
    var colNames = ["姓名","id"];
    var colModel = [
        {name: 'name', index: 'name', width: 100, sortable: false, searchoptions: {sopt: ['cn']}},
        {name: 'id', index: 'id', key: true, hidden: true}
    ];
    __common_dialog_select(dialogId, "成员选择", dataUrl, colNames, colModel, selectedId, selectedName, callback, opt);
}

/**
 * 岗位多选
 */
function __posts_dialog_select(dialogId, postIds, postNames, callback, opt){
    var dataUrl = "/main/post/list";
    var colNames = ["名称","id"];
    var colModel = [
        {name: 'name', index: 'name', width: 100, sortable: false, searchoptions: {sopt: ['cn']}},
        {name: 'id', index: 'id', key: true, hidden: true}
    ];
    __common_dialog_selects(dialogId, "岗位选择", dataUrl, colNames, colModel, postIds, postNames, callback, opt);
}

/**
 * 职权多选
 */
function __powers_dialog_select(dialogId, powerIds, powerNames, callback, opt){
    var dataUrl = "/main/power/list_department";
    var colNames = ["名称","id"];
    var colModel = [
        {name: 'name', index: 'name', width: 100, sortable: false, searchoptions: {sopt: ['cn']}},
        {name: 'id', index: 'id', key: true, hidden: true}
    ];
    __common_dialog_selects(dialogId, "职权选择", dataUrl, colNames, colModel, powerIds, powerNames, callback, opt);
}

/**
 * 群组多选
 */
function __groups_dialog_select(dialogId, groupIds, groupNames, callback, opt){
    var dataUrl = "/main/group/list";
    var colNames = ["名称","id"];
    var colModel = [
        {name: 'name', index: 'name', width: 100, sortable: false, searchoptions: {sopt: ['cn']}},
        {name: 'id', index: 'id', key: true, hidden: true}
    ];
    __common_dialog_selects(dialogId, "群组选择", dataUrl, colNames, colModel, groupIds, groupNames, callback, opt);
}

/**
 * 部门多选
 * fn_dialog_departments_select
 */
function __departments_dialog_select(dialogId, deptIds, callback){
    __layX_html_select(dialogId,"选择部门", "../main/department/dialog?multi=y&deptIds="+deptIds, function(id, button, event){
        fn_dialog_departments_select(callback);
    });
}

/**
 * 部门单选
 * fn_dialog_departments_select
 */
function __department_dialog_select(dialogId, deptIds, callback){
    __layX_html_select(dialogId, "选择部门", "../main/department/dialog?multi=n&deptIds="+deptIds, function(id, button, event){
        fn_dialog_departments_select(callback);
    });
}

/**
 * excel文件导入页面
 */
function __excel_import_view(importUrl, name){
    setCookie("excelImportUrl", importUrl);
    __layX_html_read("excel-import-dialog", name ? name : "excel导入", ctx+"main/file/excelImportView",{
        width : "50%",
        shadable : 0.6,
        minMenu : false
    })
}

/* validate 封装 */
/*======================================*/
//digits:true整数
function __init_validate(form_id, opt ,wx) {
    var defaults = {
        ignore: "",
        // 设置错误信息存放标签
        errorElement : 'em',
        // 指定错误信息位置
        errorPlacement : function(error, element) {
            if(wx == 1){
                element.parent().after(error);
            }else{
                if (element.is(':checkbox') || element.is(':radio')) {
                    error.appendTo(element.parent().parent());
                } else if(element.parent().hasClass("input-group")) {
                    error.appendTo(element.parent().parent());
                } else{
                    error.appendTo(element.parent());
                }
            }

        }
    };
    var options = $.extend({}, defaults, opt);

    $('#'+form_id).validate(options)
}


/* url 返回菜单视图 */
/*======================================*/
function returnToHashUrl() {
    var urlHash = window.location.hash;
    var url = urlHash.substring(1,urlHash.length);
    loadURL(url,$('#page-content'));
}

/*Switchery开关样式*/
/*======================================*/
function __switchery(element_id, opt){
    new Switchery(document.getElementById(element_id), opt);
}


/* dropzone 封装 */
/*======================================*/

function __init_dropzone_read(element_id) {
    __init_dropzone(element_id, true, {maxFiles: 0});
}

function __init_dropzone_edit(element_id, onchange, opt) {
    __init_dropzone(element_id, false, onchange, opt);
}

function __init_dropzone(element_id, readonly, onchange, opt) {

    var $element = $('#'+element_id);
    // 已上传文件
    var fileIds = $element.val();

    if (!readonly) {
        $element.parent().append('<div><button type="button" id="'+element_id+'-dz-upload" class="btn btn-primary btn-sm dz-clickable"><i class="ti-plus"></i> 添加文件...</button></div>');
    }
    $element.parent().append('<div id="'+element_id+'-dz-previews"></div>');

    var template =
        '<div id="dz-template" class="pad-top bord-btm">'+
        '<div class="media">'+
        '<div class="media-body">'+
        '<div class="media-block">'+
        '<div class="media-left">'+
        '<img class="img-responsive media-img" data-dz-thumbnail>'+
        '</div>'+
        '<div class="media-body">'+
        '<p class="text-main text-bold mar-no text-overflow" data-dz-name></p>'+
        '<p class="text-sm" data-dz-size></p>'+
        '<span class="dz-error text-danger text-sm" data-dz-errormessage></span>'+
        '<div id="dz-total-progress" style="opacity:0">'+
        '<div class="progress progress-xs active" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0">'+
        '<div class="progress-bar progress-bar-success" style="width:0%;" data-dz-uploadprogress></div>'+
        '</div>'+
        '</div>'+
        '</div>'+
        '</div>'+
        '</div>'+
        '<div class="media-right">'+
        (readonly?'<a data-dz-preview target="_blank" class="btn btn-xs btn-primary"><i class="ti-book"></i> 预览</a>':'<button data-dz-remove class="btn btn-xs btn-danger"><i class="ti-close"></i> 删除</button>')+
        '<a data-dz-download class="btn btn-xs btn-success" disabled><i class="ti-download"></i> 下载</a>'+
        '</div>'+
        '</div>'+
        '</div>';

    var defaults = {

        url: '../main/file/upload',
        maxFiles: 10,// 最大文件数
        maxFilesize: 100,// 最大文件大小(MB)
        parallelUploads: 1,// 最大同时上传文件数
        previewTemplate: template,
        previewsContainer: '#'+element_id+'-dz-previews',
        clickable: readonly?false:'#'+element_id+'-dz-upload',
        thumbnailWidth: 40,
        thumbnailHeight: 40,

        // 中文
        dictDefaultMessage: '上传文件拖拽至此',
        dictFallbackMessage: '当前浏览器不支持此插件',
        dictFallbackText: '请使用以下兼容模式上传附件',
        dictInvalidFileType: '无法上传此类型的文件',
        dictFileTooBig: '无法上传超过 {{maxFilesize}}MB 的文件 当前文件大小：({{filesize}}MB',
        dictResponseError: '服务器错误 {{statusCode}} code',
        dictCancelUpload: '取消上传',
        dictCancelUploadConfirmation: '你确定要取消上传当前文件吗？',
        dictRemoveFile: '删除文件',
        dictMaxFilesExceeded: '无法上传更多的文件',

        init: function() {
            var myDropzone = this;

            myDropzone.on('addedfile', function(file) {
                if (isNotNull(file.id)) {
                    $(file.previewElement).attr('file-id', file.id);
                    $(file.previewElement).attr('file-binding', true);// 标记为 已绑定
                    $(file.previewElement).find('[data-dz-download]').attr('disabled', false).attr('href', file.url);


                    if (isNotNull(file.type) && file.type.match(/image.*/)) {
                        myDropzone.emit("thumbnail", file, file.url);
                        $(file.previewElement).find('[data-dz-preview]').attr('href', "../main/file/imgView?fileId="+file.id);
                    }else{
                        $(file.previewElement).find('[data-dz-preview]').attr('href', file.fileViewUrl);
                    }
                }
                if (isNull(file.type) || !file.type.match(/image.*/)) {
                    myDropzone.emit("thumbnail", file, "../static/img/file-img.png");
                }
            });

            // Update the total progress bar
            myDropzone.on('totaluploadprogress', function(progress) {
                $(myDropzone.previewsContainer).find('#dz-total-progress .progress-bar').width(progress + '%');
            });

            myDropzone.on('sending', function(file) {
                // Show the total progress bar when upload starts
                $(myDropzone.previewsContainer).find('#dz-total-progress').css('opacity','1');
            });

            // Hide the total progress bar when nothing's uploading anymore
            myDropzone.on('queuecomplete', function(progress) {
                $(myDropzone.previewsContainer).find('#dz-total-progress').css('opacity','0');
            });

            myDropzone.on('success', function(file, data) {
                if (data.state === 1) {
                    var fileId = data.data.id;
                    var filePath = ".."+data.data.url;

                    $(file.previewElement).attr('file-id', fileId);
                    $(file.previewElement).find('[data-dz-name]').append('<strong class="pad-lft text-success"><i class="ti-check"></i></strong>');
                    $(file.previewElement).find('[data-dz-download]').attr('disabled', false).attr('href', filePath);
                    // $(file.previewElement).find('[data-dz-preview]').attr('href', "http://localhost:8012/onlinePreview?url="+encodeURIComponent(data.completeUrl));

                    // 更新上传文件Id
                    updateFileId('add', fileId);
                }
            });

            myDropzone.on('error', function(file, data) {
                $(file.previewElement).find('[data-dz-name]').append('<strong class="pad-lft text-danger"><i class="ti-close"></i></strong>');
            });

            myDropzone.on('removedfile', function(file) {
                // 删除服务器文件
                var id = $(file.previewElement).attr('file-id');
                var isBinding = $(file.previewElement).attr('file-binding') === 'true';
                if (!readonly && id && !isBinding) {// 忽略 只读状态 已绑定文件
                    __ajax_post('../main/file/delete?keyId='+id, null, function (data) {
                        if (data.state === 1) {
                            __toastr_success('删除成功');

                            // 更新上传文件Id
                            updateFileId('delete', id);
                        }
                    })
                } else {
                    // 更新上传文件Id
                    updateFileId('delete');
                }
                if (isNotNull(file.id)) {
                    myDropzone.options.maxFiles = myDropzone.options.maxFiles + 1;
                }
            });

            myDropzone.on('maxfilesexceeded', function(file) {
                myDropzone.removeFile(file);
                __toastr_error('超过最大上传文件数: '+myDropzone.options.maxFiles)
            });

            if (isNotNull(fileIds)) {
                __ajax_get('../main/file/fileList', {keyId: fileIds}, function (data) {
                    if (data.state === 1) {
                        var existingFileCount = 0;// The number of files already uploaded
                        $(data.data).each(function (i, obj) {
                            var mockFile = {id: obj.id, name: obj.name, size: obj.size, type: obj.type, url: obj.filePath, fileViewUrl: obj.fileViewUrl};
                            myDropzone.emit("addedfile", mockFile);
                            myDropzone.emit("complete", mockFile);

                            existingFileCount++;
                        });
                        myDropzone.options.maxFiles = myDropzone.options.maxFiles - existingFileCount;
                    }
                })
            }

            function updateFileId(event, id) {
                var ids = '';
                $(myDropzone.previewsContainer).find('div#dz-template').each(function () {
                    var id = $(this).attr('file-id');
                    if (ids.length>0)
                        ids += ',';
                    ids += id;
                });
                $element.val(ids);

                if(typeof onchange === 'function'){
                    onchange(ids, event, id);
                }
            }

        }
    };
    var options = $.extend({}, defaults, opt);
    $element.parents('.form-group').dropzone(options);

}

/* 省市联动选择 */
/*======================================*/
function __city_picker(elem, province, city, district, opt){
    var defaults = {
        province : province,
        city : city,
        district : district,
        placeholder : "点击从下拉面板中选择省/市/区",
        responsive : true
    };
    var option = $.extend({},defaults,opt);
    elem.citypicker("reset");
    elem.citypicker("destroy");
    elem.citypicker(option);
}


/* jqGrid 封装 */
/*======================================*/

function __init_jqgrid(table_id, page_id, url, colNames, colModel, ifPage, opt) {
    var defaults = {
        url : url,
        loadonce : ifPage!=null ? !ifPage : false,
        datatype : 'json',
        height : 'auto',
        autowidth : true,
        colNames : colNames,
        colModel : colModel,
        pager : page_id!=null ? '#'+page_id : false,
        sortorder : 'desc',
        rowList : [10, 30, 50, 100],
        rowNum : page_id!=null&&page_id!== '' ? 10 : 10000,
        rownumbers : true,
        jsonReader : {
            root : 'dataRows',
            repeatitems : false
        },
        prmNames : {
            search : 'search'
        },
        postData : {
            ifPage : ifPage!=null ? ifPage : true
        },
        gridview : true,
        viewrecords : true,
        viewsortcols : [true, 'vertical', true],
        subGrid : false,
        subGridOptions : {
            plusicon : "ace-icon fa fa-plus center bigger-110 blue", //展开图标
            minusicon : "ace-icon fa fa-minus center bigger-110 blue", //收缩图标
            openicon : "ace-icon fa fa-chevron-right center orange", //打开时左侧图标
            reloadOnExpand : false,
            selectOnExpand : true
        },
        loadComplete : function(){
            $.minimalTips(); //增加tip提示工具
        },
        resizeStart : function(event, index){
            $('#'+table_id).closest(".ui-jqgrid-bdiv").css({"overflow-x":"auto"})
        }
    };

    var options = $.extend({}, defaults, opt);
    $('#'+table_id).jqGrid(options);
    $('#'+table_id).jqGrid('navGrid', '#'+page_id, {
        add: false,
        edit: false,
        del: false,
        search: false
    });

    $('#'+table_id).jqGrid('filterToolbar', {searchOperators:false, stringResult:true});
    $(".ui-search-table").find("input").attr("autocomplete", "off");
}

//表格刷新
function __reflash_jqgrid(tableId, param){
    $('#'+tableId).jqGrid().setGridParam({datatype:'json',postData: param}).trigger('reloadGrid');
}

//通过id获取所有数据jqgrid
function __jqGrid_data(tableId, id){
    return jQuery("#"+tableId).jqGrid('getRowData', id)
}

//调整表格宽度
function __jqGrid_width(tableId, width){
    $("#"+tableId).jqGrid("setGridWidth", width);
}


/* __select2 */
/*======================================*/
function __select2(selectId, placeholder, opt){
    var $select = $("#"+selectId);
    var defaults = {
        placeholder : placeholder ? placeholder : "请选择",
        allowClear: true,
        width: "100%"
    };
    var options = $.extend({}, defaults, opt);
    $select.select2(options);
    $select.on("change", function(){
        if($select.attr("aria-required")){
            $select.valid();
        }
    });
    $(".bootstrap-dialog").removeAttr("tabindex");
}


/* selectpicker */
/*======================================*/
function __selectpicker(selectId, opt){
    var $select = $("#"+selectId);
    var defaults = {
        style: 'btn-white'
    };
    var options = $.extend({}, defaults, opt);
    $select.selectpicker(options);
}


/* treepicker */
/*======================================*/
function __treepicker(selectId, opt){
    var $select = $("#"+selectId);
    var defaults = {
        style: 'btn-white'
    };
    var options = $.extend({}, defaults, opt);
    $select.treepicker(options);
}


/* panelOverlay */
/*======================================*/
function __panelOverlay(eleId, opt){
    var $ele = $("#" + eleId);
    var defaults = {
        title: '请稍后...'
    };
    var options = $.extend({}, defaults, opt);
    $ele.panelOverlay(options);
}

function __panelOverlay_show(eleId){
    $("#" + eleId).panelOverlay('show');
}

function __panelOverlay_hide(eleId){
    $("#" + eleId).panelOverlay('hide');
}

/* laydate */
/*======================================*/
function __laydate(eleId, type, isRange, opt){
    var defaults = {
        elem : "#" + eleId,
        type : type, //year,month,date,time,datetime
        range : isRange ? '~' : false, //是否是时间范围选择
        // value : new Date(), //初始值
        isInitValue : false, //是否自动填充初始值
        min : '1900-1-1', //min: -7，即代表最小日期在7天前，正数代表若干天后
        max : '2099-12-31',
        trigger : 'focus', //自定义弹出控件的事件
        show : false, //默认显示, 如果设置: true，则控件默认显示在绑定元素的区域
        position : 'absolute', //定位方式
        zIndex : 66666666, //层叠顺序
        showBottom : true, //是否显示底部栏
        btns : ['clear', 'now', 'confirm'], //工具按钮，右下角显示的按钮，会按照数组顺序排列
        lang : 'cn', //语言
        calendar : false, //是否显示公历节日
        mark : null, //标注重要日子，如： {'0-12-31': '跨年', '0-0-10': '工资', '2017-8-21': '发布'}
        done : function(value, date, endDate){ //控件选择完毕后的回调, 三个参数，分别代表：生成的值、日期时间对象、结束的日期时间对象
            if($("#"+eleId).attr("aria-required")){
                $("#"+eleId).valid();
            }
        }
    };
    var options = $.extend({}, defaults, opt);
    laydate.render(options);
}

function __laydate_year(eleId, opt){
    __laydate(eleId, "year", false, opt);
}
function __laydate_month(eleId, opt){
    __laydate(eleId, "month", false, opt);
}
function __laydate_date(eleId, opt){
    __laydate(eleId, "date", false, opt);
}
function __laydate_time(eleId, opt){
    __laydate(eleId, "time", false, opt);
}
function __laydate_datetime(eleId, opt){
    __laydate(eleId, "datetime", false, opt);
}

function __laydate_year_range(eleId, opt){
    __laydate(eleId, "year", true, opt);
}
function __laydate_month_range(eleId, opt){
    __laydate(eleId, "month", true, opt);
}
function __laydate_date_range(eleId, opt){
    __laydate(eleId, "date", true, opt);
}
function __laydate_time_range(eleId, opt){
    __laydate(eleId, "time", true, opt);
}
function __laydate_datetime_range(eleId, opt){
    __laydate(eleId, "datetime", true, opt);
}


/* layX dialog */
/*======================================*/
function __layX(dialogId, title, type, content, buttons, onloadAfter, opt){
    var defaults = {
        //唯一ID
        id : dialogId,

        //图标 或 标题栏左边内容
        icon : "<i class='fa fa-paper-plane-o'></i>",

        //标题
        title : title,

        //初始化宽度
        width : "80%",

        //初始化高度
        height : "80%",

        //最小宽度
        minWidth : "600",

        //最小高度
        minHeight : "50",

        //存储窗口位置、大小信息
        storeStatus : false,

        //控制窗口拖动到顶部自动最大化
        dragInTopToMax : true,

        //控制初始化窗口时超出可视区域自动最大化
        isOverToMax : true,

        //窗口位置（两个字母组合 t r b l c  ct代表正中间）
        position : "ct",

        //是否显示控制标题栏 设置 false 将不显示控制标题栏，同时窗口拖动功能失效，需手动调用关闭方法关闭窗口
        control : true,

        //是否启用esc按键关闭窗口
        escKey : true,

        //嵌入窗口样式 支持强大的CSS样式表、如需插入多行可用 layx.multiLine(function(){/* 多行样式 */})。通过修改此属性可以获得最大外观修改效果
        style : "",

        //控制标题栏样式 修改控制标题栏样式，如：background-color:#f00;color:#fff;
        controlStyle : "",

        //窗口背景颜色
        bgColor : "#fff",

        //窗口阴影
        shadow : true,

        //窗口边框 支持css边框设置属性
        border : false,

        //设置圆角 设置0px将不启用圆角，圆角必须带 px单位
        borderRadius : "3px",

        //窗口皮肤 【default/cloud/turquoise/river/asphalt】
        skin : "default",

        //设置窗口获取焦点前置,设置false后，窗口将不能前置
        focusToReveal : true,

        //窗口类型 【html：文本类型/url：页面类型/group：窗口组类型】
        type : type,

        //是否浮动窗口 设置为DOM对象时，窗口将吸附到按钮上
        floatTarget : false,

        //浮动方向
        floatDirection : "ct",

        //窗口存在闪烁 设置false后打开已存在的窗口将不会闪烁。
        existFlicker : true,

        //启用跨域点击切换窗口 设置false后将不支持非同源网站点击切换窗口，设置true有少许性能问题，如果没有涉及到第三方网页，建议设置为false。
        enableDomainFocus : false,

        //文本窗口内容 设置 type:html 有效，支持传入字符串、HTML代码以及DOM对象
        content : content,

        //设置文本窗口DOM对象拷贝模式 如果content传入的是DOM对象并且DOM对象包含触发事件，需设置false采用原对象填充content内容。设置为true拷贝一份填充content内容，但所有事件失效。
        cloneElementContent : true,

        //页面窗口地址 设置 type:url 有效，设置页面窗口URL地址，支持本地路径、互联网路径以及 'about:blank'
        url : "",

        //主窗口透明度 设置主窗口透明度、取值0~1 浮点值【0：完全透明，1：不透明，0.5：半透明】
        opacity : 1,

        //窗口阻隔、遮罩 设置true窗口以外的区域将不能操作，支持传入0-1浮点范围数值，用来设置阻隔透明度
        shadable : 0.5,

        //设置true点击空白地方将关闭窗口
        shadeDestroy : false,

        //设置true窗口将为只读、窗口将禁止右键功能
        readonly : false,

        //窗口内容加载时显示文本
        loadingText : "内容正在加载中，请稍后",

        //置顶按钮
        stickMenu : false,

        //最小化按钮
        minMenu : false,

        //最大化按钮
        maxMenu : true,

        //调试按钮
        debugMenu : true,

        //关闭按钮
        closeMenu : true,

        //设置窗口关闭等待秒数，如5000毫秒。设置false需手动关闭窗口。
        autodestroy : false,

        //是否允许拖曳调整大小
        resizable : true,

        //设置false将不启用状态栏。支持传入HTML字符串、DOM对象
        statusBar : buttons.length>0,

        //设置 statusBar:true 有效，配置见 按钮参数 buttons
        buttons : buttons,

        //是否允许拖动窗口位置
        movable : true,

        event : {
            onload : {
                // 加载之前，return false 不执行
                before : function(layxWindow, winform){

                },
                after : onloadAfter
            }
        }
    };

    var options = $.extend({}, defaults, opt);
    layx.open(options);
}

//关闭layX dialog
function __layX_close(id){
    layx.destroy(id);
}

//调整dialog高度
function __layX_adapt_height(id){
    var $layXEle = $("#layx-"+id);
    var $contentEle = $("#layx-"+id+"-html").children(":first");
    if($layXEle && $contentEle){
        if($layXEle.height() > $contentEle.height() || $contentEle.height() < 600){
            layx.setSize(id, {height : $contentEle.height()+80});
        }
    }
}

//只读dialog 带一个关闭按钮
function __layX_html_read(dialogId, title, content, opt){

    var buttons = [{
        id : 'help-btn',
        classes : ["btn", "hidden"],
        label : '阻止回车'
    },{
        id : 'close',
        classes : ["btn", "btn-default","mar-lft"],
        label : '关闭',
        callback:function(id, button, event){
            layx.destroy(id);
        }
    }];

    __layX(dialogId, title, "html", "", buttons, function(ayxWindow, winform){
        setTimeout(function(){
            $(".layx-button-item").removeClass("layx-button-item");
            loadURL(content, $("#layx-"+dialogId+"-html"));
        }, 200)
    },opt)
}

//一个保存按钮和一个关闭按钮
function __layX_html_save(dialogId, title, content, saveFunction, opt){

    var buttons = [{
        id : 'help-btn',
        classes : ["btn", "hidden"],
        label : '阻止回车'
    },{
        id : 'save',
        classes : ["btn", "btn-success"],
        label : '保存',
        callback : saveFunction
    },{
        id : 'close',
        classes : ["btn", "btn-default"],
        label : '关闭',
        callback:function(id, button, event){
            layx.destroy(id);
        }
    }];

    __layX(dialogId, title, "html", "", buttons, function(ayxWindow, winform){
        setTimeout(function(){
            $(".layx-button-item").removeClass("layx-button-item");
            loadURL(content, $("#layx-"+dialogId+"-html"));
        }, 200)
    },opt)
}

//选择dialog（一个确定按钮，加遮罩层）
function __layX_html_select(dialogId, title, url, confirm, opt){
    var buttons = [{
        id : 'help-btn',
        classes : ["btn", "hidden"],
        label : '阻止回车'
    },{
        id : 'save',
        classes : ["btn", "btn-success"],
        label : '确定',
        callback : confirm
    },{
        id : 'close',
        classes : ["btn", "btn-default"],
        label : '关闭',
        callback:function(id, button, event){
            layx.destroy(id);
        }
    }];

    var options = $.extend({}, {
        shadable : 0.8,
        width : "605",
        height : "68%",
        minMenu : false,
        maxMenu : false,
        dragInTopToMax : false,
        resizable : false
    }, opt);

    __layX(dialogId, title, "html", "", buttons, function(ayxWindow, winform){
        setTimeout(function(){
            $(".layx-button-item").removeClass("layx-button-item");
            loadURL(url, $("#layx-"+dialogId+"-html"));
        }, 200)
    }, options)
}

//流程创建input dialog（保存草稿按钮，提交按钮，关闭按钮）
function __layX_flow_input(dialogId, title, url, commitFun, saveFun, opt){
    var buttons = [{
        id : 'help-btn',
        classes : ["btn", "hidden"],
        label : '阻止回车'
    },{
        id : 'commit',
        classes : ["btn", "btn-primary"],
        label : '提交',
        callback : commitFun
    },{
        id : 'save',
        classes : ["btn", "btn-success"],
        label : '保存',
        callback : saveFun
    },{
        id : 'close',
        classes : ["btn", "btn-default"],
        label : '关闭',
        callback:function(id, button, event){
            layx.destroy(id);
        }
    }];

    __layX(dialogId, title, "html", "", buttons, function(ayxWindow, winform){
        setTimeout(function(){
            $(".layx-button-item").removeClass("layx-button-item");
            loadURL(url, $("#layx-"+dialogId+"-html"));
        }, 200)
    },opt)
}

/* summernote */
/*======================================*/
function __summernote($ele, opt){
    if($ele){
        var defaults = {
            placeholder : "请输入内容",
            lang : 'zh-CN',
            height : 150,
            dialogsInBody : false,
            tooltip : true,
            // dialogsFade : false,
            // shortcuts : false,
            // disableDragAndDrop : true,
            toolbar : [
                ['style', ['style']],
                ['font', ['fontname','fontsize','bold','italic', 'underline', 'clear']],
                ['color', ['color']],
                ['para', ['ul', 'ol', 'paragraph']],
                ['table', ['table']],
                ['insert', ['hr', 'flowfile']]
            ],
            callbacks : {
                onDialogShown : function(){ //bootstrap的遮罩层和layx的遮罩层冲突，这里给隐藏掉
                    $("body").find(".modal-backdrop:last").hide();
                }
            }
        };

        var options = $.extend({}, defaults, opt);
        $ele.summernote(options);
    }else{
        __toastr_warning("summernote init fail")
    }
}

/* flow */
/*======================================*/
function __flow_duty_handle(entityData){
    if(!entityData.keyId){entityData.keyId = ""}
    var keyId = entityData.keyId=="create" ? "" : entityData.keyId;
    var dataUrl = "/main/duty/findDutiesOfProcess";
    var param = {path : entityData.path, keyId : keyId};
    __ajax_get(dataUrl, param, function(data){
        var $layx = $("#layx-"+entityData.keyId).size()==0 && isNotNull(entityData.parentId) ? $("#layx-"+entityData.parentId) : $layx = $("#layx-"+entityData.keyId);
        if(data && data.dataRows.length>0){
            $("#"+entityData.formId).find("input[name='flowDutyId']").val(data.dataRows[0].id);
            $("#"+entityData.formId).find("input[name='flowDutyName']").val(data.dataRows[0].name);
            $layx.find($("#"+entityData.titleId)).text(" | " + data.dataRows[0].name).append('<button class="btn btn-xs btn-default mar-lft" onclick="__flow_duty_select('+ JSON.stringify(entityData).replace(/"/g, '&quot;') +')">重新选择</button>');
        }else{
            $layx.find($("#"+entityData.titleId)).text(" | 没有符合的职责");
        }
    })
}

//流程中使用职权选择
function __flow_duty_select(entityData){
    if(!entityData.keyId){entityData.keyId = ""}
    var keyId = entityData.keyId=="create" ? "" : entityData.keyId;
    var dataUrl = "/main/duty/findDutiesOfProcess?path="+entityData.path+"&keyId="+keyId;
    var colNames = ["名称","id"];
    var colModel = [
        {name: 'name', index: 'name', width: 100, sortable: false, searchoptions: {sopt: ['cn']}},
        {name: 'id', index: 'id', key: true, hidden: true}
    ];
    setCookie("jqGrid_common_page", "false");
    __common_dialog_select("flow_duty_select", "职权选择", dataUrl, colNames, colModel, "","", function(data){
        if(data && data.id){
            $("#"+entityData.formId).find("input[name='flowDutyId']").val(data.id);
            $("#"+entityData.formId).find("input[name='flowDutyName']").val(data.name);
            var $layx = $("#layx-"+entityData.keyId).size()==0 && isNotNull(entityData.parentId) ? $("#layx-"+entityData.parentId) : $layx = $("#layx-"+entityData.keyId);
            // $("#"+entityData.layxId).find($("#"+entityData.titleId)).text(" | " + data.name).append('<button class="btn btn-xs btn-default mar-lft" onclick="__flow_duty_select('+ JSON.stringify(entityData).replace(/"/g, '&quot;') +')">重新选择</button>');
            $layx.find($("#"+entityData.titleId)).text(" | " + data.name).append('<button class="btn btn-xs btn-default mar-lft" onclick="__flow_duty_select('+ JSON.stringify(entityData).replace(/"/g, '&quot;') +')">重新选择</button>');
            __layX_close("flow_duty_select");
        }else{
            __toastr_warning("未选择职责")
        }
    });
}

/**
 * 流程input页面处理
 * @param entityData 实体信息
 * @param save 保存方法fun
 * @param commit 提交方法fun
 */
function __flow_button_input_handle(entityData, save, commit){
    if(!entityData.step){entityData.step = 1}
    if(!entityData.keyId){entityData.keyId = ""}

    var url = "/flow/taskEntity/getFlowInfo";
    var p = {keyId : entityData.keyId};
    __ajax_get(url, p, function(data){
        if(!data){
            __toastr_warning("获取流程信息失败");
            return false;
        }

        var flag = $("#layx-"+entityData.keyId).size()==0 && isNotNull(entityData.parentId) ? "layx-"+entityData.parentId : "layx-"+entityData.keyId;
        var btn_close = '<button class="btn btn-default mar-lft" title="关闭" id="'+flag+'-button-close">关闭</button>';
        var btn_save = '<button class="btn btn-success" title="保存" id="'+flag+'-button-save">保存</button>';
        var btn_submit = '<button class="btn btn-primary" title="提交" id="'+flag+'-button-submit">提交</button>';
        var btn_reSubmit = '<button class="btn btn-primary" title="再提交" id="'+flag+'-button-submit">再提交</button>';

        //先把按钮组清空，并直接添加一个关闭按钮
        $("#"+flag).find("div.layx-buttons").empty();
        $("#"+flag).find("div.layx-buttons").prepend(btn_close);
        $("#"+flag+"-button-close").on("click", function(){
            __layX_close($("#layx-"+entityData.keyId).size()==0 && isNotNull(entityData.parentId) ? entityData.parentId : entityData.keyId);
        });

        //再添加流程所需按钮
        for(var i=0;i<data.flowButtonList.length;i++){
            if(data.flowButtonList[i].name == "save"){
                $("#"+flag).find("div.layx-buttons").prepend(btn_save);
                $("#"+flag+"-button-save").on("click", save)
            }else if(data.flowButtonList[i].name == "submit"){
                $("#"+flag).find("div.layx-buttons").prepend(btn_submit);
                $("#"+flag+"-button-submit").on("click", commit)
            }else if(data.flowButtonList[i].name == "reSubmit"){
                $("#"+flag).find("div.layx-buttons").prepend(btn_reSubmit);
                $("#"+flag+"-button-submit").on("click", commit)
            }
        }
    })
}

/**
 * 流程read页面处理
 * @param entityData 实体信息
 * @param approve 审批通过方法fun
 * @param back 退回方法fun
 * @param deny 否决方法fun
 * @param edit 编辑按钮方法fun
 * @param withdraw 撤回按钮方法fun
 */
function __flow_button_read_handle(entityData, approve, back, deny, edit, withdraw){
    var url = "/flow/taskEntity/getFlowInfo";
    var p = {keyId : entityData.keyId};
    __ajax_get(url, p, function(data){
        if(!data){
            __toastr_warning("获取流程信息失败");
            return false;
        }

        if(data.processState == "Running" && data.flowButtonList.length>0){
            //设置审批意见框
            if($("#"+entityData.formId+" #flowComment").size()==0){
                var textarea = '<hr/><div class="form-group">' +
                    '<label class="col-md-2 control-label">审批意见</label>' +
                    '<div class="col-md-8">' +
                    '<textarea id="flowComment" name="flowComment"></textarea>'+
                    '</div>' +
                    '</div>';
                $("#"+entityData.formId).append(textarea);
            }

            //设置流程审批附件按钮
            if($("#"+entityData.formId+" #flowFileIds_"+entityData.keyId).size()==0){
                var flowFileBtn = '<div class="form-group">' +
                    '<div class="col-md-8 col-md-offset-2">' +
                    '<input id="flowFileIds_'+entityData.keyId+'" name="flowFileIds" style="display: none" value="">' +
                    '</div>' +
                    '</div>';
                $("#"+entityData.formId).append(flowFileBtn);
                __init_dropzone_edit("flowFileIds_"+entityData.keyId);
                $("#flowFileIds_"+entityData.keyId+"-dz-upload").hide();

                //初始化编辑器，并指定自定义附件上传按钮
                __summernote($("#"+entityData.formId).find("#flowComment"), {
                    buttons : {
                        flowfile : function(context){
                            var button = $.summernote.ui.button({
                                contents: '<i class="fa fa-cloud-upload"/> ',
                                tooltip: '附件上传',
                                click: function () {
                                    $("#flowFileIds_"+entityData.keyId+"-dz-upload").trigger("click");
                                }
                            });
                            return button.render();
                        }
                    }
                });
            }

        };

        //设置按钮组
        var flag = $("#layx-"+entityData.keyId).size()==0 && isNotNull(entityData.parentId) ? "layx-"+entityData.parentId : "layx-"+entityData.keyId;
        var btn_close = '<button class="btn btn-default mar-lft" title="关闭" id="'+flag+'-button-close">关闭</button>';
        var btn_approve = '<button class="btn btn-primary" title="通过" id="'+flag+'-button-approve">通过</button>';
        var btn_back = '<button class="btn btn-warning" title="退回" id="'+flag+'-button-back">退回</button>';
        var btn_deny = '<button class="btn btn-danger" title="否决" id="'+flag+'-button-deny">否决</button>';
        var btn_edit = '<button class="btn btn-default" title="编辑" id="'+flag+'-button-edit">编辑</button>';
        var btn_withdraw = '<button class="btn btn-default" title="编辑" id="'+flag+'-button-withdraw">撤回</button>';

        //先把按钮组清空，并直接添加一个关闭按钮
        $("#"+flag).find("div.layx-buttons").empty();
        $("#"+flag).find("div.layx-buttons").prepend(btn_close);
        $("#"+flag+"-button-close").on("click", function(){
            __layX_close($("#layx-"+entityData.keyId).size()==0 && isNotNull(entityData.parentId) ? entityData.parentId : entityData.keyId);
        });

        //再添加流程所需按钮
        for(var i=0;i<data.flowButtonList.length;i++){
            if(data.flowButtonList[i].name == "deny"){
                $("#"+flag).find("div.layx-buttons").prepend(btn_deny);
                $("#"+flag+"-button-deny").on("click", deny)
            }else if(data.flowButtonList[i].name == "approve"){
                $("#"+flag).find("div.layx-buttons").prepend(btn_approve);
                $("#"+flag+"-button-approve").on("click", approve)
            }else if(data.flowButtonList[i].name == "back"){
                $("#"+flag).find("div.layx-buttons").prepend(btn_back);
                $("#"+flag+"-button-back").on("click", back)
            }else if(data.flowButtonList[i].name == "withdraw"){
                $("#"+flag).find("div.layx-buttons").prepend(btn_withdraw);
                $("#"+flag+"-button-withdraw").on("click", withdraw)
            }else if(data.flowButtonList[i].name == "reSubmit" || data.flowButtonList[i].name == "save"){
                if($("#"+flag+"-button-edit").size() == 0){
                    $("#"+flag).find("div.layx-buttons").prepend(btn_edit);
                    $("#"+flag+"-button-edit").on("click", edit);
                }
            }
        }

        //草稿状态添加编辑按钮
        if(data.processState == 'Draft' && $("#"+flag+"-button-edit").size() == 0){
            $("#"+flag).find("div.layx-buttons").prepend(btn_edit);
            $("#"+flag+"-button-edit").on("click", edit)
        }

        //设置审批记录
        if(data.processState == "Running" || data.processState == "Finished" || data.processState == "Backed" || data.processState == "Deny"){
            $("#"+entityData.formId).append('</div><div id="process-log-'+entityData.keyId+'" class="process-log row"></div>');
            loadURL("/flow/taskEntity/processLog?keyId="+entityData.keyId, $("#process-log-"+entityData.keyId));
        }

    })
}

/**
 * 清除layx弹框下的所有按钮，并保留一个关闭按钮
 * @param entityData 实体信息
 */
function __flow_button_clean_handle(entityData){
    if(!entityData.keyId){entityData.keyId = ""};

    var flag = $("#layx-"+entityData.keyId).size()==0 && isNotNull(entityData.parentId) ? "layx-"+entityData.parentId : "layx-"+entityData.keyId;
    var btn_close = '<button class="btn btn-default mar-lft" title="关闭" id="'+flag+'-button-close">关闭</button>';

    $("#"+flag).find("div.layx-buttons").empty();
    $("#"+flag).find("div.layx-buttons").prepend(btn_close);
    $("#"+flag+"-button-close").on("click", function(){
        __layX_close($("#layx-"+entityData.keyId).size()==0 && isNotNull(entityData.parentId) ? entityData.parentId : entityData.keyId);
    });
}

/**
 * 使用流程的文档关闭layx弹窗
 */
function __flow_close_layx(entityData){
    var flag = $("#layx-"+entityData.keyId).size()==0 && isNotNull(entityData.parentId) ? entityData.parentId : entityData.keyId;
    __layX_close(flag);
}