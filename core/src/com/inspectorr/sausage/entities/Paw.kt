package com.inspectorr.sausage.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import kotlin.math.atan2
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.*
import com.inspectorr.sausage.utils.*
import kotlin.math.PI
import kotlin.math.round

class Paw(private val batch: SpriteBatch, val key: String, private val maskRenderer: ShapeRenderer) {
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
            setScale(1f, 1f)
        }
    }

    enum class State {
        MOVING_CENTER,
        MOVING_CUT,
        MOVING_BACK,
        PAUSE
    }

    private var state = State.MOVING_CENTER

    private fun action() {
        when (state) {
            State.MOVING_CENTER -> moveCenter()
            State.MOVING_BACK -> moveBack()
            State.MOVING_CUT -> movingCut()
            State.PAUSE -> pause()
        }
    }

    private val centerSpeed = 0.01f
    private fun moveCenter() {
        position.add(delta.x*centerSpeed,delta.y*centerSpeed)
    }

//    private val backSpeed = 0.075f
    private val backSpeed = 0.01f
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
//        state = State.MOVING_BACK
    }

    // todo know about any high performance hardcore algos
    // todo at least do without vector
    fun isIntersectsByLine(start: Vector2, end: Vector2) : Boolean {
        if (state == State.MOVING_CUT) return false

        val polyline = floatToVector2(shape.transformedVertices)

        val intersectionPoints = List(polyline.size) { Vector2() }

        val isIntersectsLines = List(polyline.size) { index ->
            Intersector.intersectSegments(
                    start, end,
                    polyline[index],
                    polyline[if (index == polyline.size-1) 0 else index+1],
                    intersectionPoints[index]
            )
        }

        val intersectedLinesCount = isIntersectsLines.fold(0) {
            count, isIntersected->
            if (isIntersected) count + 1 else count
        }

        return if (intersectedLinesCount >= 2) {
            // cut paw
            cutPaw(intersectionPoints)
            true
        } else {
            false
        }
    }

    private val ear = EarClippingTriangulator()
    private var trgIndexes = ear.computeTriangles(shape.vertices)

    private fun cutPaw(intersectionPoints: List<Vector2>) {
        println("CUT PAW")
        state = State.MOVING_CUT

        val points = intersectionPoints.filter { it.x != 0f || it.y != 0f }
        if (points.size != 2) {
            println(points)
            println("wtf is wrong with this world")
            return
        }

        val start = points[0]
        val end = points[1]

        val vertices = floatToVector2(shape.transformedVertices)
        val clockwise = mutableListOf(start, end)
        val antiClockwise = mutableListOf(start, end)

        vertices.forEach {
            val determinant = determinant2x3(
                    start.x, start.y, end.x, end.y, it.x, it.y
            )
            if (determinant >= 0) clockwise.add(it)
            else antiClockwise.add(it)
        }

        val angle = shape.rotation * PI/180
        val origin = Vector2(shape.originX, shape.originY)
        val position = Vector2(shape.x, shape.y)

        val vertices1 = clockwise.map {
            val translated = Vector2(it.x - position.x, it.y - position.y)
            val rotated = rotatePoint(translated, angle.toFloat(), origin)
//            Vector2(rotated.x - position.x, rotated.y - position.y)
//            it
            rotated
        }

        println(vertices1)

        shape.vertices = vector2ToFloat(vertices1)
        trgIndexes = ear.computeTriangles(shape.vertices)
//        shape.vertices = vector2ToFloat(clockwise)
    }

    private fun movingCut() {
        moveBack()
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
        enableMasking()

        maskRenderer.apply {
            begin(ShapeRenderer.ShapeType.Filled)
            val vertices = shape.transformedVertices
            for (i in 0 until trgIndexes.size step 3) {
                triangle(
                        vertices[trgIndexes[i]*2],
                        vertices[trgIndexes[i]*2+1],
                        vertices[trgIndexes[i+1]*2],
                        vertices[trgIndexes[i+1]*2+1],
                        vertices[trgIndexes[i+2]*2],
                        vertices[trgIndexes[i+2]*2+1]
                )
            }
            end()
        }

        batch.apply {
            begin()
            applyMasking()
            draw(
                    texture,
                    shape.x, shape.y,
                    shape.originX, shape.originY,
                    textureWidth, textureHeight,
                    shape.scaleX, shape.scaleY,
                    shape.rotation
            )
            end()
        }

        disableMasking()

        maskRenderer.apply {
            begin(ShapeRenderer.ShapeType.Line)
            color = Color.CYAN
            polygon(shape.transformedVertices)
            color = Color.FIREBRICK
            polygon(shape.vertices)
            end()
        }

    }

    var complete = false
    val remove = { complete = true }

    fun onRemove() {

    }
}

private fun enableMasking() {
    //2. clear our depth buffer with 1.0
    Gdx.gl.glClearDepthf(1f)
    Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT)
    //3. set the function to LESS
    Gdx.gl.glDepthFunc(GL20.GL_LESS)
    //4. enable depth writing
    Gdx.gl.glEnable(GL20.GL_DEPTH_TEST)
    //5. Enable depth writing, disable RGBA color writing
    Gdx.gl.glDepthMask(true)
    Gdx.gl.glColorMask(false, false, false, false)
}

private fun applyMasking() {
    //8. Enable RGBA color writing
    //   (SpriteBatch.begin() will disable depth mask)
    Gdx.gl.glColorMask(true, true, true, true);
    //10. Now depth discards pixels outside our masked shapes
    Gdx.gl.glDepthFunc(GL20.GL_EQUAL)
}

private fun disableMasking() {
    Gdx.gl.glDisable(GL20.GL_DEPTH_TEST)
}