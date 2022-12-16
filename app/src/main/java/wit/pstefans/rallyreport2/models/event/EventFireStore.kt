package wit.pstefans.rallyreport2.models.event

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await


class EventFireStore : EventStore {
    private val db = Firebase.firestore
    private val collection = "Events"
    private var events = mutableListOf<EventModel>()


    @OptIn(ExperimentalCoroutinesApi::class)
    override fun findAll(): MutableList<EventModel> = runBlocking{
        val snapshot = db.collection(collection).get().await()
        events.clear()
        for (document in snapshot) {
            val event = document.toObject<EventModel>()

            events.add(event)
        }
        return@runBlocking events
    }

    override fun create(event: EventModel) {
        event.ownerUID = FirebaseAuth.getInstance().currentUser!!.uid
        db.collection(collection).document(event.uid).set(event)
    }

    override fun delete(event: EventModel) {
        db.collection(collection).document(event.uid).delete()
    }

    override fun update(event: EventModel) {
        db.collection(collection).document(event.uid).update(mapOf(
            "uid" to event.uid,
            "name" to event.name,
            "startDate" to event.startDate,
            "endDate" to event.endDate,
            "competitors" to event.competitors,
            ))
    }
}