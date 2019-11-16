package com.inspectorr.sausage.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.inspectorr.sausage.entities.Sausage.State.*
import com.inspectorr.sausage.utils.Screen
import com.inspectorr.sausage.utils.animation

const val SAUSAGE_WIDTH = 256
const val SAUSAGE_HEIGHT = 256

class Sausage(camera: OrthographicCamera) {
    private val batch = SpriteBatch()

    init {
        batch.projectionMatrix = camera.combined
    }

    enum class State {
        FINE,
        FINE_TO_SCREAMING,
        SCREAMING
    }

    private var state = FINE

    private val fineTexture = Texture(Gdx.files.internal("sausage_fine_256_1-16.png"))
    private val fineAnimation = animation(fineTexture, SAUSAGE_WIDTH, SAUSAGE_HEIGHT, 16)

    private val screamingTexture = Texture(Gdx.files.internal("sausage_screaming_256_1-4.png"))
    private val screamingAnimation = animation(screamingTexture, SAUSAGE_WIDTH, SAUSAGE_HEIGHT, 4)

    private val fineToScreamingTexture = Texture(Gdx.files.internal("sausage_fine-to-screaming_256_1-4.png"))
    private val fineToScreamingAnimation = animation(fineToScreamingTexture, SAUSAGE_WIDTH, SAUSAGE_HEIGHT, 4)

    private var screamTimer = 0f
    private val fineToScreamingDuration = 0.4f

    fun scream(delta: Float) {
        if (screamTimer >= fineToScreamingDuration) {
            state = SCREAMING
        } else {
            screamTimer += delta
            state = FINE_TO_SCREAMING
        }
    }

    fun stopScreaming() {
        screamTimer = 0f
        state = FINE
    }

    private fun getFrame(time: Float): TextureRegion {
        return when (state) {
            FINE -> fineAnimation.getKeyFrame(time, true)
            FINE_TO_SCREAMING -> fineToScreamingAnimation.getKeyFrame(screamTimer, false)
            SCREAMING -> screamingAnimation.getKeyFrame(time, true)
        }
    }

    var offset = 40f
    var scale = Screen.TEXTURE_SCALE
    private val scaledOffset: Float
        get() = scale*offset

    var position = Vector2(0f, 0f)
    var rotation = 0f

    fun draw(time: Float) {
        val frame = getFrame(time)
        batch.begin()
        batch.draw(
                frame,
                position.x-scaledOffset,
                position.y-scaledOffset,
                SAUSAGE_WIDTH/2f,
                SAUSAGE_HEIGHT/2f,
                SAUSAGE_WIDTH.toFloat(),
                SAUSAGE_HEIGHT.toFloat(),
                scale, scale,
                rotation
        )
        batch.end()
    }

}