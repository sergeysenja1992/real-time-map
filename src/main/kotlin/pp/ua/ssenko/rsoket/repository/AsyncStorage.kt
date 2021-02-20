package pp.ua.ssenko.rsoket.repository

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.conflate
import pp.ua.ssenko.rsoket.domain.StorableEntity
import pp.ua.ssenko.rsoket.utils.log
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AsyncFileStorage<T: StorableEntity>(
    locationMapsDirectory: String, name: String, type: Class<T>
): FileStorage<T>(locationMapsDirectory, name, type) {

    val storeExecutor: Executor = Executors.newSingleThreadExecutor()
    val flow = MutableSharedFlow<Long>()

    init {
        GlobalScope.launch(storeExecutor.asCoroutineDispatcher()) {
            flow.buffer(2, DROP_OLDEST).collect {
                super.store()
                delay(100)
            }
        }
    }

    override fun store() {
        GlobalScope.launch(storeExecutor.asCoroutineDispatcher()) {
            flow.emit(System.currentTimeMillis())
        }
    }

}
