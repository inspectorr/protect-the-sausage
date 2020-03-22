package com.inspectorr.sausage.ui

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Vector2
import com.inspectorr.sausage.Assets
import com.inspectorr.sausage.utils.UserScreen
import com.inspectorr.sausage.utils.drawTextureRegionByShape

const val WIDTH = 256f
const val HEIGHT = 256f
const val PRESSED_TEXTURE_NAME = "button-state-middle-256.png"
const val RELEASED_TEXTURE_NAME = "button-state-start-256.png"

class PlayButton(
        private val batch: SpriteBatch,
        assets: Assets,
//        var x: Float = -WIDTH*UserScreen.TEXTURE_SCALE/2f,
//        var y: Float = UserScreen.BOTTOM/3f - HEIGHT*UserScreen.TEXTURE_SCALE/2f,
//        var x: Float = -WIDTH*UserScreen.TEXTURE_SCALE/2,
//        var y: Float = HEIGHT*UserScreen.TEXTURE_SCALE/2,
        var x: Float = -WIDTH*UserScreen.TEXTURE_SCALE/2,
        var y: Float = -HEIGHT*UserScreen.TEXTURE_SCALE/2,
        val onPress: () -> Unit
) {
    private var isPressed = false
    private var pressedTextureRegion = TextureRegion(assets.get(PRESSED_TEXTURE_NAME, Texture::class.java))
    private var releasedTextureRegion = TextureRegion(assets.get(RELEASED_TEXTURE_NAME, Texture::class.java))
    private val shape = Polygon()

    init {
        val initPosition = Vector2(x, y)
        shape.apply {
            setOrigin(0f, 0f)
            setPosition(initPosition.x, initPosition.y)
            vertices = floatArrayOf(
                    0f, 0f,
                    0f, HEIGHT,
                    WIDTH, HEIGHT,
                    WIDTH, 0f
            )
            setScale(UserScreen.TEXTURE_SCALE, UserScreen.TEXTURE_SCALE)
        }
    }

    private fun press() {
        if (isPressed) return
        isPressed = true
        onPress()
    }

    private fun release() {
        isPressed = false
    }

    fun handleTouchStart(x: Float, y: Float): Boolean {
        val isJustPressed = shape.contains(Vector2(x, y))
        if (isJustPressed) {
            press()
        }
        return isJustPressed
    }

    fun handleTouchEnd() {
//        release()
    }

    fun draw() {
        batch.begin()
        batch.drawTextureRegionByShape(
                if (isPressed) pressedTextureRegion
                else releasedTextureRegion,
                shape
        )
        batch.end()
    }
}