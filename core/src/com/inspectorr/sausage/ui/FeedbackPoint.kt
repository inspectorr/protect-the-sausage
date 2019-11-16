package com.inspectorr.sausage.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.inspectorr.sausage.utils.Screen
import com.inspectorr.sausage.utils.glEnableAlpha
import com.inspectorr.sausage.utils.rgba
import java.util.*
import kotlin.math.pow

class FeedbackPoint(private val point: Vector2, private val shapeRenderer: ShapeRenderer) {
    private val feedbackLength = 0.3f
    private val initPointsCount = 75 * Gdx.graphics.density
    private val initRadius = 25 * Screen.TEXTURE_SCALE
    private val endRadius = 60 * Screen.TEXTURE_SCALE + initRadius
    private val minPointSize = 2 * Screen.TEXTURE_SCALE
    private val maxPointSize = 25 * Screen.TEXTURE_SCALE

    private val random = Random()

    var timeLeft = feedbackLength
    fun draw () {
        val progress = Interpolation.pow2In.apply(timeLeft / feedbackLength)

        var radius = (initRadius +(endRadius - initRadius)*(1-progress)).toInt()
        if (radius <= 1) radius = 1

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        glEnableAlpha()
        shapeRenderer.color = rgba(255f, 255f, 255f, progress)

        val pointsCount = (initPointsCount * progress).toInt()
        println(progress)

        for (i in 0..pointsCount) {
            val pointSize = minPointSize + random.nextInt(maxPointSize.toInt() - minPointSize.toInt()).toFloat()*progress

            val x = point.x + random.nextInt(radius*2) - radius - pointSize/2f
            val y = point.y + random.nextInt(radius*2) - radius - pointSize/2f

            if ((point.x-x).pow(2) + (point.y-y).pow(2) <= radius.toFloat().pow(2)) {
                shapeRenderer.rect(x, y, pointSize, pointSize)
            }
        }

        shapeRenderer.end()
    }
}