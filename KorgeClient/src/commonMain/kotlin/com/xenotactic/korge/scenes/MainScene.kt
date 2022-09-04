package com.xenotactic.korge.scenes

import com.soywiz.korev.Key
import com.soywiz.korge.input.keys
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.*
import com.soywiz.korim.color.MaterialColors

val Key.lowerCaseChar: Char get() = this.name.lowercase()[0]
val Key.lowerCaseString: String get() = this.name.lowercase()
val Key.upperCaseChar: Char get() = this.name[0]
val Key.upperCaseString: String get() = this.name
fun Char.toKey(): Key {
    return Key.valueOf(this.uppercase())
}

class MainScene : Scene() {
    override suspend fun SContainer.sceneInit() {
        println("MainScene")
        val UNPRESSED_COLOR = MaterialColors.CYAN_400
        val PRESSED_COLOR = MaterialColors.CYAN_600
        val HORIZONTAL_KEY_PADDING = 6.0
        val VERTICAL_KEY_PADDING = 6.0
        val KEY_HEIGHT = 50.0
        val KEY_WIDTH = 120.0
        val MIDDLE_ROW_OFFSET = 30.0
        val BOTTOM_ROW_OFFSET = 60.0
        val KEY_TEXT_HEIGHT = 35.0
        val KEYBOARD_DISTANCE_FROM_BOTTOM_OF_WINDOW = 20.0

        val keyboardContainer = container {

            val charRows = listOf("qwertyuiop", "asdfghjkl", "zxcvbnm")
            val rowContainers = mutableListOf<Container>()
            for (charRow in charRows) {
                rowContainers += container {
                    val viewRow = mutableListOf<View>()
                    for (char in charRow) {
                        val key = char.toKey()
                        viewRow += container {
                            val bg = solidRect(KEY_WIDTH, KEY_HEIGHT, color = UNPRESSED_COLOR)
                            val text = text(key.upperCaseString) {
                                scaleWhileMaintainingAspect(ScalingOption.ByHeight(KEY_TEXT_HEIGHT))
                                centerOn(bg)
                            }
                            keys {
                                this.down(key) {
                                    bg.color = PRESSED_COLOR
                                }
                                up(key) {
                                    bg.color = UNPRESSED_COLOR
                                }
                            }
                        }
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