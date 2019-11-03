package com.inspectorr.sausage.utils

import com.badlogic.gdx.math.Vector2

fun floatToVector2(array: FloatArray) : MutableList<Vector2> {
    val list = mutableListOf<Vector2>()
    for (i in 0 until array.size step 2) {
        list.add(Vector2(array[i], array[i+1]))
    }
    return list
}