package wit.pstefans.rallyreport2.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import timber.log.Timber.i
import wit.pstefans.rallyreport2.R
import wit.pstefans.rallyreport2.databinding.ActivityPostBinding
import wit.pstefans.rallyreport2.models.PostModel

class PostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostBinding
    var post = PostModel()
    val posts = ArrayList<PostModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Timber.plant(Timber.DebugTree())
        i("Post Activity started...")

        binding.btnAdd.setOnClickListener() {
            post.title = binding.postTitle.text.toString()
            post.description = binding.description.text.toString()

            if (post.title.isNotEmpty()) {
                posts.add(post.copy())
                i("add Button Pressed: ${post}")
                for (i in posts.indices)
                { i("Post[$i]:${this.posts[i]}") }
            }
            else {
                Snackbar.make(it,"Please Enter a title", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}