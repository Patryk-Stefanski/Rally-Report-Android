package wit.pstefans.rallyreport2.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import wit.pstefans.rallyreport2.R
import wit.pstefans.rallyreport2.adapters.PostAdapter
import wit.pstefans.rallyreport2.adapters.PostListener
import wit.pstefans.rallyreport2.databinding.ActivityPostListBinding
import wit.pstefans.rallyreport2.main.MainApp
import wit.pstefans.rallyreport2.models.PostModel

class PostListActivity : AppCompatActivity(), PostListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityPostListBinding
    private var position: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostListBinding.inflate(layoutInflater)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        setContentView(binding.root)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = PostAdapter(app.posts.findAll(), this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, PostActivity::class.java)
                getResult.launch(launcherIntent)
            }
            R.id.item_map -> {
                val launcherIntent = Intent(this, PostMapsActivity::class.java)
                mapIntentLauncher.launch(launcherIntent)
            }
            R.id.item_logout -> {
                FirebaseAuth.getInstance().signOut()
                val launcherIntent = Intent(this, LogInActivity::class.java)
                mapIntentLauncher.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.posts.findAll().size)
            }
        }

    override fun onPostClick(post: PostModel, adapterPosition: Int) {
        if ( post.ownerUID ==  FirebaseAuth.getInstance().currentUser!!.uid) {
            val launcherIntent = Intent(this, PostActivity::class.java)
            launcherIntent.putExtra("post-edit", post)
            position = adapterPosition
            getClickResult.launch(launcherIntent)
        }
    }

    private val getClickResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.posts.findAll().size)
            }
            else // Deleting
                if (it.resultCode == 99)
                    binding.recyclerView.adapter = PostAdapter(app.posts.findAll(), this)
                    (binding.recyclerView.adapter)?.notifyItemRemoved(position)
        }


    private val mapIntentLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        )    { }
}