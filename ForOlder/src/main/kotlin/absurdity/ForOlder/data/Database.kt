package absurdity.ForOlder.data

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        val databaseUrl = System.getenv("DATABASE_URL") ?: "jdbc:postgresql://localhost:5432/brothers"
        val databaseDriver = System.getenv("DATABASE_DRIVER") ?: "org.postgresql.Driver"
        val databaseUser = System.getenv("DATABASE_USER") ?: "minthug"
        val databasePassword = System.getenv("DATABASE_PASSWORD") ?: "1234"

        Database.connect(
            url = databaseUrl,
            driver = databaseDriver,
            user = databaseUser,
            password = databasePassword
        )

        transaction {
            SchemaUtils.drop(Calls)
            SchemaUtils.create(Calls)
        }
    }
}