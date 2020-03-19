package com.inspectorr.sausage.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import java.lang.Exception

const val FILE_PATH = "highscore.txt"
val file: FileHandle = Gdx.files.local(FILE_PATH)

fun getHighscore(): String {
    lateinit var highscore: String
    try {
        highscore = file.readString()
    } catch (e: Exception) {
        file.writeString("0", false)
    }

    return file.readString()
}

fun setHighscore(num: Int) {
    val highscore = getHighscore()
    if (highscore.toInt() < num) {
        file.writeString(num.toString(), false)
    }
}