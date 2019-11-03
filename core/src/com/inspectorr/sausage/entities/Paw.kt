package com.inspectorr.sausage.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import kotlin.math.atan2
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.*
import com.inspectorr.sausage.utils.*
import kotlin.math.round

class Paw(private val batch: SpriteBatch, val key: String, private val debugShapeRenderer: ShapeRenderer) {
    private lateinit var position: Vector2
    private var delta: Vector2
    private var start: Vector2

    private val texture = TextureRegion(Texture(Gdx.files.internal("4.png")))
    private val textureWidth = texture.regionWidth.toFloat()
    private val textureHeight = texture.regionHeight.toFloat()

    private fun randomX() = randomInt(Gdx.graphics.width).toFloat() - Screen.RIGHT
    private fun randomY() = randomInt(Gdx.graphics.height).toFloat() - Screen.TOP

    private fun randomPosTop() = Vector2(randomX(), Screen.TOP)
    private fun randomPosRight() = Vector2(Screen.RIGHT, randomY())
    private fun randomPosBottom() = Vector2(randomX(), Screen.BOTTOM)
    private fun randomPosLeft() = Vector2(Screen.LEFT, randomY())

    private val shape = Polygon()

    init {
        when (randomInt(4)) {
            0 -> position = randomPosTop()
            1 -> position = randomPosRight()
            2 -> position = randomPosBottom()
            3 -> position = randomPosLeft()
        }

        start = Vector2(position.x, position.y)
        delta = Vector2(-position.x, -position.y)

        shape.apply {
            setOrigin(0f, textureHeight/2)
            vertices = floatArrayOf(
                    0f, 0f,
                    0f, textureHeight,
                    textureWidth, textureHeight,
                    textureWidth, 0f
            )
            setScale(2f, 2f)
        }
    }

    enum class State {
        MOVING_CENTER,
        MOVING_BACK,
        PAUSE
    }

    private var state = State.MOVING_CENTER

    private fun action() {
        when (state) {
            State.MOVING_CENTER -> moveCenter()
            State.MOVING_BACK -> moveBack()
            State.PAUSE -> pause()
        }
    }

    private val centerSpeed = 0.01f
    private fun moveCenter() {
        position.add(delta.x*centerSpeed,delta.y*centerSpeed)
    }

    private val backSpeed = 0.075f
    private fun moveBack() {
        position.add(-delta.x*backSpeed,-delta.y*backSpeed)
    }

    private var isPaused = false
    private val pauseLength = 0.5f
    private var pauseTimer = 0f
    private fun setPause() {
        isPaused = true
        state = State.PAUSE
        pauseTimer = pauseLength
    }

    private fun handlePauseTimer(delta: Float) {
        if (pauseTimer > 0f) pauseTimer -= delta
    }

    private fun pause() {
        if (pauseTimer <= 0f) {
            isPaused = false
            state = State.MOVING_BACK
        }
    }

    private fun triggerState() {
        if (state == State.MOVING_CENTER && !isPaused && distance(position, Screen.CENTER) < 20) {
            setPause()
        }

        if (isOutOfScreen(position)) {
            remove()
        }
    }

    fun onTouch() {
        println("paw touch")
        state = State.MOVING_BACK
    }

    // todo know about any hardcore algos
    fun isIntersectsByLine(start: Vector2, end: Vector2) : Boolean {
        val polyline = floatToVector2(shape.transformedVertices)

        val intersectionPoints = List(4) { Vector2() }

        val isIntersectsLines = List(4) { index ->
            Intersector.intersectSegments(
                    start, end,
                    polyline[index],
                    polyline[if (index == 3) 0 else index+1],
                    intersectionPoints[index]
            )
        }

        val intersectedLinesCount = isIntersectsLines.fold(0) {
            count, isIntersected->
            if (isIntersected) count + 1 else count
        }

//        intersectionPoints.forEach { println(it) }
//        println("\n")

        return intersectedLinesCount >= 2
    }

    private fun updateShape() {
        shape.apply {
            setPosition(position.x, position.y)
            rotation = angle
        }
    }

    fun update(delta: Float) {
        handlePauseTimer(delta)
        triggerState()
        action()
        updateShape()
    }

    val angle: Float get() = atan2(position.y, position.x)*(180/Math.PI).toFloat()
    val progress: Float get() {
        val posToDelta = position.x / delta.x
        val progress = if (posToDelta + 1 > 0f) posToDelta + 1 else 0f
        return round(progress * 100f) / 100f
    }

    fun draw(camera: OrthographicCamera) {
        batch.begin()
        batch.draw(
             texture,
             shape.x, shape.y,
             shape.originX, shape.originY,
             textureWidth, textureHeight,
             shape.scaleX, shape.scaleY,
             shape.rotation
        )
        batch.end()
    }

    fun debugDrawObjectBorder() {
//        val vertices = shape.transformedVertices
//        debugShapeRenderer.color = Color.CYAN
//        debugShapeRenderer.polygon(vertices)
//        debugShapeRenderer.circle(position.x, position.y, 100f)
//        debugShapeRenderer.rect(100f, 100f, 100f, 100f)
    }

    var complete = false
    val remove = { complete = true }

    fun onRemove() {

    }
}