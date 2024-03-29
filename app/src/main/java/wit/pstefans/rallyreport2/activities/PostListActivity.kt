package wit.pstefans.rallyreport2.activities

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber.i
import wit.pstefans.rallyreport2.R
import wit.pstefans.rallyreport2.adapters.PostAdapter
import wit.pstefans.rallyreport2.adapters.PostListener
import wit.pstefans.rallyreport2.databinding.ActivityPostListBinding
import wit.pstefans.rallyreport2.main.MainApp
import wit.pstefans.rallyreport2.models.post.PostModel
import java.util.*

class PostListActivity : AppCompatActivity(), PostListener,
    NavigationView.OnNavigationItemSelectedListener {

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

        val toggle =
            ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener(this)


        val header: View = binding.navView.getHeaderView(0)
        val userEmail: TextView = header.findViewById(R.id.user_email_textView)
        if (FirebaseAuth.getInstance().currentUser != null) {
            userEmail.text = FirebaseAuth.getInstance().currentUser!!.email
        }

        var postlist = app.posts.findAll()

        binding.checkbox.setOnClickListener {
            postlist = app.posts.findAll()
            if (binding.checkbox.isChecked) {
                postlist.sortBy { it.description }
                i("checked")
            } else {
                i("unchecked")
                postlist.sortBy { it.title }
            }
            binding.recyclerView.adapter = PostAdapter(postlist, this)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = PostAdapter(postlist, this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val home = menu.findItem(R.id.item_home)
        home.isVisible = false
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_drawer -> {
                binding.drawerLayout.openDrawer(GravityCompat.END)
            }
            R.id.item_home -> {
                val launcherIntent = Intent(this, PostListActivity::class.java)
                startActivity(launcherIntent)
            }
            R.id.item_nightMode -> {
                if (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_NO) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                binding.recyclerView.adapter = PostAdapter(app.posts.findAll(), this)
                (binding.recyclerView.adapter)?.notifyItemRangeChanged(0, app.posts.findAll().size)
            }
        }

    override fun onPostClick(post: PostModel, adapterPosition: Int) {
        if (post.ownerUID == FirebaseAuth.getInstance().currentUser!!.uid) {
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
                binding.recyclerView.adapter = PostAdapter(app.posts.findAll(), this)
                (binding.recyclerView.adapter)?.notifyItemRangeChanged(0, app.posts.findAll().size)
            } else // Deleting
                if (it.resultCode == 99)
                    binding.recyclerView.adapter = PostAdapter(app.posts.findAll(), this)
            (binding.recyclerView.adapter)?.notifyItemRemoved(position)
        }


    private val mapIntentLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, PostActivity::class.java)
                getResult.launch(launcherIntent)
                binding.drawerLayout.closeDrawer(GravityCompat.END)
            }
            R.id.item_map -> {
                val launcherIntent = Intent(this, PostMapsActivity::class.java)
                mapIntentLauncher.launch(launcherIntent)
                binding.drawerLayout.closeDrawer(GravityCompat.END)
            }
            R.id.item_logout -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LogInActivity::class.java)
                startActivity(intent)
                binding.drawerLayout.closeDrawer(GravityCompat.END)
            }
            R.id.item_user_management -> {
                val intent = Intent(this, UserManagement::class.java)
                getResult.launch(intent)
                binding.drawerLayout.closeDrawer(GravityCompat.END)
            }
            R.id.item_competitor -> {
                val intent = Intent(this, CompetitorsListActivity::class.java)
                getResult.launch(intent)
                binding.drawerLayout.closeDrawer(GravityCompat.END)
            }
            R.id.item_add_competitor -> {
                val intent = Intent(this, CompetitorActivity::class.java)
                getResult.launch(intent)
                binding.drawerLayout.closeDrawer(GravityCompat.END)
            }
            R.id.item_event -> {
                val intent = Intent(this, EventListActivity::class.java)
                getResult.launch(intent)
                binding.drawerLayout.closeDrawer(GravityCompat.END)
            }
            R.id.item_add_event -> {
                val intent = Intent(this, EventActivity::class.java)
                getResult.launch(intent)
                binding.drawerLayout.closeDrawer(GravityCompat.END)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}