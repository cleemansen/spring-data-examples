package example.springdata.jdbc.kotlin

import org.springframework.data.repository.CrudRepository

interface BikeRepository : CrudRepository<Bike, String> {
}