package fr.lusinelogicielle.mgm

import MetricRepository
import fr.lusinelogicielle.mgm.model.metric.Metric
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@ComponentScan
@EnableJpaRepositories(basePackageClasses = arrayOf(MetricRepository::class))
@EntityScan(basePackageClasses = arrayOf(Metric::class))
class MgmApplication
fun main(args: Array<String>) {
    runApplication<MgmApplication>(*args)
}
