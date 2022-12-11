package wit.pstefans.rallyreport2.models.competitor

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await


class CompetitorFireStore : CompetitorStore {
    private val db = Firebase.firestore
    private val collection = "Competitors"
    private var competitors = mutableListOf<CompetitorModel>()


    @OptIn(ExperimentalCoroutinesApi::class)
    override fun findAll(): MutableList<CompetitorModel> = runBlocking{
        val snapshot = db.collection(collection).get().await()
        competitors.clear()
        for (document in snapshot) {
            val competitor = document.toObject<CompetitorModel>()

            competitors.add(competitor)
        }
        return@runBlocking competitors
    }

    override fun create(competitor: CompetitorModel) {
        db.collection(collection).document(competitor.uid).set(competitor)
    }

    override fun delete(competitor: CompetitorModel) {
        db.collection(collection).document(competitor.uid).delete()
    }

    override fun update(competitor: CompetitorModel) {
        db.collection(collection).document(competitor.uid).update(mapOf(
            "driverFirstName" to competitor.driverFirstName,
            "driverLastName" to competitor.driverLastName,
            "navFirstName" to competitor.navFirstName,
            "navLastName" to competitor.navLastName,
            "compNo" to competitor.compNo,
            "car" to competitor.car,
        ))
    }
}