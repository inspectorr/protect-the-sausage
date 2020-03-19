package com.inspectorr.sausage.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.inspectorr.sausage.utils.UserScreen
import com.inspectorr.sausage.utils.glEnableAlpha

const val FADE_DURATION = 1f

class Fader(
        camera: OrthographicCamera,
        private val fadeInCallback: () -> Unit = {},
        private val fadeOutCallback: () -> Unit = {},
        defaultAlpha: Float = 0f
) {
    private val shapeRenderer = ShapeRenderer()

    init {
        shapeRenderer.projectionMatrix = camera.combined
    }

    private var alpha = 0f

    fun update(delta: Float) {
        updateFading(delta)
        alpha = 1-progress
        if (alpha>1f) alpha = 1f
        if (alpha<0f) alpha = 0f
    }

    fun draw() {
        shapeRenderer.apply {
            begin(ShapeRenderer.ShapeType.Filled)
            glEnableAlpha()
            color = Color(0f, 0f, 0f, alpha)
            rect(UserScreen.LEFT, UserScreen.BOTTOM, UserScreen.WIDTH, UserScreen.HEIGHT)
            end()
        }
    }

    private var defaultProgress = 1-defaultAlpha
    private val fadingDuration = FADE_DURATION
    private var fadingOutTimer = 0f
    private var fadingInTimer = 0f
    private val progress: Float
        get() {
            var value = defaultProgress

            if (fadingInTimer > 0f) {
                value = 1 - fadingInTimer / fadingDuration
            } else if (fadingOutTimer > 0f) {
                value = fadingOutTimer / fadingDuration
            }

            return value
        }

    fun fadeOut() {
        fadingOutTimer = fadingDuration
        defaultProgress = 0f
    }

    fun fadeIn() {
        fadingInTimer = fadingDuration
        defaultProgress = 1f
    }

    private fun updateFading(delta: Float) {
        if (fadingOutTimer > 0f) {
            fadingOutTimer -= delta
            if (fadingOutTimer <= 0f) {
                fadeOutCallback()
                defaultProgress = 0f
            }
        } else {
            fadingOutTimer = 0f
        }
        if (fadingInTimer > 0f) {
            fadingInTimer -= delta
            if (fadingInTimer <= 0f) {
                fadeInCallback()
                defaultProgress = 1f
            }
        } else {
            fadingInTimer = 0f
        }
    }
}
