package com.calendarize.calendarize.Schedulers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.calendarize.calendarize.Services.CalendarTokenCleanupService;

@Component
public class TokensCleanupJob {
    private static final Logger logger = LoggerFactory.getLogger(TokensCleanupJob.class);
    private final CalendarTokenCleanupService calendarTokenCleanupService;

    public TokensCleanupJob(CalendarTokenCleanupService calendarTokenCleanupService) {
        this.calendarTokenCleanupService = calendarTokenCleanupService;
    }
    
    @Scheduled(cron = "0 */10 * * * *")
    public void run() {
        logger.info("Running token clean up job...");

        calendarTokenCleanupService.cleanupTokens();
    }
}
