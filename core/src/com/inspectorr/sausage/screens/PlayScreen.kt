package com.inspectorr.sausage.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.inspectorr.sausage.entities.Paw
import com.inspectorr.sausage.entities.Sausage
import com.inspectorr.sausage.utils.randomString

class PlayScreen : ScreenAdapter() {
    private lateinit var batch: SpriteBatch
    private lateinit var shapeRenderer: ShapeRenderer
    private val camera = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())

    private lateinit var sausage: Sausage

    private val paws = mutableMapOf<String, Paw>()

    private var time = 0f

    override fun show() {
        batch = SpriteBatch()
        shapeRenderer = ShapeRenderer()
        batch.projectionMatrix = camera.combined
        shapeRenderer.projectionMatrix = camera.combined
        sausage = Sausage(batch)
        addPaw()
    }

    private fun addPaw() {
        val key = randomString()
        paws[key] = Paw(batch, key, shapeRenderer)
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

//        print("paws count: ${paws.values.size}\n")

//        sausage.update(delta)

        camera.update()
    }

    fun handleTouch(screenX: Int, screenY: Int) {
        val x = screenX - Gdx.graphics.width/2f
        val y = -screenY + Gdx.graphics.height/2f
        println("x: $x, y: $y")
        paws.values.forEach {
            if (it.shape.contains(x, y)) {
                it.onTouch()
            }
        }
    }

    private fun draw() {
        batch.begin()
        //

        sausage.draw(time)
        paws.values.forEach { it.draw(camera) }

        //
        batch.end()
    }

    private fun debugDrawObjectBorders() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        shapeRenderer.color = Color.FIREBRICK
        paws.values.forEach { it.debugDrawObjectBorder() }
        shapeRenderer.end()
    }

    override fun render(delta: Float) {
        update(delta)
        clear()
        draw()
        debugDrawObjectBorders()
    }

    override fun resize(width: Int, height: Int) {
//        viewport.update(width, height)
    }
}