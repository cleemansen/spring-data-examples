package example.springdata.jdbc.kotlin

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import java.util.*
import javax.sql.DataSource

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [Application::class])
@AutoConfigureJdbc
@TestPropertySource("/application.yml")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
internal class SimpleTest {

    @Autowired lateinit var bikeRepository: BikeRepository

    @Test
    fun `basic CRUD showcase`() {
        // prepare
        val baseColor = Color(id = UUID.randomUUID().toString(), r = 171, g = 47, b = 28)
        val highlightColor = Color(r = 0, g = 0, b = 0, paintedAbove = baseColor.id)
        val bike = Bike(manufacturer = "müsing", colors = mutableSetOf(baseColor, highlightColor))

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