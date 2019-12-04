package example.springdata.jdbc.kotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}