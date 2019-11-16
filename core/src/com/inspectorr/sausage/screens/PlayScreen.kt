package com.inspectorr.sausage.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.inspectorr.sausage.Game
import com.inspectorr.sausage.Screens
import com.inspectorr.sausage.entities.PawState
import com.inspectorr.sausage.ui.Background
import com.inspectorr.sausage.entities.Paws
import com.inspectorr.sausage.entities.SAUSAGE_WIDTH
import com.inspectorr.sausage.entities.Sausage
import com.inspectorr.sausage.ui.FeedbackPoints
import com.inspectorr.sausage.ui.Score
import com.inspectorr.sausage.utils.Screen
import com.inspectorr.sausage.utils.isOutOfScreen

class PlayScreen(private val game: Game) : ScreenAdapter() {
    private val camera = OrthographicCamera(Screen.WIDTH, Screen.HEIGHT)

    private val background = Background(camera)
    private val sausage = Sausage(camera)
    private val paws = Paws(camera)
    private val score = Score(camera)
    private val touches = FeedbackPoints(camera)

    init {
        Gdx.input.inputProcessor = PlayScreenInputAdapter(this)
    }

    private var time = 0f

    override fun show() {
        paws.add()
    }

    private fun update(delta: Float) {
        time += delta

        background.update(0f, time)

        // todo refactor
        sausage.apply {
            val shouldScream = paws.list.any {
                if (it.state == PawState.MOVING_BACK_KILL) {
                    position = it.position
                }
                it.state != PawState.MOVING_BACK_EMPTY
            }

            if (shouldScream) {
                scream(delta)
            } else {
                stopScreaming()
            }

            if (isOutOfScreen(position, SAUSAGE_WIDTH*scale)) {
                game.setScreen(Screens.GAME_OVER)
            }
        }

        paws.update(delta)

        touches.update(delta)

        camera.update()
    }

    fun handleTouch(screenX: Int, screenY: Int) {
        val x = screenX - Gdx.graphics.width/2f
        val y = -screenY + Gdx.graphics.height/2f
        println("x: $x, y: $y")
        paws.list.forEach {
            if (it.shape.contains(x, y)) {
                score.increment()
                it.onTouch()
            }
        }
        touches.add(x, y)
    }

    private fun clear() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    }

    private fun draw() {
        background.draw()
        sausage.draw(time)
        paws.draw()
        score.draw()
        touches.draw()
    }

    override fun render(delta: Float) {
        update(delta)
        clear()
        draw()
    }
}

class PlayScreenInputAdapter(private val screen: PlayScreen) : InputAdapter() {
    override fun touchDown(x: Int, y: Int, pointer: Int, button: Int): Boolean {
        // your touch down code here
        screen.handleTouch(x, y)
        return true // return true to indicate the event was handled
    }

    override fun touchUp(x: Int, y: Int, pointer: Int, button: Int): Boolean {
        // your touch up code here
        return true // return true to indicate the event was handled
    }
}
