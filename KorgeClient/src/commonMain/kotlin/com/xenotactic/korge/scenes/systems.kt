package com.xenotactic.korge.scenes

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

class RemoveBeatsSystem(
    val gameWorld: GameWorld
) : System() {
    override var familyConfiguration: FamilyConfiguration =
        FamilyConfiguration(allOfComponents = setOf(BeatComponent::class),
        anyOfComponents = setOf(UIVerticalBeat::class, UIHorizontalBeat::class)
        )

    override fun update(deltaTime: Duration) {
        getFamily().getSequence().forEach {
            val verticalUI = gameWorld.verticalBeatsContainer.getComponent(it)
            val horizontalUI = gameWorld.horizontalBeatsContainer.getComponentOrNull(it)
            if (verticalUI.y > gameWorld.windowHeight) {
                verticalUI.removeFromParent()
                horizontalUI?.removeFromParent()
            }
        }
    }
}
