package com.inspectorr.sausage.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.inspectorr.sausage.utils.Screen
import com.inspectorr.sausage.utils.glEnableAlpha
import com.inspectorr.sausage.utils.randomFloat
import com.inspectorr.sausage.utils.rgba
import java.lang.Math.sin
import kotlin.math.sin

val START_BG_COLOR = rgba(80f, 160f, 250f)
val END_BG_COLOR = rgba(80f, 70f, 140f)

val deltaRed = END_BG_COLOR.r - START_BG_COLOR.r
val deltaGreen = END_BG_COLOR.g - START_BG_COLOR.g
val deltaBlue = END_BG_COLOR.b - START_BG_COLOR.b

class Background(camera: OrthographicCamera) {
    private val shader: ShaderProgram = ShaderProgram(Gdx.files.internal("shaders/bg.vsh"), Gdx.files.internal("shaders/bg.fsh"))
    private val shapeRenderer = ShapeRenderer(5000, shader)


    init {
        shapeRenderer.projectionMatrix = camera.combined
        println("shader ${shader.isCompiled}")
        println(shader.log)
    }

    fun show() {
//        shader.setUniformMatrix("u_projTrans", camera.combined);
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

    private fun shadering(progress: Float) {
        println("$progress\n")
        shader.apply {
            begin()
            setUniformf(
                    "u_progress",
                    sin(progress)
//                        progress
            )
//            setUniformf(
//                    "u_colorOffset",
//            )
            setUniformf(
                    "u_resolution",
                    Screen.WIDTH, Screen.HEIGHT
            )
            end()
        }
    }

    fun update(pawProgress: Float) {
        shadering(pawProgress)
        updateBackgroundColor(pawProgress)
    }

    fun draw() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        glEnableAlpha()

        drawBackground()

        shapeRenderer.end()
    }
}