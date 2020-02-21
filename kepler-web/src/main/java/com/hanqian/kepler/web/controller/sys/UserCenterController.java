package com.hanqian.kepler.web.controller.sys;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.Constants;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.core.entity.primary.sys.Duty;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.core.service.sys.UserService;
import com.hanqian.kepler.security.annotation.CurrentUser;
import com.hanqian.kepler.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/main/userCenter")
public class UserCenterController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 个人中心页面跳转
     */
    @GetMapping("loadUrl/{type}")
    public String loadUrlUserCenter(@CurrentUser User user, @PathVariable String type, Model model){
        model.addAttribute("user", user);

        //我的职责列表
        if(StrUtil.equals("center_duty", type)){
            List<Duty> userDutyList  = dutyService.findByUser(user);
            model.addAttribute("userDutyList", userDutyList);
        }
        return "main/menu/center/"+type;
    }

    /**
     * 基本资料更新
     */
    @PostMapping("profileSave")
    @ResponseBody
    public AjaxResult profileSave(@CurrentUser User user, String name, String username, String phone, String email, String gender,
                                  String birthday, String addressAll, String address){
        if(user==null || StrUtil.isBlank(user.getId())) return AjaxResult.error("获取当前登陆人员失败");

        user.setName(name);
        user.setUsername(username);
        user.setPhone(phone);
        user.setEmail(email);
        user.setBirthday(DateUtil.parseDate(birthday));
        user.setAddress(address);

        if(StrUtil.isBlank(gender)) user.setGender(null);
        if(ObjectUtil.equal(BaseEnumManager.SexEnum.female.name(), gender)) user.setGender(BaseEnumManager.SexEnum.female);
        if(ObjectUtil.equal(BaseEnumManager.SexEnum.male.name(), gender)) user.setGender(BaseEnumManager.SexEnum.male);

        if(StrUtil.isNotBlank(addressAll)){
            String[] placeArr = addressAll.split("/");
            if(placeArr.length>0) user.setProvince(placeArr[0]);
            if(placeArr.length>1) user.setCity(placeArr[1]);
            if(placeArr.length>2) user.setCounty(placeArr[2]);
        }
        userService.save(user);
        return AjaxResult.success();
    }

    /**
     * 修改头像
     */
    @PostMapping("updateAvatar")
    @ResponseBody
    public AjaxResult updateAvatar(@CurrentUser User user, String newAvatarId){
        if(StrUtil.isBlank(newAvatarId)) return AjaxResult.error("未获取到新头像id");
        user.setAvatarId(newAvatarId);
        user = userService.save(user);
        request.setAttribute(Constants.CURRENT_USER, user);
        return AjaxResult.success("头像更换成功，请刷新网页");
    }

    /**
     * 修改密码
     */
    @PostMapping("updatePassword")
    @ResponseBody
    public AjaxResult sec_password_update(@CurrentUser User user, int index, String password, String newPassword) {
        if (index == 1) {// 密码校验
            return userService.validPassword(user, password) ? AjaxResult.success("校验通过") : AjaxResult.error("密码错误");
        } else if (index == 2) {// 修改密码
            return userService.updatePasswordByPassword(user, password, newPassword);
        }
        return AjaxResult.error("步骤异常");
    }

    /**
     * 设置默认职责
     */
    @PostMapping("setDefaultDuty")
    @ResponseBody
    public AjaxResult setDefaultDuty(@CurrentUser User user, String dutyId){
        return dutyService.setDefaultDuty(user, dutyService.get(dutyId));
    }

}
