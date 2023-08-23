package fr.lusinelogicielle.mgm.service.metric

import MetricRepository
import fr.lusinelogicielle.mgm.exceptions.metric.MetricException
import fr.lusinelogicielle.mgm.model.metric.Metric
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MetricServiceImpl : MetricService {

    @Autowired
    private lateinit var metricRepository: MetricRepository

    override fun getAllMetrics(): List<Metric> {
        return metricRepository.findAll()
    }

    override fun addMetric(metric: Metric): Metric {
        return metricRepository.save(metric)
    }

    override fun findByName(name: String): Metric {
        return metricRepository.findByName(name)
    }

    override fun incrementMetric(name: String, increment: Int): Metric {
        val existingMetric = metricRepository.findByName(name)
        if (existingMetric != null) {
            existingMetric.value += increment.toBigDecimal()
            val updatedMetric = metricRepository.save(existingMetric)
            return updatedMetric
        } else {
            throw MetricException("Metric with name $name not found")
        }
    }
}
