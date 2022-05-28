package app.core.jobs;


import app.core.services.AdminService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class CouponExpirationDailyJob {

    private AdminService adminService;

    public CouponExpirationDailyJob(AdminService adminService) {
        this.adminService = adminService;
    }

    @Scheduled(timeUnit = TimeUnit.DAYS, fixedRate = 1,initialDelay = 1)
    public void checkExpired() {
        this.adminService.checkExpired();
    }
}