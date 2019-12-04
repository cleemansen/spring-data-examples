package example.springdata.jdbc.kotlin.entity.rider

import example.springdata.jdbc.kotlin.entity.bike.Bike
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.event.BeforeConvertCallback
import org.springframework.stereotype.Component
import java.util.*

// root of aggregation unit `rider
data class Rider(@Id val id: String? = null, val name: String)

@Component
class RiderEntityCallback : BeforeConvertCallback<Rider> {

    override fun onBeforeConvert(aggregate: Rider): Rider {
        return if (aggregate.id == null) {
            aggregate.copy(id = UUID.randomUUID().toString())
        } else aggregate
    }
}