package com.inspectorr.sausage.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.inspectorr.sausage.ui.Background
import com.inspectorr.sausage.entities.Paw
import com.inspectorr.sausage.entities.Paws
import com.inspectorr.sausage.entities.Sausage
import com.inspectorr.sausage.ui.Score
import com.inspectorr.sausage.ui.SwipePath
import com.inspectorr.sausage.utils.Screen
import com.inspectorr.sausage.utils.randomString

class PlayScreen : ScreenAdapter() {
    private val camera = OrthographicCamera(Screen.WIDTH, Screen.HEIGHT)

    private lateinit var background: Background
    private lateinit var sausage: Sausage
    private lateinit var score: Score
    private lateinit var paws: Paws

    private var time = 0f

    override fun show() {
        initEntities()
    }

    private fun initEntities() {
        sausage = Sausage(camera)
        background = Background(camera)
        score = Score(camera)
        paws = Paws(camera)
    }

    private fun update(delta: Float) {
        time += delta

        background.update(paws.progress, time)

        // todo refactor
        sausage.apply {
            if (paws.entities.values.isNotEmpty()) {
                scream(delta)
            } else {
                stopScreaming()
            }
        }

        path.update(delta)

        paws.update(delta, path)

        camera.update()
    }

    private val path = SwipePath(camera)

    fun handlePan(screenX: Float, screenY: Float) {
        val x = screenX - Gdx.graphics.width/2f
        val y = -screenY + Gdx.graphics.height/2f
        path.add(Vector2(x, y))
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
        path.draw()
    }

    override fun render(delta: Float) {
        update(delta)
        clear()
        draw()
    }
}
