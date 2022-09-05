package com.xenotactic.korge.scenes

import com.soywiz.korev.Key
import com.soywiz.korge.view.*
import com.xenotactic.korge.scenes.GameConstants.HORIZONTAL_BEAT_COLOR
import com.xenotactic.korge.scenes.GameConstants.HORIZONTAL_BEAT_WIDTH

data class BeatModel(
    val key: Key,
    val uiVerticalBeat: UIVerticalBeat,
    val uiHorizontalBeat: UIHorizontalBeat
) {
    fun removeFromParent() {
        uiVerticalBeat.removeFromParent()
        uiHorizontalBeat.removeFromParent()
    }
}

class UIVerticalBeat(
    gameState: GameState,
    val key: Key
) : Container() {
    init {
        val rect = solidRect(
            GameConstants.KEY_WIDTH,
            GameConstants.KEY_HEIGHT,
            color = GameConstants.VERTICAL_BEAT_COLOR
        )
        val text = text(key.upperCaseString) {
            scaleWhileMaintainingAspect(ScalingOption.ByHeight(GameConstants.KEY_TEXT_HEIGHT))
            centerOn(rect)
        }
    }
}

class UIHorizontalBeat(
    val key: Key
) : Container() {
    init {
        val rect = solidRect(HORIZONTAL_BEAT_WIDTH, GameConstants.HORIZONTAL_BEAT_TEXT_HEIGHT, HORIZONTAL_BEAT_COLOR)
        text(key.upperCaseString) {
            scaleWhileMaintainingAspect(ScalingOption.ByHeight(GameConstants.HORIZONTAL_BEAT_TEXT_HEIGHT))
            centerOn(rect)
        }
    }
}