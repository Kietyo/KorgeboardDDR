package com.xenotactic.korge.scenes

import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.SContainer
import com.soywiz.korge.view.text

class MainScene : Scene() {
    override suspend fun SContainer.sceneInit() {
        text("Hello world!")
    }
}