package com.hanqian.kepler.core.service.sys.impl;

import com.hanqian.kepler.common.base.dao.BaseDao;
import com.hanqian.kepler.common.base.service.BaseServiceImpl;
import com.hanqian.kepler.core.dao.primary.sys.GlobalExceptionDao;
import com.hanqian.kepler.core.entity.primary.sys.GlobalException;
import com.hanqian.kepler.core.service.sys.GlobalExceptionService;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.flow.utils.FlowUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;

/**
 * newFile
 * ======================================
 * author dzw
 * date 2021/2/2 16:08
 * =======================================
 */
@Service
public class GlobalExceptionServiceImpl extends BaseServiceImpl<GlobalException, String> implements GlobalExceptionService {

    @Autowired
    private GlobalExceptionDao globalExceptionDao;

    @Override
    public BaseDao<GlobalException, String> getBaseDao() {
        return globalExceptionDao;
    }

    @Async
    @Override
    public void saveLog(Exception e) {
        GlobalException globalException = new GlobalException();
        globalException.setCreateTime(new Date());

        User user = FlowUtil.getCurrentUser();
        if(user!=null){
            globalException.setCreatorId(user.getId());
            globalException.setCreatorName(user.getName());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream pout = new PrintStream(out);
        e.printStackTrace(pout);
        String ret = new String(out.toByteArray());
        try {
            pout.close();
            out.close();
        } catch (Exception ex) {

        } finally {
            globalException.setTitle(e.toString());
            globalException.setContent(ret);
            save(globalException);
        }

    }
}
