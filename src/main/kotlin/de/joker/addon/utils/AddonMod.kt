package de.joker.addon.utils

import de.joker.addon.AddonManager
import de.miraculixx.challenge.api.modules.challenges.ChallengeTags
import de.miraculixx.challenge.api.modules.challenges.CustomChallengeData
import de.miraculixx.challenge.api.settings.ChallengeBoolSetting
import de.miraculixx.challenge.api.settings.ChallengeData
import de.miraculixx.challenge.api.utils.Icon
import de.miraculixx.challenge.api.utils.IconNaming
import de.joker.addon.MAddon
import de.joker.addon.mods.BlockRandomizer
import java.util.*

/**
 * All of our addon mods. Each mod is unique by his [uuid] but not bound to it on each startup. It is only important that the [uuid] is the same at all time of one session.
 * @param uuid The unique ID. Don't choose a simple hard coded [uuid], it could conflict with other addons
 */
enum class AddonMod(val uuid: UUID) {
    BLOCK_RANDOMIZER_EXTENDED(UUID.randomUUID());

    /**
     * Holds all mod data. Should only be called once at startup to ship all data to the MUtils API
     * @see AddonManager.loadMods
     */
    fun getModData(): CustomChallengeData {
        return when (this) {
            BLOCK_RANDOMIZER_EXTENDED -> CustomChallengeData(
                uuid,
                BlockRandomizer(),
                AddonManager.getSettings(this),
                Icon("ENDER_CHEST", naming = IconNaming(cmp("Block Randomizer"), listOf(cmp("An advanced Block Randomizer"), cmp("interactions")))),
                setOf(ChallengeTags.RANDOMIZER),
                MAddon.addonName
            )
        }
    }

    /**
     * Holds all settings information. Should only be called on initial startup if no saved settings are present.
     *
     * @see AddonManager.getSettings
     */
    fun getDefaultSetting(): ChallengeData {
        return when (this) {
            BLOCK_RANDOMIZER_EXTENDED -> ChallengeData(
                    mapOf(
                        "random" to ChallengeBoolSetting("DIAMOND_PICKAXE", false),
                        "player" to ChallengeBoolSetting("REDSTONE", false),
                    ),
                    mapOf(
                        "random" to IconNaming(cmp("Full Random"), listOf(cmp("Randomizes all blocks"))),
                        "player" to IconNaming(cmp("Per Player"), listOf(cmp("Randomizes all blocks per player"))),
                    ),
                )

        }
    }
}