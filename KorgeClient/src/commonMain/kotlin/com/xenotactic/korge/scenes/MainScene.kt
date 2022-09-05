package com.xenotactic.korge.scenes

import com.soywiz.klock.Frequency
import com.soywiz.klock.TimeSpan
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
        val fallingBeats = mutableListOf<BeatModel>()

        val createBeatModelFn = {
            val randomKey = allChars.random().toKey()

            val keyUI = keyToUIKey[randomKey]!!
            val uiVerticalBeat = UIVerticalBeat(gameState, randomKey).addTo(this).apply {
                alignLeftToLeftOf(keyToUIKey[randomKey]!!)
                y = -500.0
            }
            val distanceY = keyUI.getPositionRelativeTo(this).y - uiVerticalBeat.y
            val numVerticalUpdates = distanceY / VERTICAL_FALL_SPEED
            val extrapolatedHorizontalDistanceToMatchNumVerticalUpdates =
                numVerticalUpdates * HORIZONTAL_FALL_SPEED
            val uiHorizontalBeat = UIHorizontalBeat(randomKey).addTo(this) {
                alignTopToTopOf(line)
                x = line.x + extrapolatedHorizontalDistanceToMatchNumVerticalUpdates - width / 2.0
            }
            BeatModel(randomKey, uiVerticalBeat, uiHorizontalBeat)
        }

        addFixedUpdater(TimeSpan(800.0)) {
            fallingBeats += createBeatModelFn()
        }

        addFixedUpdater(Frequency(UPDATES_PER_SECOND)) {
            fallingBeats.toList().forEach {
                it.uiVerticalBeat.y += VERTICAL_FALL_SPEED
                it.uiHorizontalBeat.x -= HORIZONTAL_FALL_SPEED
                if (it.uiVerticalBeat.y > height) {
                    it.removeFromParent()
                    fallingBeats.remove(it)
                }
            }

            fallingBeats.toList().forEach {
                if (gameState.recentlyPressedKeys.contains(it.key)) {
                    val keyUi = keyToUIKey[it.key]!!
                    val keyUiPos = keyUi.getPositionRelativeTo(this)
                    if (it.uiVerticalBeat.y in (keyUiPos.y - keyUi.height)..(keyUiPos.y + keyUi.height)) {
                        it.removeFromParent()
                        fallingBeats.remove(it)
                    }
                }
            }

            gameState.recentlyPressedKeys.clear()
        }

    }
}

