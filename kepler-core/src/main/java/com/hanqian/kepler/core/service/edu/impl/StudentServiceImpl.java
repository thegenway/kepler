package com.hanqian.kepler.core.service.edu.impl;

import com.hanqian.kepler.core.dao.primary.edu.StudentDao;
import com.hanqian.kepler.core.entity.primary.education.Student;
import com.hanqian.kepler.core.service.edu.StudentService;
import com.hanqian.kepler.core.service.flow.impl.BaseFlowServiceImpl;
import com.hanqian.kepler.flow.base.dao.BaseFlowDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl extends BaseFlowServiceImpl<Student> implements StudentService {

    @Autowired
    private StudentDao studentDao;

    @Override
    public BaseFlowDao<Student> getBaseFlowDao() {
        return studentDao;
    }
}
