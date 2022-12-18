package wit.pstefans.rallyreport2.activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber.i
import wit.pstefans.rallyreport2.R
import wit.pstefans.rallyreport2.databinding.ActivityEventBinding
import wit.pstefans.rallyreport2.main.MainApp
import wit.pstefans.rallyreport2.models.event.EventModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


class EventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventBinding
    private var event = EventModel()
    lateinit var app: MainApp

    private var myCalendar: Calendar = Calendar.getInstance()

    private var edit = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val myFormat = "dd.MM.yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)

        binding = ActivityEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        myCalendar.set(Calendar.YEAR, LocalDate.now().year)
        myCalendar.set(Calendar.MONTH, LocalDate.now().monthValue)
        myCalendar.set(Calendar.DAY_OF_MONTH, LocalDate.now().dayOfMonth)



        binding.startDatePickerButton.text = sdf.format(myCalendar.time)
        binding.endDatePickerButton.text = sdf.format(myCalendar.time)


        app = application as MainApp
        i("Event Activity started...")


        if (intent.hasExtra("event-edit")) {
            edit = true
            event = intent.extras?.getParcelable("event-edit")!!
            binding.eventNameInput.setText(event.name)
            binding.startDatePickerButton.text = event.startDate
            binding.endDatePickerButton.text = event.endDate
            binding.addEventBtn.setText(R.string.save_event)
            binding.deleteEventBtn.isVisible = true
        }

        binding.addEventBtn.setOnClickListener {
            event.name = binding.eventNameInput.text.toString()
            event.startDate = binding.startDatePickerButton.text.toString()
            event.endDate = binding.endDatePickerButton.text.toString()
            event.competitors = binding.compListTextView.text.toString()



            if (event.name.isEmpty()) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Error").setMessage(R.string.fill_in_all)
                    .setIcon(R.drawable.ic_baseline_error_outline_24).setNeutralButton(
                        "Ok"
                    ) { dialogInterface, _ -> // dismiss dialog
                        dialogInterface.dismiss()
                    }
                builder.show()
            } else {
                if (edit) {
                    app.events.update(event.copy())
                } else {
                    app.events.create(event.copy())
                }
            }
            setResult(RESULT_OK)
            finish()
        }

        binding.deleteEventBtn.setOnClickListener {
            app.events.delete(event.copy())
            setResult(99)
            finish()
        }

        val startDateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                binding.startDatePickerButton.text = sdf.format(myCalendar.time)
            }

        val endDateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                binding.endDatePickerButton.text = sdf.format(myCalendar.time)
            }


        binding.startDatePickerButton.setOnClickListener {
            DatePickerDialog(
                this,
                startDateSetListener,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }


        binding.endDatePickerButton.setOnClickListener {
            DatePickerDialog(
                this,
                endDateSetListener,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }


        binding.compListTextView.setOnClickListener {

            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            val competitors = app.competitors.findAll()


            val compArray = Array<String?>(competitors.size) { null }
            var i = 0


            competitors.forEach {
                compArray[i] =
                    "Car No :" + it.compNo + "\n" + "Driver Name : " + it.driverFirstName + " " + it.driverLastName + "\n" + "Navigator Name : " + it.navFirstName + " " + it.navLastName + "\n" + i++
            }

            val compList: ArrayList<Int> = ArrayList()
            val selectedCompetitors = BooleanArray(competitors.size)

            builder.setTitle("Select Competitor")
            builder.setMultiChoiceItems(compArray, selectedCompetitors) { _, a, b ->
                if (b) {
                    compList.add(a)
                    compList.sort()
                } else {
                    compList.remove(a)
                }

            }

            builder.setPositiveButton("OK") { _, _ -> // Initialize string builder
                val stringBuilder = StringBuilder()
                for (j in 0 until compList.size) {
                    // concat array value
                    stringBuilder.append(compArray[compList[j]])
                    // check condition
                    if (j != compList.size - 1) {
                        // When j value  not equal
                        // to lang list size - 1
                        // add comma
                        stringBuilder.append(", ")
                    }
                }
                // set text on textView
                binding.compListTextView.text = stringBuilder.toString()
            }

            builder.setNegativeButton("Cancel") { dialogInterface, _ -> // dismiss dialog
                dialogInterface.dismiss()
            }

            builder.setNeutralButton("Clear All") { _, _ ->
                // use for loop
                for (j in selectedCompetitors.indices) {
                    // remove all selection
                    selectedCompetitors[j] = false
                    // clear language list
                    compList.clear()
                    // clear text view value
                    binding.compListTextView.text = ""
                }
            }
            // show dialog
            builder.show()
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