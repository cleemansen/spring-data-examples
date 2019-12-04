package example.springdata.jdbc.kotlin.entity.bike

import example.springdata.jdbc.kotlin.entity.bike.Bike
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository

interface BikeRepository : CrudRepository<Bike, String> {

    @Query("""
        SELECT *
        FROM bike
        WHERE rider_id = :riderId
    """)
    fun findAllBikesOfRider(riderId: String?): List<Bike>
}