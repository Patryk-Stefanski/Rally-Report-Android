package wit.pstefans.rallyreport2.models

interface PostStore {
    fun findAll(): List<PostModel>
    fun create(post: PostModel)
    fun update(post: PostModel)
}