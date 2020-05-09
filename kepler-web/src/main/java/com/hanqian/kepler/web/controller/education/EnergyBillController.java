package com.hanqian.kepler.web.controller.education;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.hanqian.kepler.common.bean.NameValueVo;
import com.hanqian.kepler.common.bean.jqgrid.JqGridContent;
import com.hanqian.kepler.common.bean.jqgrid.JqGridFilter;
import com.hanqian.kepler.common.bean.jqgrid.JqGridPager;
import com.hanqian.kepler.common.bean.jqgrid.JqGridReturn;
import com.hanqian.kepler.common.bean.other.ImportProgress;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.common.enums.DictEnum;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.common.jpa.specification.SpecificationFactory;
import com.hanqian.kepler.common.utils.ExcelUtils;
import com.hanqian.kepler.core.entity.primary.education.EnergyBill;
import com.hanqian.kepler.core.service.education.EnergyBillService;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.flow.enums.FlowEnum;
import com.hanqian.kepler.flow.vo.ProcessLogVo;
import com.hanqian.kepler.security.annotation.CurrentUser;
import com.hanqian.kepler.web.annotation.RequestJsonParam;
import com.hanqian.kepler.web.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * 基本信息-能源账单 controller with generator
 * ============================================================================
 * author : DongZhengWei
 * createDate:  2020-05-06 15:30:31  。
 * ============================================================================
 */
@Slf4j
@Controller
@RequestMapping("/energyBill")
public class EnergyBillController extends BaseController {

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
            map.put("name", StrUtil.nullToEmpty(entity.getName()));
            map.put("energyTypeDict.name", entity.getEnergyTypeDict()!=null ? entity.getEnergyTypeDict().getName() : ""); //账单类型
            map.put("billDate", entity.getBillDate()!=null ? DateUtil.format(entity.getBillDate(), "yyyy-MM-dd") : ""); //账单月份
            map.put("price", StrUtil.nullToEmpty(Convert.toStr(entity.getPrice()))); //单价
            map.put("useCount", StrUtil.nullToEmpty(Convert.toStr(entity.getUseCount()))); //使用量
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

