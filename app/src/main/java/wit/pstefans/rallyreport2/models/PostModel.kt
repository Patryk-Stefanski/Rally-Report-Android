package wit.pstefans.rallyreport2.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostModel(var id: Long = 0,
                     var title:String = "" ,
                     var description: String = "") : Parcelable
