import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.serialization") version "1.9.23"
    id("io.papermc.paperweight.userdev") version "2.0.0-20241223.032535-116"
    id("xyz.jpenilla.run-paper") version "2.3.0"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "de.joker"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle("1.21.1-R0.1-SNAPSHOT")

    implementation("net.axay:kspigot:1.21.0")

    // The MChallenge API
    compileOnly("de.miraculixx:challenge-api:1.5.0")

    library(kotlin("stdlib"))
    library("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.+")
    library("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.+")

    compileOnly("net.kyori:adventure-text-serializer-gson:4.13.1")

}



java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {
    shadowJar {
        archiveFileName.set("MRandomizer.jar")
    }
    assemble {
        dependsOn(shadowJar)
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
        minecraftVersion("1.20.1")
    }
}


bukkit {
    main = "de.joker.addon.MAddon"
    apiVersion = "1.16"
    foliaSupported = false
    description = "MUtils Addon for randomizing blocks"
    website = "https://mutils.net/ch/info"
    authors = listOf("InvalidJoker")

    load = BukkitPluginDescription.PluginLoadOrder.STARTUP

    depend = listOf()
    softDepend = listOf("MUtils-Challenge")
}