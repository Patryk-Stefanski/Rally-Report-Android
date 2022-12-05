package wit.pstefans.rallyreport2.models

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import timber.log.Timber.i
import java.util.*
import kotlin.collections.ArrayList


class PostMemStore : PostStore{
    var posts = ArrayList<PostModel>()
    var post = PostModel()
    private val db = Firebase.firestore
    private lateinit var firebaseAuth: FirebaseAuth
    private val collection = "Posts"



    override fun findAll(): List<PostModel> {
        db.collection(collection).get().addOnSuccessListener { result ->
            for (document in result) {
                i("${document.id} => ${document.data}")
                post.uid = document.get("uid") as UUID
                post.description = document.get("description") as String
                post.image = document.get("image") as Uri
                post.lat = document.get("lat") as Double
                post.lng = document.get("lng") as Double
                post.zoom = document.get("zoom") as Float
                post.ownerUID = document.get("ownerUID") as String

                posts.add(post)
            }
        }.addOnFailureListener {
            i("Error getting documents.")
               }

       return  posts
    }

    override fun create(post: PostModel) {
        post.ownerUID = firebaseAuth.uid!!
        db.collection(collection).document(post.uid.toString()).set(post)
    }

    override fun delete(post: PostModel) {
        db.collection(collection).document(post.uid.toString()).delete()
    }

    override fun update(post: PostModel) {
        db.collection(collection).document(post.uid.toString()).update(mapOf(
            "Title" to post.title,
            "Description" to post.description,
            "image" to post.image,
            "lat" to post.lat,
            "lng" to post.lng,
            "zoom" to post.zoom
        ))
    }
}