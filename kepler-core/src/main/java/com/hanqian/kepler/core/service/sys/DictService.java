package com.hanqian.kepler.core.service.sys;

import com.hanqian.kepler.common.bean.dict.DictTypeVo;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.common.enums.DictEnum;
import com.hanqian.kepler.core.entity.primary.sys.Dict;
import com.hanqian.kepler.common.base.service.BaseService;

import java.util.List;

public interface DictService extends BaseService<Dict, String> {

    /**
     * 获取所有字典列表
     */
    List<DictTypeVo> findDictTypeList();

    /**
     * 创建系统字典
     */
    AjaxResult createDict(DictEnum dictEnum, String name, String key, String description, Integer sortNo);

    /**
     * 根据字典获取可用字典项
     */
    List<Dict> findDictList(DictEnum dictEnum);

    /**
     * 根据字典分类获取所有字典项（可用的+停用的）
     */
    List<Dict> findAllDictList(DictEnum dictEnum);

    /**
     * 根据名字获取字典项
     */
    Dict getDictByName(DictEnum dictEnum, String name);
    Dict getDictByNameIfNullJustCreate(DictEnum dictEnum, String name);

}
