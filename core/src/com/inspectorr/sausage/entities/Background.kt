package com.inspectorr.sausage.entities

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.inspectorr.sausage.utils.Screen
import com.inspectorr.sausage.utils.glEnableAlpha
import com.inspectorr.sausage.utils.rgba

val START_BG_COLOR = rgba(80f, 170f, 240f)
val END_BG_COLOR = rgba(80f, 70f, 140f)

val deltaRed = END_BG_COLOR.r - START_BG_COLOR.r
val deltaGreen = END_BG_COLOR.g - START_BG_COLOR.g
val deltaBlue = END_BG_COLOR.b - START_BG_COLOR.b

class Background(camera: OrthographicCamera) {
    private val shapeRenderer = ShapeRenderer()

    init {
        shapeRenderer.projectionMatrix = camera.combined
    }

    private var backgroundColor = START_BG_COLOR

    private fun updateBackgroundColor(pawProgress: Float) {
        val red = START_BG_COLOR.r + pawProgress * deltaRed
        val green = START_BG_COLOR.g + pawProgress * deltaGreen
        val blue = START_BG_COLOR.b + pawProgress * deltaBlue
//        println("$pawProgress;    $red     $blue     $green")
        backgroundColor = Color(red, green, blue, 1f)
    }

    private fun drawBackground() {
        shapeRenderer.color = backgroundColor
        shapeRenderer.rect(
                Screen.LEFT, Screen.BOTTOM,
                Screen.WIDTH, Screen.HEIGHT
        )
    }

    fun update(pawProgress: Float) {
        updateBackgroundColor(pawProgress)
    }

    fun draw() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        glEnableAlpha()

        drawBackground()

        shapeRenderer.end()
    }
}