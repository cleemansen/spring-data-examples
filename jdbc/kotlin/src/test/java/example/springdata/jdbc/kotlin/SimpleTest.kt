package example.springdata.jdbc.kotlin

import example.springdata.jdbc.kotlin.entity.bike.Bike
import example.springdata.jdbc.kotlin.entity.bike.BikeRepository
import example.springdata.jdbc.kotlin.entity.bike.Color
import example.springdata.jdbc.kotlin.entity.rider.Rider
import example.springdata.jdbc.kotlin.entity.rider.RiderRepository
import org.assertj.core.api.Assertions.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringRunner
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [DataJdbcConfiguration::class])
@AutoConfigureJdbc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
internal class SimpleTest {

    @Autowired lateinit var bikeRepository: BikeRepository
    @Autowired lateinit var riderRepository: RiderRepository

    @Test
    fun `basic CRUD showcase`() {
        // prepare
        val rider = riderRepository.save(Rider(name = "Clemens"))

        val baseColor = Color(id = UUID.randomUUID().toString(), r = 171, g = 47, b = 28)
        val highlightColor = Color(r = 0, g = 0, b = 0, paintedAbove = baseColor.id)
        val bike = Bike(manufacturer = "müsing", riderId = rider.id, colors = mutableSetOf(baseColor, highlightColor))

        // create
        val actual = bikeRepository.save(bike) // C
        assertThat(actual.colors)
                .`as`("It is recommended to use immutable entities in order to get out of trouble w/ hash-sets!")
                .hasSize(2)

        // read
        val read = bikeRepository.findById(actual.id!!).get() // R
        assertThat(read.manufacturer).isEqualTo("müsing")
        assertThat(read.colors).hasSize(2)
        val actualBaseColor = read.colors.find { it.r == 171 }
        val actualHighlightColor = read.colors.find { it.r == 0 }
        assertThat(actualBaseColor?.paintedAbove).isNull()
        assertThat(actualHighlightColor?.paintedAbove).isEqualTo(actualBaseColor?.id)

        val actualByRider = bikeRepository.findAllBikesOfRider(riderId = rider.id)
        assertThat(actualByRider).containsExactly(read)

        // update
        val change = read.copy(manufacturer = "Müsing")
        change.colors.add(Color(r = 255, g = 255, b = 255, paintedAbove = highlightColor.id))
        val updated = bikeRepository.save(change)
        assertThat(updated.id).isEqualTo(read.id)
        assertThat(updated.manufacturer).isEqualTo("Müsing")
        assertThat(updated.colors).hasSize(3)

        // delete
        bikeRepository.delete(change)
        assertThat(bikeRepository.findAll()).isEmpty()
    }
}