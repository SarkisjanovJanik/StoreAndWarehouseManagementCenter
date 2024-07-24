import org.example.ExpiredProductTask

beans = {
    expiredProduct(ExpiredProductTask){
    }

    xmlns task: "http://www.springframework.org/schema/task"

    task.'scheduled-tasks'{
        task.scheduled(ref:'expiredProductTask', method: 'returnExpiredProductsToWarehouse', cron: '*/10 * * * * ?')
    }
}