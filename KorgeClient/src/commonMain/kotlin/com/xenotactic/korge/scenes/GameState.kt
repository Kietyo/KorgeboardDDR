package com.xenotactic.korge.scenes

import com.soywiz.korau.sound.Sound
import com.soywiz.korev.Key
import com.xenotactic.ecs.Injections

class GameState(
    val sfxClap: Sound,
    val recentlyPressedKeys: MutableSet<Key> = mutableSetOf<Key>(),
    val keyToUIKey: MutableMap<Key, UIKey> = mutableMapOf<Key, UIKey>()
) {
}