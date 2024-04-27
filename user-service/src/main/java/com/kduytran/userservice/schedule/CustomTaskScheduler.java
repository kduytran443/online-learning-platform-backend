package com.kduytran.userservice.schedule;

import com.kduytran.userservice.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class CustomTaskScheduler {
    private static final Logger logger = LoggerFactory.getLogger(CustomTaskScheduler.class);

    private final IUserService userService;

    @Autowired
    public CustomTaskScheduler(IUserService userService) {
        this.userService = userService;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteInactiveUsersDailyTask() {
        int deletedUserCount = userService.deleteAllInActiveUsers();
        logger.info(String.format("%d users were deleted.", deletedUserCount));
    }

}
