package com.inspectorr.sausage.entities

import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.inspectorr.sausage.Assets
import com.inspectorr.sausage.entities.Sausage.State.*
import com.inspectorr.sausage.utils.UserScreen
import com.inspectorr.sausage.utils.animation

const val SAUSAGE_WIDTH = 256
const val SAUSAGE_HEIGHT = 256

class Sausage(val camera: OrthographicCamera, assets: Assets) {
    private val batch = SpriteBatch()

    init {
        batch.projectionMatrix = camera.combined
    }

    enum class State {
        FINE,
        FINE_TO_SCREAMING,
        SCREAMING,
        DYING,
    }

    var state = FINE

    private val fineTexture = assets.get("sausage_fine_256_1-16.png", Texture::class.java)
    private val fineAnimation = animation(fineTexture, SAUSAGE_WIDTH, SAUSAGE_HEIGHT, 16)

    private val screamingTexture = assets.get("sausage_screaming_256_1-4.png", Texture::class.java)
    private val screamingAnimation = animation(screamingTexture, SAUSAGE_WIDTH, SAUSAGE_HEIGHT, 4)

    private val fineToScreamingTexture = assets.get("sausage_fine-to-screaming_256_1-4.png", Texture::class.java)
    private val fineToScreamingAnimation = animation(fineToScreamingTexture, SAUSAGE_WIDTH, SAUSAGE_HEIGHT, 4)

    private var screamTimer = 0f
    private val fineToScreamingDuration = 0.395f

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

    private var startScreamSound = assets.get("sounds/scream_start.wav", Sound::class.java)
    private var mainScreamSound = assets.get("sounds/scream_body.wav", Sound::class.java)

//    var screamingLevel = 0f
    var screamingLevel = 0.25f

    var startId = 0L
    private fun playScreamStart() {
        if (startId != 0L) return
        startId = startScreamSound.play(screamingLevel)
    }

    var mainId = 0L
    private fun playScreamBody() {
        if (mainId != 0L) return
        mainId = mainScreamSound.loop(screamingLevel)
    }

//     playing sounds in get frame function BRILLIANT
    private fun getFrame(time: Float): TextureRegion {
        return when (state) {
            FINE -> {
                startScreamSound.stop(startId)
                mainScreamSound.stop(mainId)
                startId = 0L
                mainId = 0L
                fineAnimation.getKeyFrame(time, true)
            }
            FINE_TO_SCREAMING -> {
                playScreamStart()
                fineToScreamingAnimation.getKeyFrame(screamTimer, false)
            }
            SCREAMING -> {
                playScreamBody()
                screamingAnimation.getKeyFrame(time, true)
            }
            DYING -> {
                screamingAnimation.getKeyFrame(time, true)
            }
        }
    }

    var offset = SAUSAGE_WIDTH/16f
    var scale = UserScreen.TEXTURE_SCALE
    private val scaledOffset: Float
        get() = scale*(-offset+SAUSAGE_WIDTH/2f)

    var position = Vector2(0f, 0f)
    var rotation = 0f

    fun draw(time: Float) {
        batch.projectionMatrix = camera.combined
        val frame = getFrame(time)
        batch.begin()
        batch.draw(
                frame,
                position.x-scaledOffset,
                position.y-scaledOffset,
                SAUSAGE_WIDTH/2f*scale,
                SAUSAGE_HEIGHT/2f*scale,
                SAUSAGE_WIDTH*scale,
                SAUSAGE_HEIGHT*scale,
                1f, 1f,
                rotation
        )
        batch.end()
    }

    fun dispose() {
        startScreamSound.stop(startId)
        mainScreamSound.stop(mainId)
    }

}
