package com.inspectorr.sausage.ui

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2

class FeedbackPoints(camera: OrthographicCamera) {
    private val renderer = ShapeRenderer()
    private var list = mutableListOf<FeedbackPoint>()

    init {
        renderer.projectionMatrix = camera.combined
    }

    fun add(x: Float, y: Float) {
        list.add(FeedbackPoint(Vector2(x, y), renderer))
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