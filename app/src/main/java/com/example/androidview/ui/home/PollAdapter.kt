package com.example.androidview.ui.home
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidview.R
import com.example.androidview.database.PollEntity

interface OnPollClickListener {
    fun onPollClick(poll: PollEntity)
}
class PollAdapter(private var polls: List<PollEntity>, private val listener: OnPollClickListener) : RecyclerView.Adapter<PollAdapter.PollViewHolder>() {

    fun updatePolls(newPolls: List<PollEntity>) {
        polls = newPolls
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PollViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.poll_item, parent, false)
        return PollViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: PollViewHolder, position: Int) {
        val poll = polls[position]
        holder.bind(poll)
    }

    override fun getItemCount() = polls.size

    inner class PollViewHolder(itemView: View, private val listener: OnPollClickListener) : RecyclerView.ViewHolder(itemView) {
        fun bind(poll: PollEntity) {
            itemView.findViewById<TextView>(R.id.pollTitle).text = poll.title
            itemView.findViewById<TextView>(R.id.pollVotes).text = "${poll.votesCount} glasova"
            //itemView.findViewById<TextView>(R.id.pollVotes).text = "0 votes"
            itemView.setOnClickListener{ listener.onPollClick(poll) }
        }
    }
}
