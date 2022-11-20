package wit.pstefans.rallyreport2.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import timber.log.Timber.i
import wit.pstefans.rallyreport2.R
import wit.pstefans.rallyreport2.databinding.ActivityPostBinding
import wit.pstefans.rallyreport2.helpers.showImagePicker
import wit.pstefans.rallyreport2.main.MainApp
import wit.pstefans.rallyreport2.models.PostModel

class PostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostBinding
    var post = PostModel()
    lateinit var app: MainApp
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var edit = false

        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp
        i("Post Activity started...")

        if (intent.hasExtra("post-edit")) {
            edit = true
            post = intent.extras?.getParcelable("post-edit")!!
            binding.postTitle.setText(post.title)
            binding.description.setText(post.description)
            binding.btnAdd.setText(R.string.save_post)
            Picasso.get()
                .load(post.image)
                .into(binding.postImage)

            binding.chooseImage.setText(R.string.change_post_image)
        }

        binding.btnAdd.setOnClickListener() {
            post.title = binding.postTitle.text.toString()
            post.description = binding.description.text.toString()
            if (post.title.isEmpty()) {
                Snackbar.make(it, R.string.enter_post_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.posts.update(post.copy())
                } else {
                    app.posts.create(post.copy())
                }
            }
            i("add Button Pressed: $post")
            setResult(RESULT_OK)
            finish()
        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }

        registerImagePickerCallback()
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

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            post.image = result.data!!.data!!
                            Picasso.get()
                                .load(post.image)
                                .into(binding.postImage)

                            binding.chooseImage.setText(R.string.change_post_image)
                        }
                    }
                    RESULT_CANCELED -> {}
                    else -> {}
                }
            }
    }
}