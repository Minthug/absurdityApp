package absurdity.ForOlder.config

import absurdity.ForOlder.data.CallRepository
import absurdity.ForOlder.data.CallRepositoryImpl
import absurdity.ForOlder.data.DatabaseFactory
import absurdity.ForOlder.service.CallService
import org.koin.dsl.module


object AppConfig {
    val module = module {
        single {
            DatabaseFactory.init()
        }

        single<CallRepository> { CallRepositoryImpl() }

        single { CallService(get()) }
    }
}


