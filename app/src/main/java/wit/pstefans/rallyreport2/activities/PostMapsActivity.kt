package wit.pstefans.rallyreport2.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import wit.pstefans.rallyreport2.R
import wit.pstefans.rallyreport2.databinding.ActivityPostMapsBinding
import wit.pstefans.rallyreport2.databinding.ContentPostMapsBinding
import wit.pstefans.rallyreport2.main.MainApp

class PostMapsActivity : AppCompatActivity(), GoogleMap.OnMarkerClickListener {

    private lateinit var binding: ActivityPostMapsBinding
    private lateinit var contentBinding: ContentPostMapsBinding
    private lateinit var map: GoogleMap
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        contentBinding = ContentPostMapsBinding.bind(binding.root)
        contentBinding.mapView.onCreate(savedInstanceState)
        app = application as MainApp
        contentBinding.mapView.getMapAsync {
            map = it
            configureMap()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val drawer = menu.findItem(R.id.item_drawer)
        drawer.isVisible = false
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_home -> {
                val launcherIntent = Intent(this, PostListActivity::class.java)
                startActivity(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun configureMap() {
        map.uiSettings.isZoomControlsEnabled = true
        app.posts.findAll().forEach {
            val loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions().title(it.title).position(loc).snippet(it.description)
            map.addMarker(options)?.tag = it.mapID
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
            map.setOnMarkerClickListener(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        contentBinding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        contentBinding.mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        contentBinding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        contentBinding.mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        contentBinding.mapView.onSaveInstanceState(outState)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        contentBinding.currentTitle.text = marker.title
        contentBinding.currentDescription.text = marker.snippet

        return false
    }
}