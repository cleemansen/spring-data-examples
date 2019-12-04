package example.springdata.jdbc.kotlin

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column

data class Color(
        @Id val id: String? = null,
        val bikeId: String? = null,
        val r: Int,
        val g: Int,
        val b: Int,
        @Column("painted_above") val paintedAbove: String? = null)