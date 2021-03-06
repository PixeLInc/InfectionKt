package cf.pixelinc.entities.pathfinders

import cf.pixelinc.InfectionPlugin
import cf.pixelinc.infection.InfectionType
import cf.pixelinc.util.isInfected
import net.minecraft.server.v1_16_R3.*
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

/*
    An extension of the attack pathfinder to only attack players which are not infected.
 */
class PathfinderGoalAttackUninfected(creature : EntityCreature, damage : Double, flag : Boolean) : PathfinderGoalMeleeAttack(creature, damage, flag) {

    private val namespacedKey = NamespacedKey(InfectionPlugin.instance, "infected")

    override fun b(): Boolean {
        val entity : EntityLiving? = this.a.goalTarget
        if (entity is EntityHuman) {
            val player : Player = (entity.bukkitEntity as Player)

            if (player.isInfected()) {
                this.a.goalTarget = null
                return false
            }
        } else if (entity is EntityLiving) {
            // if it's an infected zombie mob, don't attack
            val bukkitEntity = entity.bukkitEntity
            val persistedInfection = bukkitEntity.persistentDataContainer.getOrDefault(namespacedKey, PersistentDataType.INTEGER, 0)
            if (persistedInfection == InfectionType.ZOMBIE.value) {
                this.a.goalTarget = null
                return false
            }
        }
        return super.b()
    }

}