package com.hanqian.kepler.flow.utils;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hanqian.kepler.common.Constants;
import com.hanqian.kepler.common.bean.NameValueVo;
import com.hanqian.kepler.common.utils.ServletUtils;
import com.hanqian.kepler.flow.annotation.Flow;
import com.hanqian.kepler.flow.entity.ProcessStep;
import com.hanqian.kepler.flow.entity.TaskEntity;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.flow.enums.FlowEnum;
import com.hanqian.kepler.flow.vo.FlowParticipantInputVo;
import com.hanqian.kepler.flow.vo.FlowParticipantVo;
import com.hanqian.kepler.flow.vo.FlowTaskEntity;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
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

    /**
     * 获取到参与者信息
     */
    public static FlowParticipantVo getFlowParticipantVo(ProcessStep processStep){
        if(processStep!=null && JSONUtil.isJsonObj(processStep.getParticipantJson())){
            return JSONUtil.toBean(processStep.getParticipantJson(), FlowParticipantVo.class);
        }
        return null;
    }

    /**
     * 获取到流程编辑页面回显数据
     */
    public static FlowParticipantInputVo getFlowParticipantInputVo(FlowParticipantVo flowParticipantVo){
        if(flowParticipantVo!=null){
            List<String> departmentIds = new ArrayList<>();
            List<String> departmentNames = new ArrayList<>();
            flowParticipantVo.getDepartment().forEach(department->{departmentIds.add(department.getValue());departmentNames.add(department.getName());});

            List<String> postIds = new ArrayList<>();
            List<String> postNames = new ArrayList<>();
            flowParticipantVo.getPost().forEach(post->{postIds.add(post.getValue());postNames.add(post.getName());});

            List<String> powerIds = new ArrayList<>();
            List<String> powerNames = new ArrayList<>();
            flowParticipantVo.getPower().forEach(power->{powerIds.add(power.getValue());powerNames.add(power.getName());});

            List<String> groupIds = new ArrayList<>();
            List<String> groupNames = new ArrayList<>();
            flowParticipantVo.getGroup().forEach(group->{groupIds.add(group.getValue());groupNames.add(group.getName());});

            List<String> userIds = new ArrayList<>();
            List<String> userNames = new ArrayList<>();
            flowParticipantVo.getUser().forEach(user->{userIds.add(user.getValue());userNames.add(user.getName());});

            return new FlowParticipantInputVo(
                    StrUtil.join(",",departmentIds),
                    StrUtil.join(",", departmentNames),
                    StrUtil.join(",", postIds),
                    StrUtil.join(",", postNames),
                    StrUtil.join(",", powerIds),
                    StrUtil.join(",", powerNames),
                    StrUtil.join(",", groupIds),
                    StrUtil.join(",", groupNames),
                    StrUtil.join(",", userIds),
                    StrUtil.join(",", userNames),
                    flowParticipantVo.getVariable(),
                    flowParticipantVo.getSuperior(),
                    flowParticipantVo.getLeader()
            );
        }
        return null;
    }

    public static FlowParticipantInputVo getFlowParticipantInputVo(ProcessStep processStep){
        return getFlowParticipantInputVo(getFlowParticipantVo(processStep));
    }

    public static User getCurrentUser(){
        HttpServletRequest request = ServletUtils.getRequest();
        return (User)request.getSession().getAttribute(Constants.CURRENT_USER);
    }


}
