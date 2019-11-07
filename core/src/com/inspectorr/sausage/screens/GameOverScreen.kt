package com.inspectorr.sausage.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.inspectorr.sausage.Game
import com.inspectorr.sausage.Screens
import com.inspectorr.sausage.ui.Text
import com.inspectorr.sausage.utils.Screen

const val DURATION = 2.5f

class GameOverScreen(private val game: Game) : ScreenAdapter() {
    private val camera = OrthographicCamera(Screen.WIDTH, Screen.HEIGHT)

    private val batch = SpriteBatch()
    private val text = Text(batch, "GAME OVER")

    init {
        Gdx.input.inputProcessor = GameOverScreenInputAdapter(this)
        batch.projectionMatrix = camera.combined
        text.size = 40
    }

    override fun show() {

    }

    private fun clear() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    }

    var timer = 0f

    fun navigateToPlayScreen() {
        game.setScreen(Screens.PLAY)
    }

    private fun update(delta: Float) {
        timer += delta
        if (timer > DURATION) {
            navigateToPlayScreen()
        }
        camera.update()
    }

    private fun draw() {
        batch.apply {
            begin()
            color = Color.WHITE
            text.draw(
                    -text.width/2,
                    text.height/2
            )
            end()
        }
    }

    fun handleTouch() {
        navigateToPlayScreen()
    }

    override fun render(delta: Float) {
        update(delta)
        clear()
        draw()
    }
}

class GameOverScreenInputAdapter(private val screen: GameOverScreen) : InputAdapter() {
    override fun touchDown(x: Int, y: Int, pointer: Int, button: Int): Boolean {
        // your touch down code here
        screen.handleTouch()
        return true // return true to indicate the event was handled
    }

    override fun touchUp(x: Int, y: Int, pointer: Int, button: Int): Boolean {
        // your touch up code here
        return true // return true to indicate the event was handled
    }
}