        return getPrefixUrl("education/energyBill_read");
    }

     /**
        * input页面
        */
     @GetMapping("input")
     public String input(String keyId, Model model){
        EnergyBill energyBill = energyBillService.get(keyId);
        model.addAttribute("energyBill", energyBill);

        return "main/education/energyBill_input";
     }

    /**
     * 赋值
     */
    private EnergyBill setData(User user, String keyId, String name,String energyTypeDictId,String billDate,String price,String useCount,String totalAmount,String barcode,String remark){
        EnergyBill energyBill = energyBillService.get(keyId);
        if(energyBill == null){
            energyBill = new EnergyBill();
            energyBill.setCreator(user);
        }
        energyBill.setCreator(user);
        energyBill.setEnergyTypeDict(dictService.get(energyTypeDictId)); //账单类型
        energyBill.setBillDate(DateUtil.parse(billDate)); //账单月份
        energyBill.setPrice(Convert.toBigDecimal(price)); //单价
        energyBill.setUseCount(Convert.toBigDecimal(useCount)); //使用量
        energyBill.setTotalAmount(Convert.toBigDecimal(totalAmount)); //总金额
        energyBill.setBarcode(barcode); //条形码
        energyBill.setRemark(remark); //备注

        return energyBill;
     }

    /**
     * 保存
     */
    @PostMapping("save")
    @ResponseBody
    public AjaxResult save(@CurrentUser User user, ProcessLogVo processLogVo, String name,String energyTypeDictId,String billDate,String price,String useCount,String totalAmount,String barcode,String remark){
        EnergyBill energyBill = setData(user,processLogVo.getKeyId(),name,energyTypeDictId,billDate,price,useCount,totalAmount,barcode,remark);

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
    public AjaxResult commit(@CurrentUser User user, ProcessLogVo processLogVo, String name,String energyTypeDictId,String billDate,String price,String useCount,String totalAmount,String barcode,String remark){
        EnergyBill energyBill = setData(user,processLogVo.getKeyId(),name,energyTypeDictId,billDate,price,useCount,totalAmount,barcode,remark);
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

    /**
     * 撤回
     */
    @PostMapping("withdraw")
    @ResponseBody
    public AjaxResult withdraw(@CurrentUser User user, ProcessLogVo processLogVo){
        EnergyBill energyBill = energyBillService.get(processLogVo.getKeyId());
        return energyBillService.withdraw(energyBill, processLogVo);
    }

    /**
     * 数据导出
     */
    @GetMapping("export")
    @ResponseBody
    public void export() throws IOException {
    List<Rule> rules = new ArrayList<>();
        rules.add(Rule.eq("state", BaseEnumManager.StateEnum.Enable));
        rules.add(Rule.in("processState", FlowEnum.FLOW_DATA_LIST_ENUMS));
        List<EnergyBill> energyBillList = energyBillService.findAll(SpecificationFactory.where(rules));

        List<Map<String, String>> dataMapList = new ArrayList<>();
        List<NameValueVo> nameValueVos = new ArrayList<>();
        energyBillList.forEach(entity -> {
            Map<String, String> map = new HashMap<>();
            map.put("name", Convert.toStr(entity.getName()));
            nameValueVos.add(new NameValueVo("名称", "name"));

            map.put("energyTypeDict", Convert.toStr(entity.getEnergyTypeDict()!=null ? entity.getEnergyTypeDict().getName() : ""));
            nameValueVos.add(new NameValueVo("账单类型", "energyTypeDict"));

            map.put("billDate", entity.getBillDate()!=null ? DateUtil.format(entity.getBillDate(), "yyyy-MM-dd") : "");
            nameValueVos.add(new NameValueVo("账单月份", "billDate"));

            map.put("price", Convert.toStr(entity.getPrice()));
            nameValueVos.add(new NameValueVo("单价", "price"));

            map.put("useCount", Convert.toStr(entity.getUseCount()));
            nameValueVos.add(new NameValueVo("使用量", "useCount"));

            map.put("totalAmount", Convert.toStr(entity.getTotalAmount()));
            nameValueVos.add(new NameValueVo("总金额", "totalAmount"));

            map.put("barcode", Convert.toStr(entity.getBarcode()));
            nameValueVos.add(new NameValueVo("条形码", "barcode"));

            map.put("remark", Convert.toStr(entity.getRemark()));
            nameValueVos.add(new NameValueVo("备注", "remark"));


            dataMapList.add(map);
        });
        List<Map<String, String>> rows = CollUtil.newArrayList(dataMapList);
        ExcelUtils.export(response, "EnergyBill", rows, nameValueVos);
    }

    /**
     * 导入
     */
    @PostMapping("importData")
    @ResponseBody
    public AjaxResult importData(@RequestParam("file") MultipartFile multipartFile) {
        if(multipartFile == null) return AjaxResult.error("multipartFile is null");
        int globalIndex = 0;

        HttpSession session = request.getSession();
        String progressSessionName = "energyBill/importData";

        try{
            ExcelReader excelReader = ExcelUtil.getReader(multipartFile.getInputStream(), 0);
            int rowCount = excelReader.getRowCount();
            log.info("开始执行导入数据【EnergyBill】，共有行数【"+rowCount+"】");
            List<EnergyBill> importList = new ArrayList<>();
            for(int i=0;i<rowCount;i++){
                if(i==0) continue;
                globalIndex = i;
                session.setAttribute(progressSessionName, ImportProgress.build(globalIndex,rowCount-1,"正在分析"));

                String name = Convert.toStr(excelReader.readCellValue(0,i));
                String energyTypeDict = Convert.toStr(excelReader.readCellValue(1,i)); //账单类型
                String billDate = Convert.toStr(excelReader.readCellValue(2,i)); //账单月份
                String price = Convert.toStr(excelReader.readCellValue(3,i)); //单价
                String useCount = Convert.toStr(excelReader.readCellValue(4,i)); //使用量
                String totalAmount = Convert.toStr(excelReader.readCellValue(5,i)); //总金额
                String barcode = Convert.toStr(excelReader.readCellValue(6,i)); //条形码
                String remark = Convert.toStr(excelReader.readCellValue(7,i)); //备注

                EnergyBill energyBill = new EnergyBill();
                energyBill.setName(name);
                energyBill.setEnergyTypeDict(dictService.getDictByNameIfNullJustCreate(DictEnum.energy_energyType, energyTypeDict));
                energyBill.setBillDate(DateUtil.parse(billDate));
                energyBill.setPrice(Convert.toBigDecimal(price));
                energyBill.setUseCount(Convert.toBigDecimal(useCount));
                energyBill.setTotalAmount(Convert.toBigDecimal(totalAmount));
                energyBill.setBarcode(barcode);
                energyBill.setRemark(remark);

                importList.add(energyBill);
            }

            int createCount = 0,updateCount = 0;
            for(int i=0;i<importList.size();i++){
                globalIndex = i;
                session.setAttribute(progressSessionName, ImportProgress.build(globalIndex,rowCount-1,"正在导入"));

                EnergyBill energyBill = importList.get(i);
                if(StrUtil.isBlank(energyBill.getId())){
                energyBillService.commit(energyBill, new ProcessLogVo());
                    createCount++;
                }else{
                    energyBillService.save(energyBill);
                    updateCount++;
                }
            }
            session.removeAttribute(progressSessionName);
            return AjaxResult.success(StrUtil.format("导入成功，本次导入：新建【{}】个，更新【{}】个", createCount, updateCount));

        }catch (Exception e){
            session.removeAttribute(progressSessionName);
            return AjaxResult.error(StrUtil.format("出错行数【{}】。\n message【{}】。\n trace{}", globalIndex+1, e.getMessage(), e.getStackTrace()));
        }
    }


}
