package com.inspectorr.sausage.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.*
import com.inspectorr.sausage.utils.*
import kotlin.math.atan2
import kotlin.math.round

enum class PawState {
    MOVING_CENTER,
    MOVING_BACK_EMPTY,
    MOVING_BACK_KILL,
    PAUSE
}

val textures = listOf("paw1.png", "paw2.png", "paw3.png")

class Paw(private val batch: SpriteBatch, private val debugShapeRenderer: ShapeRenderer) {
    lateinit var position: Vector2
    private var delta: Vector2
    private var start: Vector2

    private val texture = TextureRegion(Texture(asset(textures[randomInt(3)])))
    private val textureWidth = texture.regionWidth.toFloat()
    private val textureHeight = texture.regionHeight.toFloat()

    private fun randomX() = randomInt(Gdx.graphics.width).toFloat() - Screen.RIGHT
    private fun randomY() = randomInt(Gdx.graphics.height).toFloat() - Screen.TOP

    private fun randomPosTop() = Vector2(randomX(), Screen.TOP)
    private fun randomPosRight() = Vector2(Screen.RIGHT, randomY())
    private fun randomPosBottom() = Vector2(randomX(), Screen.BOTTOM)
    private fun randomPosLeft() = Vector2(Screen.LEFT, randomY())

    val shape = Polygon()

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
            setScale(Screen.TEXTURE_SCALE*0.9f, Screen.TEXTURE_SCALE*0.9f)
        }
    }

    var state = PawState.MOVING_CENTER

    private fun action() {
        when (state) {
            PawState.MOVING_CENTER -> moveCenter()
            PawState.MOVING_BACK_EMPTY -> moveBack()
            PawState.MOVING_BACK_KILL -> moveBack()
            PawState.PAUSE -> pause()
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
    private val pauseLength = 0.1f
    private var pauseTimer = 0f
    private fun setPause() {
        isPaused = true
        state = PawState.PAUSE
        pauseTimer = pauseLength
    }

    private fun handlePauseTimer(delta: Float) {
        if (pauseTimer > 0f) pauseTimer -= delta
    }

    private fun pause() {
        if (pauseTimer <= 0f) {
            isPaused = false
            state = PawState.MOVING_BACK_KILL
        }
    }

    private val offset = relativeValue(10f)

    private fun triggerState() {
        if (state == PawState.MOVING_CENTER && !isPaused && distance(position, Screen.CENTER) <= offset) {
            setPause()
        }

        if (state == PawState.MOVING_BACK_EMPTY && isOutOfScreen(position)) {
            remove()
        }
    }

    fun onTouch() {
        println("paw touch")
        state = PawState.MOVING_BACK_EMPTY
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

    private val angle: Float get() = atan2(position.y, position.x)*(180/Math.PI).toFloat()
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

    fun onRemove(): Boolean {
        // todo эээ
        return true
    }
}