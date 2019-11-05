package com.inspectorr.sausage.utils

import com.badlogic.gdx.math.Vector2
import kotlin.math.cos
import kotlin.math.sin

fun floatToVector2(array: FloatArray) : MutableList<Vector2> {
    val list = mutableListOf<Vector2>()
    for (i in 0 until array.size step 2) {
        list.add(Vector2(array[i], array[i+1]))
    }
    return list
}

fun vector2ToFloat(list: List<Vector2>) : FloatArray {
    val array = FloatArray(list.size*2)
    for (i in 0 until list.size) {
        array[i*2] = list[i].x
        array[i*2+1] = list[i].y
    }
    return array
}

fun comparePointsFloat(p1x: Float, p2x: Float) : Int {
    return when {
        p1x > p2x -> 1
        p1x < p2x -> -1
        else -> 0
    }
}


fun determinant2x3(x1:Float,y1:Float,x2:Float,y2:Float,x3:Float,y3:Float): Float {
    return x1*y2+x2*y3+x3*y1-y1*x2-y2*x3-y3*x1
}

fun rotatePoint(point: Vector2, angle: Float, origin: Vector2): Vector2 {
    val s = sin(angle)
    val c = cos(angle)
    val untranslatedPoint = Vector2(point.x - origin.x, point.y - origin.y)
    val rotatedX = untranslatedPoint.x * c + untranslatedPoint.y * s
    val rotatedY = untranslatedPoint.x * s - untranslatedPoint.y * c
    return Vector2(rotatedX + origin.x, rotatedY + origin.y)
}