package de.joker.addon

import de.miraculixx.kpaper.main.KPaper

class MAddon : KPaper() {
    companion object {
        lateinit var INSTANCE: KPaper
        lateinit var addonName: String
    }

    override fun load() {
        INSTANCE = this
        @Suppress("DEPRECATION")
        addonName = description.name
    }

    override fun startup() {
        AddonManager.loadMods()
    }

    override fun shutdown() {
        AddonManager.saveMods()
    }
}

val PluginInstance by lazy { MAddon.INSTANCE }