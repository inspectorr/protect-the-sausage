package com.inspectorr.sausage.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20

fun rgba(r: Float, g: Float, b: Float, a: Float = 1f): Color {
    return Color(r/255f, g/255f, b/255f, a)
}

fun glEnableAlpha() {
    Gdx.gl.glEnable(GL20.GL_BLEND)
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
}

fun generateColor(max: Float = 255f, min: Float = 0f, alpha: Float = 1f) : Color {
    return rgba(
            min + randomFloat(max-min),
            min + randomFloat(max-min),
            min + randomFloat(max-min),
            alpha
    )
}