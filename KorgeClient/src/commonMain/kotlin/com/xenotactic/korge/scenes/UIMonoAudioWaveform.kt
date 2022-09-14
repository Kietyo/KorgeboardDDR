package com.xenotactic.korge.scenes

import com.soywiz.korge.component.docking.dockedTo
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.graphics
import com.soywiz.korim.color.Colors
import com.soywiz.korma.geom.Anchor
import com.soywiz.korma.geom.vector.StrokeInfo
import com.soywiz.korma.geom.vector.line

class UIMonoAudioWaveform(
    val waveformHeight: Double,
    val averageBuckets: DoubleArray,
    val xOffsetDelta: Double
) : Container() {
    init {

        var xOffset = 0.0

        dockedTo(Anchor.LEFT)
//                val maxSample = averageBuckets.max()


        val graphics = graphics {

        }

        graphics.updateShape {
            var prevXOffset = 0.0
            var prevSample = 0.0
            averageBuckets.take(5000).forEach { sample ->
                val currentSample = if (sample.isNaN()) 0.0 else sample
                stroke(Colors.WHITE, StrokeInfo(thickness = 1.0)) {
                    val nextXOffset = prevXOffset + xOffsetDelta
                    line(prevXOffset, prevSample, nextXOffset, currentSample)
                    prevXOffset = nextXOffset
                    prevSample = currentSample
                }
            }
        }

        println("Finished drawing shape")

//        averageBuckets.forEach { sample ->
//            solidRect(
//                xOffsetDelta,
//                -sample
//            ) {
//                x = xOffset
//            }
//            xOffset += xOffsetDelta
//        }

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
        scaledWidth = 1280.0
    }
}