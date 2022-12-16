package wit.pstefans.rallyreport2.models.competitor
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import  java.util.*

@Parcelize
data class CompetitorModel (
    var uid: String = UUID.randomUUID().toString(),
    var ownerUID: String = "",
    var driverFirstName: String = "",
    var driverLastName: String = "",
    var navFirstName : String = "",
    var navLastName: String = "",
    var compNo: Int = 0,
    var carDetails: String = ""
) : Parcelable