package com.example.myapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.data.db.playlist.PlaylistTrack
import com.example.myapplication.data.models.Track
import com.example.myapplication.databinding.ItemHorizontalTrackBinding


class HorizontalTrackAdapter(private val onTrackClicked: (Track) -> Unit, private val onItemRemoved: (PlaylistTrack) -> Unit ) :
    ListAdapter<Pair<PlaylistTrack, Track?>, HorizontalTrackAdapter.HorizontalTrackViewHolder>(
        DiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalTrackViewHolder {
        val binding = ItemHorizontalTrackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HorizontalTrackViewHolder(binding, onTrackClicked, onItemRemoved)
    }

    override fun onBindViewHolder(holder: HorizontalTrackViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item.first, item.second)
    }

    inner class HorizontalTrackViewHolder(private val binding: ItemHorizontalTrackBinding, private val onTrackClicked: (Track) -> Unit, private val onItemRemoved: (PlaylistTrack) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PlaylistTrack, track: Track?) {
            binding.apply {
                if(track != null) {
                    trackTitle.text = track.title
                    artistName.text = track.artist.name
                    dateAdded.text = "Added: ${item.formatDate()}"
                    Glide.with(itemView)
                        .load(track.album.cover)
                        .into(trackCover)
                }
                else{
                    trackTitle.text = item.trackId.toString()
                    artistName.text = ""
                    Glide.with(itemView)
                        .load("https://e-cdn-images.dzcdn.net/images/cover/93b50f6ab0f44d53f8aee5ad207271ee/56x56-000000-80-0-0.jpg")
                        .into(trackCover)
                }
                root.setOnClickListener {
                    track?.let { onTrackClicked(it) }
                }
                removeButton.setOnClickListener {
                    onItemRemoved(item)
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Pair<PlaylistTrack, Track?>>() {
        override fun areItemsTheSame(oldItem: Pair<PlaylistTrack, Track?>, newItem: Pair<PlaylistTrack, Track?>): Boolean {
            return oldItem.first.id == newItem.first.id
        }

        override fun areContentsTheSame(oldItem: Pair<PlaylistTrack, Track?>, newItem: Pair<PlaylistTrack, Track?>): Boolean {
            return oldItem == newItem
        }
    }
}