package wit.pstefans.rallyreport2.activities

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber.i
import wit.pstefans.rallyreport2.R
import wit.pstefans.rallyreport2.databinding.ActivityCompetitorBinding
import wit.pstefans.rallyreport2.main.MainApp
import wit.pstefans.rallyreport2.models.competitor.CompetitorModel

class CompetitorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCompetitorBinding
    private var comp = CompetitorModel()
    lateinit var app: MainApp
    //var location = Location(52.245696, -7.139102, 15f)
    private var edit = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCompetitorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp
        i("Competitor Activity started...")

        if (intent.hasExtra("comp-edit")) {
            edit = true
            comp = intent.extras?.getParcelable("comp-edit")!!
            binding.driverFirstName.setText(comp.driverFirstName)
            binding.driverLastName.setText(comp.driverLastName)
            binding.navFirstName.setText(comp.navFirstName)
            binding.navLastName.setText(comp.navLastName)
            binding.compNo.setText(comp.compNo.toString())
            binding.carDetails.setText(comp.carDetails)
            binding.btnAdd.setText(R.string.save_competitor)
            binding.deleteCompBtn.isVisible = true
        }

        binding.btnAdd.setOnClickListener {
            comp.driverFirstName = binding.driverFirstName.text.toString()
            comp.driverLastName = binding.driverLastName.text.toString()
            comp.navFirstName = binding.navFirstName.text.toString()
            comp.navLastName = binding.navLastName.text.toString()
            comp.compNo = binding.compNo.text.toString()
            comp.carDetails = binding.carDetails.text.toString()

            if ( comp.driverFirstName.isEmpty() || comp.driverLastName.isEmpty() || comp.navFirstName.isEmpty() || comp.navLastName.isEmpty() || comp.compNo == "" ) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Error").setMessage(R.string.fill_in_all)
                    .setIcon(R.drawable.ic_baseline_error_outline_24).setNeutralButton(
                        "Ok"
                    ) { dialogInterface, _ -> // dismiss dialog
                        setResult(RESULT_OK)
                        finish()
                        dialogInterface.dismiss()
                    }
                builder.show()
            }

            else {
                if (edit) {
                    app.competitors.update(comp.copy())
                }
                else {
                    app.competitors.create(comp.copy())
                }
                setResult(RESULT_OK)
                finish()
            }

        }

        binding.deleteCompBtn.setOnClickListener {
            app.competitors.delete(comp.copy())
            setResult(99)
            finish()
        }

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
}