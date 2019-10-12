package com.inspectorr.sausage.utils

import java.util.*

val random = Random()

fun randomInt(maxInt: Int) : Int {
    return random.nextInt(maxInt)
}

fun randomFloat() : Float  {
    return random.nextFloat()
}

const val length = 10
fun randomString(): String {
    return (1..length).map { randomInt(9) }.joinToString { "$it" }
}
