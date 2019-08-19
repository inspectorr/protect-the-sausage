package com.inspectorr.sausage.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.inspectorr.sausage.entities.Sausage.State.*
import com.inspectorr.sausage.utils.parseAnimation

class Sausage(private val batch: SpriteBatch) {
    private val width = 256
    private val height = 256

    enum class State {
        FINE,
        FINE_TO_SCREAMING,
        SCREAMING
    }

    private val fineTexture = Texture(Gdx.files.internal("sausage_fine_256_1-16.png"))
    private val fineAnimation = parseAnimation(fineTexture, width, height, 16)

    private val screamingTexture = Texture(Gdx.files.internal("sausage_screaming_256_1-4_d.png"))
    private val screamingAnimation = parseAnimation(screamingTexture, width, height, 4)

    private val fineToScreamingTexture = Texture(Gdx.files.internal("sausage_fine-to-screaming_256_1-4.png"))
    private val fineToScreamingAnimation = parseAnimation(fineToScreamingTexture, width, height, 4)

    var state = FINE
    private val animation: Animation<TextureRegion> get() {
        return when (state) {
            FINE -> fineAnimation
            FINE_TO_SCREAMING -> fineToScreamingAnimation
            SCREAMING -> screamingAnimation
        }
    }

    var screamTimer = 0f
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

    var position = Vector2(-width/2f, -height/2f)

    fun setPosition(x: Float, y: Float) {
        position = Vector2(x, y)
    }

    private fun getFrame(time: Float): TextureRegion {
        return when (state) {
            FINE -> animation.getKeyFrame(time, true)
            FINE_TO_SCREAMING -> animation.getKeyFrame(screamTimer, false)
            SCREAMING -> animation.getKeyFrame(time, true)
        }
    }

    fun draw(time: Float) {
        val frame = getFrame(time)
        batch.draw(frame, position.x, position.y)
    }

}