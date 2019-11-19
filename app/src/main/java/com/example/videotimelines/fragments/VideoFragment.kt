package com.example.videotimelines.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import com.example.videotimelines.R
import com.example.videotimelines.databinding.FragmentVideoBinding
import com.example.videotimelines.keys.DeveloperKey
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import kotlinx.android.synthetic.main.fragment_video.*


class VideoFragment : Fragment() {

    private lateinit var binding: FragmentVideoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_video, container, false)

        val args = VideoFragmentArgs.fromBundle(arguments!!)

        val youTubePlayerSupportFragment: YouTubePlayerSupportFragment = YouTubePlayerSupportFragment.newInstance()

        youTubePlayerSupportFragment.initialize(DeveloperKey.DEVELOPER_KEY, object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
            }

            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider,
                player: YouTubePlayer,
                wasRestored: Boolean
            ) {
                player.setFullscreen(false)
                if (!wasRestored) {
                    player.cueVideo(args.videoId)
                }
            }

        })


        val transaction: FragmentTransaction =  childFragmentManager.beginTransaction()

        transaction.replace(R.id.youtube_fragment, youTubePlayerSupportFragment as Fragment).commit()


//        FragmentTransaction transaction = childFragmentManager.youtube_fragment
//        transaction.replace(R.id.youTube_frame, new ArticleYouTubeFragment());
//        transaction.commit();


        return binding.root
    }

}
