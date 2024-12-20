package com.example.myapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.db.quiz.QuizWithQuestions
import com.example.myapplication.databinding.ItemQuizBinding

class QuizAdapter(
    private val onItemClicked: (QuizWithQuestions) -> Unit,
    private val onItemRemoved: (QuizWithQuestions) -> Unit
) :
    ListAdapter<QuizWithQuestions, QuizAdapter.QuizViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val binding = ItemQuizBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuizViewHolder(binding, onItemClicked, onItemRemoved)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class QuizViewHolder(
        private val binding: ItemQuizBinding,
        private val onItemClicked: (QuizWithQuestions) -> Unit,
        private val onItemRemoved: (QuizWithQuestions) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: QuizWithQuestions) {
            binding.apply {
                quizName.text = item.quiz.name
                root.setOnClickListener {
                    onItemClicked(item)
                }
                removeButton.setOnClickListener{
                    onItemRemoved(item)
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<QuizWithQuestions>() {
        override fun areItemsTheSame(oldItem: QuizWithQuestions, newItem: QuizWithQuestions): Boolean {
            return oldItem.quiz.id == newItem.quiz.id
        }

        override fun areContentsTheSame(oldItem: QuizWithQuestions, newItem: QuizWithQuestions): Boolean {
            return oldItem == newItem
        }
    }
}