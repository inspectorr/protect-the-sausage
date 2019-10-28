package com.inspectorr.sausage.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.inspectorr.sausage.Game

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration().apply {
            height = 1024
            width = 576
            resizable = false
            useGL30 = true
        }
        LwjglApplication(Game(), config)
    }
}
