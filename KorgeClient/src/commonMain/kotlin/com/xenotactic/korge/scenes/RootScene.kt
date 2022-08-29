package com.xenotactic.korge.scenes

import com.soywiz.klogger.Logger
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.SContainer
import com.soywiz.korge.view.Views
import com.soywiz.korge.view.centerOnStage
import com.soywiz.korge.view.text

class RootScene(
    override var views: Views
) : Scene() {
    override suspend fun SContainer.sceneInit() {
//        injector.mapPrototype {
//            MainScene()
//        }
//

        this.text("Loading...", textSize = 50.0).centerOnStage()

        sceneContainer.changeTo<MainScene>()
//        sceneContainer.changeTo<MapViewerScene>()
    }

    override suspend fun sceneAfterDestroy() {
        super.sceneAfterDestroy()
        logger.info {
            "sceneAfterDestroy called"
        }
    }

    override suspend fun sceneDestroy() {
        super.sceneDestroy()
        logger.info {
            "sceneDestroy called"
        }
    }

    companion object {
        val logger = Logger<RootScene>()
    }
}
