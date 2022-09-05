package com.xenotactic.korge.scenes

import com.soywiz.korev.Key
import com.xenotactic.ecs.Injections

class GameState(
    val recentlyPressedKeys: MutableSet<Key> = mutableSetOf<Key>(),
    val keyToUIKey: MutableMap<Key, UIKey> = mutableMapOf<Key, UIKey>()
) {
}