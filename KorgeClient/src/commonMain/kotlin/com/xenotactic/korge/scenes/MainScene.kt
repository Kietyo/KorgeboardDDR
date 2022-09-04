package com.xenotactic.korge.scenes

import com.soywiz.korev.Key
import com.soywiz.korge.input.keys
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.*
import com.soywiz.korim.color.MaterialColors
import com.xenotactic.korge.scenes.GameConstants.HORIZONTAL_KEY_PADDING
import com.xenotactic.korge.scenes.GameConstants.KEYBOARD_DISTANCE_FROM_BOTTOM_OF_WINDOW
import com.xenotactic.korge.scenes.GameConstants.KEY_HEIGHT
import com.xenotactic.korge.scenes.GameConstants.KEY_TEXT_HEIGHT
import com.xenotactic.korge.scenes.GameConstants.KEY_WIDTH
import com.xenotactic.korge.scenes.GameConstants.PRESSED_COLOR
import com.xenotactic.korge.scenes.GameConstants.UNPRESSED_COLOR
import com.xenotactic.korge.scenes.GameConstants.VERTICAL_KEY_PADDING

class MainScene : Scene() {
    override suspend fun SContainer.sceneInit() {
        println("MainScene")

        val keyboardContainer = container {
            val charRows = listOf("qwertyuiop", "asdfghjkl", "zxcvbnm")
            val rowContainers = mutableListOf<Container>()
            for (charRow in charRows) {
                rowContainers += container {
                    val viewRow = mutableListOf<View>()
                    for (char in charRow) {
                        viewRow += UIKey(char.toKey()).addTo(this)
                    }

                    viewRow.windowed(2) {
                        it[1].alignLeftToRightOf(it[0], padding = HORIZONTAL_KEY_PADDING)
                    }
                }
            }
            rowContainers.windowed(2) {
                it[1].alignTopToBottomOf(it[0], padding = VERTICAL_KEY_PADDING)
            }

            rowContainers[1].centerXOn(rowContainers[0])
            rowContainers[2].centerXOn(rowContainers[0])

//        rowContainers[1].x += MIDDLE_ROW_OFFSET
//        rowContainers[2].x += BOTTOM_ROW_OFFSET
        }

        keyboardContainer.centerXOnStage()
        keyboardContainer.alignBottomToBottomOf(this, padding = KEYBOARD_DISTANCE_FROM_BOTTOM_OF_WINDOW)


    }
}

