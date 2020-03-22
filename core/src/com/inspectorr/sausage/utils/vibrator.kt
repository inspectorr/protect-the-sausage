package com.inspectorr.sausage.utils

import com.badlogic.gdx.Gdx

var isOn = true

fun vibrate(ms: Int) {
    if (isOn) Gdx.input.vibrate(ms)
}

fun offVibrator() {
    isOn = false
}
fun onVibrator() {
    isOn = true
}
fun toggleVibrator() {
    isOn = !isOn
}