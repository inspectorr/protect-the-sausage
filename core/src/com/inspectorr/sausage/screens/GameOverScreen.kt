package com.inspectorr.sausage.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.inspectorr.sausage.Game
import com.inspectorr.sausage.Screens
import com.inspectorr.sausage.entities.Sausage
import com.inspectorr.sausage.ui.Text
import com.inspectorr.sausage.utils.Screen
import com.inspectorr.sausage.utils.glEnableAlpha
import com.inspectorr.sausage.utils.randomFloat
import com.inspectorr.sausage.utils.relativeValue

const val DURATION = 2.5f

class GameOverScreen(private val game: Game) : ScreenAdapter() {
    private val camera = OrthographicCamera(Screen.WIDTH, Screen.HEIGHT)

    private val batch = SpriteBatch()
    private val text = Text(batch, "GAME OVER")

    private val sausage = Sausage(camera)

    init {
        Gdx.input.inputProcessor = GameOverScreenInputAdapter(this)
        batch.projectionMatrix = camera.combined
        text.size = 40
        text.parameter.color = Color(1f, 1f, 1f, startAnimationProgress)
        sausage.apply {
            scale = 0.3f
            position = Vector2(
                    (randomFloat(Screen.WIDTH) - Screen.RIGHT)*0.7f,
                    (randomFloat(Screen.HEIGHT) - Screen.TOP)*0.7f
            )
            rotation = randomFloat(360f)
        }
    }

    override fun show() {

    }

    private fun clear() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    }

    private var timer = 0f

    private fun navigateToPlayScreen() {
        game.setScreen(Screens.PLAY)
    }

    private val posSpeedPx = relativeValue(20f)
    private val angleSpeed = 40f

    private fun update(delta: Float) {
        timer += delta
        if (timer > DURATION) {
            navigateToPlayScreen()
        }

        text.parameter.color = Color(1f, 1f, 1f, startAnimationProgress)

        sausage.scream(delta)
        sausage.position.add(-posSpeedPx*delta, 0f)
        sausage.rotation += angleSpeed*delta

        camera.update()
    }

    private val startAnimationProgress: Float
        get() {
            var progress = timer/1f
            if (progress > 1f) progress = 1f
            return Interpolation.pow2In.apply(progress)
        }

    private fun draw() {
        sausage.draw(timer)
        batch.apply {
            begin()
            glEnableAlpha()
            text.draw(
                    -text.width/2,
                    text.height/2
            )
            end()
        }
    }

    fun handleTouch() {
        navigateToPlayScreen()
    }

    override fun render(delta: Float) {
        update(delta)
        clear()
        draw()
    }
}

class GameOverScreenInputAdapter(private val screen: GameOverScreen) : InputAdapter() {
    override fun touchDown(x: Int, y: Int, pointer: Int, button: Int): Boolean {
        // your touch down code here
        screen.handleTouch()
        return true // return true to indicate the event was handled
    }

    override fun touchUp(x: Int, y: Int, pointer: Int, button: Int): Boolean {
        // your touch up code here
        return true // return true to indicate the event was handled
    }
}
