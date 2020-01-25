package com.inspectorr.sausage.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Interpolation
import com.inspectorr.sausage.utils.Screen
import com.inspectorr.sausage.utils.glEnableAlpha

class Fader(camera: OrthographicCamera) {
    private val shapeRenderer = ShapeRenderer()

    init {
        shapeRenderer.projectionMatrix = camera.combined
    }

    private var alpha = 0f

    fun update(progress: Float) {
        alpha = 1-progress
        if (alpha>1f) alpha = 1f
        if (alpha<0f) alpha = 0f
    }

    fun draw() {
        shapeRenderer.apply {
            begin(ShapeRenderer.ShapeType.Filled)
            glEnableAlpha()
            color = Color(0f, 0f, 0f, alpha)
            rect(Screen.LEFT, Screen.BOTTOM, Screen.WIDTH, Screen.HEIGHT)
            end()
        }
    }
}