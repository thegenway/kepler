/**
 * 配置 js
 * ============================================================================
 * 版权所有 2016 。
 *
 * @author fallenpanda
 *
 * @version 1.0 2016-10-24 。
 * ============================================================================
 */

window._admin.title = 'KEPLER';

/* dialog */
/*======================================*/

BootstrapDialog.defaultOptions = {
    type: BootstrapDialog.TYPE_DEFAULT,
    size: BootstrapDialog.SIZE_NORMAL,
    cssClass: 'modal-example',// 自定义样式覆盖
    title: null,
    message: null,
    nl2br: true,
    closable: true,
    closeByBackdrop: false,
    closeByKeyboard: true,
    // closeIcon: '&#215;',
    closeIcon: '<i class="pci-cross pci-circle"></i>',// 自定义关闭图标
    spinicon: BootstrapDialog.ICON_SPINNER,
    autodestroy: true,
    draggable: false,
    animate: true,
    description: '',
    tabindex: -1
};

/*======================================*/

/* jqGrid */
/*======================================*/

$.jgrid.defaults.width = 780;
$.jgrid.defaults.responsive = true;
$.jgrid.defaults.styleUI = 'Bootstrap';
$.jgrid.styleUI.Bootstrap.base.rowTable = "table table-bordered table-striped";