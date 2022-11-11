package wit.pstefans.rallyreport2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import wit.pstefans.rallyreport2.databinding.CardPostBinding
import wit.pstefans.rallyreport2.models.PostModel

interface PostListener {
    fun onPostClick(post: PostModel)
}

class PostAdapter constructor(private var posts: List<PostModel>,
                                   private val listener: PostListener) :
    RecyclerView.Adapter<PostAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardPostBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val post = posts[holder.adapterPosition]
        holder.bind(post, listener)
    }

    override fun getItemCount(): Int = posts.size

    class MainHolder(private val binding : CardPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(post: PostModel, listener: PostListener) {
            binding.postTitle.text = post.title
            binding.description.text = post.description
            binding.root.setOnClickListener { listener.onPostClick(post) }
        }
    }
}