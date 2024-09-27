package com.example.androidview.ui.home
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidview.R
import com.example.androidview.database.PollEntity

interface OnPollClickListener {
    fun onPollClick(poll: PollEntity){
        if (!poll.hasExpired()) {
            // Navigate to pollFragment
        }
    }
}
class PollAdapter(private var polls: List<PollEntity>, private val listener: OnPollClickListener, private val viewModel: PollViewModel) : RecyclerView.Adapter<PollAdapter.PollViewHolder>() {

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
        if (poll.hasExpired()) {
            holder.titleTextView.setTextColor(Color.GRAY)
            holder.titleTextView.text = "${poll.title} (glasanje isteklo)"
        }

    }

    override fun getItemCount() = polls.size

    inner class PollViewHolder(itemView: View, private val listener: OnPollClickListener) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.pollTitle)
        val votesTextView: TextView = itemView.findViewById(R.id.pollVotes)

        fun bind(poll: PollEntity) {
            //itemView.findViewById<TextView>(R.id.pollTitle).text = poll.title
            titleTextView.text = poll.title
            viewModel.getVoteCount(poll.pollId).observeForever { count ->
                votesTextView.text = "$count glasova"
            }
            //itemView.findViewById<TextView>(R.id.pollVotes).text = "${poll.votesCount} glasova"
            //itemView.findViewById<TextView>(R.id.pollVotes).text = "0 votes"
            itemView.setOnClickListener{ listener.onPollClick(poll) }
        }
    }
}
