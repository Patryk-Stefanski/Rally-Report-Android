package wit.pstefans.rallyreport2.main

import android.app.Application
import timber.log.Timber
import timber.log.Timber.i
import wit.pstefans.rallyreport2.models.competitor.CompetitorFireStore
import wit.pstefans.rallyreport2.models.competitor.CompetitorStore
import wit.pstefans.rallyreport2.models.event.EventFireStore
import wit.pstefans.rallyreport2.models.post.PostMemStore

class MainApp : Application() {
    //val posts = PostMemStore()
    lateinit var posts: PostMemStore
    lateinit var competitors: CompetitorFireStore
    lateinit var events: EventFireStore


    override fun onCreate() {
        super.onCreate()
        posts = PostMemStore()
        competitors = CompetitorFireStore()
        events = EventFireStore()
        Timber.plant(Timber.DebugTree())
        i("Rally Report Started")
    }
}