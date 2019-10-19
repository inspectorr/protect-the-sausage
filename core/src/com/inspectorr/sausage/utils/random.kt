package com.inspectorr.sausage.utils

import java.util.*

val random = Random()

fun randomInt(maxInt: Int) : Int {
    return random.nextInt(maxInt)
}

fun randomFloat(maxFloat: Float = 1f) : Float  {
    return random.nextFloat()*maxFloat
}

fun randomString(length: Int = 10) : String {
    return (1..length).map { randomInt(9) }.joinToString { "$it" }
}
