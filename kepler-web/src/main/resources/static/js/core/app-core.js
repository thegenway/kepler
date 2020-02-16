/**
 * 核心 js
 * ============================================================================
 * 版权所有 2016 。
 *
 * @author fallenpanda
 *
 * @version 1.0 2016-10-24 。
 * ============================================================================
 */

<!-- 导航栏 异步 load -->
<!--===================================================-->
(function ($) {
    "use strict";

    $.navAsAjax = true; // Your left nav in your app will no longer fire ajax calls

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

    // 全局变量
    window._admin = {
        $window: $(window),
        $document: $(document),
        $body: $('body'),

        randomInt: function (n, t) {
            return Math.floor(Math.random() * (t - n + 1) + n)
        },
        isMobile: function () {
            return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent)
        }(),
        
        title: 'Admin'
    };

    if ($.navAsAjax) {

        //全局刷新后自动定位到刷新前的页面
        if ($('#mainnav-menu').length) {
            checkURL();
        }

        // side-menu 忽略链接
        // $(document).on('click', '#mainnav-menu a[href="#"]', function(e) {
        $(document).on('click', 'ul[id^="mainnav-menu"] a[href="#"]', function(e) {
            e.preventDefault();
        });

        // side-menu 其他链接处理
        // $(document).on('click', '#mainnav-menu a[href!="#"]', function(e) {
        $(document).on('click', 'ul[id^="mainnav-menu"] a[href!="#"]', function(e) {
            e.preventDefault();
            var $this = $(e.currentTarget);

            if ($this.parent().hasClass('active-link')) {
                returnToHashUrl();
            }else if (!$this.attr('target') || $this.attr('target') == '_self') {
                window.location.hash = $this.attr('href');
            } else if ($this.attr('target') == '_blank') {
                window.open($this.attr('href'));
            } else if ($this.attr('target') == '_top') {
                window.location = ($this.attr('href'));
            }
            e.preventDefault();
        });

        // hash 改变事件绑定
        $(window).on('hashchange', function() {
            checkURL();
        });

    }

    // hash 改变检查
    function checkURL() {
        var url = '';
        if (location.hash) {
            //获取目标及参数
            url = location.hash.replace(/^#/, '');
        }

        // 左侧菜单
        var $sideMenu = $('#mainnav-menu');
        // content
        var $ajax_container = $('#content-container');

        if (url) {

            // 移除 active 样式
            $sideMenu.find('li.active-link').each(function () {
                var $li_link = $(this);
                $li_link.removeClass('active-link');
                $li_link.parents('ul.collapse').each(function () {
                    var $this_ul = $(this);
                    $this_ul.parent('li').removeClass('active-sub');
                });
            });

            // 增加 active 样式
            var $a_link = $sideMenu.find('a[href="' + url + '"]');
            $a_link.parent('li').addClass('active-link');
            $a_link.parents('ul.collapse').each(function () {
                var $this_ul = $(this);
                $this_ul.addClass('in');
                $this_ul.parent('li').addClass('active-sub').addClass('active');
            });

            // 改变窗口 Title
            var title = $a_link.text();
            if (title) {
                document.title = window._admin.title + ' | ' +title;
            }

            // load 页面
            loadURL(url, $ajax_container);

        } else {
            // 默认菜单
            var defaultLink = $sideMenu.find('li.active-link > a[href!="#"]');
            if(!defaultLink.length){
                defaultLink = $sideMenu.find('li:first > a[href!="#"]');
            }
            // 更新 hash
            window.location.hash = defaultLink.attr('href');
        }

    }

    //缩小左侧导航栏时点击菜单的情况
    $("#container").on("click",".menu-popover li", function(e){
        e.preventDefault();
        var href = $(this).find("a[href!='#']:first").attr("href");
        if(href){
            window.location.hash = href
        }
    });

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

                ajax_container.html(errorPage);
            }
        });
    }

})(jQuery);

<!--===================================================-->

