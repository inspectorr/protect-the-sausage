package com.inspectorr.sausage.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Interpolation
import com.inspectorr.sausage.utils.Screen
import com.inspectorr.sausage.utils.rgba
import java.lang.Math.cos
import java.lang.Math.sin
import kotlin.math.ln

val START_BG_COLOR = rgba(145f, 200f, 255f)
val END_BG_COLOR = rgba(105f, 9f, 0f)

val deltaRed = END_BG_COLOR.r - START_BG_COLOR.r
val deltaGreen = END_BG_COLOR.g - START_BG_COLOR.g
val deltaBlue = END_BG_COLOR.b - START_BG_COLOR.b


class Background (private val shapeRenderer: ShapeRenderer) {
    private var color = START_BG_COLOR
    private val maxProgress = 3

    fun update(pawProgress: Float) {
        val progress = Interpolation.pow2In.apply(pawProgress)
        val red = START_BG_COLOR.r + progress * deltaRed
        val green = START_BG_COLOR.g + progress * deltaGreen
        val blue = START_BG_COLOR.b + progress * deltaBlue
        println("$progress;    $red     $blue     $green")
        color = Color(red.toFloat(), green.toFloat(), blue.toFloat(), 1f)
    }

    fun draw() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        Gdx.gl.glEnable(GL20.GL_BLEND)
        shapeRenderer.color = color
        shapeRenderer.rect(
                Screen.LEFT, Screen.BOTTOM,
                Screen.WIDTH, Screen.HEIGHT
        )
        shapeRenderer.end()
    }
}