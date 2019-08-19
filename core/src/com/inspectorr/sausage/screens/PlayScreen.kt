package com.inspectorr.sausage.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.inspectorr.sausage.entities.Paw
import com.inspectorr.sausage.entities.Sausage
import com.inspectorr.sausage.utils.randomString

class PlayScreen : ScreenAdapter() {
    private lateinit var batch: SpriteBatch
    private val camera = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())

    private lateinit var sausage: Sausage

    private val paws = mutableMapOf<String, Paw>()

    private var time = 0f

    override fun show() {
        batch = SpriteBatch()
        batch.projectionMatrix = camera.combined
        sausage = Sausage(batch)
        addPaw()
    }

    private fun addPaw() {
        val key = randomString()
        paws[key] = Paw(batch, key)
    }

    private fun clear() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    }

    private var pawTimer = 0f
    private val pawFreq = 5f

    private fun update(delta: Float) {
        time += delta


        pawTimer += delta
        if (pawTimer > pawFreq) {
            addPaw()
            pawTimer = 0f
        }

        paws.values.forEach {
            if (it.complete) {
                paws.remove(it.key)
                return
            }

            it.update(delta)
        }

        if (paws.values.isNotEmpty()) {
            sausage.scream(delta)
        } else {
            sausage.stopScreaming()
        }

        print("paws count: ${paws.values.size}\n")

//        sausage.update(delta)

        camera.update()
    }

    private fun draw() {
        batch.begin()
        //

        sausage.draw(time)
        paws.values.forEach { it.draw(camera) }

        //
        batch.end()
    }

    override fun render(delta: Float) {
        update(delta)
        clear()
        draw()
    }

    override fun resize(width: Int, height: Int) {
//        viewport.update(width, height)
    }
}