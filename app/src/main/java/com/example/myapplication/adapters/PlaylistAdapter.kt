package com.example.myapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.db.playlist.PlaylistWithTracks
import com.example.myapplication.databinding.ItemPlaylistBinding

class PlaylistAdapter(
    private val onItemClicked: (PlaylistWithTracks) -> Unit,
    private val onItemRemoved: (PlaylistWithTracks) -> Unit
) :
    ListAdapter<PlaylistWithTracks, PlaylistAdapter.PlaylistViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding = ItemPlaylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistViewHolder(binding, onItemClicked, onItemRemoved)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class PlaylistViewHolder(
        private val binding: ItemPlaylistBinding,
        private val onItemClicked: (PlaylistWithTracks) -> Unit,
        private val onItemRemoved: (PlaylistWithTracks) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PlaylistWithTracks) {
            binding.apply {
                playlistName.text = item.playlist.name
                root.setOnClickListener {
                    onItemClicked(item)
                }
//                removeButton.setOnClickListener{
//                    onItemRemoved(item)
//                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<PlaylistWithTracks>() {
        override fun areItemsTheSame(oldItem: PlaylistWithTracks, newItem: PlaylistWithTracks): Boolean {
            return oldItem.playlist.id == newItem.playlist.id
        }

        override fun areContentsTheSame(oldItem: PlaylistWithTracks, newItem: PlaylistWithTracks): Boolean {
            return oldItem == newItem
        }
    }
}