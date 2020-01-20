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
                '<div class="page-content">'+
                '<div class="cls-container">'+
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
        closable: false,
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

// 选择框 宽dialog
function __open_dialog_selectwide(title, url, confirm, opt) {
    var dialogRef =  __open_dialog(BootstrapDialog.SIZE_WIDE, title, url,
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

/* validate 封装 */
/*======================================*/

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
                } else {
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

        url: '../common/file/upload',
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
                        $(file.previewElement).find('[data-dz-preview]').attr('href', "../common/file/imgView?fileId="+file.id);
                    }else{
                        $(file.previewElement).find('[data-dz-preview]').attr('href', file.fileViewUrl);
                    }
                }
                if (isNull(file.type) || !file.type.match(/image.*/)) {
                    myDropzone.emit("thumbnail", file, "../resource/core/img/file-img.png");
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
                    var fileId = data.data.fileId;
                    var filePath = "../common/file/download?"+data.data.url;

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
                    __ajax_post('../common/file/delete?keyId='+id, null, function (data) {
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
                __ajax_get('../common/file/fileList', {keyId: fileIds}, function (data) {
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
        placeholder : "点击从下拉面板中选择省/市/区"
    };
    var option = $.extend({},defaults,opt);
    elem.citypicker("reset");
    elem.citypicker("destroy");
    elem.citypicker(option);
}

/**
 * 通用数据单选选择框
 * @param title 标题
 * @param dataUrl 获取数据地址
 * @param colNames jqgrid表头
 * @param colModel jqgrid表体
 * @param confirm
 * @param opt
 */
function __common_dialog_select(title, dataUrl, colNames, colModel, selectedId, confirm, opt){
    var url = "../common/dialog/selectDialog?dataUrl="+dataUrl.replace(new RegExp('&','g'), '%26')+"&colNames="+colNames.replace(/\s+/g,"")+"&colModel="+colModel.replace(/\s+/g,"")+"&selectedId="+selectedId;
    __open_dialog_select(title, url, confirm, opt);
}

/**
 * 通用数据多选选择框
 * @param title 标题
 * @param dataUrl 获取数据地址
 * @param colNames jqgrid表头
 * @param colModel jqgrid表体
 * @param selectIds 默认选中的ids（英文逗号隔开）
 * @param confirm callback
 * @param opt
 */
function __common_dialog_selects(title, dataUrl, colNames, colModel, selectIds, confirm, opt){
    var url = "../common/dialog/selectDialogs?dataUrl="+dataUrl+"&colNames="+colNames+"&colModel="+colModel+"&selectIds="+selectIds;
    __open_dialog_select(title, url, confirm, opt);
}

/**
 * 系统字典多选框
 * @param title 标题
 * @param dictValue 字典关键字
 * @param selectedIds 默认选中的id（英文逗号隔开）
 * @param confirm callback
 * @param opt
 */
function __dictItem_dialog_selects(title, dictValue, selectedIds, confirm, opt){
    var dataUrl = "../main/dictitem/selectlist?dictValue="+dictValue;
    var colNames = '["名称","关键字","描述","id"]';
    var colModel = "["+
        "{name:'name',index:'name',width:100,search:true,sortable:false,searchoptions:{sopt:['cn']}},"+
        "{name:'value',index:'value',width:100,search:true,sortable:false,hidden:true},"+
        "{name:'description',index:'description',width:100,search:true,sortable:false,hidden:true},"+
        "{name:'id',index:'id',search:false,hidden:true}"+
        "]";
    __common_dialog_selects(title, dataUrl, colNames, colModel, selectedIds, confirm, opt);
}

function __dictItem_dialog_select(title, dictValue, selectedIds, confirm, opt){
    var dataUrl = "../main/dictitem/selectlist?dictValue="+dictValue;
    var colNames = '["名称","关键字","描述","id"]';
    var colModel = "["+
        "{name:'name',index:'name',width:100,search:true,sortable:false,searchoptions:{sopt:['cn']}},"+
        "{name:'value',index:'value',width:100,search:true,sortable:false,hidden:true},"+
        "{name:'description',index:'description',width:100,search:true,sortable:false,hidden:true},"+
        "{name:'id',index:'id',search:false,hidden:true}"+
        "]";
    __common_dialog_select(title, dataUrl, colNames, colModel, selectedIds, confirm, opt);
}

function __dictItem_dialog_select_simple(title, dictValue, selectedIds, nameFiledId, idFiledId, opt){
    var dataUrl = "../main/dictitem/selectlist?dictValue="+dictValue;
    var colNames = '["名称","关键字","描述","id"]';
    var colModel = "["+
        "{name:'name',index:'name',width:100,search:true,sortable:false,searchoptions:{sopt:['cn']}},"+
        "{name:'value',index:'value',width:100,search:true,sortable:false,hidden:true},"+
        "{name:'description',index:'description',width:100,search:true,sortable:false,hidden:true},"+
        "{name:'id',index:'id',search:false,hidden:true}"+
        "]";
    __common_dialog_select(title, dataUrl, colNames, colModel, selectedIds, function(dialogRef){
        var callback = function(data){
            dialogRef.enableButtons();
            if(data){
                if(nameFiledId){
                    $("#"+nameFiledId).val(data.name);
                }
                if(idFiledId){
                    $("#"+idFiledId).val(data.id);
                }
                dialogRef.close();
            }
        };
        fn_common_dialog_select(callback);
    }, opt);
}


/* jqGrid 封装 */
/*======================================*/

function __init_jqgrid(table_id, page_id, url, colNames, colModel, search, opt) {
    var defaults = {
        url: url,
        loadonce:false,
        datatype: 'json',
        height: 'auto',
        autowidth:true,
        colNames: colNames,
        colModel: colModel,
        pager: '#'+page_id,
        rowList: [10, 30, 50,100],
        rowNum: 10,
        rownumbers: true,
        jsonReader: {
            root: 'dataRows',
            repeatitems: false
        },
        prmNames: {
            search:'search'
        },
        gridview: true,
        viewrecords: true,
        viewsortcols: [true, 'vertical', true],
        loadComplete : function(){
            $.minimalTips(); //增加tip提示工具
        },
        resizeStart:function(event, index){
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

    if (search) {
        $('#'+table_id).jqGrid('filterToolbar', {searchOperators:false, stringResult:true});
    }
}

//表格刷新
function __reflash_jqgrid(tableId, param){
    $('#'+tableId).jqGrid().setGridParam({datatype:'json',postData: param}).trigger('reloadGrid');
}