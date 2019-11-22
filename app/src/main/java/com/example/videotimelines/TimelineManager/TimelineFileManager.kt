package com.example.videotimelines.TimelineManager

import android.content.Context
import java.io.File

class TimelineFileManager(context: Context, private val videoId: String) : TimelineManager {

    private val timelineCatalog: File

    init {
        val dir: File = context.filesDir
        timelineCatalog = File(dir, "timelines")
        if (!timelineCatalog.exists())
            timelineCatalog.mkdirs()
        createTimelineFile(videoId)
    }

    private fun createTimelineFile(videoId: String) : Boolean {
        if (!timelineCatalog.exists())
            return false
        return File(timelineCatalog, videoId).createNewFile()
    }

    override fun add(timeCode: Int, timeCodeDesc: String) {
        if (contains(timeCode))
            return

        val timeliesMap: Map<Int, String> = getAll()

        val printWriter = File(timelineCatalog, videoId).printWriter()

        for((timeCode, timeCodeDesc) in timeliesMap) {
            printWriter.println("$timeCode:$timeCodeDesc")
        }

        printWriter.println("$timeCode:$timeCodeDesc")
        printWriter.close()
    }

    override fun remove(timeCode: Int) {
        if (!contains(timeCode))
            return

        val tempFile = File(timelineCatalog, "$videoId.tmp")
        tempFile.createNewFile()
        val file = File(timelineCatalog, videoId)
        val printWriter = tempFile.printWriter()
        val bufferedReader = file.bufferedReader()

        bufferedReader.forEachLine {
            if (it.split(':')[0].toInt() != timeCode)
                printWriter.println(it.trim())
        }

        bufferedReader.close()
        printWriter.close()

        file.delete()
        tempFile.renameTo(file)
    }

    override fun contains(timeCode: Int) : Boolean {
        return getAll().contains(timeCode)
    }

    override fun getAll(): Map<Int, String> {
        val mapOfTimelines: MutableMap<Int, String> = HashMap()

        File(timelineCatalog, videoId).forEachLine {
            val timeCode = it.split(':')[0].toInt()
            val timeCodeDesc = it.split(':')[1]
            mapOfTimelines.put(timeCode, timeCodeDesc)
        }

        return mapOfTimelines
    }
}