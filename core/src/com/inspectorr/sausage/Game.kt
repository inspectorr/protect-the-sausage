package com.inspectorr.sausage

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.Vector2
import com.inspectorr.sausage.screens.PlayScreen

class Game : ApplicationAdapter() {

    private lateinit var playScreen: PlayScreen

    override fun create() {
//        println(ImmediateModeRenderer20.createDefaultShader(false, true, 0))
//        println(ShaderProgram.TEXCOORD_ATTRIBUTE)
//        ShaderProgram.pedantic = false
//        ShaderProgram.prependVertexCode = "#version 140\n#define varying out\n#define attribute in\n";
//        ShaderProgram.prependFragmentCode = "#version 140\n#define varying in\n#define texture2D texture\n#define gl_FragColor fragColor\nout vec4 fragColor;\n";
        playScreen = PlayScreen()

        Gdx.input.inputProcessor = GestureDetector(object : GestureDetector.GestureListener {
            override fun touchDown(x: Float, y: Float, pointer: Int, button: Int): Boolean {

                return false
            }

            override fun tap(x: Float, y: Float, count: Int, button: Int): Boolean {

                return false
            }

            override fun longPress(x: Float, y: Float): Boolean {

                return false
            }

            override fun fling(velocityX: Float, velocityY: Float, button: Int): Boolean {

                return false
            }

            override fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float): Boolean {
                println("pan $x, $y, $deltaX, $deltaY")
                playScreen.handlePan(x, y)
                return false
            }

            override fun panStop(x: Float, y: Float, pointer: Int, button: Int): Boolean {

                return false
            }

            override fun zoom(originalDistance: Float, currentDistance: Float): Boolean {
                return false
            }

            override fun pinch(initialFirstPointer: Vector2, initialSecondPointer: Vector2, firstPointer: Vector2, secondPointer: Vector2): Boolean {

                return false
            }

            override fun pinchStop() {}
        })

    }

    override fun render() {
        val delta = Gdx.graphics.deltaTime
        playScreen.render(delta)
    }

    override fun dispose() {

    }
}
