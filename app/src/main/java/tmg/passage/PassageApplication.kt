package tmg.passage

import android.app.Application
import io.realm.Realm
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import tmg.passage.di.passageModule

class PassageApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        // Realm
        Realm.init(this)

        // Koin
        startKoin {
            androidContext(this@PassageApplication)
            modules(passageModule)
        }
    }
}