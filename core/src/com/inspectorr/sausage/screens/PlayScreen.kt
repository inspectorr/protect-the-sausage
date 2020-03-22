package com.inspectorr.sausage.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.inspectorr.sausage.Assets
import com.inspectorr.sausage.Game
import com.inspectorr.sausage.Screens
import com.inspectorr.sausage.entities.PawState
import com.inspectorr.sausage.ui.Background
import com.inspectorr.sausage.entities.Paws
import com.inspectorr.sausage.entities.SAUSAGE_WIDTH
import com.inspectorr.sausage.entities.Sausage
import com.inspectorr.sausage.ui.Fader
import com.inspectorr.sausage.ui.FeedbackPoints
import com.inspectorr.sausage.ui.Score
import com.inspectorr.sausage.utils.UserScreen
import com.inspectorr.sausage.utils.distance
import com.inspectorr.sausage.utils.isOutOfScreen
import com.inspectorr.sausage.utils.vibrate
import kotlin.math.sin

class PlayScreen(private val game: Game, assets: Assets) : ScreenAdapter() {
    private val camera = OrthographicCamera(UserScreen.WIDTH, UserScreen.HEIGHT)
    private val background = Background(camera)

    private val sausage = Sausage(camera, assets)
    private val paws = Paws(camera, assets)
    private val score = Score(camera)
    private val touches = FeedbackPoints(camera, assets, silent=false)
    private val fader = Fader(camera)

    init {
        game.playerScore = 0
        Gdx.input.inputProcessor = PlayScreenInputAdapter(this)
    }


    private var time = 0f

    override fun show() {
//        paws.add()
    }

    private val rotationSpeed = 30f

    private fun update(delta: Float) {
        time += delta

//        sausage.rotation = (delta*(sin(time*3))*rotationSpeed)
        sausage.rotation = sin(time)*rotationSpeed

        val killingPaw = paws.list.find { it.state == PawState.MOVING_BACK_KILL }
        if (killingPaw != null) {
            val distanceToStart = distance(sausage.position, killingPaw.start) / distance(killingPaw.start, Vector2(0f, 0f)).toFloat()
            fader.update(distanceToStart)
        }

//        background.update(dist, time)

        // todo refactor
        sausage.apply {
            val shouldScream = paws.list.any {
                if (it.state == PawState.MOVING_BACK_KILL) {
                    position = it.position
                    paws.restrictNewPaws = true
                }
                paws.restrictNewPaws = false
                it.state != PawState.MOVING_BACK_EMPTY
            }

            if (shouldScream) {
                scream(delta)
            } else {
                stopScreaming()
            }

            if (isOutOfScreen(position, SAUSAGE_WIDTH*scale)) {
                game.setScreen(Screens.GAME_OVER)
            }
        }

        paws.update(delta)

        touches.update(delta)

        camera.update()
    }

    fun handleTouch(screenX: Int, screenY: Int) {
        val touch = camera.unproject(Vector3(screenX.toFloat(), screenY.toFloat(), 0f))
        var hit = false
        paws.list.forEach {
            if (
                    it.shape.contains(touch.x, touch.y) &&
                    it.state != PawState.MOVING_BACK_KILL &&
                    it.state != PawState.MOVING_BACK_EMPTY
            ) {
                score.increment()
                game.playerScore = score.points
                it.onTouch()
                hit = true
            }
        }
        touches.add(touch.x, touch.y, hit)
        if (hit) {
            onHit()
        }
    }

    private fun onHit() {
        background.changeColor()
        vibrate(100)
    }

    private fun clear() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    }

    private fun draw() {
        background.draw()
        sausage.draw(time)
        paws.draw()
        fader.draw()
        score.draw()
        touches.draw()
    }

    override fun render(delta: Float) {
        update(delta)
        clear()
        draw()
    }

    override fun dispose() {
        sausage.dispose()
    }
}

class PlayScreenInputAdapter(private val screen: PlayScreen) : InputAdapter() {
    override fun touchDown(x: Int, y: Int, pointer: Int, button: Int): Boolean {
        // your touch down code here
        screen.handleTouch(x, y)
        return true // return true to indicate the event was handled
    }

    override fun touchUp(x: Int, y: Int, pointer: Int, button: Int): Boolean {
        // your touch up code here
        return true // return true to indicate the event was handled
    }
}
