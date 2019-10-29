package com.inspectorr.sausage.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.inspectorr.sausage.entities.Background
import com.inspectorr.sausage.entities.FeedbackPoint
import com.inspectorr.sausage.entities.Paw
import com.inspectorr.sausage.entities.Sausage
import com.inspectorr.sausage.utils.Screen
import com.inspectorr.sausage.utils.randomString

class PlayScreen : ScreenAdapter() {
    private lateinit var batch: SpriteBatch
    private lateinit var shapeRenderer: ShapeRenderer
    private val camera = OrthographicCamera(Screen.WIDTH, Screen.HEIGHT)

    private lateinit var background: Background
    private lateinit var sausage: Sausage

    private val paws = mutableMapOf<String, Paw>()

    private var time = 0f

    override fun show() {
        initCanvas()
        initEntities()
    }

    private fun initCanvas() {
        batch = SpriteBatch()
        batch.projectionMatrix = camera.combined
        shapeRenderer = ShapeRenderer()
        shapeRenderer.projectionMatrix = camera.combined
    }

    private fun initEntities() {
        sausage = Sausage(batch)
        background = Background(camera)
        background.show()
        addPaw()
    }

    private fun addPaw() {
        return
        val key = randomString()
        paws[key] = Paw(batch, key, shapeRenderer)
    }

    private var pawTimer = 0f
    private val pawFreq = 2.5f
    private var pawsProgress = 0f

    private fun update(delta: Float) {
        time += delta

        // update before renderBatch
        pawsProgress = 0f

        pawTimer += delta
        if (pawTimer > pawFreq) {
            addPaw()
            pawTimer = 0f
        }

        paws.values.forEach {
            if (it.complete) {
                it.onRemove()
                paws.remove(it.key)
                return
            }
            it.update(delta)
            pawsProgress += it.progress
        }

        background.update(pawsProgress, time)

        sausage.apply {
            if (paws.values.isNotEmpty()) {
                scream(delta)
            } else {
                stopScreaming()
            }
        }

        updateTouches(delta)

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
        addFeedbackPoint(x, y)
    }

    private val feedbackPoints = mutableMapOf<String, FeedbackPoint>()

    private fun addFeedbackPoint(x: Float, y: Float) {
        val key = randomString()
        feedbackPoints[key] = FeedbackPoint(Vector2(x, y), shapeRenderer, key)
    }

    private fun updateTouches(delta: Float) {
        var removeKey = ""
        var shouldRemove = false
        feedbackPoints.values.forEach {
            it.timeLeft -= delta
            if (it.timeLeft <= 0f) {
                removeKey = it.key
                shouldRemove = true
            }
        }
        if (shouldRemove) feedbackPoints.remove(removeKey)
    }

    private fun drawTouchFeedback() {
        feedbackPoints.values.forEach {
            it.draw()
        }
    }

    private fun drawPaws() {
        paws.values.forEach { it.draw(camera) }
    }

    private fun clear() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    }

    private fun draw() {
        background.draw()
        sausage.draw(time)
        drawPaws()
        drawTouchFeedback()
    }

    override fun render(delta: Float) {
        update(delta)
        clear()
        draw()
    }
}