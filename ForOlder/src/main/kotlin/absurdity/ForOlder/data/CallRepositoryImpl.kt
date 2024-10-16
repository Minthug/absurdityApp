package absurdity.ForOlder.data

import absurdity.ForOlder.domain.Call
import absurdity.ForOlder.domain.CallStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update


class CallRepositoryImpl : CallRepository {

    override suspend fun updateCallStatus(id: Int, status: CallStatus): Unit = newSuspendedTransaction {
        Calls.update({ Calls.id eq id }) {
            it[Calls.status] = status.name
        }
    }

    override fun getAllCalls(): Flow<List<Call>> = flow {
        val calls = newSuspendedTransaction {
            Calls.selectAll().map { resultRow ->
                Call(
                    id = resultRow[Calls.id],
                    timeStomp = resultRow[Calls.timestamp],
                    fromBro = resultRow[Calls.fromBro],
                    toBro = resultRow[Calls.toBro],
                    status = CallStatus.valueOf(resultRow[Calls.status])
                )
            }
        }
        emit(calls)
    }

    override suspend fun getCallById(id: Int): Call? = newSuspendedTransaction {
        Calls.select { Calls.id eq id }
            .singleOrNull()
            ?.let { resultRow ->
                Call(
                    id = resultRow[Calls.id],
                    timeStomp = resultRow[Calls.timestamp],
                    fromBro = resultRow[Calls.fromBro],
                    toBro = resultRow[Calls.toBro],
                    status = CallStatus.valueOf(resultRow[Calls.status])
                )
            }
    }

    override suspend fun insertCall(call: Call): Call = newSuspendedTransaction {
        val id = Calls.insert {
            it[timestamp] = call.timeStomp
            it[fromBro] = call.fromBro
            it[toBro] = call.toBro
            it[status] = call.status.name
        } get Calls.id

        call.copy(id = id)
    }
}