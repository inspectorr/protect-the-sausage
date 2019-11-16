package com.inspectorr.sausage.entities

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.inspectorr.sausage.utils.randomFloat

class Paws(private val camera: OrthographicCamera) {
    private val batch = SpriteBatch()
    private val maskRenderer = ShapeRenderer()

    var list = mutableListOf<Paw>()

    fun add() {
        list.add(Paw(batch, ShapeRenderer()))
    }

    init {
        batch.projectionMatrix = camera.combined
        maskRenderer.projectionMatrix = camera.combined
    }

    private var timer = 0f

    private val minFreq: Float
        get() = 20f / timer

    // todo time function
    private val pawFreq: Float
        get() = 2f + randomFloat(5f)
//        get() = 0.2f + randomFloat(5f)

    private var pawTimer = 0f

    var progress = 0f

    fun update(delta: Float) {
        timer += delta

        progress = 0f

        pawTimer += delta
        if (pawTimer > pawFreq) {
            add()
            pawTimer = 0f
        }

        list.removeAll {
            progress += it.progress
            it.update(delta)
            it.complete && it.onRemove()
        }
    }

    fun draw() {
        list.forEach { it.draw(camera) }
    }
}