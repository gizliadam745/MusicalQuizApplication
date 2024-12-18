package com.example.musicalquiz.ui.quiz

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.musicalquiz.data.models.Track
import com.example.musicalquiz.databinding.QuizChoiceItemBinding

class QuizChoiceAdapter(private val trackList:List<Track>, private val onItemClick: (Track) -> Unit) :
    ListAdapter<Track, QuizChoiceAdapter.QuizChoiceViewHolder>(QuizChoiceDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizChoiceViewHolder {
        val binding = QuizChoiceItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return QuizChoiceViewHolder(binding)
    }
    override fun onBindViewHolder(holder: QuizChoiceViewHolder, position: Int) {
        val track = getItem(position)
        holder.bind(track)
    }
    override fun getItemCount(): Int {
        return trackList.size
    }
    override fun getItem(position: Int): Track {
        return trackList[position]
    }
    inner class QuizChoiceViewHolder(private val binding:QuizChoiceItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(track: Track){
            binding.quizChoiceName.text = track.title
            itemView.setOnClickListener {
                onItemClick(track)
            }
        }
    }
}

class QuizChoiceDiffCallback: DiffUtil.ItemCallback<Track>(){
    override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
        return oldItem == newItem
    }
}