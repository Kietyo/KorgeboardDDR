package com.xenotactic.korge.scenes

import com.soywiz.klock.DateTime
import com.soywiz.klock.Frequency
import com.soywiz.klock.TimeSpan
import com.soywiz.korau.sound.readSound
import com.soywiz.korev.Key
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.Point
import com.soywiz.korma.geom.vector.StrokeInfo
import com.soywiz.korma.geom.vector.line
import com.xenotactic.ecs.World
import com.xenotactic.korge.scenes.GameConstants.BOTTOM_ROW_OFFSET
import com.xenotactic.korge.scenes.GameConstants.HORIZONTAL_BEAT_HEIGHT
import com.xenotactic.korge.scenes.GameConstants.HORIZONTAL_BEAT_VERTICAL_PADDING
import com.xenotactic.korge.scenes.GameConstants.HORIZONTAL_FALL_SPEED_PER_UPDATE
import com.xenotactic.korge.scenes.GameConstants.VERTICAL_FALL_SPEED_PER_UPDATE
import com.xenotactic.korge.scenes.GameConstants.HORIZONTAL_KEY_PADDING
import com.xenotactic.korge.scenes.GameConstants.KEYBOARD_DISTANCE_FROM_BOTTOM_OF_WINDOW
import com.xenotactic.korge.scenes.GameConstants.MIDDLE_ROW_OFFSET
import com.xenotactic.korge.scenes.GameConstants.UPDATES_PER_SECOND
import com.xenotactic.korge.scenes.GameConstants.VERTICAL_KEY_PADDING

class MainScene : Scene() {
    override suspend fun SContainer.sceneInit() {
        println("MainScene")
        val keyToUIKey = mutableMapOf<Key, UIKey>()
        val gameState = GameState(resourcesVfs["clap.mp3"].readSound(), keyToUIKey = keyToUIKey)
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
            it.y += 90.0
        }

        val allChars = "abcdefghijklmnopqrstuvwxyz"
        val fallingBeats = mutableListOf<BeatModel>()
        val startTimeMillis = DateTime.nowUnixMillis()

        val createBeatModelFn = { ch: Char ->
            val currentTimeMillis = DateTime.nowUnixMillis()
            val timeTillBeatMillis = 3000
            val beatTimeMillis = currentTimeMillis + timeTillBeatMillis

            val numUpdatesTillBeat = timeTillBeatMillis / 1000.0 * UPDATES_PER_SECOND

            val key = ch.toKey()

            val distanceHorizontalBeat = numUpdatesTillBeat * HORIZONTAL_FALL_SPEED_PER_UPDATE
            val uiHorizontalBeat = UIHorizontalBeat(key, beatTimeMillis).addTo(this) {
                x = line.x + distanceHorizontalBeat - width / 2.0
                alignTopToTopOf(line)
                while (fallingBeats.any {
                        collidesWith(it.uiHorizontalBeat)
                    }) {
                    y += HORIZONTAL_BEAT_HEIGHT + HORIZONTAL_BEAT_VERTICAL_PADDING
                }
            }

            val distanceVerticalBeat = numUpdatesTillBeat * VERTICAL_FALL_SPEED_PER_UPDATE
            val keyUI = keyToUIKey[key]!!
            val uiVerticalBeat = UIVerticalBeat(gameState, key, beatTimeMillis).addTo(this).apply {
                alignLeftToLeftOf(keyToUIKey[key]!!)
                y = keyUI.getPositionRelativeTo(this).y - distanceVerticalBeat
            }

            BeatModel(key, beatTimeMillis, uiVerticalBeat, uiHorizontalBeat)
        }

        val corpus = listOf(
            "cow",
            "assume",
            "therapist",
            "baby",
            "action",
            "stun",
            "buy",
            "flood",
            "negligence",
            "rich",
        ).joinToString(" ")
        val corpusIterator = corpus.iterator()

        val world = World().apply {
        }


        addFixedUpdater(TimeSpan(1000.0)) {
            if (!corpusIterator.hasNext()) return@addFixedUpdater
            val nextChar = corpusIterator.nextChar()
            if (nextChar != ' ') fallingBeats += createBeatModelFn(nextChar)
        }


        addFixedUpdater(Frequency(UPDATES_PER_SECOND)) {
            fallingBeats.toList().forEach {
                it.uiVerticalBeat.y += VERTICAL_FALL_SPEED_PER_UPDATE
                it.uiHorizontalBeat.x -= HORIZONTAL_FALL_SPEED_PER_UPDATE
                if (it.uiVerticalBeat.y > height) {
                    it.removeFromParent()
                    fallingBeats.remove(it)
                }
            }

            val currentTime = DateTime.nowUnixMillis()

            fallingBeats.toList().forEach {
                if (gameState.recentlyPressedKeys.contains(it.key)) {

                    val inside = currentTime in (it.timeMillis - 200)..(it.timeMillis + 200)
                    if (inside) {
                        it.removeFromParent()
                        fallingBeats.remove(it)
                    }

//                    val keyUi = keyToUIKey[it.key]!!
//                    val keyUiPos = keyUi.getPositionRelativeTo(this)
//                    if (it.uiVerticalBeat.y in (keyUiPos.y - keyUi.height)..(keyUiPos.y + keyUi.height)) {
//                        it.removeFromParent()
//                        fallingBeats.remove(it)
//                    }
                }
            }

            gameState.recentlyPressedKeys.clear()
        }

//        val sound = resourcesVfs["bird_world.mp3"].readSound()
//        val channel = sound.play()

    }
}

