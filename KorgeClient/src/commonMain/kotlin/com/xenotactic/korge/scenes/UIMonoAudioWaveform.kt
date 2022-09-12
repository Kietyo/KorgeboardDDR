package com.xenotactic.korge.scenes

import com.soywiz.korge.component.docking.dockedTo
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.solidRect
import com.soywiz.korma.geom.Anchor
import kotlin.math.absoluteValue
import kotlin.math.log
import kotlin.math.max

class UIMonoAudioWaveform(
    val waveformHeight: Double,
    val averageBuckets: DoubleArray
) : Container() {
    init {
        val xOffsetDelta = 0.25
        var xOffset = 0.0

        dockedTo(Anchor.LEFT)
//                val maxSample = averageBuckets.max()
        averageBuckets.forEach { sample ->
            val height = if (sample.absoluteValue == 0.0) 0.0 else log(
                max(sample.absoluteValue, 1.0),
                5.0
            )
            solidRect(xOffsetDelta, height) {
                x = xOffset
                y = -height / 2
            }
            xOffset += xOffsetDelta
        }

        scaledHeight = waveformHeight
    }
}