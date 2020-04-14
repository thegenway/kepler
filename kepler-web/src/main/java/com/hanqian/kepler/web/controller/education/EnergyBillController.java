package com.hanqian.kepler.web.controller.education;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.bean.jqgrid.JqGridContent;
import com.hanqian.kepler.common.bean.jqgrid.JqGridFilter;
import com.hanqian.kepler.common.bean.jqgrid.JqGridPager;
import com.hanqian.kepler.common.bean.jqgrid.JqGridReturn;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.common.enums.DictEnum;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.core.entity.primary.education.EnergyBill;
import com.hanqian.kepler.core.service.education.EnergyBillService;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.flow.vo.ProcessLogVo;
import com.hanqian.kepler.security.annotation.CurrentUser;
import com.hanqian.kepler.web.annotation.RequestJsonParam;
import com.hanqian.kepler.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.*;

/**
 * 基本信息-能源账单 controller with generator
 * ============================================================================
 * author : DongZhengWei
 * createDate:  2020-04-14 13:00:35  。
 * ============================================================================
 */
@Controller
@RequestMapping("/energyBill")
public class EnergyBillController extends BaseController {
    private static final long serialVersionUID = 8461258693930622544L;

    @Autowired
    private EnergyBillService energyBillService;

    /**
     * list
     */
    @GetMapping("list")
    @ResponseBody
    public JqGridReturn list(@CurrentUser User user, JqGridPager pager, @RequestJsonParam("filters") JqGridFilter filters){
        Pageable pageable = getJqGridPageable(pager);
        List<Rule> rules = getJqGridSearchWithFlow(filters);
        JqGridContent<EnergyBill> jqGridContent = energyBillService.getJqGridContentWithFlow(rules, pageable, user);

        List<Map<String, Object>> dataRows = new ArrayList<>();
        jqGridContent.getList().forEach(entity -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", entity.getId());
            map.put("name", StrUtil.nullToDefault(entity.getName(), ""));
            map.put("billDate", entity.getBillDate()!=null ? DateUtil.format(entity.getBillDate(), "yyyy-MM") : "");
            map.put("energyTypeDict.name", entity.getEnergyTypeDict()!=null ? entity.getEnergyTypeDict().getName() : "");

            map.put("processState", entity.getProcessState()!=null ? entity.getProcessState().value() : "");
            dataRows.add(map);
        });
        return getJqGridReturn(dataRows, jqGridContent.getPage());
    }

    /**
     * read页面
     */
    @GetMapping("read")
    public String read(String keyId, Model model){
        EnergyBill energyBill = energyBillService.get(keyId);
        model.addAttribute("energyBill", energyBill);
        model.addAttribute("energyTypeDictList", getDictList(DictEnum.energy_energyType));

        return "main/education/energyBill_read";
    }

     /**
        * input页面
        */
     @GetMapping("input")
     public String input(String keyId, Model model){
        EnergyBill energyBill = energyBillService.get(keyId);
        model.addAttribute("energyBill", energyBill);
        model.addAttribute("energyTypeDictList", getDictList(DictEnum.energy_energyType));

        return "main/education/energyBill_input";
     }

    /**
     * 赋值
     */
    private EnergyBill setData(User user, String keyId,String energyTypeDictId,String billDate,String price,String useCount,String totalAmount,String barcode,String remark){
        EnergyBill energyBill = energyBillService.get(keyId);
        if(energyBill == null){
            energyBill = new EnergyBill();
            energyBill.setCreator(user);
        }

        energyBill.setEnergyTypeDict(dictService.get(energyTypeDictId));
        energyBill.setBillDate(StrUtil.isNotBlank(billDate) ? DateUtil.parse(billDate, "yyyy-MM") : null);
        energyBill.setPrice(NumberUtil.isNumber(price) ? new BigDecimal(price) : null);
        energyBill.setUseCount(NumberUtil.isNumber(useCount) ? new BigDecimal(useCount) : null);
        energyBill.setTotalAmount(NumberUtil.isNumber(totalAmount) ? new BigDecimal(totalAmount) : null);
        energyBill.setBarcode(barcode);
        energyBill.setRemark(remark);

         return energyBill;
     }

    /**
     * 保存
     */
    @PostMapping("save")
    @ResponseBody
    public AjaxResult save(@CurrentUser User user, ProcessLogVo processLogVo,String energyTypeDictId,String billDate,String price,String useCount,String totalAmount,String barcode,String remark){
        EnergyBill energyBill = setData(user,processLogVo.getKeyId(),energyTypeDictId,billDate,price,useCount,totalAmount,barcode,remark);

        if(StrUtil.isBlank(energyBill.getId())){
            return energyBillService.draft(energyBill);
        } else {
            energyBillService.save(energyBill);
            return AjaxResult.success("保存成功");
        }
    }

    /**
     * 提交
     */
    @PostMapping("commit")
    @ResponseBody
    public AjaxResult commit(@CurrentUser User user, ProcessLogVo processLogVo,String energyTypeDictId,String billDate,String price,String useCount,String totalAmount,String barcode,String remark){
        EnergyBill energyBill = setData(user,processLogVo.getKeyId(),energyTypeDictId,billDate,price,useCount,totalAmount,barcode,remark);
        return energyBillService.commit(energyBill, processLogVo);
    }

    /**
     * 审批
     */
    @PostMapping("approve")
    @ResponseBody
    public AjaxResult approve(@CurrentUser User user, ProcessLogVo processLogVo){
        EnergyBill energyBill = energyBillService.get(processLogVo.getKeyId());
        return energyBillService.approve(energyBill, processLogVo);
    }

    /**
     * 退回
     */
    @PostMapping("back")
    @ResponseBody
    public AjaxResult back(@CurrentUser User user, ProcessLogVo processLogVo){
        EnergyBill energyBill = energyBillService.get(processLogVo.getKeyId());
        return energyBillService.back(energyBill, processLogVo);
    }

    /**
     * 否决
     */
    @PostMapping("deny")
    @ResponseBody
    public AjaxResult deny(@CurrentUser User user, ProcessLogVo processLogVo){
        EnergyBill energyBill = energyBillService.get(processLogVo.getKeyId());
        return energyBillService.deny(energyBill, processLogVo);
    }

}
