package wit.pstefans.rallyreport2.models.post

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class PostModel(
    var uid: String = UUID.randomUUID().toString(),
    var ownerUID: String = "",
    var mapID: String = Random().nextLong().toString(),
    var title:String = "",
    var description: String = "",
    var image: Uri = Uri.EMPTY,
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f) : Parcelable


@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable