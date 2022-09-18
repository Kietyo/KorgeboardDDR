package com.xenotactic.korge.scenes

import com.soywiz.korge.input.DraggableInfo
import com.soywiz.korge.input.draggable
import com.soywiz.korge.view.ClipContainer
import com.soywiz.korge.view.graphics
import com.soywiz.korge.view.solidRect
import com.soywiz.korim.color.Colors
import com.soywiz.korma.geom.vector.StrokeInfo
import com.soywiz.korma.geom.vector.line
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

fun DraggableInfo.asString(): String {
    return """
        DraggableInfo(localDX: $localDX, localDY: $localDY, lastDx: $deltaDx, lastDy: $deltaDy, viewStartXY: $viewStartXY, viewPrevXY: $viewPrevXY, viewNextXY: $viewNextXY, viewDeltaXY: $viewDeltaXY)
    """.trimIndent()
}

@OptIn(ExperimentalTime::class)
class UIMonoAudioWaveform(
    val clipWidth: Double,
    val clipHeight: Double,
    val bucketSamples: DoubleArray,
) : ClipContainer(clipWidth, clipHeight) {
    val numBucketsToDraw = bucketSamples.size
    val waveformHalfHeight = clipHeight / 2.0

    val bg = solidRect(
        clipWidth,
        Short.MAX_VALUE.toDouble() * 2.0,
        Colors.BLACK
    )
    val graphics = graphics().apply {
        draggable() {
            println(it.asString())
            this@apply.y = 0.0
        }
    }
    val maxAbsoluteSample = 32768.0
    val minimumXOffsetDelta = clipWidth / bucketSamples.size
    var lastDrawnXOffsetDelta = 0.0

    init {

//        dockedTo(Anchor.LEFT)
//                val maxSample = averageBuckets.max()

//        val maxAbsoluteSample = bucketSamples.maxOf {
//            it.absoluteValue
//        }

        redrawWaveform(minimumXOffsetDelta)

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

        println("Went here")

//        graphics.scaledHeight = clipHeight
//        graphics.scaledWidth = clipWidth

        println("Finished drawing shape")


    }

    fun redrawWaveform(xOffsetDelta: Double) {
        val actualXOffsetDelta = max(xOffsetDelta, minimumXOffsetDelta)
        if (lastDrawnXOffsetDelta == actualXOffsetDelta) {
            // Already drawn
            return
        }
        val timeToDrawWaveform = measureTime {
            graphics.updateShape {
                var prevXOffset = 0.0
                var prevSample = waveformHalfHeight
                bucketSamples.forEach { sample ->
                    val nextXOffset = prevXOffset + actualXOffsetDelta
                    if (nextXOffset > clipWidth) {
                        return@forEach
                    }
                    val currentSample =
                        (if (sample.isNaN()) 0.0 else -sample) / maxAbsoluteSample * waveformHalfHeight
                    val finalSample = currentSample + waveformHalfHeight
                    stroke(Colors.RED, StrokeInfo(thickness = xOffsetDelta)) {
                        line(prevXOffset, prevSample, nextXOffset, finalSample)
                        prevXOffset = nextXOffset
                        prevSample = finalSample
                    }
                }
            }
        }

        lastDrawnXOffsetDelta = actualXOffsetDelta

        println("maxAbsoluteSample: $maxAbsoluteSample, timeToDrawWaveform: $timeToDrawWaveform")

    }
 }