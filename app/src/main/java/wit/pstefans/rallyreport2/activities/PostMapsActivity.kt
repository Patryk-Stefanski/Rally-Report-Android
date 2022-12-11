package wit.pstefans.rallyreport2.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import wit.pstefans.rallyreport2.databinding.ActivityPostMapsBinding
import wit.pstefans.rallyreport2.databinding.ContentPostMapsBinding
import wit.pstefans.rallyreport2.main.MainApp

class PostMapsActivity : AppCompatActivity(),GoogleMap.OnMarkerClickListener {

    private lateinit var binding: ActivityPostMapsBinding
    private lateinit var contentBinding: ContentPostMapsBinding
    lateinit var map: GoogleMap
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

    private fun configureMap() {
        map.uiSettings.isZoomControlsEnabled = true
        app.posts.findAll().forEach {
            val loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions().title(it.title).position(loc)
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

        return false
    }
}