package com.inspectorr.sausage.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.inspectorr.sausage.utils.asset
import kotlin.math.roundToInt

const val FONT_SIZE = 10

fun relativeFontSize(value: Int) : Int {
    return (value * Gdx.graphics.density).roundToInt()
}

class Text(
        private val batch: SpriteBatch,
        initContent: String,
        private val generator: FreeTypeFontGenerator = FreeTypeFontGenerator(asset("fonts/fr73.ttf"))
) {
    private var font: BitmapFont
    private val parameter = FreeTypeFontGenerator.FreeTypeFontParameter()

    init {
        parameter.size = FONT_SIZE
        parameter.characters = initContent
        font = generator.generateFont(parameter)
    }

    val width: Float
        get() {
            val layout = GlyphLayout(font, parameter.characters)
            return layout.width
        }

    val height: Float
        get() {
            val layout = GlyphLayout(font, parameter.characters)
            return layout.height
        }

    var size: Int = relativeFontSize(FONT_SIZE)
        set(value) {
            field = relativeFontSize(value)
            update()
        }

    var content: String = initContent

    fun update() {
        font.dispose()
        parameter.size = size
        parameter.characters = content
        font = generator.generateFont(parameter)
    }

    fun draw(x: Float, y: Float) {
        font.draw(
                batch,
                parameter.characters,
                x, y
        )
    }

}



