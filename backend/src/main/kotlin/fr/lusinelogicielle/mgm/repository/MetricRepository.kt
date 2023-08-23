import fr.lusinelogicielle.mgm.model.metric.Metric
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MetricRepository : JpaRepository<Metric, Long> {
    fun findByName(name: String): Metric
}