<!-- 覆盖层 panel -->
<!--===================================================-->
(function ($) {
    "use strict";

    var defaults = {
            'displayIcon'	: true,
            // DESC	 		: Should we display the icon or not.
            // VALUE	 	: true or false
            // TYPE 	 	: Boolean


            'iconColor'		: 'text-dark',
            // DESC	 		: The color of the icon..
            // VALUE	 	: text-light || text-primary || text-info || text-success || text-warning || text-danger || text-mint || text-purple || text-pink || text-dark
            // TYPE 	 	: String

            'iconClass'		: 'fa fa-refresh fa-spin fa-2x',
            // DESC  		: Class name of the font awesome icons", Currently we use font-awesome for default value.
            // VALUE 		: (Icon Class Name)
            // TYPE			: String


            'title'			: '',
            // DESC			: Overlay title
            // TYPE			: String

            'desc'			: ''
            // DESC			: Descrition
            // TYPE			: String


        },
        uID = function() {
            return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
        },
        methods = {
            'show' : function(el){
                var ovId = 'nifty-overlay-' + uID() + uID()+"-" + uID(),
                    panelOv = $('<div id="'+ ovId +'" class="panel-overlay"></div>');

                el.data('niftyOverlay',ovId);
                el.addClass('panel-overlay-wrap');
                panelOv.appendTo(el).html(el.data('overlayTemplate'));
                return null;
            },
            'hide': function(el){
                var boxLoad = $('#'+ el.data('niftyOverlay'));

                if (boxLoad.length) {
                    el.prop('disabled', false);
                    el.removeClass('panel-overlay-wrap');
                    boxLoad.hide().remove();
                }
                return null;
            }
        },
        loadBox = function(el,options){
            if (el.data('overlayTemplate')) {
                return null;
            }
            var opt = $.extend({},defaults,options),
                icon = (opt.displayIcon)?'<span class="panel-overlay-icon '+opt.iconColor+'"><i class="'+opt.iconClass+'"></i></span>':'';
            el.data('overlayTemplate', '<div class="panel-overlay-content pad-all unselectable">'+icon+'<h4 class="panel-overlay-title">'+opt.title+'</h4><p>'+opt.desc+'</p></div>');
            return null;
        };

    $.fn.panelOverlay = function(method){
        if (methods[method]){
            return methods[method](this);
        }else if (typeof method === 'object' || !method) {
            return this.each(function () {
                loadBox($(this), method);
            });
        }
        return null;
    };

})(jQuery);
<!--===================================================-->

