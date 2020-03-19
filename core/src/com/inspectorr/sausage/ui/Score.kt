package com.inspectorr.sausage.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.inspectorr.sausage.utils.UserScreen
import com.inspectorr.sausage.utils.rgba

const val INIT_POINTS = 0

class Score(camera: OrthographicCamera, initPoints: Int = INIT_POINTS) {
    var points = initPoints
    private val batch = SpriteBatch()
    private val text = Text(batch, points.toString())

    init {
        text.size = 75
        text.content = "$points"
        text.parameter.borderColor = rgba(255F, 170F, 0F)
        text.parameter.borderWidth = 3f * Gdx.graphics.density
        batch.projectionMatrix = camera.combined
        draw()
    }

    fun increment() {
        points++
        text.content = "$points"
    }

    fun draw() {
        batch.apply {
            begin()
            color = Color.WHITE
            text.draw(-text.width/2,UserScreen.TOP - Gdx.graphics.density*50f)
            end()
        }
    }

}
