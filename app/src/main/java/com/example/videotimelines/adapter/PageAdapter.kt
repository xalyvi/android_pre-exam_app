package com.example.videotimelines.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.videotimelines.keys.DeveloperKey
import com.example.videotimelines.R
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubeThumbnailLoader
import com.google.android.youtube.player.YouTubeThumbnailView
import kotlinx.android.synthetic.main.video_list_item.view.*
import java.util.ArrayList
import java.util.HashMap

class PageAdapter(private val entries: List<VideoEntry>, private val onVideosClickListener: OnVideosClickListener) :
    RecyclerView.Adapter<PageAdapter.ViewHolder>() {

    private val entryViews: List<View>


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.video_list_item, viewGroup, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = entries[position]
        holder.bind(item, position, thumbnailListener, onVideosClickListener)
    }

    override fun getItemCount() = entries.size

    fun getItem(position: Int): VideoEntry {
        return entries[position]
    }



    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: VideoEntry, position: Int, thumbnailListener: ThumbnailListener, onVideosClickListener: OnVideosClickListener) {
            view.apply {
                thumbnail.tag = item.getVideoId()
                thumbnail.initialize(DeveloperKey.DEVELOPER_KEY, thumbnailListener)
                label.setText(item.getText())
                label.visibility = View.VISIBLE
            }
            setOnItemClickListener(position, onVideosClickListener)
        }
        private fun setOnItemClickListener(position: Int, onVideosClickListener: OnVideosClickListener) {
            view.setOnClickListener { onVideosClickListener.onItemClick(position) }
        }
    }


    private val thumbnailViewToLoaderMap: MutableMap<YouTubeThumbnailView, YouTubeThumbnailLoader>
//    private val inflater: LayoutInflater
    private val thumbnailListener: ThumbnailListener
//
//    private var labelsVisible: Boolean = false
//
    init {

        entryViews = ArrayList()
        thumbnailViewToLoaderMap = HashMap()
//        inflater = LayoutInflater.from(context)
        thumbnailListener = ThumbnailListener()
//
//        labelsVisible = true
    }
//
//    fun releaseLoaders() {
//        for (loader in thumbnailViewToLoaderMap.values) {
//            loader.release()
//        }
//    }
//
//    fun setLabelVisibility(visible: Boolean) {
//        labelsVisible = visible
//        for (view in entryViews) {
//            view.findViewById<View>(R.id.text).setVisibility(if (visible) View.VISIBLE else View.GONE)
//        }
//    }

//    override fun getItem(position: Int): VideoEntry {
//        return entries[position]
//    }

//    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
//        var view: View? = convertView
//        val entry = entries[position]
//
//        if (view == null) {
//            view = inflater.inflate(R.layout.video_list_item, parent, false)
//            val thumbnail = view!!.findViewById(R.id.thumbnail) as YouTubeThumbnailView
//            thumbnail.tag = entry.getVideoId()
//            thumbnail.initialize(DeveloperKey.DEVELOPER_KEY, thumbnailListener)
//        } else {
//            val thumbnail = view.findViewById(R.id.thumbnail) as YouTubeThumbnailView
//            val loader = thumbnailViewToLoaderMap[thumbnail]
//            if (loader == null) {
//                thumbnail.tag = entry.getVideoId()
//            } else {
//                thumbnail.setImageResource(R.drawable.loading_thumbnail)
//                loader.setVideo(entry.getVideoId())
//            }
//        }
//        val label = view.findViewById(R.id.text) as TextView
//        label.setText(entry.getText())
//        label.visibility = if (labelsVisible) View.VISIBLE else View.GONE
//        return view
//    }

    inner class ThumbnailListener : YouTubeThumbnailView.OnInitializedListener,
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

interface OnVideosClickListener {
    fun onItemClick(position: Int)
}


//class ComicsAdapter(private val creators: List<Comics>, private val clickListener: OnComicsClickListener,
//                    private val likedListener: OnLikedClickListener)
//    : RecyclerView.Adapter<ComicsAdapter.ViewHolder>() {
//
//    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) =
//        ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.comics_item, viewGroup, false))
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = creators[position]
//        holder.bind(item, position, clickListener, likedListener)
//    }
//
//    override fun getItemCount() = creators.size
//
//    fun getItem(position: Int) = creators[position]
//
//    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
//
//        fun bind(item: Comics, position: Int, clickListener: OnComicsClickListener, likedListener: OnLikedClickListener) {
//            view.apply {
//                comics_title.text = item.title
//                comics_price.text = "Цена комикса: ${item.price}"
//                comics_img.loadImg(item.imageUrl)
//                like_btn.setOnClickListener { likedListener.onLikedClick(position) }
//            }
//            setOnItemClickListener(position, clickListener)
//        }
//
//        private fun setOnItemClickListener(position: Int, listener: OnComicsClickListener) {
//            view.setOnClickListener { listener.onItemClick(position) }
//        }
//    }
//
//}