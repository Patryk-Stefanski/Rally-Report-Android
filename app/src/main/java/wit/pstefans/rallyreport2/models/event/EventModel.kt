package wit.pstefans.rallyreport2.models.event

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import wit.pstefans.rallyreport2.models.competitor.CompetitorModel
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList


@Parcelize
data class EventModel (
    var uid: String = UUID.randomUUID().toString(),
    var ownerUID: String = "",
    var name: String = "",
    var startDate: String = "",
    var endDate: String = "",
    var competitors: String = ""
) : Parcelable