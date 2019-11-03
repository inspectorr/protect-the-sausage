package com.inspectorr.sausage.entities

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.inspectorr.sausage.ui.SwipePath
import com.inspectorr.sausage.utils.randomString

class Paws(private val camera: OrthographicCamera) {
    private val batch = SpriteBatch()
    private val shapeRenderer = ShapeRenderer()

    val entities = mutableMapOf<String, Paw>()

    private fun add() {
        val key = randomString()
        entities[key] = Paw(batch, key, shapeRenderer)
    }

    init {
        batch.projectionMatrix = camera.combined
        shapeRenderer.projectionMatrix = camera.combined
        add()
    }

    private val pawFreq = 2.5f
    private var pawTimer = 0f

    var progress = 0f

    fun update(delta: Float, path: SwipePath) {
        // update before renderBatch
        progress = 0f

        pawTimer += delta
        if (pawTimer > pawFreq) {
            add()
            pawTimer = 0f
        }

        // todo extract to Paws class
        entities.values.forEach {
            if (it.complete) {
                it.onRemove()
                entities.remove(it.key)
                return
            }

            if (path.original.size >= 2 &&
                    it.isIntersectsByLine(
                            path.original.first().p,
                            path.original.last().p
                    )) {
                it.onTouch()
            }

            it.update(delta)
            progress += it.progress
        }
    }

    fun draw() {
        entities.values.forEach { it.draw(camera) }
    }
}