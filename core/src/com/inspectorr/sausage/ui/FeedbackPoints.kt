package com.inspectorr.sausage.ui

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.inspectorr.sausage.Assets

class FeedbackPoints(val camera: OrthographicCamera, val assets: Assets) {
    private val renderer = ShapeRenderer()
    private var list = mutableListOf<FeedbackPoint>()

    init {
        renderer.projectionMatrix = camera.combined
    }

    fun add(x: Float, y: Float, hit: Boolean) {
        renderer.projectionMatrix = camera.combined
        list.add(FeedbackPoint(Vector2(x, y), renderer, assets, hit))
    }

    fun update(delta: Float) {
        list.removeAll {
            it.timeLeft -= delta
            it.timeLeft <= 0f
        }
    }

    fun draw() {
        list.forEach {
            it.draw()
        }
    }
}