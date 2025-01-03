package de.joker.addon.mods

import de.joker.addon.AddonManager
import de.joker.addon.utils.AddonMod
import de.miraculixx.challenge.api.modules.challenges.Challenge
import net.axay.kspigot.event.listen
import net.axay.kspigot.event.register
import net.axay.kspigot.event.unregister
import net.axay.kspigot.extensions.worlds
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.entity.TNTPrimed
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockExplodeEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

/**
 * BlockRandomizer
 * @author InvalidJokerDE, Miraculixx
 */
class BlockRandomizer : Challenge {
    private var fullRandom: Boolean = false
    private var playerRandom: Boolean = false
    private val map: MutableMap<Material, Material> = mutableMapOf() // map
    private val list: MutableList<Material> = mutableListOf() // list
    private val playerMap: MutableMap<Player, MutableMap<Material, Material>> = mutableMapOf() // player, map
    private val playerList: MutableMap<Player, MutableList<Material>> = mutableMapOf() // player, list

    override fun start(): Boolean {
        val rnd = Random(worlds.first().seed)
        val settings = AddonManager.getSettings(AddonMod.BLOCK_RANDOMIZER_EXTENDED).settings
        fullRandom = settings["random"]?.toBool()?.getValue() ?: false
        playerRandom = settings["player"]?.toBool()?.getValue() ?: false
        if (!fullRandom) {
            if (playerRandom) {
                for (p in Bukkit.getOnlinePlayers()) {
                    val drops = Material.entries.filter { it.isItem }.shuffled(rnd)
                    var block = mutableListOf<Material>()
                    block.shuffle(rnd)
                    drops.forEach { dropMaterial ->
                        if (block.isEmpty()) {
                            block = Material.entries.filter { it.isBlock }.toMutableList()
                            block.shuffle(rnd)
                        }
                        if (!playerMap.containsKey(p)) {
                            playerMap[p] = mutableMapOf()
                        }
                        playerMap[p]!![block[0]] = dropMaterial
                        block.removeAt(0)

                    }
                }
            } else {
                val drops = Material.entries.filter { it.isItem }.shuffled(rnd)
                var block = mutableListOf<Material>()
                block.shuffle(rnd)
                drops.forEach { dropMaterial ->
                    if (block.isEmpty()) {
                        block = Material.entries.filter { it.isBlock }.toMutableList()
                        block.shuffle(rnd)
                    }
                    map[block[0]] = dropMaterial
                    block.removeAt(0)
                }
            }
        } else {
            if (playerRandom) {
                for (p in Bukkit.getOnlinePlayers()) {
                    if (!playerList.containsKey(p)) {
                        playerList[p] = mutableListOf()
                    }
                    val listX = playerList[p]!!
                    listX.addAll(Material.entries.toTypedArray())
                    listX.shuffle(rnd)
                }
            } else {
                list.addAll(Material.entries.toTypedArray())
                list.shuffle(rnd)
            }
        }
        return true
    }

    override fun stop() {
        map.clear()
        list.clear()
        playerMap.clear()
        playerList.clear()
    }

    override fun register() {
        onBlockBreak.register()
        onExplode.register()
        onExplode2.register()
    }

    override fun unregister() {
        onBlockBreak.unregister()
        onExplode.unregister()
        onExplode2.unregister()
    }

    private val onBlockBreak = listen<BlockBreakEvent>(register = false) {
        it.isDropItems = false
        dropItem(it.block, it.player)
    }

    private val onExplode = listen<BlockExplodeEvent>(register = false) {
        it.blockList().forEach { block ->
            dropItem(block)
            block.type = Material.AIR
        }
        it.blockList().clear()
    }

    private val onExplode2 = listen<EntityExplodeEvent>(register = false) {
        val entity = it.entity
        if (entity is TNTPrimed) {
            val source = entity.source

            if (source is Player) {
                it.blockList().forEach { block ->
                    dropItem(block, source)
                    block.type = Material.AIR
                }
                it.blockList().clear()
                return@listen
            }
        }

        it.blockList().forEach { block ->
            dropItem(block)
            block.type = Material.AIR
        }

        it.blockList().clear()
    }

    private fun dropItem(block: Block, player: Player? = null) {
        lateinit var material: Material
        if (fullRandom) {
            material = if (playerRandom && player != null) {
                val pList = playerList[player].orEmpty()
                if (pList.isNotEmpty()) pList[Random.nextInt(pList.size)]
                else Material.STONE
            } else {
                if (list.isNotEmpty()) list[Random.nextInt(list.size)]
                else Material.STONE
            }
        } else {
            val mat = block.type
            material = if (playerRandom && player != null) {
                val p = playerMap[player]!!
                p[mat] ?: Material.STONE

            } else {
                map[mat] ?: Material.STONE
            }
        }
        val loc = block.location.add(0.5, 0.5, 0.5)
        loc.world.dropItem(loc, ItemStack(material))
    }
}