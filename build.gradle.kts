plugins {
    java
    `maven-publish`
    id("com.github.johnrengelman.shadow") version("8.1.1")
}

group = "me.dave"
version = "1.1.0-BETA"

repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
    maven { url = uri("https://ci.ender.zone/plugin/repository/everything/") }
    maven { url = uri("https://maven.enginehub.org/repo/") }
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20-R0.1-SNAPSHOT")
    compileOnly("net.ess3:EssentialsX:2.18.1")
    compileOnly("net.ess3:EssentialsXSpawn:2.18.1")
    compileOnly("net.william278:HuskHomes2:4.1.1")
    compileOnly("com.github.CodingAir:WarpSystem-API:5.1.6")
    compileOnly("com.github.CodingAir:CodingAPI:1.64")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.9")
    compileOnly(files("libs/SunLight-3.9.6.jar"))
    compileOnly("su.nexmedia:NexEngine:2.2.12")
    implementation("com.github.CoolDCB:ChatColorHandler:v2.1.3")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    shadowJar {
        relocate("me.dave.chatcolorhandler", "me.dave.voidwarp.libraries.chatcolor")

        val folder = System.getenv("pluginFolder_1-20")
        if (folder != null) destinationDirectory.set(file(folder))
        archiveFileName.set("${project.name}-${project.version}.jar")
    }

    processResources{
        expand(project.properties)

        inputs.property("version", rootProject.version)
        filesMatching("plugin.yml") {
            expand("version" to rootProject.version)
        }
    }
}