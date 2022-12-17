package wit.pstefans.rallyreport2.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import timber.log.Timber.i
import wit.pstefans.rallyreport2.R
import wit.pstefans.rallyreport2.databinding.ActivityPostBinding
import wit.pstefans.rallyreport2.helpers.showImagePicker
import wit.pstefans.rallyreport2.main.MainApp
import wit.pstefans.rallyreport2.models.post.Location
import wit.pstefans.rallyreport2.models.post.PostModel

class PostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostBinding
    private var post = PostModel()
    lateinit var app: MainApp
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private var edit = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            binding.deletePostBtn.isVisible = true
            Picasso.get()
                .load(post.image)
                .into(binding.postImage)
            if (post.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_post_image)
            }
        }

        binding.btnAdd.setOnClickListener {
            post.title = binding.postTitle.text.toString()
            post.description = binding.description.text.toString()
            if (post.title.isEmpty()) {
                Snackbar.make(it, R.string.enter_post_title, Snackbar.LENGTH_LONG).show()
            }
            if (post.description == "") {
                Snackbar.make(it, R.string.enter_post_description, Snackbar.LENGTH_LONG).show()
            }
            else {
                if (edit) {
                    app.posts.update(post.copy())
                }
                else {
                    app.posts.create(post.copy())
                }
            }
            setResult(RESULT_OK)
            finish()
        }

        binding.deletePostBtn.setOnClickListener {
            app.posts.delete(post.copy())
            setResult(99)
            finish()
        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }

        binding.postLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            if (post.zoom != 0f) {
                location.lat =  post.lat
                location.lng = post.lng
                location.zoom = post.zoom
            }
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }

        registerImagePickerCallback()
        registerMapCallback()


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_post, menu)
        if (edit) menu.getItem(0).isVisible = true
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

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            post.lat = location.lat
                            post.lng = location.lng
                            post.zoom = location.zoom
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}