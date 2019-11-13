package com.example.videotimelines.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.videotimelines.Keys.DeveloperKey
import com.example.videotimelines.R
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubeThumbnailLoader
import com.google.android.youtube.player.YouTubeThumbnailView
import java.util.ArrayList
import java.util.HashMap

class PageAdapter(context: Context, private val entries: List<VideoEntry>) : BaseAdapter() {
    private val entryViews: List<View>
    private val thumbnailViewToLoaderMap: MutableMap<YouTubeThumbnailView, YouTubeThumbnailLoader>
    private val inflater: LayoutInflater
    private val thumbnailListener: ThumbnailListener

    private var labelsVisible: Boolean = false

    init {

        entryViews = ArrayList()
        thumbnailViewToLoaderMap = HashMap()
        inflater = LayoutInflater.from(context)
        thumbnailListener = ThumbnailListener()

        labelsVisible = true
    }

    fun releaseLoaders() {
        for (loader in thumbnailViewToLoaderMap.values) {
            loader.release()
        }
    }

    fun setLabelVisibility(visible: Boolean) {
        labelsVisible = visible
        for (view in entryViews) {
            view.findViewById(R.id.text).setVisibility(if (visible) View.VISIBLE else View.GONE)
        }
    }

    override fun getCount(): Int {
        return entries.size
    }

    override fun getItem(position: Int): VideoEntry {
        return entries[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        var view: View? = convertView
        val entry = entries[position]

        if (view == null) {
            view = inflater.inflate(R.layout.video_list_item, parent, false)
            val thumbnail = view!!.findViewById(R.id.thumbnail) as YouTubeThumbnailView
            thumbnail.tag = entry.getVideoId()
            thumbnail.initialize(DeveloperKey.DEVELOPER_KEY, thumbnailListener)
        } else {
            val thumbnail = view.findViewById(R.id.thumbnail) as YouTubeThumbnailView
            val loader = thumbnailViewToLoaderMap[thumbnail]
            if (loader == null) {
                thumbnail.tag = entry.getVideoId()
            } else {
                thumbnail.setImageResource(R.drawable.loading_thumbnail)
                loader.setVideo(entry.getVideoId())
            }
        }
        val label = view.findViewById(R.id.text) as TextView
        label.setText(entry.getText())
        label.visibility = if (labelsVisible) View.VISIBLE else View.GONE
        return view
    }

    private inner class ThumbnailListener : YouTubeThumbnailView.OnInitializedListener,
        YouTubeThumbnailLoader.OnThumbnailLoadedListener {

        override fun onInitializationSuccess(
            view: YouTubeThumbnailView, loader: YouTubeThumbnailLoader
        ) {
            loader.setOnThumbnailLoadedListener(this)
            thumbnailViewToLoaderMap[view] = loader
            view.setImageResource(R.drawable.loading_thumbnail)
            val videoId = view.tag as String
            loader.setVideo(videoId)
        }

        override fun onInitializationFailure(
            view: YouTubeThumbnailView, loader: YouTubeInitializationResult
        ) {
            view.setImageResource(R.drawable.no_thumbnail)
        }

        override fun onThumbnailLoaded(view: YouTubeThumbnailView, videoId: String) {}

        override fun onThumbnailError(
            view: YouTubeThumbnailView,
            errorReason: YouTubeThumbnailLoader.ErrorReason
        ) {
            view.setImageResource(R.drawable.no_thumbnail)
        }
    }

}