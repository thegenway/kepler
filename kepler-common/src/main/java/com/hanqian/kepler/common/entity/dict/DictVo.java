package com.hanqian.kepler.common.entity.dict;

import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.common.enums.DictEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DictVo {

    private String id;
    private BaseEnumManager.StateEnum state;
    private Date createTime;
    private String name;
    private DictEnum dictType;
    private String value;
    private String description;
    private Integer sortNo;

}
