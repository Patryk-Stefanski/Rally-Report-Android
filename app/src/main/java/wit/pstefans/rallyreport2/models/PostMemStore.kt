package wit.pstefans.rallyreport2.models

import timber.log.Timber.i

var lastId = 0L

class PostMemStore : PostStore{
    val posts = ArrayList<PostModel>()

    override fun findAll(): List<PostModel> {
        return posts
    }

    override fun create(post: PostModel) {
        posts.add(post)
        logAll()
    }

    override fun delete(post: PostModel) {
        posts.remove(post)
    }

    override fun update(post: PostModel) {
        var foundPost: PostModel? = posts.find { p -> p.id == post.id }
        if (foundPost != null) {
            foundPost.title = post.title
            foundPost.description = post.description
            foundPost.image = post.image
            foundPost.lat = post.lat
            foundPost.lng = post.lng
            foundPost.zoom = post.zoom
            logAll()
        }
    }

    fun logAll() {
        posts.forEach{ i("${it}") }
    }
}