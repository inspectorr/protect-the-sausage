package com.inspectorr.sausage

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.inspectorr.sausage.screens.PlayScreen

class Game : ApplicationAdapter() {

    private lateinit var playScreen: PlayScreen

    override fun create() {
        playScreen = PlayScreen()
        playScreen.show()
    }

    override fun render() {
        val delta = Gdx.graphics.deltaTime
        playScreen.render(delta)
    }

    override fun dispose() {

    }
}
