package com.xenotactic.korge.scenes

import com.soywiz.klock.Frequency
import com.soywiz.korev.Key
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korma.geom.Point
import com.soywiz.korma.geom.vector.StrokeInfo
import com.soywiz.korma.geom.vector.line
import com.xenotactic.korge.scenes.GameConstants.BOTTOM_ROW_OFFSET
import com.xenotactic.korge.scenes.GameConstants.HORIZONTAL_FALL_SPEED
import com.xenotactic.korge.scenes.GameConstants.VERTICAL_FALL_SPEED
import com.xenotactic.korge.scenes.GameConstants.HORIZONTAL_KEY_PADDING
import com.xenotactic.korge.scenes.GameConstants.KEYBOARD_DISTANCE_FROM_BOTTOM_OF_WINDOW
import com.xenotactic.korge.scenes.GameConstants.KEY_TEXT_HEIGHT
import com.xenotactic.korge.scenes.GameConstants.MIDDLE_ROW_OFFSET
import com.xenotactic.korge.scenes.GameConstants.UPDATES_PER_SECOND
import com.xenotactic.korge.scenes.GameConstants.VERTICAL_KEY_PADDING

class MainScene : Scene() {
    override suspend fun SContainer.sceneInit() {
        println("MainScene")
        val keyToUIKey = mutableMapOf<Key, UIKey>()
        val gameState = GameState(keyToUIKey = keyToUIKey)
        val keyboardContainer = container {
            val charRows = listOf("qwertyuiop", "asdfghjkl", "zxcvbnm")
            val rowContainers = mutableListOf<Container>()
            for (charRow in charRows) {
                rowContainers += container {
                    val viewRow = mutableListOf<View>()
                    for (char in charRow) {
                        val key = char.toKey()
                        val uiKey = UIKey(gameState, key).addTo(this)
                        keyToUIKey[key] = uiKey
                        viewRow += uiKey
                    }

                    viewRow.windowed(2) {
                        it[1].alignLeftToRightOf(it[0], padding = HORIZONTAL_KEY_PADDING)
                    }
                }
            }
            rowContainers.windowed(2) {
                it[1].alignTopToBottomOf(it[0], padding = VERTICAL_KEY_PADDING)
            }

//            rowContainers[1].centerXOn(rowContainers[0])
//            rowContainers[2].centerXOn(rowContainers[0])

            rowContainers[1].x += MIDDLE_ROW_OFFSET
            rowContainers[2].x += BOTTOM_ROW_OFFSET
        }

        keyboardContainer.centerXOnStage()
        keyboardContainer.alignBottomToBottomOf(
            this,
            padding = KEYBOARD_DISTANCE_FROM_BOTTOM_OF_WINDOW
        )

        val line = graphics {
            this.stroke(Colors.WHITE, StrokeInfo(3.0)) {
                line(Point(0.0, 0.0), Point(0.0, 300.0))
            }
            it.alignLeftToLeftOf(keyboardContainer)
            it.y += 50.0
        }

        val allChars = "abcdefghijklmnopqrstuvwxyz"
        val fallingRectsForKeys = mutableListOf<UIBeat>()



//        addFixedUpdater(TimeSpan(1500.0)) {
//            val randomChar = allChars.random().toKey()
//            fallingRectsForKeys += UIBeat(gameState, randomChar).addTo(this).apply {
//                alignLeftToLeftOf(keyToUIKey[randomChar]!!)
//            }
//        }

        val randomKey = allChars.random().toKey()
        fallingRectsForKeys += UIBeat(gameState, randomKey).addTo(this).apply {
            alignLeftToLeftOf(keyToUIKey[randomKey]!!)
        }

        val firstUIBeat = fallingRectsForKeys.first()
        val keyUI = keyToUIKey[firstUIBeat.key]!!

        val distanceY = keyUI.getPositionRelativeTo(this).y - firstUIBeat.y

        val numVerticalUpdates = distanceY / VERTICAL_FALL_SPEED
        val extrapolatedHorizontalDistanceToMatchNumUpdates = numVerticalUpdates * HORIZONTAL_FALL_SPEED

        val horizontalText = text(randomKey.upperCaseString) {
            scaleWhileMaintainingAspect(ScalingOption.ByHeight(KEY_TEXT_HEIGHT))
            centerYOn(line)
            x = line.x + extrapolatedHorizontalDistanceToMatchNumUpdates - width
        }


        addFixedUpdater(Frequency(UPDATES_PER_SECOND)) {
            fallingRectsForKeys.forEach {
                it.y += VERTICAL_FALL_SPEED
                if (it.y > height) it.removeFromParent()
            }

            horizontalText.x -= HORIZONTAL_FALL_SPEED

            fallingRectsForKeys.forEach {
                if (gameState.recentlyPressedKeys.contains(it.key)) {
                    val keyUi = keyToUIKey[it.key]!!
                    val keyUiPos = keyUi.getPositionRelativeTo(this)
                    if (it.y in (keyUiPos.y - keyUi.height)..(keyUiPos.y + keyUi.height)) {
                        it.removeFromParent()
                    }
                }
            }

            gameState.recentlyPressedKeys.clear()
        }

    }
}

