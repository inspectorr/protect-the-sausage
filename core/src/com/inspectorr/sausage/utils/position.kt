package com.inspectorr.sausage.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2

class Screen {
    companion object {
        val TOP = Gdx.graphics.height/2f
        val RIGHT = Gdx.graphics.width/2f
        val BOTTOM = -Gdx.graphics.height/2f
        val LEFT = -Gdx.graphics.width/2f
        val CENTER = Vector2(0f, 0f)
        val HEIGHT = Gdx.graphics.height.toFloat()
        val WIDTH = Gdx.graphics.width.toFloat()
    }
}

fun isOutOfTop(position: Vector2) : Boolean {
    return position.y > Screen.TOP
}

fun isOutOfRight(position: Vector2) : Boolean {
    return position.x > Screen.RIGHT
}

fun isOutOfBottom(position: Vector2) : Boolean {
    return position.y < Screen.BOTTOM
}

fun isOutOfLeft(position: Vector2) : Boolean {
    return position.x < Screen.LEFT
}

fun isOutOfScreen(position: Vector2) : Boolean {
    return isOutOfTop(position) || isOutOfRight(position) || isOutOfBottom(position) || isOutOfLeft(position)
}

fun distance(v1: Vector2, v2: Vector2): Int {
    return v1.dst(v2).toInt()
}

