package wit.pstefans.rallyreport2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import wit.pstefans.rallyreport2.databinding.CompetitorCardViewBinding
import wit.pstefans.rallyreport2.models.competitor.CompetitorModel

interface CompListener {
    fun onCompClick(comp: CompetitorModel, adapterPosition: Int)
}

class CompAdapter constructor(
    private var comps: List<CompetitorModel>,
    private val listener: CompListener
) :
    RecyclerView.Adapter<CompAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CompetitorCardViewBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val comp = comps[holder.adapterPosition]
        holder.bind(comp, listener)
    }

    override fun getItemCount(): Int = comps.size

    class MainHolder(private val binding: CompetitorCardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(comp: CompetitorModel, listener: CompListener) {
            binding.driverFirstName.text = comp.driverFirstName
            binding.driverLastName.text = comp.driverLastName
            binding.navFirstName.text = comp.navFirstName
            binding.navLastName.text = comp.navLastName
            binding.compNo.text = comp.compNo.toString()
            binding.carDetails.text = comp.carDetails

            binding.root.setOnClickListener { listener.onCompClick(comp , adapterPosition) }
        }
    }
}
