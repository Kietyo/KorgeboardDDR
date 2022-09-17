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
import com.xenotactic.korge.scenes.ParsedChannel
import com.xenotactic.korge.scenes.UIMonoAudioWaveform
import kotlin.jvm.JvmStatic
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.log
import kotlin.math.max

object DebugMain2 {

    @JvmStatic
    fun main(args: Array<String>) = runBlockingNoJs {
        Korge(width = 800, height = 720) {

//            val anchors = mutableMapOf(
//                "TOP_LEFT" to Anchor.TOP_LEFT,
//                "TOP_CENTER" to Anchor.TOP_CENTER,
//                "TOP_RIGHT" to Anchor.TOP_RIGHT,
//
//                "MIDDLE_LEFT" to Anchor.MIDDLE_LEFT,
//                "MIDDLE_CENTER" to Anchor.MIDDLE_CENTER,
//                "MIDDLE_RIGHT" to Anchor.MIDDLE_RIGHT,
//
//                "BOTTOM_LEFT" to Anchor.BOTTOM_LEFT,
//                "BOTTOM_CENTER" to Anchor.BOTTOM_CENTER,
//                "BOTTOM_RIGHT" to Anchor.BOTTOM_RIGHT,
//            )
//
//            for ((anchorText, anchor) in anchors) {
//                container {
//                    val r = solidRect(50.0, 50.0)
//                    text(anchorText, color = Colors.RED) {
//                        scaledWidth = 40.0
//                    }.centerOn(r)
//                }.dockedTo(anchor)
//            }

//            val rect1 = solidRect(100.0, 100.0) {
////                y = 150.0
//            }
//
//            val rect2 = solidRect(50.0, 50.0, Colors.RED) {
////                x = 300.0
////                y = 150.0
//            }
//
//            rect2.dockedTo(Anchor.CENTER)

            val sound = resourcesVfs["bird_world.mp3"].readSound()
            val audioData = sound.toAudioData()
            val channel1 = audioData.samples[1]
            val parsedChannel = ParsedChannel(channel1, audioData.rate, 256.0)
            val waveformHeight = 200.0
            val xOffsetDelta = 1.0


            val waveform1 = UIMonoAudioWaveform(
                waveformHeight,
                parsedChannel.resultBuckets4,
                xOffsetDelta
            ).addTo(this)

        }
    }

    private fun abs(sh: Short): Short {
        if (sh < 0) return (-sh).toShort()
        return sh
    }
}