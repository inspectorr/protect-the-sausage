package com.inspectorr.sausage.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import kotlin.math.atan2
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.*
import com.inspectorr.sausage.utils.Screen
import com.inspectorr.sausage.utils.distance
import com.inspectorr.sausage.utils.isOutOfScreen
import com.inspectorr.sausage.utils.randomInt

class Paw(private val batch: SpriteBatch, val key: String, private val debugShapeRenderer: ShapeRenderer) {
    private lateinit var position: Vector2
    private var delta: Vector2
    private var start: Vector2

    private val texture = TextureRegion(Texture(Gdx.files.internal("4.png")))
    private val textureWidth = texture.regionWidth.toFloat()
    private val textureHeight = texture.regionHeight.toFloat()

    private val randomX = { randomInt(Gdx.graphics.width).toFloat() - Screen.RIGHT }
    private val randomY = { randomInt(Gdx.graphics.height).toFloat() - Screen.TOP }

    private val randomPosTop = { Vector2(randomX(), Screen.TOP) }
    private val randomPosRight = { Vector2(Screen.RIGHT, randomY()) }
    private val randomPosBottom = { Vector2(randomX(), Screen.BOTTOM) }
    private val randomPosLeft = { Vector2(Screen.LEFT, randomY()) }

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
            setScale(2f, 2f)
        }
    }

    enum class State {
        MOVING_CENTER,
        MOVING_BACK,
    }

    private var state = State.MOVING_CENTER

    private fun action() {
        when (state) {
            State.MOVING_CENTER -> moveCenter()
            State.MOVING_BACK -> moveBack()
        }
    }

    private val centerSpeed = 0.01f

    private fun moveCenter() {
        position.set(
                position.x + delta.x*centerSpeed,
                position.y + delta.y*centerSpeed
        )
    }

    private val backSpeed = 0.05f

    private fun moveBack() {
        position.set(
                position.x - delta.x*backSpeed,
                position.y - delta.y*backSpeed
        )
    }

    private fun triggerState() {
        println("distance ${distance(position, Screen.CENTER)}")
        if (distance(position, Screen.CENTER) < 20) {
            state = State.MOVING_BACK
        }
        if (isOutOfScreen(position)) {
            remove()
        }
    }

    fun onTouch() {
        println("paw touch")
        state = State.MOVING_BACK
    }

    private fun updateShape() {
        shape.apply {
            setPosition(position.x, position.y)
            rotation = angle
        }
    }

    fun update(delta: Float) {
        triggerState()
        action()
        updateShape()
    }

    private val angle: Float get() = atan2(position.y, position.x)*(180/Math.PI).toFloat()

    fun draw(camera: OrthographicCamera) {
        batch.draw(
             texture,
             shape.x, shape.y,
             shape.originX, shape.originY,
             textureWidth, textureHeight,
             shape.scaleX, shape.scaleY,
             shape.rotation
        )
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
}