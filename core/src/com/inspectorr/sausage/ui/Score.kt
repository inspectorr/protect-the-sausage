package com.inspectorr.sausage.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.inspectorr.sausage.utils.Screen

const val INIT_POINTS = 0

class Score(camera: OrthographicCamera) {
    private var points = INIT_POINTS
    private val batch = SpriteBatch()
    private val text = Text(batch, points.toString())

    init {
        batch.projectionMatrix = camera.combined
        text.size = 50
    }

    fun increment() {
        points++
        text.content = points.toString()
        text.update()
    }

    fun draw() {
        batch.apply {
            begin()
            color = Color.WHITE
            text.draw(Screen.LEFT + 20,Screen.TOP - 20)
            end()
        }
    }

}
