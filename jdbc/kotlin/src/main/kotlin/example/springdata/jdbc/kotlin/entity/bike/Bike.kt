package example.springdata.jdbc.kotlin.entity.bike

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.event.BeforeConvertCallback
import org.springframework.stereotype.Component
import java.util.*

// root of aggregation unit `bike`
data class Bike(
        @Id val id: String? = null,
        val manufacturer: String,
        val riderId: String? = null,
        val colors: MutableSet<Color> = mutableSetOf())

@Component
class BikeEntityCallback : BeforeConvertCallback<Bike> {

    override fun onBeforeConvert(aggregate: Bike): Bike {
        var workingCopy = aggregate
        if(workingCopy.id == null) {
            workingCopy = workingCopy.copy(id = UUID.randomUUID().toString())
        }
        if (workingCopy.colors.any{ it.id == null }) {
            workingCopy = workingCopy.copy(colors = aggregate.colors
                    .map {
                        if(it.id == null) it.copy(id = UUID.randomUUID().toString()) else it
                    }.toMutableSet())
        }
        return workingCopy
    }
}