package com.example.videotimelines.fragments


import android.os.Bundle
import android.view.*
import android.widget.ListView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.ListFragment
import com.example.videotimelines.R
import com.example.videotimelines.adapter.PageAdapter
import com.example.videotimelines.adapter.VideoEntry
import com.example.videotimelines.databinding.FragmentVideoListBinding

class VideoListFragment : ListFragment() {

    private var VIDEO_LIST: List<VideoEntry> =  listOf(
        VideoEntry("YouTube Collection", "Y_UmWdcTrrc"),
        VideoEntry("GMail Tap", "1KhZKNZO8mQ"),
        VideoEntry("Chrome Multitask", "UiLSiqyDf4Y"),
        VideoEntry("Google Fiber", "re0VRK6ouwI"),
        VideoEntry("Autocompleter", "blB_X38YSxQ"),
        VideoEntry("GMail Motion", "Bu927_ul_X0"),
        VideoEntry("Translate for Animals", "3I24bSteJpw")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentVideoListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_video_list, container, false
        )
        return binding.root
    }

    private lateinit var adapter: PageAdapter
    private lateinit var videoBox: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = PageAdapter(activity!!.applicationContext, VIDEO_LIST)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        videoBox = activity!!.findViewById(R.id.video_box)
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE)
        setListAdapter(adapter)
    }

//    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
//        val videoId = VIDEO_LIST[position].getVideoId()
//
//        val videoFragment =
//            fragmentManager.findFragmentById(R.id.video_fragment_container) as VideoFragment
//        videoFragment.setVideoId(videoId)
//
//        // The videoBox is INVISIBLE if no video was previously selected, so we need to show it now.
//        if (videoBox!!.visibility != View.VISIBLE) {
//            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
//                // Initially translate off the screen so that it can be animated in from below.
//                videoBox!!.translationY = videoBox!!.height.toFloat()
//            }
//            videoBox!!.visibility = View.VISIBLE
//        }
//
//        // If the fragment is off the screen, we animate it in.
//        if (videoBox!!.translationY > 0) {
//            videoBox!!.animate().translationY(0f).duration = ANIMATION_DURATION_MILLIS.toLong()
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()

        adapter.releaseLoaders()
    }

    fun setLabelVisibility(visible: Boolean) {
        adapter.setLabelVisibility(visible)
    }


}
