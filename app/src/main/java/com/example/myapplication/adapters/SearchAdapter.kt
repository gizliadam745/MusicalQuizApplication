package com.example.myapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ItemSearchBinding
import com.example.myapplication.data.models.Album
import com.example.myapplication.data.models.Track

class SearchAdapter(
    private val onItemClicked: (Any) -> Unit,
    private val onItemLongClicked: (Any) -> Unit
) :
    ListAdapter<Any, SearchAdapter.SearchViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding, onItemClicked, onItemLongClicked)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class SearchViewHolder(
        private val binding: ItemSearchBinding,
        private val onItemClicked: (Any) -> Unit,
        private val onItemLongClicked: (Any) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Any) {
            binding.apply {
                when (item) {
                    is Track -> {
                        titleTextView.text = item.title
                        artistTextView.text = item.artist.name

                        Glide.with(itemView)
                            .load(item.album.cover)
                            .into(coverImageView)
                    }
                    is Album -> {
                        titleTextView.text = item.title
                        // Show the album's artist name
                        artistTextView.text = item.artist.name
                        Glide.with(itemView)
                            .load(item.cover_medium)
                            .into(coverImageView)
                    }
                }
                root.setOnClickListener {
                    onItemClicked(item)
                }
                root.setOnLongClickListener {
                    onItemLongClicked(item)
                    true // consume long click event
                }

            }
        }
    }


    class DiffCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when {
                oldItem is Track && newItem is Track -> oldItem.id == newItem.id
                oldItem is Album && newItem is Album -> oldItem.id == newItem.id
                else -> oldItem == newItem
            }
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when {
                oldItem is Track && newItem is Track -> areTracksEqual(oldItem, newItem)
                oldItem is Album && newItem is Album -> areAlbumsEqual(oldItem, newItem)
                else -> false
            }
        }
        private fun areTracksEqual(oldTrack: Track, newTrack: Track): Boolean {
            return oldTrack.album == newTrack.album &&
                    oldTrack.artist == newTrack.artist &&
                    oldTrack.available_countries == newTrack.available_countries &&
                    oldTrack.bpm == newTrack.bpm &&
                    oldTrack.contributors == newTrack.contributors &&
                    oldTrack.disk_number == newTrack.disk_number &&
                    oldTrack.duration == newTrack.duration &&
                    oldTrack.explicit_content_cover == newTrack.explicit_content_cover &&
                    oldTrack.explicit_content_lyrics == newTrack.explicit_content_lyrics &&
                    oldTrack.explicit_lyrics == newTrack.explicit_lyrics &&
                    oldTrack.gain == newTrack.gain &&
                    oldTrack.id == newTrack.id &&
                    oldTrack.isrc == newTrack.isrc &&
                    oldTrack.link == newTrack.link &&
                    oldTrack.md5_image == newTrack.md5_image &&
                    oldTrack.preview == newTrack.preview &&
                    oldTrack.rank == newTrack.rank &&
                    oldTrack.readable == newTrack.readable &&
                    oldTrack.release_date == newTrack.release_date &&
                    oldTrack.share == newTrack.share &&
                    oldTrack.title == newTrack.title &&
                    oldTrack.title_short == newTrack.title_short &&
                    oldTrack.title_version == newTrack.title_version &&
                    oldTrack.track_position == newTrack.track_position &&
                    oldTrack.track_token == newTrack.track_token &&
                    oldTrack.type == newTrack.type
        }

        private fun areAlbumsEqual(oldAlbum: Album, newAlbum: Album): Boolean {
            return oldAlbum.artist == newAlbum.artist &&
                    oldAlbum.available == newAlbum.available &&
                    oldAlbum.contributors == newAlbum.contributors &&
                    oldAlbum.cover == newAlbum.cover &&
                    oldAlbum.cover_big == newAlbum.cover_big &&
                    oldAlbum.cover_medium == newAlbum.cover_medium &&
                    oldAlbum.cover_small == newAlbum.cover_small &&
                    oldAlbum.cover_xl == newAlbum.cover_xl &&
                    oldAlbum.duration == newAlbum.duration &&
                    oldAlbum.explicit_content_cover == newAlbum.explicit_content_cover &&
                    oldAlbum.explicit_content_lyrics == newAlbum.explicit_content_lyrics &&
                    oldAlbum.explicit_lyrics == newAlbum.explicit_lyrics &&
                    oldAlbum.fans == newAlbum.fans &&
                    oldAlbum.genre_id == newAlbum.genre_id &&
                    oldAlbum.genres == newAlbum.genres &&
                    oldAlbum.id == newAlbum.id &&
                    oldAlbum.label == newAlbum.label &&
                    oldAlbum.link == newAlbum.link &&
                    oldAlbum.md5_image == newAlbum.md5_image &&
                    oldAlbum.nb_tracks == newAlbum.nb_tracks &&
                    oldAlbum.record_type == newAlbum.record_type &&
                    oldAlbum.release_date == newAlbum.release_date &&
                    oldAlbum.share == newAlbum.share &&
                    oldAlbum.title == newAlbum.title &&
                    oldAlbum.tracklist == newAlbum.tracklist &&
                    oldAlbum.tracks == newAlbum.tracks &&
                    oldAlbum.type == newAlbum.type &&
                    oldAlbum.upc == newAlbum.upc
        }
    }
}