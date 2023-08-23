package fr.lusinelogicielle.mgm.service.metric

import fr.lusinelogicielle.mgm.model.metric.Metric

interface MetricService {

    fun getAllMetrics(): List<Metric>

    fun addMetric(metric: Metric): Metric

    fun findByName(name: String): Metric

    fun incrementMetric(name: String, increment: Int): Metric
}
