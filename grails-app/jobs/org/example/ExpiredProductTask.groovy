package org.example.service

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Slf4j
@Component
class ExpiredProductTask {

    private final StoreService storeService

    @Autowired
    ExpiredProductTask(StoreService storeService) {
        this.storeService = storeService
    }

    @Scheduled(cron = "*/10 * * * * ?")
    void returnExpiredProductsToWarehouse() {
        log.info("Starting ExpiredProductTask at ${new Date()}")
        storeService.returnExpiredProductsToWarehouse()
        log.info("Completed ExpiredProductTask at ${new Date()}")
    }

}