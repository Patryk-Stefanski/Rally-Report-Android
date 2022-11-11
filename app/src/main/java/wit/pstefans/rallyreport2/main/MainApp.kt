package wit.pstefans.rallyreport2.main

import android.app.Application
import timber.log.Timber
import timber.log.Timber.i
import wit.pstefans.rallyreport2.models.PostMemStore

class MainApp : Application() {

    val posts = PostMemStore()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Rally Report Started")
//        posts.add(PostModel("One", "About one..."))
//        posts.add(PostModel("Two", "About two..."))
//        posts.add(PostModel("Three", "About three..."))
    }
}