package com.inspectorr.sausage.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.inspectorr.sausage.Assets
import com.inspectorr.sausage.Game
import com.inspectorr.sausage.Screens
import com.inspectorr.sausage.entities.Sausage
import com.inspectorr.sausage.ui.*
import com.inspectorr.sausage.utils.*
import java.util.*
import kotlin.concurrent.schedule

const val DURATION = 10f
//const val MIN_DURATION = 0f
const val MIN_DURATION = 2f

class GameOverScreen(private val game: Game, scoreCount: Int, assets: Assets) : ScreenAdapter() {
    private val camera = OrthographicCamera(UserScreen.WIDTH, UserScreen.HEIGHT)

    private val batch = SpriteBatch()
    private val sound = assets.get("sounds/gameover.mp3", Sound::class.java)

    private val gameOverText = Text(batch, "GAME OVER")
    private var highscoreText: Text

    private val sausage = Sausage(camera, assets)
    private val score = Score(camera, scoreCount)
    private val touches = FeedbackPoints(camera, assets, silent=true)

    private val navigateToPlayScreen = {
        println("NAVIGATING")
        game.setScreen(Screens.PLAY)
    }

    private val onEnd = {
        fader.fadeOut()
    }

    private val playButton = PlayButton(batch, assets, onPress = onEnd)

    private val fader = Fader(camera,
            fadeInCallback = {},
            fadeOutCallback = navigateToPlayScreen,
            defaultAlpha = 0f
    )

    fun handleTouch(screenX: Int, screenY: Int) {
        val touch = camera.unproject(Vector3(screenX.toFloat(), screenY.toFloat(), 0f))
        println("touch x=${touch.x} y=${touch.y}")
        val isButtonTouched = playButton.handleTouchStart(touch.x, touch.y)
        if (!isButtonTouched) touches.add(touch.x, touch.y, false)
    }

    fun handleTouchEnd(screenX: Int, screenY: Int) {
        playButton.handleTouchEnd()
    }

    init {
        Gdx.input.inputProcessor = GameOverScreenInputAdapter(this)
        batch.projectionMatrix = camera.combined

        setHighscore(score.points)
        highscoreText = Text(batch, "HIGHSCORE: ${getHighscore()}").apply {
            size = 20
            parameter.color = Color(1f, 1f, 1f, 1f)
        }

        gameOverText.apply {
            size = 40
            parameter.color = Color(1f, 1f, 1f, startAnimationProgress)
        }

        sausage.apply {
            scale = 0.3f
            position = Vector2(
                    (randomFloat(UserScreen.WIDTH) - UserScreen.RIGHT)*0.7f,
                    (randomFloat(UserScreen.HEIGHT) - UserScreen.TOP)*0.7f
            )
            rotation = randomFloat(360f)
            screamingLevel = 0f
            state = Sausage.State.SCREAMING
        }
    }

    override fun show() {
        sound.play(0.2f)
        fader.fadeIn()
    }

    private fun clear() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    }

    private var timer = 0f

    private val posSpeedPx = relativeValue(20f)
    private val angleSpeed = 40f

    private fun update(delta: Float) {
        timer += delta
        if (timer > DURATION) {
            navigateToPlayScreen()
        }

        gameOverText.parameter.color = Color(1f, 1f, 1f, startAnimationProgress)

        sausage.position.add(-posSpeedPx*delta, 0f)
        sausage.rotation += angleSpeed*delta

        touches.update(delta)

        camera.update()

        fader.update(delta)
    }

    private val startAnimationProgress: Float
        get() {
            var progress = timer/1f
            if (progress > 1f) progress = 1f
            return Interpolation.pow2In.apply(progress)
        }

    private fun drawHighscore() {
        batch.apply {
            begin()
            glEnableAlpha()
            highscoreText.draw(
                    -highscoreText.width/2,
                    UserScreen.TOP - UserScreen.HEIGHT/5
            )
            end()
        }

    }

    private fun drawGameOver() {
        batch.apply {
            begin()
            glEnableAlpha()
            gameOverText.apply {
                draw(-width/2, UserScreen.TOP/3 + height/2)
            }
            end()
        }
    }

    private fun draw() {
        drawHighscore()
        score.draw()
        playButton.draw()
        drawGameOver()
        sausage.draw(timer)
        fader.draw()
        touches.draw()
    }

    override fun render(delta: Float) {
        update(delta)
        clear()
        draw()
    }

    override fun dispose() {
        sausage.dispose()
        sound.stop()
    }
}

class GameOverScreenInputAdapter(private val screen: GameOverScreen) : InputAdapter() {
    override fun touchDown(x: Int, y: Int, pointer: Int, button: Int): Boolean {
        // your touch down code here
        screen.handleTouch(x, y)
        return true // return true to indicate the event was handled
    }

    override fun touchUp(x: Int, y: Int, pointer: Int, button: Int): Boolean {
        // your touch up code here
        screen.handleTouchEnd(x, y)
        return true // return true to indicate the event was handled
    }
}
