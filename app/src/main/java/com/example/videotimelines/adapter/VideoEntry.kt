package com.example.videotimelines.adapter

class VideoEntry(private val text: String, private val videoId: String) {
    fun getText(): String {
        return text
    }
    fun getVideoId(): String {
        return videoId
    }
}