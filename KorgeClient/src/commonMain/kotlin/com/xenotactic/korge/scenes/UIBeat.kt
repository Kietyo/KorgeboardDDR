package com.xenotactic.korge.scenes

import com.soywiz.korev.Key
import com.soywiz.korge.view.*
import com.soywiz.korim.color.MaterialColors

class UIBeat(
    gameState: GameState,
    val key: Key
) : Container() {
    init {
        val rect = solidRect(
            GameConstants.KEY_WIDTH,
            GameConstants.KEY_HEIGHT,
            color = MaterialColors.AMBER_400
        )
        val text = text(key.upperCaseString) {
            scaleWhileMaintainingAspect(ScalingOption.ByHeight(GameConstants.KEY_TEXT_HEIGHT))
            centerOn(rect)
        }
    }
}