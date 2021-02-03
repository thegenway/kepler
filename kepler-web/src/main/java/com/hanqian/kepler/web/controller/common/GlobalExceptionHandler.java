package com.hanqian.kepler.web.controller.common;

import com.hanqian.kepler.core.service.sys.GlobalExceptionService;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.flow.utils.FlowUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * 全局异常处理
 * ======================================
 * author dzw
 * date 2021/2/2 15:35
 * =======================================
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private GlobalExceptionService globalExceptionService;

    @ExceptionHandler(Exception.class)
    public void handleException(Exception e) {
        globalExceptionService.saveLog(e);
        e.printStackTrace();
    }

}
