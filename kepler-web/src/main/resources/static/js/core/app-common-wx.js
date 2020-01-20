/* ajax 封装 */
/*======================================*/
// 异步 load content 页面
function loadURL(url, ajax_container) {
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
    $('#'+form_id+' input:disabled').removeAttr('disabled');
    var params = $('#'+form_id).serialize();
    __ajax_post(url, params, function(data, params){
        if(data && data.message){
            __mui_toast_short(data.message,"short")
        }
        if(typeof success === 'function'){
            success(data);
        }

    });
}

//dialog toast
function __mui_toast_short(message){
    mui.toast(message,{ duration:'short', type:'div' })
}
function __mui_toast_long(message){
    mui.toast(message,{ duration:'long', type:'div' })
}
function __mui_alert(message, success){
    mui.alert(message, "提示", "确认", success);
}

//button_loading状态
function __mui_button_loading(ele){
    mui(ele).button('loading');
}
function __mui_button_reset(ele){
    mui(ele).button('reset');
}


//加载file列表
function __mp_file_list(eleId){
    var $ele = $("#"+eleId);
    if($ele.size() > 0){
        var fileIds = $ele.val();
        var $ul = '<ul class="mui-table-view"></ul>';
        $ele.parent().append($ul);
        if(fileIds){
            __ajax_get('/common/file/fileList', {keyId: fileIds}, function (data) {
                if (data.state === 1) {
                    $(data.data).each(function (i, obj) {
                        var newLi = '<li class="mui-table-view-cell mui-media">' +
                            '<a href="/common/file/download?fileId='+obj.id+'">\n' +
                            '<img class="mui-media-object mui-pull-right" src="/resource/core/img/file-img.png">\n' +
                            '<div class="mui-media-body">\n' +
                            obj.name +
                            '<p class="mui-ellipsis">'+getFileSize(obj.size)+'</p>\n' +
                            '</div>\n' +
                            '</a>\n' +
                            '</li>';
                        $ele.parent().find("ul[class='mui-table-view']").append(newLi);
                    });
                }
            })
        }
    }
}

//附件大小转换
function getFileSize(fileByte) {
    var fileSizeByte = fileByte;
    var fileSizeMsg = "";
    if (fileSizeByte < 1048576) fileSizeMsg = (fileSizeByte / 1024).toFixed(2) + "KB";
    else if (fileSizeByte == 1048576) fileSizeMsg = "1MB";
    else if (fileSizeByte > 1048576 && fileSizeByte < 1073741824) fileSizeMsg = (fileSizeByte / (1024 * 1024)).toFixed(2) + "MB";
    else if (fileSizeByte > 1048576 && fileSizeByte == 1073741824) fileSizeMsg = "1GB";
    else if (fileSizeByte > 1073741824 && fileSizeByte < 1099511627776) fileSizeMsg = (fileSizeByte / (1024 * 1024 * 1024)).toFixed(2) + "GB";
    else fileSizeMsg = "超过1TB";
    return fileSizeMsg;
}