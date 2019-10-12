package com.inspectorr.sausage

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.GL20
import com.inspectorr.sausage.screens.PlayScreen

class Game : ApplicationAdapter() {

    private lateinit var playScreen: PlayScreen

    override fun create() {
        playScreen = PlayScreen()
        playScreen.show()

        Gdx.input.inputProcessor = object : InputAdapter() {
            override fun touchDown(x: Int, y: Int, pointer: Int, button: Int): Boolean {
                // your touch down code here
                //
                playScreen.handleTouch(x, y)

                return true // return true to indicate the event was handled
            }

            override fun touchUp(x: Int, y: Int, pointer: Int, button: Int): Boolean {
                // your touch up code here
                return true // return true to indicate the event was handled
            }
        }
    }

    override fun render() {
        val delta = Gdx.graphics.deltaTime
        playScreen.render(delta)
    }

    override fun dispose() {

    }
}
