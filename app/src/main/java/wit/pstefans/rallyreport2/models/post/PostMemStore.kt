package wit.pstefans.rallyreport2.models.post

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await


class PostMemStore : PostStore {
    private val db = Firebase.firestore
    private val collection = "Posts"
    private var posts = mutableListOf<PostModel>()


    @OptIn(ExperimentalCoroutinesApi::class)
    override fun findAll(): MutableList<PostModel> = runBlocking{
            val snapshot = db.collection(collection).get().await()
            posts.clear()
            for (document in snapshot) {
                val post = PostModel()
                 post.uid = document.data["uid"].toString()
                 post.mapID = document.data["mapId"].toString()
                 post.title = document.data["title"].toString()
                 post.description = document.data["description"].toString()
                 //post.image = document.get("image") as Uri#
                 post.image = Uri.EMPTY
                 post.lat = document.data["lat"] as Double
                 post.lng = document.data["lng"] as Double
                 post.zoom = (document.data["zoom"] as Double).toFloat()
                 post.ownerUID = document.data["ownerUID"].toString()

                 posts.add(post)
            }
        return@runBlocking posts
    }

    override fun create(post: PostModel) {
        post.ownerUID = FirebaseAuth.getInstance().currentUser!!.uid
        db.collection(collection).document(post.uid).set(post)
    }

    override fun delete(post: PostModel) {
        db.collection(collection).document(post.uid).delete()
    }

    override fun update(post: PostModel) {
        db.collection(collection).document(post.uid).update(mapOf(
            "title" to post.title,
            "description" to post.description,
            //"image" to post.image,
            "lat" to post.lat,
            "lng" to post.lng,
            "zoom" to post.zoom
        ))
    }
}