import com.soywiz.korge.gradle.*

//plugins {
//	alias(libs.plugins.korge)
//}

apply<KorgeGradlePlugin>()

korge {
	id = "com.kietyo.xenotactic.korge.xenotactic"

// To enable all targets at once

//	targetAll()

// To enable targets based on properties/environment variables
	//targetDefault()

// To selectively enable targets
	
	targetJvm()
	targetJs()
//	targetDesktop()
//	targetIos()
//	targetAndroidIndirect() // targetAndroidDirect()
	//targetAndroidDirect()

//	serializationJson()
	entrypoint("DebugMain", "DebugMain")
	entrypoint("DebugMain2", "DebugMain2")

}

dependencies {
	add("commonMainImplementation", project(":Common"))
	add("commonMainImplementation", project(":XenoECS"))
	//    add("commonMainImplementation", project(":Fleks"))
}