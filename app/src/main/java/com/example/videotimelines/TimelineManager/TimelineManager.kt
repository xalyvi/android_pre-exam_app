package com.example.videotimelines.TimelineManager

interface TimelineManager {

    fun add(timeCode: Int, timeCodeDesc: String)

    fun remove(timeCode: Int)

    fun getAll() : Map<Int, String>

    fun contains(timeCode: Int) : Boolean
}