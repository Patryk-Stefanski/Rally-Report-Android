package wit.pstefans.rallyreport2.main

import android.app.Application
import com.google.firebase.ktx.Firebase
import timber.log.Timber
import timber.log.Timber.i
import wit.pstefans.rallyreport2.models.PostMemStore

class MainApp : Application() {
    //val posts = PostMemStore()
    lateinit var posts: PostMemStore


    override fun onCreate() {
        super.onCreate()
        posts = PostMemStore()
        Timber.plant(Timber.DebugTree())
        i("Rally Report Started")
    }
}