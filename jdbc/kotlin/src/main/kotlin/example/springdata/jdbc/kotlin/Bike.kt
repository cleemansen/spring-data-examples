package example.springdata.jdbc.kotlin

import org.springframework.data.annotation.Id
import org.springframework.data.mapping.callback.EntityCallback
import org.springframework.data.relational.core.conversion.AggregateChange
import org.springframework.data.relational.core.mapping.event.BeforeConvertCallback
import org.springframework.data.relational.core.mapping.event.BeforeSaveCallback
import org.springframework.stereotype.Component
import java.util.*

data class Bike(
        @Id val id: String? = null,
        val manufacturer: String,
        val colors: MutableSet<Color> = mutableSetOf())

@Component
class BikeEntityCallback : BeforeConvertCallback<Bike> {

    override fun onBeforeConvert(aggregate: Bike): Bike {
        return if (aggregate.id == null) {
            aggregate.copy(
                    id = UUID.randomUUID().toString(),
                    colors = aggregate.colors
                            .map { if(it.id == null) it.copy(id = UUID.randomUUID().toString()) else it }
                            .toMutableSet())
        } else aggregate
    }
}