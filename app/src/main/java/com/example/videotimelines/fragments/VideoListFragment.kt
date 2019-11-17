package com.example.videotimelines.fragments


import android.os.Bundle
import android.view.*
import android.widget.ListView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.videotimelines.R
import com.example.videotimelines.adapter.PageAdapter
import com.example.videotimelines.adapter.VideoEntry
import com.example.videotimelines.databinding.FragmentVideoListBinding
import com.google.android.youtube.player.YouTubeApiServiceUtil
import com.google.android.youtube.player.YouTubeInitializationResult

class VideoListFragment : Fragment() {

    private val RECOVERY_DIALOG_REQUEST = 1

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: PageAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager


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

        viewAdapter = PageAdapter(VIDEO_LIST)

        binding.videoFragment = this

        checkYouTubeApi()

        viewManager = LinearLayoutManager(this.activity)
        recyclerView = binding.Videos.apply {
            setHasFixedSize(true)

            layoutManager = viewManager

            adapter = viewAdapter
        }

        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE)
//        setListAdapter(adapter)
//    }

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

//    override fun onDestroyView() {
//        super.onDestroyView()
//
//        viewAdapter.releaseLoaders()
//    }
//
//    fun setLabelVisibility(visible: Boolean) {
//        viewAdapter.setLabelVisibility(visible)
//    }


    private fun checkYouTubeApi() {
        val errorReason = YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(this.activity)
        if (errorReason.isUserRecoverableError) {
            errorReason.getErrorDialog(this.activity, RECOVERY_DIALOG_REQUEST).show()
        } else if (errorReason != YouTubeInitializationResult.SUCCESS) {
            val errorMessage =
                String.format(getString(R.string.error_player), errorReason.toString())
            Toast.makeText(this.context, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

}
