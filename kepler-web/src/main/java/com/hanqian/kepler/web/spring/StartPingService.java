package com.hanqian.kepler.web.spring;

import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.core.service.sys.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StartPingService implements CommandLineRunner {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    /**
     * 项目启动后检查系统内如果没有系统管理员，则自动创建一个系统管理员
     */
    @Override
    public void run(String... args) throws Exception {
        logger.info("============ 项目启动成功 ============");
        List<User> users = userService.findManagers();
        if(users.size() == 0){
            User user = new User();
            user.setName("系统管理员");
            user.setUsername("admin");
            user.setAccountType(BaseEnumManager.AccountTypeEnum.SystemManager);
            user.setPassword(new BCryptPasswordEncoder().encode("password"));
            userService.save(user);
            logger.info("============ 成功自动创建了系统管理员 ============");
        }
    }

}
