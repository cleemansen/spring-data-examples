package example.springdata.jdbc.kotlin.entity.rider

import org.springframework.data.repository.CrudRepository

interface RiderRepository : CrudRepository<Rider, String>