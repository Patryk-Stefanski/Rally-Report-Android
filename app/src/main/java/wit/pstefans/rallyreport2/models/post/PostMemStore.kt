package wit.pstefans.rallyreport2.models.post

import android.content.ContentResolver
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import timber.log.Timber.i
import java.net.URL


class PostMemStore : PostStore {
    private val db = Firebase.firestore
    private val collection = "Posts"
    private var posts = mutableListOf<PostModel>()
    private val storageRef = Firebase.storage.reference


    @OptIn(ExperimentalCoroutinesApi::class)
    override fun findAll(): MutableList<PostModel> = runBlocking {
        val snapshot = db.collection(collection).get().await()
        posts.clear()
        for (document in snapshot) {
            val post = PostModel()
            post.uid = document.data["uid"].toString()
            post.mapID = document.data["mapId"].toString()
            post.title = document.data["title"].toString()
            post.description = document.data["description"].toString()
            //post.image = document.get("image") as Uri#
            post.image = Uri.parse(document.data["image"].toString())
            post.imageRef = document.data["imageRef"].toString()
            post.lat = document.data["lat"] as Double
            post.lng = document.data["lng"] as Double
            post.zoom = (document.data["zoom"] as Double).toFloat()
            post.ownerUID = document.data["ownerUID"].toString()

            posts.add(post)
        }
        return@runBlocking posts
    }

    override fun create(post: PostModel): Boolean = runBlocking {
        if (post.image != Uri.EMPTY) {
            val ref = storageRef.child("images/${post.uid}")
            ref.putFile(post.image).await()

            val q = ref.downloadUrl.await()

            val url = q.toString()
            post.imageRef = url
        }

        post.ownerUID = FirebaseAuth.getInstance().currentUser!!.uid
        db.collection(collection).document(post.uid).set(post)
        return@runBlocking true
    }

    override fun delete(post: PostModel) {
        db.collection(collection).document(post.uid).delete()
    }

    override fun update(post: PostModel): Boolean = runBlocking {
        val doc = db.collection(collection).document(post.uid).get().await()
        val image = Uri.parse(doc.data?.get("image").toString())

        if (image != post.image){
            val ref = storageRef.child("images/${post.uid}")
            ref.putFile(post.image).await()

            val q = ref.downloadUrl.await()

            val url = q.toString()
            post.imageRef = url
        }

        db.collection(collection).document(post.uid).update(
            mapOf(
                "title" to post.title,
                "description" to post.description,
                "image" to post.image,
                "imageRef" to post.imageRef,
                "lat" to post.lat,
                "lng" to post.lng,
                "zoom" to post.zoom
            )
        )
        return@runBlocking true
    }
}