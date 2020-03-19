package com.inspectorr.sausage.entities

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.inspectorr.sausage.Assets
import com.inspectorr.sausage.utils.randomFloat
import com.inspectorr.sausage.utils.randomInt
import kotlin.math.abs
import kotlin.math.sin

class Paws(private val camera: OrthographicCamera, assets: Assets) {
    private val batch = SpriteBatch()
    private val maskRenderer = ShapeRenderer()
    private val textureVariants = listOf(
            "paw1.png",
            "paw2.png",
            "paw3.png"
    ).map { assets.get(it, Texture::class.java) }

    var list = mutableListOf<Paw>()

    var restrictNewPaws = false

    private fun add() {
        if (restrictNewPaws) return
        list.add(Paw(batch, textureVariants[randomInt(3)]))
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
        get() = randomFloat(5f) + randomFloat(10f)*abs(sin(timer))
    private var pawTimer = 0f

    var progress = 0f

    fun update(delta: Float) {

//        batch.projectionMatrix = camera.combined
//        maskRenderer.projectionMatrix = camera.combined

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
        list.forEach { it.draw(timer) }
    }
}
