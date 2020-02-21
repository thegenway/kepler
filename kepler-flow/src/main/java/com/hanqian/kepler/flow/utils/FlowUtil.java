package com.hanqian.kepler.flow.utils;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ClassUtil;
import com.hanqian.kepler.flow.annotation.Flow;
import com.hanqian.kepler.flow.vo.FlowTaskEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FlowUtil {

    /**
     * 获取需要使用流程引擎的实体类列表
     */
    public static List<FlowTaskEntity> getFlowTaskEntityList(){
        List<FlowTaskEntity> flowTaskEntityList = new ArrayList<>();
        Set<Class<?>> entitySet = ClassUtil.scanPackageByAnnotation("com.hanqian.kepler.core.entity", Flow.class);
        entitySet.forEach(entity -> {
            String name = AnnotationUtil.getAnnotationValue(entity, Flow.class);
            String path = ClassUtil.getClassName(entity, false);
            flowTaskEntityList.add(new FlowTaskEntity(name, path));
        });
        return flowTaskEntityList;
    }

}
