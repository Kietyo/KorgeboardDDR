package com.xenotactic.korge.scenes

import com.soywiz.korau.sound.Sound
import com.soywiz.korev.Key
import com.xenotactic.ecs.FamilyConfiguration
import com.xenotactic.ecs.World

class GameWorld(
    val sfxClap: Sound,
    val windowWidth: Double,
    val windowHeight: Double,
) {
    val world = World()
    val beatsContainer = world.getComponentContainer<BeatComponent>()
    val verticalBeatsContainer = world.getComponentContainer<UIVerticalBeat>()
    val horizontalBeatsContainer = world.getComponentContainer<UIHorizontalBeat>()
    val recentlyPressedKeys: MutableSet<Key> = mutableSetOf<Key>()
    val horizontalBeatsFamily = world.getOrCreateFamily(FamilyConfiguration(
        allOfComponents = setOf(UIHorizontalBeat::class)
    ))
    val fallingBeats = mutableListOf<BeatModel>()
}