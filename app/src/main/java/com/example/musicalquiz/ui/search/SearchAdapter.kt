package com.example.musicalquiz.ui.search
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicalquiz.data.models.AlbumItem
import com.example.musicalquiz.data.models.Track
import com.example.musicalquiz.databinding.TrackItemBinding

class SearchAdapter(private val onItemClick: (Track) -> Unit):
    ListAdapter<Any, SearchAdapter.SearchViewHolder>(SearchDiffCallback()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = TrackItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SearchViewHolder(binding)
    }
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = getItem(position)
        if (item is Track){
            holder.bind(item)
        } else if (item is AlbumItem)
            holder.bind(item)

    }
    inner class SearchViewHolder(private val binding: TrackItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(track:Track){
            binding.trackTitle.text = track.title
            binding.artistName.text = track.artistInfo.name
            Glide.with(itemView.context)
                .load(track.albumInfo.coverMedium)
                .into(binding.trackImage)

            itemView.setOnClickListener {
                onItemClick(track)
            }

        }
        fun bind(album:AlbumItem){
            binding.trackTitle.text = album.title
            binding.artistName.text = album.artist.name
            Glide.with(itemView.context)
                .load(album.coverMedium)
                .into(binding.trackImage)
            itemView.setOnClickListener {
                //  onItemClick()
            }
        }
    }
}
class SearchDiffCallback : DiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return if(oldItem is Track && newItem is Track){
            oldItem.id == newItem.id
        }else if (oldItem is AlbumItem && newItem is AlbumItem){
            oldItem.id == newItem.id
        } else{
            false
        }
    }
    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return if(oldItem is Track && newItem is Track){
            oldItem == newItem
        } else if (oldItem is AlbumItem && newItem is AlbumItem) {
            oldItem == newItem
        } else {
            false
        }
    }
}