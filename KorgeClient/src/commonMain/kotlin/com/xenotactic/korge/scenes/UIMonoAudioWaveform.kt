package com.xenotactic.korge.scenes

import com.soywiz.korge.input.DraggableInfo
import com.soywiz.korge.input.draggable
import com.soywiz.korge.view.ClipContainer
import com.soywiz.korge.view.cpuGraphics
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
    val allowDragging: Boolean = false
) : ClipContainer(clipWidth, clipHeight) {
    val numBucketsToDraw = bucketSamples.size
    val waveformHalfHeight = clipHeight / 2.0

    // Suppose that `requiredVisibleWaveform=200.0`
    // If you move the waveform to the left or right while dragging,
    // requires that at least 200.0 units are visible.
    // This is so you don't accidentally move it out too much that you can't see the
    // waveform anymore.
    val requiredVisibleWaveform = 200.0

    val minXMoveRightOffset = clipWidth - requiredVisibleWaveform

    val bg = solidRect(
        clipWidth,
        Short.MAX_VALUE.toDouble() * 2.0,
        Colors.BLACK
    )
    val graphics = graphics().also { graphics ->
        if (allowDragging) {
            graphics.draggable() {
                graphics.y = 0.0
                if (graphics.x >= minXMoveRightOffset) {
                    graphics.x = minXMoveRightOffset
                }
                val minXMoveLeftOffset = -(graphics.scaledWidth - requiredVisibleWaveform)
                if (graphics.x < minXMoveLeftOffset) {
                    graphics.x = minXMoveLeftOffset
                }
                println("Graphics(x=${graphics.x}, width=${graphics.scaledWidth})")
            }
        }
    }

    val maxAbsoluteSample = 32768.0
    val minimumXOffsetDelta = clipWidth / bucketSamples.size

    // Suppose that this is 0.75.
    // This value means that 75% of the waveform fits the window.
    var renderedZoomIn = 0.0

    init {

//        dockedTo(Anchor.LEFT)
//                val maxSample = averageBuckets.max()

//        val maxAbsoluteSample = bucketSamples.maxOf {
//            it.absoluteValue
//        }

        redrawWaveform(1.0)

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

    fun zoom(percentageToZoomIn: Double) {
//        if (percentageToZoomIn <= 0.5) {
//            if (renderedZoomIn > 0.5) {
//                redrawWaveform(percentageToZoomIn)
//            }
//        } else {
//            if (renderedZoomIn <= 0.5) {
//                redrawWaveform(percentageToZoomIn)
//            }
//        }
        val originalZoomInMultiplier = 1.0 / percentageToZoomIn
        graphics.scaleX = originalZoomInMultiplier
    }

    val actualXOffsetDelta get() =
        max(clipWidth / (bucketSamples.size * renderedZoomIn), minimumXOffsetDelta)

    fun redrawWaveform(percentageToZoomIn: Double) {
        if (renderedZoomIn == percentageToZoomIn) {
            // Already drawn
            return
        }

        renderedZoomIn = percentageToZoomIn

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
                    stroke(Colors.YELLOW, StrokeInfo(thickness = actualXOffsetDelta)) {
                        line(prevXOffset, prevSample, nextXOffset, finalSample)
                        prevXOffset = nextXOffset
                        prevSample = finalSample
                    }
                }
            }
        }

        renderedZoomIn = percentageToZoomIn

        println("maxAbsoluteSample: $maxAbsoluteSample, timeToDrawWaveform: $timeToDrawWaveform")

    }
}