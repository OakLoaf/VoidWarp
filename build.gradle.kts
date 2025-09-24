plugins {
    `java-library`
    id("com.gradleup.shadow") version("8.3.0")
    id("xyz.jpenilla.run-paper") version("2.2.4")
}

group = "org.lushplugins"
version = "2.0.0"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") // Spigot
    maven("https://repo.lushplugins.org/releases/") // ChatColorHandler
    maven("https://ci.ender.zone/plugin/repository/everything/")
    maven("https://maven.enginehub.org/repo/") // WorldGuard
    maven("https://repo.nightexpressdev.com/releases") // nightcore
    maven("https://jitpack.io") // SunLight
}

dependencies {
    // Dependencies
    compileOnly("org.spigotmc:spigot-api:1.21.8-R0.1-SNAPSHOT")

    // Soft Dependencies
    compileOnly("net.ess3:EssentialsX:2.18.1")
    compileOnly("net.ess3:EssentialsXSpawn:2.18.1")
    compileOnly("net.william278:HuskHomes2:4.1.1")
    compileOnly("com.github.CodingAir:WarpSystem-API:5.1.6")
    compileOnly("com.github.CodingAir:CodingAPI:1.64")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.14")
    compileOnly(files("libs/SunLight-3.14.0.jar"))
    compileOnly("su.nightexpress.nightcore:main:2.7.18")

    // Libraries
    implementation("org.lushplugins:ChatColorHandler:5.1.6")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    shadowJar {
        minimize()

        relocate("org.lushplugins.chatcolorhandler", "org.lushplugins.voidwarp.libraries.chatcolor")

        archiveFileName.set("${project.name}-${project.version}.jar")
    }

    processResources{
        filesMatching("plugin.yml") {
            expand(project.properties)
        }

        inputs.property("version", rootProject.version)
        filesMatching("plugin.yml") {
            expand("version" to rootProject.version)
        }
    }

    runServer {
        minecraftVersion("1.21.8")
    }
}