/* ajax 封装 */
/*======================================*/

/***
 *读取指定的Cookie值 readCookie("id");
 *@param {string} cookieName Cookie名称
 */
function readCookie(cookieName) {
    var theCookie = "" + document.cookie;
    var ind = theCookie.indexOf(cookieName);
    if(ind==-1 || cookieName=="") return "";
    var ind1 = theCookie.indexOf(';',ind);
    if(ind1==-1) ind1 = theCookie.length;
    /*读取Cookie值*/
    return unescape(theCookie.substring(ind+cookieName.length+1,ind1));
}

/***
 * 设置Cookie值 setCookie("id",1);
 * @param {string} cookieName Cookie名称
 * @param {string} cookieValue Cookie值
 * @param {number} nDays Cookie过期天数
 */
function setCookie(cookieName, cookieValue) {
    /*当前日期*/
    var today = new Date();
    /*Cookie过期时间*/
    var expire = new Date();
    /*如果未设置nDays参数或者nDays为0，取默认值1*/
    //if(nDays == null || nDays == 0) nDays = 1;
    /*计算Cookie过期时间【 3600000 * 24  为一天】*/
    expire.setTime(today.getTime() + 400000); //5分钟
    document.cookie = cookieName + "=" + escape(cookieValue) + ";expires=" +      expire.toGMTString();
}

/***
 * 删除cookie中指定变量函数
 * @param {string} $name Cookie名称
 */
function deleteCookie($name){
    var myDate=new Date();
    myDate.setTime(-1000);//设置时间
    document.cookie=$name+"=''; expires="+myDate.toGMTString();
}

/***
 * 删除cookie中所有定变量函数
 * @param {string} cookieName Cookie名称
 * @param {string} cookieValue Cookie值
 * @param {number} nDays Cookie过期天数
 */
function clearCookie(){
    var myDate=new Date();
    myDate.setTime(-1000);//设置时间
    var data=document.cookie;
    var dataArray=data.split("; ");
    for(var i=0;i<dataArray.length;i++){
        var varName=dataArray[i].split("=");
        document.cookie=varName[0]+"=''; expires="+myDate.toGMTString();
    }
}


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

//初始化view
function __init_view(containerId, defaultId){
    var viewApi = mui('#'+containerId).view({
        defaultPage: '#'+defaultId
    });
    var view = viewApi.view;
    mui('.mui-scroll-wrapper').scroll();

    (function($) { //处理view的后退与webview后退
        var oldBack = $.back;
        $.back = function() {
            if (viewApi.canBack()) { //如果view可以后退，则执行view的后退
                viewApi.back();
            } else { //执行webview后退
                oldBack();
            }
        };
    })(mui);

    return view;
}

//list加载
function __mui_list_init(keyName, url, titleArr, valueArr, liClickFun, opt){
    var page = readCookie(keyName+"-mui-page");
    var rows = readCookie(keyName+"-mui-rows");
    if(!rows) rows = 10;
    if(!page && page!==1){
        page = 1;
    }else{
        rows = rows*page;
    }


    var defaults = {
        keyName : keyName,
        ulId : keyName + "-ul",
        url : url,
        titleArr : titleArr,
        valueArr : valueArr,
        ifPage : true,
        page : page,
        rows : rows,
        scrollId : keyName + "-scroll",
        autoFocus : true
    };

    var options = $.extend({}, defaults, opt);
    if(!options.titleArr || !options.valueArr){
        __mui_alert("titleArr或valueArr为空");
        return false;
    }
    if(options.titleArr.length!==options.valueArr.length){
        __mui_alert("标题和值数组的长度不相等");
        return false;
    }

    mui.init({
        pullRefresh: {
            container: '#'+options.scrollId,
            down: {
                style:'circle',
                callback: function(){
                    options.page = 1; options.rows = 10;
                    __mui_list_load("down", options, options.page, options.rows, liClickFun);
                }
            },
            up: {
                auto:false,
                contentrefresh: '正在加载...',
                callback: function(){
                    options.page++;
                    // options.rows = 10;
                    __mui_list_load("up", options, options.page, 10, liClickFun);
                }
            }
        }
    });

    __mui_list_load("init", options, 1, options.rows, liClickFun);

}

function __mui_list_load(type, options, page, rows, liClickFun){
    __ajax_get(options.url, {ifPage : options.ifPage, page : page, rows : rows}, function(data){

        if("init" != type){
            setCookie(options.keyName + "-mui-page", page);
            setCookie(options.keyName + "-mui-rows", rows);
        }

        //上拉加载 下拉刷新
        if(page === 1){
            setTimeout(function(){
                mui('#'+options.scrollId).pullRefresh().endPulldownToRefresh();
                mui('#'+options.scrollId).pullRefresh().refresh(true);
            }, 500);
            $("#"+options.ulId).empty();
            if(page>=data.total) mui('#'+options.scrollId).pullRefresh().endPullupToRefresh((true));
        }else{
            mui('#'+options.scrollId).pullRefresh().endPullupToRefresh((page>=data.total));
        }

        //每个li的点击事件
        $("#"+options.ulId).on("tap", "li", function(){
            var id = $(this).attr("id");
            setCookie(options.keyName + "-mui-last-id", id);
            liClickFun(id);
        });

        //加载DOM
        var list = data.dataRows;
        for(var i=0;i<list.length;i++){
            var h5Arr = '';
            for(var j=0;j<options.titleArr.length;j++){
                h5Arr += '<h5 class="mui-ellipsis"><span data-type="title">'+options.titleArr[j]+'：</span><span data-type="value">'+list[i][options.valueArr[j]]+'</span></h5>';
            }
            var newLi = '<li id="'+list[i]['id']+'" class="mui-table-view-cell" >\n' +
                '<div class="mui-table">\n' +
                '<div class="mui-table-cell mui-col-xs-1 mui-text-center" style="vertical-align: middle">\n' +
                '<span class="mui-h5 li-index">'+(i+1)+'.</span>\n' +
                '</div>\n' +
                '<div class="mui-table-cell mui-col-xs-9">\n' +
                h5Arr+
                '</div>\n' +
                '<div class="mui-table-cell mui-col-xs-2 mui-text-right">\n' +
                '<span class="mui-h5">'+list[i]['processState']+'</span>\n' +
                '</div>\n' +
                '</div>\n' +
                '</li>';
            $("#"+options.ulId).append(newLi);
        }

        $("#"+options.ulId).find("li").each(function(i){
            $(this).find(".li-index").text(++i);
        });

        if("init"==type && options.autoFocus){
            var lastId = readCookie(options.keyName + "-mui-last-id");
            console.log(lastId);
            setTimeout(function(){
                if(lastId && $("#"+lastId).size()>0){
                    var top = $("#"+lastId).offset().top;
                    mui('#'+options.scrollId).pullRefresh().scrollTo(0, 0-top+120, 500);
                    setTimeout(function(){
                        micron.getEle("li[id='"+lastId+"']").interaction("shake").duration(".40").timing("ease-out");
                    }, 480)

                }
            }, 400);
        }

    });
}