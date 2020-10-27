package com.hanqian.kepler.web.controller.sys;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.hanqian.kepler.common.bean.dict.DictTypeVo;
import com.hanqian.kepler.common.bean.dict.DictVo;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.common.enums.DictEnum;
import com.hanqian.kepler.core.entity.primary.sys.Dict;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.security.annotation.CurrentUser;
import com.hanqian.kepler.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/main/dict")
public class DictController extends BaseController {

    /**
     * 获取所有字典类
     */
    @GetMapping("findDictTypeList")
    @ResponseBody
    public List<DictTypeVo> findDictTypeList(){
        return dictService.findDictTypeList();
    }

    /**
     * 根据字典类获取字典项
     */
    @GetMapping("findDictListByType")
    @ResponseBody
    public List<DictVo> findDictListByType(String dictType){
        List<Dict> dictList = dictService.findAllDictList(DictEnum.valueOf(dictType));
        List<DictVo> dictVoList = new ArrayList<>();
        dictList.forEach(dict->{
            DictVo dictVo = new DictVo(dict.getId(),dict.getState(),dict.getCreateTime(),dict.getName(),dict.getDictType(),dict.getValue(),dict.getDescription(),dict.getSortNo());
            dictVoList.add(dictVo);
        });
        return dictVoList;
    }

    /**
     * input
     */
    @GetMapping("input")
    public String input(String keyId, String dictType, Model model){
        model.addAttribute("dict", dictService.get(keyId));
        try {
            DictEnum dictEnum = DictEnum.valueOf(dictType);
            model.addAttribute("dictType", dictEnum);
        }catch (Exception e){
            model.addAttribute("dictType", null);
        }

        return "/main/sys/dict_input";
    }

    /**
     * 保存
     */
    @PostMapping("save")
    @ResponseBody
    public AjaxResult save(@CurrentUser User user, String keyId, String dictType, String name,
                           String value, String description, Integer sortNo){
        DictEnum dictEnum = null;
        try{
            dictEnum = DictEnum.valueOf(dictType);
        }catch (Exception e){
            return AjaxResult.error("字典类获取异常");
        }

        Dict dict = dictService.get(keyId);
        if(dict == null){
            dict = new Dict();
        }
        dict.setDictType(dictEnum);
        dict.setName(name);
        dict.setValue(value);
        dict.setDescription(description);
        dict.setSortNo(sortNo);
        dictService.save(dict);
        return AjaxResult.success();
    }

    /**
     * 操作
     */
    @PostMapping("{method}/{keyId}")
    @ResponseBody
    public AjaxResult method(@PathVariable String method, @PathVariable String keyId){
        Dict dict = dictService.get(keyId);
        if(dict==null) return AjaxResult.error("未找到系统字典");

        BaseEnumManager.StateEnum stateEnum = BaseEnumManager.StateEnum.valueOf(method);
        dict.setState(stateEnum);
        dictService.save(dict);
        return AjaxResult.success();
    }

    /**
     * 初始化字典项数据
     */
    @PostMapping("initData")
    @ResponseBody
    public AjaxResult initData(String dictType){
        DictEnum dictEnum = dictService.getDictEnum(dictType);
        if(dictEnum == null){
            return AjaxResult.error("获取字典枚举异常");
        }

        List<Dict> dictList = dictService.findAllDictList(dictEnum);
        if(dictList.size() > 0){
            return AjaxResult.error("当前已经存在有字典项，无法初始化数据");
        }

        String path = "/json/init_dict_data.json";
        File file = new File(this.getClass().getResource(path).getPath());
        JSONArray dictNameJsonArray = JSONUtil.readJSONArray(file, CharsetUtil.charset("UTF-8"));
        for(int i=0;i<dictNameJsonArray.size();i++){
            JSONObject jsonObject = dictNameJsonArray.getJSONObject(i);
            if(StrUtil.equals(dictType, jsonObject.getStr("name"))){
                JSONArray itemsJsonArray = jsonObject.getJSONArray("items");
                for(int j=0;j<itemsJsonArray.size();j++){
                    String name = itemsJsonArray.getStr(j);
                    dictService.createDict(dictEnum, name, null, null, j*10);
                }
                return AjaxResult.success();
            }
        }

        return AjaxResult.error("未从配置文件中获取到此字典");
    }

}
