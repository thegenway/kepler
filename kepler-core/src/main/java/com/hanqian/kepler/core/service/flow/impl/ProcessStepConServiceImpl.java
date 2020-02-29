package com.hanqian.kepler.core.service.flow.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.base.dao.BaseDao;
import com.hanqian.kepler.common.base.service.BaseServiceImpl;
import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.common.jpa.specification.SpecificationFactory;
import com.hanqian.kepler.core.service.flow.ProcessStepConService;
import com.hanqian.kepler.flow.base.FlowEntity;
import com.hanqian.kepler.flow.dao.ProcessStepConDao;
import com.hanqian.kepler.flow.entity.ProcessStep;
import com.hanqian.kepler.flow.entity.ProcessStepCon;
import com.hanqian.kepler.flow.enums.FlowEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProcessStepConServiceImpl extends BaseServiceImpl<ProcessStepCon, String> implements ProcessStepConService {

    @Autowired
    private ProcessStepConDao processStepConDao;

    @Override
    public BaseDao<ProcessStepCon, String> getBaseDao() {
        return processStepConDao;
    }

    @Override
    public List<ProcessStepCon> findProcessStepConByProcessStep(ProcessStep processStep) {
        if(processStep == null) return new ArrayList<>();
        List<Rule> rules = new ArrayList<>();
        rules.add(Rule.eq("state", BaseEnumManager.StateEnum.Enable));
        rules.add(Rule.eq("processStep", processStep));
        return findAll(SpecificationFactory.where(rules));
    }

    @Override
    public boolean checkStepConWithFlowTask(FlowEntity entity, ProcessStepCon con) {
        if(entity!=null && con!=null){
            String formulaField = con.getFormulaField();
            FlowEnum.ProcessStepRule formulaFlag = con.getFormulaFlag();
            String formulaVal = con.getFormulaVal();
            if(StrUtil.isNotBlank(formulaField) && formulaFlag!=null && StrUtil.isNotBlank(formulaVal)){
                Object object = null;
                try{
                    object = ReflectUtil.invoke(entity, "get" + StrUtil.upperFirst(formulaField));
                }catch (UtilException e){
                    return false;
                }

                if(object==null) return false;
                if(object instanceof String || object instanceof Enum){
                    return compareOfString(object, formulaFlag, formulaVal);
                }else if(object instanceof Date){
                    return compareOfDate(object, formulaFlag, formulaVal);
                }else if(object instanceof Number){
                    return compareOfNumber(object, formulaFlag, formulaVal);
                }

            }
        }

        return false;
    }

    //字符串类型的比较（枚举类型也使用，取name()）
    private boolean compareOfString(Object object, FlowEnum.ProcessStepRule flag, String formulaVal){
        if(object!=null && flag!=null && StrUtil.isNotBlank(formulaVal)){
            String value = String.valueOf(object);
            if(FlowEnum.ProcessStepRule.EQ.equals(flag)){
                return StrUtil.equals(value, formulaVal);
            }else if(FlowEnum.ProcessStepRule.NE.equals(flag)){
                return !StrUtil.equals(value, formulaVal);
            }else if(FlowEnum.ProcessStepRule.IN.equals(flag)){
                return value.contains(formulaVal);
            }else if(FlowEnum.ProcessStepRule.NotIn.equals(flag)){
                return !value.contains(formulaVal);
            }
        }
        return false;
    }

    //日期类型比较
    private boolean compareOfDate(Object object, FlowEnum.ProcessStepRule flag, String formulaVal){
        if(object!=null && flag!=null && StrUtil.isNotBlank(formulaVal)){
            Date value = (Date)object;
            Date refDate = null;
            try{
                refDate = formulaVal.contains(":") ? DateUtil.parseDateTime(formulaVal) : DateUtil.parseDate(formulaVal);
            }catch (Exception e){
                return false;
            }

            if(FlowEnum.ProcessStepRule.EQ.equals(flag)){
                return DateUtil.compare(value, refDate) == 0;
            }else if(FlowEnum.ProcessStepRule.NE.equals(flag)){
                return DateUtil.compare(value, refDate) != 0;
            }else if(FlowEnum.ProcessStepRule.GE.equals(flag)){
                return DateUtil.compare(value, refDate) >= 0;
            }else if(FlowEnum.ProcessStepRule.GT.equals(flag)){
                return DateUtil.compare(value, refDate) > 0;
            }else if(FlowEnum.ProcessStepRule.LE.equals(flag)){
                return DateUtil.compare(value, refDate) <= 0;
            }else if(FlowEnum.ProcessStepRule.LT.equals(flag)){
                return DateUtil.compare(value, refDate) < 0;
            }

        }
        return false;
    }

    //数字类型比较
    private boolean compareOfNumber(Object object, FlowEnum.ProcessStepRule flag, String formulaVal){
        if(object!=null && flag!=null && StrUtil.isNotBlank(formulaVal)){
            BigDecimal value = NumberUtil.toBigDecimal((Number)object);
            BigDecimal refValue = NumberUtil.toBigDecimal(formulaVal);

            if(FlowEnum.ProcessStepRule.EQ.equals(flag)){
                return NumberUtil.equals(value, refValue);
            }else if(FlowEnum.ProcessStepRule.NE.equals(flag)){
                return !NumberUtil.equals(value, refValue);
            }else if(FlowEnum.ProcessStepRule.GE.equals(flag)){
                return NumberUtil.isGreaterOrEqual(value, refValue);
            }else if(FlowEnum.ProcessStepRule.GT.equals(flag)){
                return NumberUtil.isGreater(value, refValue);
            }else if(FlowEnum.ProcessStepRule.LE.equals(flag)){
                return NumberUtil.isLessOrEqual(value, refValue);
            }else if(FlowEnum.ProcessStepRule.LT.equals(flag)){
                return NumberUtil.isLess(value, refValue);
            }else if(FlowEnum.ProcessStepRule.IN.equals(flag)){
                return formulaVal.contains(String.valueOf(object));
            }else if(FlowEnum.ProcessStepRule.NotIn.equals(flag)){
                return !formulaVal.contains(String.valueOf(object));
            }
        }
        return false;
    }
}
