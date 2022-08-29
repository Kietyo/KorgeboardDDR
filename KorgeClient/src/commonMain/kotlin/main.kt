import com.soywiz.klogger.Logger
import com.soywiz.korge.Korge
import com.soywiz.korge.scene.Module
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.Views
import com.soywiz.korgw.GameWindow
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA
import com.soywiz.korinject.AsyncInjector
import com.soywiz.korma.geom.Anchor
import com.soywiz.korma.geom.SizeInt
import com.xenotactic.korge.scenes.MainScene
import com.xenotactic.korge.scenes.RootScene
import kotlin.reflect.KClass

object MainModule : Module() {
    override val bgcolor: RGBA = Colors["#2b2b2b"]
    override val size: SizeInt = SizeInt(1000, 720)
    override val clipBorders: Boolean = false
    override val mainScene: KClass<out Scene> = RootScene::class
    override val scaleAnchor: Anchor
        get() = Anchor.MIDDLE_CENTER
    override val quality: GameWindow.Quality = GameWindow.Quality.QUALITY

    val logger = Logger<MainModule>()

    override suspend fun AsyncInjector.configure() {
        Logger.defaultLevel = Logger.Level.DEBUG
        val views = this.get<Views>()
        //        val globalBus = EventBus(CoroutineScope(Dispatchers.Main))
        println("Preparing main module")
        println(views)


        //        mapInstance(GameScene(mapBridge))
        mapPrototype { RootScene(views) }
        mapPrototype {
            MainScene()
        }

    }
}

suspend fun main() = Korge(Korge.Config(module = MainModule))