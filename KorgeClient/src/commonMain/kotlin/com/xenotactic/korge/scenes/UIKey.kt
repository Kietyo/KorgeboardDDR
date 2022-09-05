package com.xenotactic.korge.scenes

import com.soywiz.korev.Key
import com.soywiz.korge.input.keys
import com.soywiz.korge.view.*
import com.xenotactic.korge.scenes.GameConstants
import com.xenotactic.korge.scenes.upperCaseString

class UIKey(
    gameState: GameState,
    val key: Key
) : Container() {
    init {
        val bg = solidRect(
            GameConstants.KEY_WIDTH,
            GameConstants.KEY_HEIGHT, color = GameConstants.UNPRESSED_COLOR
        )
        val text = text(key.upperCaseString) {
            scaleWhileMaintainingAspect(ScalingOption.ByHeight(GameConstants.KEY_TEXT_HEIGHT))
            centerOn(bg)
        }
        keys {
            this.down(key) {
                bg.color = GameConstants.PRESSED_COLOR
                gameState.recentlyPressedKeys.add(key)
            }
            up(key) {
                bg.color = GameConstants.UNPRESSED_COLOR
            }
        }
    }
}