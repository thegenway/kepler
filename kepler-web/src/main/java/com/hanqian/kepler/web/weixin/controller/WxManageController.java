package com.hanqian.kepler.web.weixin.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.core.entity.primary.sys.FileManage;
import com.hanqian.kepler.core.service.sys.FileManageService;
import com.hanqian.kepler.web.controller.BaseController;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.menu.WxMpMenu;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信管理
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/23 。
 * ============================================================================
 */
@AllArgsConstructor
@Controller
@RequestMapping("/wx/manage")
public class WxManageController extends BaseController {
	private static final long serialVersionUID = -8841717129694701609L;

	@Autowired
	private FileManageService fileManageService;

	private final WxMpService wxService;
	private final WxMpMessageRouter messageRouter;

	/**
	 * 进入菜单栏页面
	 */
	@GetMapping("menu")
	public String menu(){
		return "main/weixin/menu";
	}

	/**
	 * 进入管理页面
	 */
	@GetMapping("info")
	public String info(Model model){

		model.addAttribute("wxMpConfig", wxService.getWxMpConfigStorage());

		try {
			//关注者信息
			WxMpUserList wxMpUserList = wxService.getUserService().userList(null);
			List<WxMpUser> wxMpUsers = new ArrayList<WxMpUser>();
			for(String openId : wxMpUserList.getOpenids()){
				wxMpUsers.add(wxService.getUserService().userInfo(openId));
			}
			model.addAttribute("wxMpUsers", wxMpUsers);

			//公众号二维码
			List<FileManage> qrFileList = fileManageService.getFileManageByName("wxQrImg.jpg");
			if(qrFileList.size() > 0){
				FileManage fileManage = qrFileList.get(0);
				model.addAttribute("qrImgId", fileManage.getId());
			}else {
				WxMpQrCodeTicket ticket = wxService.getQrcodeService().qrCodeCreateLastTicket(1);
				File file = wxService.getQrcodeService().qrCodePicture(ticket);
				if(file!=null){
					FileManage fileManage = fileManageService.saveFile(FileUtil.getInputStream(file), new MimetypesFileTypeMap().getContentType(file), "wxQrImg.jpg");
					model.addAttribute("qrImgId", fileManage.getId());
				}
			}

		} catch (WxErrorException e) {
			e.printStackTrace();
		}

		return "main/weixin/info";
	}

	/**
	 * 获取当前菜单栏
	 */
	@RequestMapping("menuList")
	@ResponseBody
	public AjaxResult menuList() {
		try {
			WxMpMenu wxMpMenu = wxService.getMenuService().menuGet();
			return AjaxResult.success("操作成功",  JSONUtil.parse(wxMpMenu.getMenu()));
		} catch (WxErrorException e) {
			e.printStackTrace();
			return AjaxResult.error(e.getMessage());
		}
	}

	/**
	 * 更新微信菜单栏
	 */
	@PostMapping("updateMenu")
	@ResponseBody
	public AjaxResult updateMenu(String menuJson){
		System.out.println(menuJson);
		String menuId = null;
		try {
			List<WxMenuButton> buttons = JSONUtil.toList(JSONUtil.parseArray(menuJson), WxMenuButton.class);
			WxMenu wxMenu = new WxMenu();
			wxMenu.setButtons(buttons);
			menuId = wxService.getMenuService().menuCreate(wxMenu);
		} catch (WxErrorException e) {
			e.printStackTrace();
			return AjaxResult.error(e.getMessage());
		}
		return AjaxResult.successWithId("更新成功", menuId);
	}

}
