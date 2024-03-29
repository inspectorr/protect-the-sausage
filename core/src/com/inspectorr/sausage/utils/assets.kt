package com.inspectorr.sausage.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Array

fun animation(texture: Texture, width: Int, height: Int, size: Int): Animation<TextureRegion> {
    val split = TextureRegion.split(texture, width, height)

    val frames = Array<TextureRegion>().apply {
        for (i in 0 until size) add(split[0][i])
    }

    return Animation(0.1f, frames)
}

fun asset(path: String): FileHandle {
    return Gdx.files.internal(path)
}