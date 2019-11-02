package com.inspectorr.sausage.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2

class SwipePath(camera: OrthographicCamera) {
    private val renderer = ShapeRenderer()
    private var original = mutableListOf<Point>()
    private var drawing = mutableListOf<Vector2>()

    init {
        renderer.projectionMatrix = camera.combined
    }

    class Point(val p: Vector2, var timer: Float = 0.1f)

    fun add(p: Vector2) {
        original.add(Point(p))
    }

    fun update(delta: Float) {
        if (original.size > 0) println(original[0].timer)
        original.forEach { it.timer -= delta }
        original.removeAll { it.timer <= 0f }
        drawing = resolve(original.map { it.p }.toMutableList())
    }

    fun draw() {
        renderer.apply {
            begin(ShapeRenderer.ShapeType.Line)
            color = Color.WHITE
            if (drawing.size >= 2) {
                var prevVector = drawing[0]
                for (i in 1 until drawing.size) {
                    line(prevVector, drawing[i])
                    prevVector = drawing[i]
                }
            }
            end()
        }
    }
}

fun simplify(points: MutableList<Vector2>, sqTolerance: Float) : MutableList<Vector2> {
    val output = mutableListOf<Vector2>()
    val len = points.size

    var point = Vector2()
    var prevPoint = points[0]

    output.add(prevPoint)

    for (i in 1 until len) {
        point = points[i]
        if (distSq(point, prevPoint) > sqTolerance) {
            output.add(point)
            prevPoint = point
        }
    }
    if (prevPoint != point) {
        output.add(point)
    }

    return output
}

fun distSq(p1: Vector2, p2: Vector2): Float {
    val dx = p1.x - p2.x
    val dy = p1.y - p2.y
    return dx * dx + dy * dy
}

fun smooth(input: MutableList<Vector2>) : MutableList<Vector2> {
    val output = mutableListOf<Vector2>()

    //first element
    output.add(input[0])
    //average elements
    for (i in 0 until input.size-1) {
        val p0 = input[i]
        val p1 = input[i + 1]

        val Q = Vector2(0.75f * p0.x + 0.25f * p1.x, 0.75f * p0.y + 0.25f * p1.y)
        val R = Vector2(0.25f * p0.x + 0.75f * p1.x, 0.25f * p0.y + 0.75f * p1.y)
        output.add(Q)
        output.add(R)
    }

    //last element
    output.add(input[input.size - 1])

    return output
}

const val ITERATIONS = 2
const val SIMPLIFY_TOLERANCE = 35f

fun resolve(input: MutableList<Vector2>) : MutableList<Vector2> {
    var output = input

    if (input.size <= 2) {
        return output
    }

    output = simplify(input, SIMPLIFY_TOLERANCE)

    for (i in 0 until ITERATIONS) {
        output = smooth(output)
    }

    return output
}