package com.gmail.devu.study.room_repository.ui.main

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gmail.devu.study.room_repository.R
import com.gmail.devu.study.room_repository.media.Media
import kotlinx.android.synthetic.main.media_listitem.view.*

class MediaListAdapter : RecyclerView.Adapter<MediaListAdapter.MediaItemViewHolder>() {
    private val TAG = this::class.java.simpleName

    private var medias = emptyList<Media>()

    internal fun setMedias(medias: List<Media>) {
        Log.v(TAG, "setMedias(%d)".format(medias.size))

        this.medias = medias
        notifyDataSetChanged()
    }

    inner class MediaItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mediaIdView: TextView = view.media_id
        val titleView: TextView = view.title
        val artistView: TextView = view.artist
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MediaItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.media_listitem, parent, false)
        return MediaItemViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MediaListAdapter.MediaItemViewHolder,
        position: Int
    ) {
        val media = medias[position]

        holder.mediaIdView.text = media.id.toString()
        holder.titleView.text = media.title
        holder.artistView.text = media.artist
    }

    override fun getItemCount(): Int {
        return medias.size
    }
}
