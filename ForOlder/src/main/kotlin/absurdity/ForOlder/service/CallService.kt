package absurdity.ForOlder.service

import absurdity.ForOlder.data.CallRepository
import absurdity.ForOlder.domain.Call
import absurdity.ForOlder.domain.CallStatus
import kotlinx.coroutines.flow.Flow

class CallService(private val repository: CallRepository) {
    suspend fun initiateCall(fromBro: String, toBro: String): Call {
        val call = Call(fromBro = fromBro, toBro = toBro, status = CallStatus.INITIATED)
        return repository.insertCall(call)
    }

    suspend fun receiveCall(callId: Int) {
        repository.updateCallStatus(callId, CallStatus.RECEIVED)
    }

    suspend fun missCall(callId: Int) {
        repository.updateCallStatus(callId, CallStatus.MISSED)
    }

    fun getAllCalls(): Flow<List<Call>> = repository.getAllCalls()

    suspend fun getCallById(id: Int): Call? = repository.getCallById(id)
}