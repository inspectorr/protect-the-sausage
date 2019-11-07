package com.inspectorr.sausage.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.inspectorr.sausage.utils.Screen
import com.inspectorr.sausage.utils.asset
import com.inspectorr.sausage.utils.glEnableAlpha
import com.inspectorr.sausage.utils.rgba
import kotlin.math.PI
import kotlin.math.sin

val START_BG_COLOR = rgba(80f, 160f, 250f)
val END_BG_COLOR = rgba(80f, 70f, 140f)

val deltaRed = END_BG_COLOR.r - START_BG_COLOR.r
val deltaGreen = END_BG_COLOR.g - START_BG_COLOR.g
val deltaBlue = END_BG_COLOR.b - START_BG_COLOR.b

class Background(camera: OrthographicCamera) {
    private val roundShader = ShaderProgram(
            asset("shaders/background/round.vsh"),
            asset("shaders/background/round.fsh")
    )
    private val roundRenderer = ShapeRenderer(5000, roundShader)

    private val backgroundColorShader = ShaderProgram(
            asset("shaders/background/cloud.vsh"),
            asset("shaders/background/cloud.fsh")
    )
//    private val backgroundColorRenderer = ShapeRenderer(5000, backgroundColorShader)
    private val backgroundColorRenderer = ShapeRenderer()

    init {
        roundRenderer.projectionMatrix = camera.combined
        backgroundColorRenderer.projectionMatrix = camera.combined
        println("roundShader ${roundShader.isCompiled}")
        println(roundShader.log)
        println("backgroundColorShader ${backgroundColorShader.isCompiled}")
        println(backgroundColorShader.log)
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
        backgroundColorRenderer.apply {
            begin(ShapeRenderer.ShapeType.Filled)
            glEnableAlpha()
            color = backgroundColor
            rect(
                    Screen.LEFT, Screen.BOTTOM,
                    Screen.WIDTH, Screen.HEIGHT
            )
            end()
        }
    }

    private fun setBackgroundColorShader(time: Float) {
        val speed = 0.05
        val progress = ((time*speed - (time*speed).toInt()) * PI * 2).toFloat()


//        println("$time\n")

        backgroundColorShader.apply {
            begin()
            setUniformf(
                    "u_resolution",
                    Screen.WIDTH, Screen.HEIGHT
            )
            setUniformf(
                    "u_progress",
                    progress
            )
            setUniformf(
                    "u_time",
                    time
            )
            end()
        }
    }

    private fun setRoundShader(progress: Float) {
        roundShader.apply {
            begin()
            setUniformf(
                    "u_progress",
                    sin(progress)
//                        progress
            )
            setUniformf(
                    "u_resolution",
                    Screen.WIDTH, Screen.HEIGHT
            )
            end()
        }
    }

    private fun updateRoundColor(progress: Float) {
        roundRenderer.color = rgba(155f, 0f, 0f, progress)
    }

    private fun drawRound() {
        roundRenderer.apply {
            begin(ShapeRenderer.ShapeType.Filled)
            glEnableAlpha()
            rect(
                    Screen.LEFT, Screen.BOTTOM,
                    Screen.WIDTH, Screen.HEIGHT
            )
            end()
        }
    }

    fun update(pawProgress: Float, time: Float) {
        setRoundShader(pawProgress)
        setBackgroundColorShader(time)
        updateBackgroundColor(pawProgress)
        updateRoundColor(pawProgress)
    }

    fun draw() {
        drawBackground()
        drawRound()
    }
}