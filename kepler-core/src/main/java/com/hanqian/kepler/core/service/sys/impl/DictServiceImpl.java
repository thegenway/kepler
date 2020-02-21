package com.hanqian.kepler.core.service.sys.impl;

import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.bean.dict.DictTypeVo;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.common.enums.DictEnum;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.common.jpa.specification.SpecificationFactory;
import com.hanqian.kepler.common.base.dao.BaseDao;
import com.hanqian.kepler.core.dao.primary.sys.DictDao;
import com.hanqian.kepler.core.entity.primary.sys.Dict;
import com.hanqian.kepler.common.base.service.BaseServiceImpl;
import com.hanqian.kepler.core.service.sys.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DictServiceImpl extends BaseServiceImpl<Dict, String> implements DictService {

    @Autowired
    private DictDao dictDao;

    @Override
    public BaseDao<Dict, String> getBaseDao() {
        return dictDao;
    }

    @Override
    public List<DictTypeVo> findDictTypeList() {
        DictEnum[] dictEnums = DictEnum.values();
        List<DictTypeVo> dataList = new ArrayList<>();
        Arrays.stream(dictEnums).forEach(type -> dataList.add(new DictTypeVo(type.name(), type.value())));
        return dataList;
    }

    @Override
    public AjaxResult createDict(DictEnum dictEnum, String name, String key, String description, Integer sortNo) {
        if(dictEnum==null || StrUtil.isBlank(name)) return AjaxResult.error("主字典和字典内容不能为空");

        Dict dict = new Dict();
        dict.setDictType(dictEnum);
        dict.setName(name);
        dict.setValue(key);
        dict.setDescription(description);
        dict.setSortNo(sortNo!=null ? sortNo : 999);
        save(dict);
        return AjaxResult.success();
    }

    @Override
    public List<Dict> findDictList(DictEnum dictEnum) {
        Sort sort = new Sort(Sort.Direction.ASC, "sortNo", "createTime");
        List<Rule> rules = new ArrayList<>();
        rules.add(Rule.eq("state", BaseEnumManager.StateEnum.Enable));
        rules.add(Rule.eq("dictType", dictEnum));
        return findAll(SpecificationFactory.where(rules), sort);
    }

    @Override
    public List<Dict> findAllDictList(DictEnum dictEnum) {
        Sort sort = new Sort(Sort.Direction.ASC, "sortNo", "createTime");
        List<Rule> rules = new ArrayList<>();
        rules.add(Rule.in("state", new BaseEnumManager.StateEnum[]{BaseEnumManager.StateEnum.Enable, BaseEnumManager.StateEnum.Disenable}));
        rules.add(Rule.eq("dictType", dictEnum));
        return findAll(SpecificationFactory.where(rules), sort);
    }

}
