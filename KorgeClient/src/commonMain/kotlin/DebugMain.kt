import com.soywiz.korau.sound.readSound
import com.soywiz.korge.Korge
import com.soywiz.korge.component.docking.dockedTo
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korio.async.runBlockingNoJs
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korio.util.endExclusiveClamped
import com.soywiz.korio.util.endExclusiveWrapped
import com.soywiz.korma.geom.Anchor
import com.soywiz.korma.geom.Point
import com.xenotactic.korge.scenes.UIMonoAudioWaveform
import kotlin.jvm.JvmStatic
import kotlin.math.*

object DebugMain {

    @JvmStatic
    fun main(args: Array<String>) = runBlockingNoJs {
        Korge(width = 1280, height = 720) {
            text("Hello world")

            val sound = resourcesVfs["bird_world.mp3"].readSound()
//            val channel = sound.play()
            val audioData = sound.toAudioData()

            println("audioData: ${audioData}")
            println("audioData.samples: ${audioData.samples}")

            val channel1 = audioData.samples[1]

            println("channel1: ${channel1}")
            println("channel1.size: ${channel1.size}")

            val totalDuration = sound.length
            val samplesPerSecond = audioData.rate
            val bucketsPerSecond = 256.0
            val millisecondsPerBucket = 1000.0 / bucketsPerSecond
            val samplesPerBucket = samplesPerSecond / bucketsPerSecond
            val samplesPerBucketInt = samplesPerBucket.toInt()
//            val totalBuckets = channel1.size / samplesPerBucketInt
            val totalBuckets = 4096

            println(
                """
                totalDuration: $totalDuration
                samplesPerSecond: $samplesPerSecond
                bucketsPerSecond: $bucketsPerSecond
                samplesPerBucket: $samplesPerBucket
                samplesPerBucketInt: $samplesPerBucketInt
                totalBuckets: $totalBuckets
                channel1.indices: ${channel1.indices}
                channel1.indices.last: ${channel1.indices.last}
                channel1.indices.endExclusiveWrapped: ${channel1.indices.endExclusiveWrapped}
                channel1.indices.endExclusiveClamped: ${channel1.indices.endExclusiveClamped}
                channel1.indices.endInclusive / samplesPerBucket: ${channel1.indices.last / samplesPerBucketInt}
            """.trimIndent()
            )

            val maxBuckets = DoubleArray(totalBuckets + 1)
            val minBuckets = DoubleArray(totalBuckets + 1)
            val averageBuckets = DoubleArray(totalBuckets + 1)
            val positiveAverageBuckets = DoubleArray(totalBuckets + 1)
            val positiveCountBuckets = IntArray(totalBuckets + 1)
            val negativeAverageBuckets = DoubleArray(totalBuckets + 1)
            val negativeCounterBuckets = IntArray(totalBuckets + 1)

            for (i in 0 until totalBuckets) {
                val bucketIndex = i / samplesPerBucketInt
                val sampleAsDouble = channel1[i].toDouble()
                averageBuckets[bucketIndex] = averageBuckets[bucketIndex] + channel1[i]
                maxBuckets[bucketIndex] = max(maxBuckets[bucketIndex], sampleAsDouble)
                minBuckets[bucketIndex] = min(minBuckets[bucketIndex], sampleAsDouble)
                if (sampleAsDouble >= 0.0) {
                    positiveAverageBuckets[bucketIndex] =
                        positiveAverageBuckets[bucketIndex] + sampleAsDouble
                    positiveCountBuckets[bucketIndex] = positiveCountBuckets[bucketIndex] + 1
                } else {
                    negativeAverageBuckets[bucketIndex] =
                        negativeAverageBuckets[bucketIndex] + sampleAsDouble
                    negativeCounterBuckets[bucketIndex] = negativeCounterBuckets[bucketIndex] + 1
                }
            }

//            println("Bucket sums")
//            println(averageBuckets.toList())

            for (i in averageBuckets.indices) {
                averageBuckets[i] = averageBuckets[i] / samplesPerBucket
            }

//            println("Bucket averages")
//            println(averageBuckets.toList())

            val waveformHeight = 100.0

//            val waveformContainer = UIMonoAudioWaveform(
//                waveformHeight,
//                averageBuckets
//            ).addTo(this) {
//                centerYOnStage()
//            }

            val fakeAverageBuckets = DoubleArray(averageBuckets.size)
            for (i in averageBuckets.indices) {
                fakeAverageBuckets[i] =
                    100.0 * sin(2 * PI * (millisecondsPerBucket * i + millisecondsPerBucket / 2.0))
            }

            val fakeWaveformContainer = UIMonoAudioWaveform(
                waveformHeight,
                fakeAverageBuckets
            ).addTo(this) {
                centerYOnStage()
            }


//            val xOffsetDelta = 0.25
//            var xOffset = 0.0
//            var maxSample = 0.0
//            val waveformContainer = container {
//                dockedTo(Anchor.LEFT)
////                val maxSample = averageBuckets.max()
//                averageBuckets.forEach { sample ->
//                    val height = if (sample.absoluteValue == 0.0) 0.0 else log(
//                        max(sample.absoluteValue, 1.0),
//                        5.0
//                    )
//                    solidRect(xOffsetDelta, height) {
//                        x = xOffset
//                        y = -height / 2
//                    }
//                    xOffset += xOffsetDelta
//                }
//
//                scaledHeight = waveformHeight
//
//                centerYOnStage()
//                println("Done drawing waveform")
//            }

//            val line = solidRect(5.0, waveformHeight, Colors.YELLOW) {
//                centerYOn(waveformContainer)
//            }
//
//            val channel = sound.play()
//
//            addUpdater {
//                val currentMillis = channel.current.milliseconds
//                val currentBucketIndex = currentMillis / millisecondsPerBucket
////                waveformContainer.x = -xOffsetDelta * currentBucketIndex
//
//                line.setPositionRelativeTo(
//                    waveformContainer,
//                    Point(xOffsetDelta * currentBucketIndex, 0.0)
//                )
//                line.centerYOn(waveformContainer)
//            }

//            sound.playAndWait { current, total ->
//
////                println("currentMillis: $currentMillis, currentBucketIndex: $currentBucketIndex")
////                line.x = xOffsetDelta * currentBucketIndex
//                waveformContainer.x = -xOffsetDelta * currentBucketIndex
//            }

        }
    }

    private fun abs(sh: Short): Short {
        if (sh < 0) return (-sh).toShort()
        return sh
    }
}