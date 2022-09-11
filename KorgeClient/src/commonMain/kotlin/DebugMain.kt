import com.soywiz.korau.sound.readSound
import com.soywiz.korge.Korge
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korio.async.runBlockingNoJs
import com.soywiz.korio.file.std.resourcesVfs
import kotlin.jvm.JvmStatic
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.max

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

            val channel1 = audioData.samples.get(1)

            println("channel1: ${channel1}")
            println("channel1.size: ${channel1.size}")

            val totalDuration = sound.length
            val samplesPerSecond = audioData.rate
            val bucketsPerSecond = 256
            val millisecondsPerBucket = 1000.0 / bucketsPerSecond
            val samplesPerBucket = samplesPerSecond / bucketsPerSecond.toDouble()
            val totalDurationSecondsFromSampleCalculation =
                audioData.totalSamples / samplesPerSecond
            val totalBuckets = channel1.size / samplesPerBucket

            println(
                """
                totalDuration: $totalDuration
                totalDurationFromSampleCalculation: $totalDurationSecondsFromSampleCalculation
                samplesPerSecond: $samplesPerSecond
                bucketsPerSecond: $bucketsPerSecond
                samplesPerBucket: $samplesPerBucket
                totalBuckets: $totalBuckets
                channel1.indices: ${channel1.indices}
            """.trimIndent()
            )

            val maxBuckets = DoubleArray(totalBuckets.toInt() + 100)
            val averageBuckets = DoubleArray(totalBuckets.toInt() + 100)

            for (i in channel1.indices) {
                val bucketIndex = i / samplesPerBucket.toInt()
                averageBuckets[bucketIndex] = averageBuckets[bucketIndex] + channel1[i]
                maxBuckets[bucketIndex] = max(maxBuckets[bucketIndex], channel1[i].toDouble())
            }

            println("Bucket sums")
            println(averageBuckets.toList())

            for (i in averageBuckets.indices) {
                averageBuckets[i] = averageBuckets[i] / samplesPerBucket
            }

            println("Bucket averages")
            println(averageBuckets.toList())

            val xOffsetDelta = 0.25
            var xOffset = 0.0
            var maxSample = 0.0
            val waveformContainer = container {
                val maxSample = averageBuckets.max()
                averageBuckets.forEach { sample ->
                    val height = sample.absoluteValue / maxSample * 1000.0
                    solidRect(xOffsetDelta, height) {
                        x = xOffset
                        y = -height / 2
                    }
                    xOffset += xOffsetDelta
                }

                centerYOnStage()
                println("Done drawing waveform")
            }

            println("maxSample: $maxSample")

            val line = solidRect(xOffsetDelta, maxSample, Colors.YELLOW) {
                centerYOn(waveformContainer)
            }

            val channel = sound.play()

            addUpdater {
                val currentMillis = channel.current.milliseconds
                val currentBucketIndex = currentMillis / millisecondsPerBucket
                waveformContainer.x = -xOffsetDelta * currentBucketIndex
            }

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