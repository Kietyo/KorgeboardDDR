package com.xenotactic.korge.scenes

import com.soywiz.klock.DateTime
import com.xenotactic.ecs.FamilyConfiguration
import com.xenotactic.ecs.System
import kotlin.time.Duration

class MoveVerticalBeatsSystem(
    val gameWorld: GameWorld
) : System() {
    override var familyConfiguration: FamilyConfiguration =
        FamilyConfiguration(allOfComponents = setOf(BeatComponent::class, UIVerticalBeat::class))

    override fun update(deltaTime: Duration) {
        getFamily().getSequence().forEach {
            val verticalBeatUI = gameWorld.verticalBeatsContainer.getComponent(it)
            verticalBeatUI.y += GameConstants.VERTICAL_FALL_SPEED_PER_UPDATE
        }
    }
}

class MoveHorizontalBeatsSystem(
    val gameWorld: GameWorld
) : System() {
    override var familyConfiguration: FamilyConfiguration =
        FamilyConfiguration(allOfComponents = setOf(BeatComponent::class, UIHorizontalBeat::class))

    override fun update(deltaTime: Duration) {
        getFamily().getSequence().forEach {
            val beatUI = gameWorld.horizontalBeatsContainer.getComponent(it)
            beatUI.x -= GameConstants.HORIZONTAL_FALL_SPEED_PER_UPDATE
        }
    }
}

class RemoveBeatsAfterMissSystem(
    val gameWorld: GameWorld
) : System() {
    override var familyConfiguration: FamilyConfiguration =
        FamilyConfiguration(allOfComponents = setOf(BeatComponent::class),
        anyOfComponents = setOf(UIVerticalBeat::class, UIHorizontalBeat::class)
        )

    override fun update(deltaTime: Duration) {
        val currentTime = DateTime.nowUnixMillis()
        getFamily().getSequence().forEach {

            val verticalUI = gameWorld.verticalBeatsContainer.getComponentOrNull(it)
            val horizontalUI = gameWorld.horizontalBeatsContainer.getComponentOrNull(it)
            val beatTime = gameWorld.beatsContainer.getComponent(it).timeMillis
            if ((currentTime - beatTime) >= 1000) {
                verticalUI?.removeFromParent()
                horizontalUI?.removeFromParent()
                gameWorld.world.removeEntity(it)
            }
        }
    }
}
