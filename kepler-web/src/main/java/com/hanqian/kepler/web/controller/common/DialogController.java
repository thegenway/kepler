package com.hanqian.kepler.web.controller.common;

import com.hanqian.kepler.web.controller.BaseController;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/common/dialog")
public class DialogController extends BaseController {

    /**
     * 跳转到选择数据页面
     */
    @GetMapping(value = "selectDialog")
    public String toSelectDialog(Model model){
        return "main/common/common_select_dialog";
    }

    /**
     * 多选的选择数据页面
     */
    @GetMapping(value = "selectDialogs")
    public String selectDialogs(Model model){
        return "main/common/common_select_dialogs";
    }

}
