package absurdity.ForOlder.data

import absurdity.ForOlder.domain.Call
import absurdity.ForOlder.domain.CallStatus
import kotlinx.coroutines.flow.Flow

interface CallRepository {
    suspend fun insertCall(call: Call): Call
    suspend fun getCallById(id: Int): Call?
    fun getAllCalls(): Flow<List<Call>>
    suspend fun updateCallStatus(id: Int, status: CallStatus)
}