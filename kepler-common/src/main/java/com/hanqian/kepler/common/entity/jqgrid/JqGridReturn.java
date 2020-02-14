package com.hanqian.kepler.common.entity.jqgrid;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * jqGrid返回参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JqGridReturn {

    /**
     * 数据对象
     */
    private List<Map<String, Object>> dataRows;

    /**
     * 当前第几页
     */
    private Integer page;

    /**
     * 每页显示几行
     */
    private Integer rows;

    /**
     * 总共查出了多少条数据
     */
    private Long records;

    /**
     * 总共有几页
     */
    private Integer total;

    public static JqGridReturn build(List<Map<String, Object>> dataRows, Page<?> page){
        JqGridReturn jqGridReturn = new JqGridReturn();
        jqGridReturn.setDataRows(dataRows);
        if(page!=null){
            jqGridReturn.setPage(page.getNumber()+1);
            jqGridReturn.setRows(page.getNumberOfElements());
            jqGridReturn.setRecords(page.getTotalElements());
            jqGridReturn.setTotal(page.getTotalPages());
        }
        return jqGridReturn;
    }

}
