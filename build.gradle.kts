import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.serialization") version "2.1.0"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.17"
    id("xyz.jpenilla.run-paper") version "2.3.0"
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
}

group = "de.joker"
version = "1.0.1"

repositories {
    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle("1.21.1-R0.1-SNAPSHOT")

    library("de.miraculixx:kpaper-light:1.2.2")

    // The MChallenge API
    compileOnly("de.miraculixx:challenge-api:1.5.0")

    library(kotlin("stdlib"))
    library("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.+")
    library("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.+")

    compileOnly("net.kyori:adventure-text-serializer-gson:4.24.0")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(21)
    }
    compileKotlin {
        kotlinOptions.jvmTarget = "21"
    }

    runServer {
        minecraftVersion("1.21.4")
    }
}


paper {
    main = "de.joker.addon.MAddon"
    apiVersion = "1.21"
    foliaSupported = false
    description = "MUtils Addon for randomizing blocks"
    website = "https://mutils.net/ch/info"
    authors = listOf("InvalidJoker")

    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
    serverDependencies {
        register("MChallenge") {
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
            required = true
        }
    }
}