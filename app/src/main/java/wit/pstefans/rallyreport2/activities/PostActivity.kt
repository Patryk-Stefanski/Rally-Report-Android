package wit.pstefans.rallyreport2.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import timber.log.Timber.i
import wit.pstefans.rallyreport2.R
import wit.pstefans.rallyreport2.databinding.ActivityPostBinding
import wit.pstefans.rallyreport2.main.MainApp
import wit.pstefans.rallyreport2.models.PostModel

class PostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostBinding
    var post = PostModel()
    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp
        i("Post Activity started...")

        binding.btnAdd.setOnClickListener() {
            post.title = binding.postTitle.text.toString()
            post.description = binding.description.text.toString()
            if (post.title.isNotEmpty()) {
                app.posts.add(post.copy())
                i("add Button Pressed: ${post}")
                for (i in app.posts.indices) {
                    i("Posts[$i]:${this.app.posts[i]}")
                }
                setResult(RESULT_OK)
                finish()
            }
            else {
                Snackbar.make(it,"Please Enter a title", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_post, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}