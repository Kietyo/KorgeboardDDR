package com.xenotactic.korge.scenes

import com.soywiz.klock.DateTime
import com.soywiz.korev.Key

val Key.lowerCaseChar: Char get() = this.name.lowercase()[0]
val Key.lowerCaseString: String get() = this.name.lowercase()
val Key.upperCaseChar: Char get() = this.name[0]
val Key.upperCaseString: String get() = this.name
fun Char.toKey(): Key {
    return Key.valueOf(this.uppercase())
}

fun DateTime.Companion.nowUnixMillis() = nowUnixLong()