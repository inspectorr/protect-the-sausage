package com.inspectorr.sausage.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.inspectorr.sausage.utils.asset
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import com.inspectorr.sausage.utils.Screen
import com.inspectorr.sausage.utils.rgba

const val FONT_SIZE = 100
const val INIT_POINTS = 0

class Score(camera: OrthographicCamera) {
    var points = INIT_POINTS
    private val batch = SpriteBatch()

    private val generator = FreeTypeFontGenerator(
            asset("fonts/fr73.ttf")
    )

    private val parameter = FreeTypeFontParameter()

    private var font: BitmapFont

    init {
        parameter.size = FONT_SIZE
        batch.projectionMatrix = camera.combined
        parameter.characters = points.toString()
        font = generator.generateFont(parameter)
    }

    private fun setPointsToFont() {
        font.dispose()
        parameter.characters = points.toString()
        font = generator.generateFont(parameter)
    }

    fun increment() {
        points++
        setPointsToFont()
    }

    fun draw() {
        batch.apply {
            begin()
            color = Color.WHITE
            font.draw(
                    batch,
                    parameter.characters,
                    Screen.LEFT + 20, Screen.TOP - 20
            )
            end()
        }
    }

}
