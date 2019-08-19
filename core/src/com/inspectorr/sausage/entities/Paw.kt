package com.inspectorr.sausage.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import java.util.Random
import kotlin.math.atan2
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Matrix4
import kotlin.math.pow

class Paw(private val batch: SpriteBatch, val key: String) {
    private lateinit var position: Vector2
    private var delta: Vector2
    private var start: Vector2

    private val texture = TextureRegion(Texture(Gdx.files.internal("4.png")))

    private val random = Random()
    private val movingToCenterDuration = 1f+random.nextFloat()
    private var timer = 0f

    private val top = Gdx.graphics.height/2f
    private val right = Gdx.graphics.width/2f
    private val bottom = -Gdx.graphics.height/2f
    private val left = -Gdx.graphics.width/2f

    val randomX = { random.nextInt(Gdx.graphics.width).toFloat() - right }
    val randomY = { random.nextInt(Gdx.graphics.height).toFloat() - top}

    val randomPosTop = { Vector2(randomX(), top) }
    val randomPosRight = { Vector2(right, randomY()) }
    val randomPosBottom = { Vector2(randomX(), bottom) }
    val randomPosLeft = { Vector2(left, randomY()) }

    init {
        when (random.nextInt(4)) {
            0 -> position = randomPosTop()
            1 -> position = randomPosRight()
            2 -> position = randomPosBottom()
            3 -> position = randomPosLeft()
        }

        start = Vector2(position.x, position.y)
        delta = Vector2(-position.x, -position.y)
    }

    var state = "center"
//    fun setStateValue(str: String) {
//        state = str
//    }

    val action = {
        when (state) {
            "center" -> moveToCenter()
            "back" -> moveBack()
            "complete" -> onRemove()
        }
    }

    private fun moveToCenter() {
        var progress = (timer / movingToCenterDuration).pow(2)

        if (timer >= movingToCenterDuration) {
            timer = 0f
            state = "back"
            return
        }

        position.set(
                start.x + delta.x*progress,
                start.y + delta.y*progress
        )
    }

    private fun moveBack() {
        var progress = (timer / 1f).pow(2)

        if (timer >= 1f) {
            timer = 0f
            state = "complete"
            return
        }

        position.set(
                start.x + delta.x - delta.x*progress,
                start.y + delta.y - delta.y*progress
        )
    }

    fun update(delta: Float) {
        timer += delta
        action()
    }

    private val angle: Float get() = atan2(position.y, position.x)*(180/Math.PI).toFloat()

    fun draw(camera: OrthographicCamera) {
        batch.draw(
             texture,
                position.x, position.y,
                0f, texture.regionHeight.toFloat()/2,
             texture.regionWidth.toFloat(), texture.regionHeight.toFloat(),
             2f, 2f,
             angle
        )
    }

    var complete = false
    val onRemove = { complete = true }
}