package com.inspectorr.sausage

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.inspectorr.sausage.screens.GameOverScreen
import com.inspectorr.sausage.screens.PlayScreen

enum class Screens {
    PLAY,
    GAME_OVER,
}

val INIT_SCREEN = Screens.GAME_OVER

class Game : ApplicationAdapter() {
    lateinit var screen: ScreenAdapter
    fun setScreen(name: Screens) {
        screen = when (name) {
            Screens.PLAY -> PlayScreen(this)
            Screens.GAME_OVER -> GameOverScreen(this)
        }
        screen.show()
    }

    override fun create() {
        setScreen(INIT_SCREEN)
    }

    override fun render() {
        val delta = Gdx.graphics.deltaTime
        screen.render(delta)
    }

    override fun dispose() {

    }
}
