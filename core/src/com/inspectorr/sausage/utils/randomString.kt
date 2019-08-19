package com.inspectorr.sausage.utils

import java.util.*

const val length = 10
val random = Random()

val randomString = { (1..length).map { random.nextInt(9) }.joinToString { "$it" } }