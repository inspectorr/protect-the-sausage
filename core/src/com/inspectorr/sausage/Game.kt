package com.inspectorr.sausage

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.inspectorr.sausage.entities.Sausage
import com.inspectorr.sausage.screens.GameOverScreen
import com.inspectorr.sausage.screens.PlayScreen
import com.badlogic.gdx.audio.Sound

enum class Screens {
    PLAY,
    GAME_OVER,
}

val INIT_SCREEN = Screens.GAME_OVER

class Game : ApplicationAdapter() {
    private lateinit var screen: ScreenAdapter
    private var wasLaunched = false

    fun setScreen(name: Screens) {
        if (wasLaunched) screen.dispose()
        wasLaunched = true
        screen = when (name) {
            Screens.PLAY -> PlayScreen(this, assets)
            Screens.GAME_OVER -> GameOverScreen(this, playerScore, assets)
        }
        screen.show()
    }

    var playerScore = 0

    private val assets = Assets()

    override fun create() {
        // here we can display splash screen
        assets.init()
        setScreen(INIT_SCREEN)
    }

    override fun render() {
        if (!assets.areLoaded) return
        assets.update()
        val delta = Gdx.graphics.deltaTime
        screen.render(delta)
    }

    override fun dispose() {

    }
}

// todo go deep in kotlin async...
class Assets : AssetManager() {

    private val textures = listOf(
            "paw1.png",
            "paw2.png",
            "paw3.png",
            "sausage_fine_256_1-16.png",
            "sausage_fine-to-screaming_256_1-4.png",
            "sausage_screaming_256_1-4.png",
            "button-state-start-256.png",
            "button-state-middle-256.png"
    )

    enum class Entities {
        SAUSAGE,
        FEEDBACK_POINT,
    }

    private val sounds = mapOf(
            Entities.SAUSAGE to mapOf(
                    Sausage.State.FINE_TO_SCREAMING to "sounds/scream_start.wav",
                    Sausage.State.SCREAMING to "sounds/scream_body.wav"
            ),
            Entities.FEEDBACK_POINT to mapOf(
                    "BZZ" to "sounds/bzz.wav",
                    "CLAP" to "sounds/clap.wav",
                    "MISS" to "sounds/miss.wav",
                    "bg" to "sounds/gameover.mp3"
            )
    )

    var areLoaded = false

    fun init(): Boolean {
        textures.forEach {
            load(it, Texture::class.java)
        }

        sounds.forEach { entry ->
            entry.value.forEach {
                load(it.value, Sound::class.java)
            }
        }

        while (!update()) {
            // wait :)
        }

        areLoaded = true

        return areLoaded
    }

}
