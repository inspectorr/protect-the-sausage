package com.inspectorr.sausage.utils

import com.badlogic.gdx.graphics.Color

fun rgba(r: Float, g: Float, b: Float, a: Float = 1f): Color {
    return Color(r/255f, g/255f, b/255f, a)
}