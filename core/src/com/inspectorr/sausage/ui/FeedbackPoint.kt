package com.inspectorr.sausage.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.inspectorr.sausage.Assets
import com.inspectorr.sausage.utils.UserScreen
import com.inspectorr.sausage.utils.generateColor
import com.inspectorr.sausage.utils.glEnableAlpha
import java.util.*
import kotlin.math.pow

class FeedbackPoint(
        private val point: Vector2,
        private val shapeRenderer: ShapeRenderer,
        assets: Assets,
        private val hit: Boolean,
        private val silent: Boolean
) {
    private var bzId: Long = 0L
    private var missId: Long = 0L
    private var clapId: Long = 0L

//    private val feedbackLength = 0.75f
    private val feedbackLength = 10f
    private val initPointsCount = 50 * Gdx.graphics.density
    private val initRadius = 25 * UserScreen.TEXTURE_SCALE
    private val endRadius = 175 * UserScreen.TEXTURE_SCALE + initRadius
    private val minPointSize = 3 * UserScreen.TEXTURE_SCALE
    private val maxPointSize = 45 * UserScreen.TEXTURE_SCALE

    private val random = Random()

    private val clapSound = assets.get("sounds/clap.wav", Sound::class.java)
    private val missSound = assets.get("sounds/miss.wav", Sound::class.java)
    private val bzSound = assets.get("sounds/bzz.wav", Sound::class.java)

    // todo wth id dd sdfiajoklgdrg

    init {
        playSounds()
    }

    private fun playSounds() {
        if (silent) return

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
        shapeRenderer.color = generateColor(alpha=progress, min=0f)


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