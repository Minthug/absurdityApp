package absurdity.ForOlder.data

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

object Calls : Table("calls") {
    val id = integer("id").autoIncrement()
    val timestamp = timestamp("timestamp")
    val fromBro = varchar("from_bro", 50)
    val toBro = varchar("to_bro", 50)
    val status = varchar("status", 20)

    override val primaryKey = PrimaryKey(id)
}