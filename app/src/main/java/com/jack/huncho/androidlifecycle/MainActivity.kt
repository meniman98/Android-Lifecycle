package com.jack.huncho.androidlifecycle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleObserver
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import timber.log.Timber


class MainActivity : AppCompatActivity(), LifecycleObserver {

    private lateinit var youTubePlayerView: YouTubePlayerView
    private lateinit var observer: YouTubeObserver
    private lateinit var tracker: YouTubePlayerTracker
    private var startSeconds: Float = 100f
    private val videoId = "gVGRIBWy6ig"

    // Keys
    private val KEY_CURRENT_SECONDS = "KEY_CURRENT_SECONDS"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.i("onCreate called")

        if (savedInstanceState != null) {
            startSeconds = savedInstanceState.getFloat(KEY_CURRENT_SECONDS)
        }

        youTubePlayerView = findViewById(R.id.youTubePlayerView)
        observer = YouTubeObserver(this.lifecycle, youTubePlayerView)
        tracker = YouTubePlayerTracker()

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(videoId, startSeconds)
                youTubePlayerView.enableBackgroundPlayback(true)
                youTubePlayer.addListener(tracker)
            }
        })
        Timber.i(tracker.currentSecond.toString())
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putFloat(KEY_CURRENT_SECONDS, tracker.currentSecond)
        Timber.i("onSave called")

    }
}