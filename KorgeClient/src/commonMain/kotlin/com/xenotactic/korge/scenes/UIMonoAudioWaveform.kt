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
            solidRect(
                xOffsetDelta,
                -sample
            ) {
                x = xOffset
            }
            xOffset += xOffsetDelta
        }

//        averageBuckets.forEach { sample ->
//            val isNegative = sample < 0.0
//            val absoluteLogAmplitude = log(
//                max(sample.absoluteValue, 1.0),
//                10.0
//            )
//            solidRect(
//                xOffsetDelta,
//                if (isNegative) -absoluteLogAmplitude else absoluteLogAmplitude
//            ) {
//                x = xOffset
////                y = absoluteLogAmplitude + if (isNegative) -absoluteLogAmplitude else absoluteLogAmplitude
//            }
//            xOffset += xOffsetDelta
//        }

        scaledHeight = waveformHeight
        scaledWidth = 5000.0
    }
}