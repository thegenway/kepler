package com.hanqian.kepler.core.service.sys;

import com.hanqian.kepler.common.base.service.BaseService;
import com.hanqian.kepler.core.entity.primary.sys.GlobalException;

/**
 * newFile
 * ======================================
 * author dzw
 * date 2021/2/2 16:07
 * =======================================
 */
public interface GlobalExceptionService extends BaseService<GlobalException, String> {

    /**
     * 保存异常记录
     */
    void saveLog(Exception e);

}
