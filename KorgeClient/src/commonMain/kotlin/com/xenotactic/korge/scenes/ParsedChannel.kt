package com.xenotactic.korge.scenes

import com.soywiz.korio.util.endExclusiveClamped
import com.soywiz.korio.util.endExclusiveWrapped
import kotlin.math.PI
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

class ParsedChannel(
    val samples: ShortArray,
    val samplesPerSecond: Int,
    val bucketsPerSecond: Double
) {
    val millisecondsPerBucket = 1000.0 / bucketsPerSecond

    val samplesPerBucketInt = (samplesPerSecond / bucketsPerSecond).toInt()
    val totalBuckets = samples.size / samplesPerBucketInt
    val totalSamples = samples.size
    val durationSeconds = totalSamples / samplesPerSecond.toDouble()

    // using avg to decide on min and max
    val resultBuckets1 = DoubleArray(totalBuckets + 1)
    // using avg
    val resultBuckets2 = DoubleArray(totalBuckets + 1)

    init {
        println(
            """
                durationSeconds: $durationSeconds
                samplesPerSecond: $samplesPerSecond
                bucketsPerSecond: $bucketsPerSecond
                samplesPerBucketInt: $samplesPerBucketInt
                totalBuckets: $totalBuckets
                samples.indices: ${samples.indices}
                samples.indices.last: ${samples.indices.last}
                samples.indices.endExclusiveWrapped: ${samples.indices.endExclusiveWrapped}
                samples.indices.endExclusiveClamped: ${samples.indices.endExclusiveClamped}
                samples.indices.endInclusive / samplesPerBucket: ${samples.indices.last / samplesPerBucketInt}
            """.trimIndent()
        )

        val maxBuckets = DoubleArray(totalBuckets + 1)
        val minBuckets = DoubleArray(totalBuckets + 1)
        val averageBuckets = DoubleArray(totalBuckets + 1)
        val positiveAverageBuckets = DoubleArray(totalBuckets + 1)
        val positiveCountBuckets = IntArray(totalBuckets + 1)
        val negativeAverageBuckets = DoubleArray(totalBuckets + 1)
        val negativeCounterBuckets = IntArray(totalBuckets + 1)

        for (i in 0 until totalSamples) {
            val bucketIndex = i / samplesPerBucketInt
            val sampleAsDouble = samples[i].toDouble()
            averageBuckets[bucketIndex] = averageBuckets[bucketIndex] + samples[i]
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
            averageBuckets[i] = averageBuckets[i] / samplesPerBucketInt
        }

//            for (i in averageBuckets.indices) {
//                val sample = averageBuckets[i]
//                if (sample >= 0.0) {
//                    averageBuckets[i] = log(max(1.0, sample), 1.5)
//                } else {
//                    averageBuckets[i] = -log(max(1.0, sample.absoluteValue), 1.5)
//                }
//            }

//            println("Bucket averages")
//            println(averageBuckets.toList())

        val fakeAverageBuckets = DoubleArray(averageBuckets.size)
        for (i in averageBuckets.indices) {
            fakeAverageBuckets[i] =
                100.0 * sin(2 * PI * (millisecondsPerBucket * i + millisecondsPerBucket / 2.0) / 1000.0)
        }



//            for (i in averageBuckets.indices) {
//                if (fakeAverageBuckets[i] >= 0) {
//                    resultBuckets[i] = if (positiveCountBuckets[i] == 0) 0.0 else positiveAverageBuckets[i] / positiveCountBuckets[i]
//                } else {
//                    resultBuckets[i] = if (negativeCounterBuckets[i] == 0) 0.0 else negativeAverageBuckets[i] / negativeCounterBuckets[i]
//                }
//            }

//        for (i in averageBuckets.indices) {
//            if (averageBuckets[i] >= 0) {
//                resultBuckets[i] = if (positiveCountBuckets[i] == 0) 0.0 else positiveAverageBuckets[i] / positiveCountBuckets[i]
//            } else {
//                resultBuckets[i] = if (negativeCounterBuckets[i] == 0) 0.0 else negativeAverageBuckets[i] / negativeCounterBuckets[i]
//            }
//        }

        for (i in averageBuckets.indices) {
            if (averageBuckets[i] >= 0) {
                resultBuckets1[i] = maxBuckets[i]
            } else {
                resultBuckets1[i] = minBuckets[i]
            }
        }

//            val fakeWaveformContainer = UIMonoAudioWaveform(
//                waveformHeight,
//                fakeAverageBuckets
//            ).addTo(this) {
//                centerYOnStage()
//            }


    }
}