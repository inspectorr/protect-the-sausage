package com.inspectorr.sausage.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.inspectorr.sausage.entities.Paw
import com.inspectorr.sausage.entities.Sausage
import com.inspectorr.sausage.utils.randomString
import java.util.*
import kotlin.math.pow

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
    private val pawFreq = 1f

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

        updateTouches(delta)

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
        addFeedbackPoint(x, y)
    }

    private fun addFeedbackPoint(x: Float, y: Float) {
        val key = randomString()
        feedbackPoints[key] = FeedbackPoint(Vector2(x, y), shapeRenderer, key)
    }

    private val feedbackPoints = mutableMapOf<String, FeedbackPoint>()

    class FeedbackPoint(private val point: Vector2, private val shapeRenderer: ShapeRenderer, val key: String) {
        companion object {
            const val feedbackLength = 0.4f
            const val pointsCount = 200
            const val initRadius = 100
            const val maxPointSize = 7
        }

        private val random = Random()

        var timeLeft = feedbackLength
        fun draw () {
            val progress = timeLeft / feedbackLength

            var radius = (initRadius*progress).toInt()
            if (radius <= 1) radius = 1

            shapeRenderer.color = Color(255f, 0f, 0f, 0.5f)
            println("$progress\n")

            for (i in 0..pointsCount) {
                val pointSize = random.nextInt(maxPointSize).toFloat()

                val x = point.x + random.nextInt(radius*2) - radius - pointSize/2f
                val y = point.y + random.nextInt(radius*2) - radius - pointSize/2f

                if ((point.x-x).pow(2) + (point.y-y).pow(2) <= radius.toFloat().pow(2)) {
                    shapeRenderer.rect(x, y, pointSize, pointSize)
                }
            }
        }
    }

    private fun updateTouches(delta: Float) {
        var removeItem = false
        var removeKey = ""
        feedbackPoints.values.forEach {
            it.timeLeft -= delta
            if (it.timeLeft <= 0f) {
                removeItem = true
                removeKey = it.key
            }
        }
        if (removeItem) {
            feedbackPoints.remove(removeKey)
        }
    }

    private fun drawTouchFeedback() {
        feedbackPoints.values.forEach {
            it.draw()
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
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = Color.FIREBRICK

//        paws.values.forEach { it.debugDrawObjectBorder() }

        drawTouchFeedback()

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