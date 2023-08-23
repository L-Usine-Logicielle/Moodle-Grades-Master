package fr.lusinelogicielle.mgm.controller

import fr.lusinelogicielle.mgm.model.metric.Metric
import fr.lusinelogicielle.mgm.service.metric.MetricServiceImpl
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/metrics")
class MetricsController() {

    @Autowired(required = false)
    private lateinit var metricService: MetricServiceImpl

    @Value("\${spring.datasource.url}")
    private lateinit var h2Url: String

    @Value("\${spring.datasource.username}")
    private lateinit var h2Username: String

    @Value("\${spring.datasource.password}")
    private lateinit var h2Password: String

    @GetMapping
    fun getMetrics(): List<Metric> {
        return metricService.getAllMetrics()
    }

    @PostMapping
    fun addMetric(@Valid @RequestBody metric: Metric): ResponseEntity<Metric> {
        return ResponseEntity.ok(metricService.addMetric(metric))
    }

    @GetMapping("/{metricName}")
    fun getMetric(@PathVariable metricName: String): ResponseEntity<Metric> {
        return ResponseEntity.ok(metricService.findByName(metricName))
    }

    @PutMapping("/{metricName}")
    fun incrementMetric(@PathVariable metricName: String, @RequestParam(value = "increment", required = false, defaultValue = "1") increment: Int): ResponseEntity<Metric> {
        return ResponseEntity.ok(metricService.incrementMetric(metricName, increment))
    }
}
