package fr.lusinelogicielle.mgm.service.metric

import MetricRepository
import fr.lusinelogicielle.mgm.exceptions.metric.MetricException
import fr.lusinelogicielle.mgm.model.metric.Metric
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

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
        try {
            return metricRepository.findByName(name)
        } catch (e: EmptyResultDataAccessException) {
            throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Metric not found",
            )
        }
    }

    override fun incrementMetric(name: String, increment: Int): Metric {
        try {
            val existingMetric = metricRepository.findByName(name)
            existingMetric.value += increment.toBigDecimal()
            return metricRepository.save(existingMetric)
        } catch (e: EmptyResultDataAccessException) {
            val newMetric = Metric(name = name, value = increment.toBigDecimal())
            return metricRepository.save(newMetric)
        } catch (e: Exception) {
            throw MetricException("Unable to increment or create metric")
        }
    }
}
