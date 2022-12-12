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
import wit.pstefans.rallyreport2.adapters.CompAdapter
import wit.pstefans.rallyreport2.adapters.CompListener
import wit.pstefans.rallyreport2.adapters.PostAdapter
import wit.pstefans.rallyreport2.adapters.PostListener
import wit.pstefans.rallyreport2.databinding.ActivityCompetitorListBinding
import wit.pstefans.rallyreport2.databinding.ActivityPostListBinding
import wit.pstefans.rallyreport2.main.MainApp
import wit.pstefans.rallyreport2.models.competitor.CompetitorModel
import wit.pstefans.rallyreport2.models.post.PostModel

class CompetitorsListActivity : AppCompatActivity(), CompListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityCompetitorListBinding
    private var position: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompetitorListBinding.inflate(layoutInflater)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        setContentView(binding.root)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = CompAdapter(app.competitors.findAll(), this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, CompetitorActivity::class.java)
                getResult.launch(launcherIntent)
            }
            R.id.item_map -> {
                val launcherIntent = Intent(this, PostMapsActivity::class.java)
                mapIntentLauncher.launch(launcherIntent)
            }
            R.id.item_logout -> {
                FirebaseAuth.getInstance().signOut()
                val launcherIntent = Intent(this, LogInActivity::class.java)
                getResult.launch(launcherIntent)
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
                notifyItemRangeChanged(0,app.competitors.findAll().size)
            }
        }

    override fun onCompClick(comp: CompetitorModel, adapterPosition: Int) {
        if ( comp.ownerUID ==  FirebaseAuth.getInstance().currentUser!!.uid) {
            val launcherIntent = Intent(this, CompetitorActivity::class.java)
            launcherIntent.putExtra("comp-edit", comp)
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
                notifyItemRangeChanged(0,app.competitors.findAll().size)
            }
            else // Deleting
                if (it.resultCode == 99)
                    binding.recyclerView.adapter = CompAdapter(app.competitors.findAll(), this)
            (binding.recyclerView.adapter)?.notifyItemRemoved(position)
        }


    private val mapIntentLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        )    { }
}