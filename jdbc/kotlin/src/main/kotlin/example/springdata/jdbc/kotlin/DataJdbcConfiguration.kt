package example.springdata.jdbc.kotlin

import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories
import org.springframework.data.relational.core.mapping.NamingStrategy
import org.springframework.data.relational.core.mapping.PersistentPropertyPathExtension
import org.springframework.data.relational.core.mapping.event.BeforeSaveEvent
import java.util.*

@Configuration
@EnableJdbcRepositories
class DataJdbcConfiguration : AbstractJdbcConfiguration() {

    @Bean
    fun namingStrategy(): NamingStrategy {
        return object  : NamingStrategy {
            override fun getReverseColumnName(path: PersistentPropertyPathExtension): String {
                return when(path.tableName) {
                    "color" -> {
                        when (path.columnName) {
                            "colors" -> "bike_id"
                            else -> super.getReverseColumnName(path)
                        }
                    }
                    else -> super.getReverseColumnName(path)
                }
            }
        }
    }
}