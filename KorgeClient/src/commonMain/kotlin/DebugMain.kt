import com.soywiz.korau.sound.readSound
import com.soywiz.korge.Korge
import com.soywiz.korge.input.draggable
import com.soywiz.korge.input.onScroll
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korio.async.runBlockingNoJs
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.Point
import com.xenotactic.korge.scenes.ParsedChannel
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

            val parsedChannel = ParsedChannel(channel1, audioData.rate, 256.0)

            val waveformHeight = 200.0

            val xOffsetDelta = 1.0

            val channel = sound.play()

            val lineWidth = 3.0
            val line = solidRect(lineWidth, waveformHeight * 5, Colors.YELLOW)

            val waveformWidth = 1000.0
            lateinit var waveform1: UIMonoAudioWaveform

            val waveformContainer = container {
                waveform1 = UIMonoAudioWaveform(
                    waveformWidth,
                    waveformHeight,
                    parsedChannel.resultBuckets1,
                ).addTo(this)
                val waveform2 = UIMonoAudioWaveform(
                    waveformWidth,
                    waveformHeight,
                    parsedChannel.resultBuckets2,
                ).addTo(this) {
                    y += waveformHeight
                }
                val waveform4 = UIMonoAudioWaveform(
                    waveformWidth,
                    waveformHeight,
                    parsedChannel.resultBuckets3,
                ).addTo(this) {
                    y += waveformHeight * 2
                }
                val waveform5 = UIMonoAudioWaveform(
                    waveformWidth,
                    waveformHeight,
                    parsedChannel.resultBuckets4,
                ).addTo(this) {
                    y += waveformHeight * 3
                }
                val waveform3 = UIMonoAudioWaveform(
                    waveformWidth,
                    waveformHeight,
                    parsedChannel.averageBuckets,
                ).addTo(this) {
                    y += waveformHeight * 4
                }

            }

            addUpdater {
                val currentMillis = channel.current.milliseconds
                val currentBucketIndex = currentMillis / parsedChannel.millisecondsPerBucket
//                waveformContainer.x = -xOffsetDelta * currentBucketIndex

                line.setPositionRelativeTo(
                    waveform1,
                    Point(xOffsetDelta * currentBucketIndex - lineWidth / 2, 0.0)
                )
                line.centerYOn(waveformContainer)
            }

            waveformContainer.centerYOnStage()



            println("Finished processing")




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


//
//


//            sound.playAndWait { current, total ->
//
////                println("currentMillis: $currentMillis, currentBucketIndex: $currentBucketIndex")
////                line.x = xOffsetDelta * currentBucketIndex
//                waveformContainer.x = -xOffsetDelta * currentBucketIndex
//            }

            waveformContainer.draggable {  }

            onScroll {
                if (it.isCtrlDown) {
                    if (it.scrollDeltaYLines < 0.0) {
                        // Zoom in
                        waveformContainer.scaledHeight += 100.0
                    } else {
                        // Zoom out
                        waveformContainer.scaledHeight = max((waveformContainer.scaledHeight - 100.0), 100.0)
                    }
                } else {
                    if (it.scrollDeltaYLines < 0.0) {
                        // Zoom in
                        waveformContainer.scaledWidth += 250.0
                    } else {
                        // Zoom out
                        waveformContainer.scaledWidth = max((waveformContainer.scaledWidth - 250.0), 100.0)
                    }
                }

            }

        }
    }

    private fun abs(sh: Short): Short {
        if (sh < 0) return (-sh).toShort()
        return sh
    }
}