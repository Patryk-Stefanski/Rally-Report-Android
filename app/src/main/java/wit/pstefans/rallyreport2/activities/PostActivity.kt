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
    var edit = false
    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp
        i("Post Activity started...")

        if (intent.hasExtra("post-edit")) {
            edit=true
            post = intent.extras?.getParcelable("post-edit")!!
            binding.postTitle.setText(post.title)
            binding.description.setText(post.description)
            binding.btnAdd.setText(R.string.save_post)
        }

        binding.btnAdd.setOnClickListener() {
            post.title = binding.postTitle.text.toString()
            post.description = binding.description.text.toString()
            if (post.title.isEmpty()) {
                Snackbar.make(it,R.string.enter_post_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.posts.update(post.copy())
                } else {
                    app.posts.create(post.copy())
                }
            }
            setResult(RESULT_OK)
            finish()
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