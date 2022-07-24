package com.shashankbhat.splitbill.util;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Schedulers {


    @Scheduled(cron = "0 30 14-23 * * *")
    void runEveryThirtyMinute(){
        System.out.println("Alive" + new Date());
    }

}
