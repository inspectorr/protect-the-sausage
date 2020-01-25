package com.inspectorr.sausage.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.inspectorr.sausage.Assets
import com.inspectorr.sausage.utils.Screen
import com.inspectorr.sausage.utils.generateColor
import com.inspectorr.sausage.utils.glEnableAlpha
import java.util.*
import kotlin.math.pow

class FeedbackPoint(private val point: Vector2, private val shapeRenderer: ShapeRenderer, assets: Assets, hit: Boolean) {
    private var bzId: Long = 0L
    private var missId: Long = 0L
    private var clapId: Long = 0L
//    private var hitId: Long

    private val feedbackLength = 0.5f
    private val initPointsCount = 70 * Gdx.graphics.density
    private val initRadius = 25 * Screen.TEXTURE_SCALE
    private val endRadius = 100 * Screen.TEXTURE_SCALE + initRadius
    private val minPointSize = 4 * Screen.TEXTURE_SCALE
    private val maxPointSize = 35 * Screen.TEXTURE_SCALE

    private val random = Random()

    private val clapSound = assets.get("sounds/clap.wav", Sound::class.java)
    private val missSound = assets.get("sounds/miss.wav", Sound::class.java)
    private val bzSound = assets.get("sounds/bzz.wav", Sound::class.java)

    // todo wth id dd sdfiajoklgdrg

    init {
        if (hit) {
            clapId = clapSound.play(1.0f)
        } else {
            missId = missSound.play(1.0f)
        }

        bzId = bzSound.play(0.42f)
    }

    fun dispose() {
//        clapSound.stop(hitId)
//        missSound.stop(hitId)
//        bzSound.stop(bzId)
    }

    var timeLeft = feedbackLength
    fun draw () {
        val progress = Interpolation.sineIn.apply(timeLeft / feedbackLength)

        var radius = (initRadius +(endRadius - initRadius)*(1-progress)).toInt()
        if (radius <= 1) radius = 1

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        glEnableAlpha()
//        shapeRenderer.color = rgba(255f, 255f, 255f, progress)
        shapeRenderer.color = generateColor(alpha=progress, min = 0f)


        val pointsCount = (initPointsCount * progress).toInt()

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