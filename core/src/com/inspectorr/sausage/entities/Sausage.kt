package com.inspectorr.sausage.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.inspectorr.sausage.entities.Sausage.State.*
import com.inspectorr.sausage.utils.animation

class Sausage(private val batch: SpriteBatch) {
    private val width = 256
    private val height = 256

    enum class State {
        FINE,
        FINE_TO_SCREAMING,
        SCREAMING
    }

    private var state = FINE

    private val fineTexture = Texture(Gdx.files.internal("sausage_fine_256_1-16.png"))
    private val fineAnimation = animation(fineTexture, width, height, 16)

    private val screamingTexture = Texture(Gdx.files.internal("sausage_screaming_256_1-4.png"))
    private val screamingAnimation = animation(screamingTexture, width, height, 4)

    private val fineToScreamingTexture = Texture(Gdx.files.internal("sausage_fine-to-screaming_256_1-4.png"))
    private val fineToScreamingAnimation = animation(fineToScreamingTexture, width, height, 4)

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

    private val offset = 20f
    private val position = Vector2(-width/2f+offset, -height/2f+offset)

    fun draw(time: Float) {
        val frame = getFrame(time)
        batch.begin()
        batch.draw(frame, position.x, position.y)
        batch.end()
    }

}