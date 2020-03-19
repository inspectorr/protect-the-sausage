package com.inspectorr.sausage.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Vector2
import kotlin.math.abs

class UserScreen {
    companion object {
        val TOP = Gdx.graphics.height / 2f
        val RIGHT = Gdx.graphics.width / 2f
        val BOTTOM = -Gdx.graphics.height / 2f
        val LEFT = -Gdx.graphics.width / 2f
        val CENTER = Vector2(0f, 0f)
        val HEIGHT = Gdx.graphics.height.toFloat()
        val WIDTH = Gdx.graphics.width.toFloat()
        val TEXTURE_SCALE = if (Gdx.graphics.density <= 2f) Gdx.graphics.density else 2f
    }
}

const val DEFAULT_MARGIN = 500f

fun isOutOfTop(position: Vector2, margin: Float): Boolean {
    return position.y - margin > UserScreen.TOP
}

fun isOutOfRight(position: Vector2, margin: Float): Boolean {
    return position.x - margin > UserScreen.RIGHT
}

fun isOutOfBottom(position: Vector2, margin: Float): Boolean {
    return position.y + margin < UserScreen.BOTTOM
}

fun isOutOfLeft(position: Vector2, margin: Float): Boolean {
    return position.x + margin < UserScreen.LEFT
}

fun isOutOfScreen(position: Vector2, margin: Float = DEFAULT_MARGIN): Boolean {
    return isOutOfTop(position, margin) ||
            isOutOfRight(position, margin) ||
            isOutOfBottom(position, margin) ||
            isOutOfLeft(position, margin)
}

fun distance(v1: Vector2, v2: Vector2): Int {
    return abs(v1.dst(v2).toInt())
}

fun relativeValue(value: Float): Float {
    return value * Gdx.graphics.density
}

//fun getRectangleShape(textureWidth: Float, textureHeight: Float): Polygon {
//    val shape = Polygon()
//    shape.apply {
//        setOrigin(0f, textureHeight/2)
//        vertices = floatArrayOf(
//                0f, 0f,
//                0f, textureHeight,
//                textureWidth, textureHeight,
//                textureWidth, 0f
//        )
//        setScale(UserScreen.TEXTURE_SCALE*0.9f, UserScreen.TEXTURE_SCALE*0.9f)
//    }
//    return shape
//}

fun SpriteBatch.drawTextureRegionByShape(textureRegion: TextureRegion, shape: Polygon) {
    this.draw(
            textureRegion,
            shape.x, shape.y,
            shape.originX, shape.originY,
            textureRegion.regionWidth.toFloat(),
            textureRegion.regionHeight.toFloat(),
            shape.scaleX, shape.scaleY,
            shape.rotation
    )
}

