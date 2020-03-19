package com.inspectorr.sausage.ui

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.inspectorr.sausage.utils.*

val START_BG_COLOR = rgba(255f, 255f, 255f)

class Background(camera: OrthographicCamera) {
    private val backgroundColorRenderer = ShapeRenderer()

    init {
        backgroundColorRenderer.projectionMatrix = camera.combined
    }

    private var backgroundColor = START_BG_COLOR

    fun changeColor() {
        backgroundColor = generateColor()
    }

    fun draw() {
        backgroundColorRenderer.apply {
            begin(ShapeRenderer.ShapeType.Filled)
            glEnableAlpha()
            color = backgroundColor
            rect(
                    UserScreen.LEFT, UserScreen.BOTTOM,
                    UserScreen.WIDTH, UserScreen.HEIGHT
            )
            end()
        }
    }
}