package com.xenotactic.korge.scenes

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
    val graphics = graphics()
    val maxAbsoluteSample = 32768.0
    val minimumXOffsetDelta = clipWidth / bucketSamples.size

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


        graphics.scaledHeight = clipHeight
        graphics.scaledWidth = clipWidth

        println("Finished drawing shape")
    }

    fun redrawWaveform(xOffsetDelta: Double) {
        val actualXOffsetDelta = max(xOffsetDelta, minimumXOffsetDelta)
        val timeToDrawWaveform = measureTime {
            graphics.updateShape {
                var prevXOffset = 0.0
                var prevSample = 0.0
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

        println("maxAbsoluteSample: $maxAbsoluteSample, timeToDrawWaveform: $timeToDrawWaveform")

    }
 }