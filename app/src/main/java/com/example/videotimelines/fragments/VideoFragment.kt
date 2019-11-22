package com.example.videotimelines.fragments


import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import com.example.videotimelines.R
import com.example.videotimelines.TimelineManager.TimelineFileManager
import com.example.videotimelines.TimelineManager.TimelineManager
import com.example.videotimelines.databinding.FragmentVideoBinding
import com.example.videotimelines.keys.DeveloperKey
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayerSupportFragment


class VideoFragment : Fragment() {

    private lateinit var timelineManager: TimelineManager
    private lateinit var binding: FragmentVideoBinding
    private lateinit var youTubePlayer: YouTubePlayer
    private lateinit var timelinesLinearLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_video, container, false)

        val args = VideoFragmentArgs.fromBundle(arguments!!)

         timelineManager = TimelineFileManager(context!!, args.videoId)

        val youTubePlayerSupportFragment: YouTubePlayerSupportFragment = YouTubePlayerSupportFragment.newInstance()

        youTubePlayerSupportFragment.initialize(DeveloperKey.DEVELOPER_KEY, object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationFailure(
                provider: YouTubePlayer.Provider?,
                youTubeInitializationResult: YouTubeInitializationResult?
            ) {
                if (youTubeInitializationResult!!.isUserRecoverableError()) {
                    youTubeInitializationResult.getErrorDialog(activity, 1).show()
                } else {
                    Toast.makeText(context, youTubeInitializationResult.toString(), Toast.LENGTH_LONG).show()
                }
            }

            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer,
                wasRestored: Boolean
            ) {
                player.setFullscreen(false)
                youTubePlayer = player
                if (!wasRestored) {
                    player.cueVideo(args.videoId)
                }
            }

        })


        val transaction: FragmentTransaction =  childFragmentManager.beginTransaction()

        transaction.replace(R.id.youtube_fragment, youTubePlayerSupportFragment as Fragment).commit()

        timelinesLinearLayout = binding.timelines

        refreshTimelines()

        binding.fab.setOnClickListener {
            val dialogFragment = TimelineDialogFragment(youTubePlayer, timelineManager::add, ::refreshTimelines)
            dialogFragment.show(fragmentManager, "timeline")
        }

        return binding.root
    }

    fun addTimelineOnLayout(timeCode: Int, timeCodeDesc: String) {
        val timelineView: View = LayoutInflater.from(context).inflate(R.layout.timeline_row, timelinesLinearLayout, false)

        val timeCodeTextView = timelineView.findViewById<TextView>(R.id.time_code)
        val timeCodeDescTextView = timelineView.findViewById<TextView>(R.id.time_code_description)
        val timeCodeDeleteButton = timelineView.findViewById<ImageButton>(R.id.delete_timeline)

        timeCodeTextView.text = intToTime(timeCode)
        timeCodeDescTextView.text = timeCodeDesc

        val setOnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                youTubePlayer.seekToMillis(timeCode)
            }
        }

        timeCodeTextView.setOnClickListener(setOnClickListener)
        timeCodeDescTextView.setOnClickListener(setOnClickListener)
        timeCodeDeleteButton.setOnClickListener {
            timelineManager.remove(timeCode)
            refreshTimelines()
        }

        timelinesLinearLayout.addView(timelineView)
    }

    class TimelineDialogFragment(val youTubePlayer: YouTubePlayer, val addTimeLine: (timeCode: Int, timeCodeDesc: String) -> Unit, val refresh: () -> Unit) : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog{
            youTubePlayer.pause()

            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            val dialogLayout = LayoutInflater.from(context).inflate(R.layout.dialog_timeline, null)
            val dialogEditText = dialogLayout.findViewById<EditText>(R.id.timeline_dialog_desc)

            builder.setMessage(String.format("Save %s timecode?", intToTime(youTubePlayer.currentTimeMillis)))
                .setTitle(R.string.add_timeline)
                .setView(dialogLayout)
                .setPositiveButton("OK") {
                        dialog, which -> addTimeLine(youTubePlayer.currentTimeMillis, dialogEditText.text.toString())
                }
            return builder.create()
        }

        override fun onDestroy() {
            youTubePlayer.play()
            refresh()
            super.onDestroy()
        }

        private fun intToTime(timeCode: Int) : String {

            var hours: String = timeCode.div(1000).div(3600).toString()
            var minutes: String = timeCode.div(1000).div(60).toString()
            var seconds: String = timeCode.div(1000).rem(60).toString()

            if (timeCode.div(1000).div(3600) < 10)
                hours = "0$hours"
            if (timeCode.div(1000).div(60) < 10)
                minutes = "0$minutes"
            if (timeCode.div(1000).rem(60) < 10)
                seconds = "0$seconds"

            return String.format("%s:%s:%s", hours, minutes, seconds)
        }
    }

    fun intToTime(timeCode: Int) : String {

        var hours: String = timeCode.div(1000).div(3600).toString()
        var minutes: String = timeCode.div(1000).div(60).toString()
        var seconds: String = timeCode.div(1000).rem(60).toString()

        if (timeCode.div(1000).div(3600) < 10)
            hours = "0$hours"
        if (timeCode.div(1000).div(60) < 10)
            minutes = "0$minutes"
        if (timeCode.div(1000).rem(60) < 10)
            seconds = "0$seconds"

        return String.format("%s:%s:%s", hours, minutes, seconds)
    }

    fun refreshTimelines() {
        val timelinesMap: Map<Int, String> = timelineManager.getAll().toSortedMap()

        timelinesLinearLayout.removeAllViews()

        for ((timeCode, timeCodeDesc) in timelinesMap) {
            addTimelineOnLayout(timeCode, timeCodeDesc)
        }
    }
}