<!-- 下拉树 -->
<!--===================================================-->
(function($){

    var Admin_Package_SelectTreePicker = function (element, options, e) {
        if (e) {
            e.stopPropagation();
            e.preventDefault();
        }

        this.$element = $(element);
        this.$newElement = null;
        this.$button = null;
        this.$menu = null;
        this.$tree = null;
        this.options = options;

        if (this.options.title === null) {
            this.options.title = this.$element.attr('title');
        }

        //Expose public methods
        this.val = Admin_Package_SelectTreePicker.prototype.val;
        this.render = Admin_Package_SelectTreePicker.prototype.render;
        this.setTreeNodes = Admin_Package_SelectTreePicker.prototype.setTreeNodes;

        this.init();
    };

    Admin_Package_SelectTreePicker.VERSION = '0.0.1';

    Admin_Package_SelectTreePicker.DEFAULTS = {
        styleBase: 'btn',
        style: 'btn-default',
        size: 'auto',
        title: null,
        width: false,
        template: {
            caret: '<span class="caret"></span>'
        },
        treeOptions: {
            data: {
                simpleData: {
                    enable: true,
                    idKey: 'id',
                    pIdKey: 'pId',
                    rootPId: ''
                }
            },
            view: {
                autoCancelSelected: false,
                dblClickExpand: false,
                selectedMulti: false
            }
        },
        treeNodes: []
    };

    Admin_Package_SelectTreePicker.prototype = {

        constructor: Admin_Package_SelectTreePicker,

        init: function () {
            var id = this.$element.attr('id');

            this.treeNodes = this.options.treeNodes;
            this.$newElement = this.createView();
            this.$element
                .after(this.$newElement)
                .appendTo(this.$newElement);
            this.$button = this.$newElement.children('button');
            this.$menu = this.$newElement.children('.dropdown-menu');
            this.$tree = this.$menu.children('.ztree');
            this.$treeObj = this.createTree();

            if (typeof id !== 'undefined') {
                this.$button.attr('data-id', id);
            }

            this.clickListener();
            this.render();
            this.setStyle();
            this.setWidth();
            this.$menu.data('this', this);
            this.$newElement.data('this', this);
        },

        createDropdown: function () {
            var drop =
                '<div class="btn-group tree-select">' +
                '<button type="button" class="' + this.options.styleBase + ' dropdown-toggle" data-toggle="dropdown">' +
                '<span class="filter-option pull-left"></span>&nbsp;' +
                '<span class="bs-caret">' +
                this.options.template.caret +
                '</span>' +
                '</button>' +
                '<div class="dropdown-menu open">' +
                '<ul class="ztree">' +
                '</ul>' +
                '</div>' +
                '</div>';

            return $(drop);
        },

        createView: function () {

            return this.createDropdown();
        },

        createTree: function () {

            return $.fn.zTree.init(this.$tree, this.options.treeOptions, this.treeNodes);
        },

        render: function () {

            if (this.options.title == undefined) {
                this.options.title = this.$element.attr('title');
            }

            var id = this.$element.val();
            var node = this.$treeObj.getNodeByParam('id', id, null);

            var title = node ? node.name : this.options.title;

            this.$button.attr('title', $.trim(title.replace(/<[^>]*>?/g, '')));
            this.$button.children('.filter-option').html(title);
        },

        /**
         * @param [style]
         * @param [status]
         */
        setStyle: function (style, status) {
            if (this.$element.attr('class')) {
                this.$newElement.addClass(this.$element.attr('class').replace(/treepicker|validate\[.*\]/gi, ''));
            }

            var buttonClass = style ? style : this.options.style;

            if (status == 'add') {
                this.$button.addClass(buttonClass);
            } else if (status == 'remove') {
                this.$button.removeClass(buttonClass);
            } else {
                this.$button.removeClass(this.options.style);
                this.$button.addClass(buttonClass);
            }
        },

        setWidth: function () {
            if (this.options.width === 'auto') {
                this.$menu.css('min-width', '0');

                // Get correct width if element is hidden
                var $selectClone = this.$menu.parent().clone().appendTo('body'),
                    ulWidth = $selectClone.children('.dropdown-menu').outerWidth(),
                    btnWidth = $selectClone.css('width', 'auto').children('button').outerWidth();

                $selectClone.remove();

                // Set width to whatever's larger, button title or longest option
                this.$newElement.css('width', Math.max(ulWidth, btnWidth) + 'px');
            } else if (this.options.width === 'fit') {
                // Remove inline min-width so width can be changed from 'auto'
                this.$menu.css('min-width', '');
                this.$newElement.css('width', '').addClass('fit-width');
            } else if (this.options.width) {
                // Remove inline min-width so width can be changed from 'auto'
                this.$menu.css('min-width', '');
                this.$newElement.css('width', this.options.width);
            } else {
                // Remove inline min-width/width so width can be changed
                this.$menu.css('min-width', '');
                this.$newElement.css('width', '');
            }
            // Remove fit-width class if width is changed programmatically
            if (this.$newElement.hasClass('fit-width') && this.options.width !== 'fit') {
                this.$newElement.removeClass('fit-width');
            }
        },

        /**
         * @param [zNodes]
         */
        setTreeNodes: function (zNodes) {
            this.treeNodes = zNodes;
            this.$treeObj.addNodes(null, zNodes);
        },

        clickListener: function (){
            var that = this;

            this.$treeObj.setting.callback.onClick = function (event, treeId, treeNode){
                var id = treeNode.id;
                if (id) {
                    if(that.val() == id){
                        that.val("");
                    }else{
                        that.val(id);
                    }
               }
            };
        },

        /**
         * @param [value]
         */
        val: function (value) {
            if (typeof value !== 'undefined') {
                this.$element.val(value);
                this.render();

                return this.$element;
            } else {
                return this.$element.val();
            }
        }

    };

    // PLUGIN DEFINITION
    // ==============================
    function Plugin(option, event) {
        // get the args of the outer function..
        var args = arguments;
        // The arguments of the function are explicitly re-defined from the argument list, because the shift causes them
        // to get lost/corrupted in android 2.3 and IE9 #715 #775
        var _option = option,
            _event = event;
        [].shift.apply(args);

        var value;
        var chain = this.each(function () {
            var $this = $(this);
            if ($this.is('input')) {
                var data = $this.data('treepicker'),
                    options = typeof _option == 'object' && _option;

                if (!data) {
                    var config = $.extend({}, Admin_Package_SelectTreePicker.DEFAULTS, $.fn.treepicker.defaults || {}, $this.data(), options);
                    config.template = $.extend({}, Admin_Package_SelectTreePicker.DEFAULTS.template, ($.fn.treepicker.defaults ? $.fn.treepicker.defaults.template : {}), $this.data().template, options.template);
                    $this.data('treepicker', (data = new Admin_Package_SelectTreePicker(this, config, _event)));
                } else if (options) {
                    for (var i in options) {
                        if (options.hasOwnProperty(i)) {
                            data.options[i] = options[i];
                        }
                    }
                }

                if (typeof _option == 'string') {
                    if (data[_option] instanceof Function) {
                        value = data[_option].apply(data, args);
                    } else {
                        value = data.options[_option];
                    }
                }
            }
        });

        if (typeof value !== 'undefined') {
            //noinspection JSUnusedAssignment
            return value;
        } else {
            return chain;
        }
    }

    $.fn.treepicker = Plugin;
    $.fn.treepicker.Constructor = Admin_Package_SelectTreePicker;

    $(document)
        .on('click', '.tree-select > .dropdown-menu', function (e) {
            e.stopPropagation();
        });

})(jQuery);
<!--===================================================-->