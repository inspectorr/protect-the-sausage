package com.inspectorr.sausage.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.inspectorr.sausage.entities.PawState
import com.inspectorr.sausage.ui.Background
import com.inspectorr.sausage.entities.Paws
import com.inspectorr.sausage.entities.Sausage
import com.inspectorr.sausage.ui.FeedbackPoints
import com.inspectorr.sausage.ui.Score
import com.inspectorr.sausage.utils.Screen

class PlayScreen : ScreenAdapter() {
    private val camera = OrthographicCamera(Screen.WIDTH, Screen.HEIGHT)

    private val background = Background(camera)
    private val sausage = Sausage(camera)
    private val paws = Paws(camera)
    private val score = Score(camera)
    private val touches = FeedbackPoints(camera)

    private var time = 0f

    override fun show() {
        paws.add()
    }

    private fun update(delta: Float) {
        time += delta

        background.update(0f, time)

        // todo refactor
        sausage.apply {
            if (paws.list.any { it.state != PawState.MOVING_BACK }) {
                scream(delta)
            } else {
                stopScreaming()
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
