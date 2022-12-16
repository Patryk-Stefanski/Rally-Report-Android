package wit.pstefans.rallyreport2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import wit.pstefans.rallyreport2.databinding.CardEventBinding
import wit.pstefans.rallyreport2.models.event.EventModel

interface EventListener {
    fun onEventClick(event: EventModel, adapterPosition: Int)
}

class EventAdapter constructor(
    private var events: List<EventModel>,
    private val listener: EventListener
) :
    RecyclerView.Adapter<EventAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardEventBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val event = events[holder.adapterPosition]
        holder.bind(event, listener)
    }

    override fun getItemCount(): Int = events.size

    class MainHolder(private val binding: CardEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: EventModel, listener: EventListener) {
            binding.eventName.text = event.name
            binding.startDate.text = event.startDate
            binding.endDate.text = event.endDate
            binding.compListView.text = event.competitors.toString()
            binding.root.setOnClickListener { listener.onEventClick(event , adapterPosition) }
        }
    }


}
