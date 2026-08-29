plugins {
    `java-library`
    `maven-publish`
    id("net.neoforged.moddev") version "2.0.144"
    idea
}

// Values from gradle.properties
val minecraftVersion: String by extra { property("minecraft_version") as String }
val minecraftVersionRange: String by extra { property("minecraft_version_range") as String }
val neoVersion: String by extra { property("neo_version") as String }
val modId: String by extra { property("mod_id") as String }
val modName: String by extra { property("mod_name") as String }
val modLicense: String by extra { property("mod_license") as String }
val modVersion: String by extra { property("mod_version") as String }
val modGroupId: String by extra { property("mod_group_id") as String }

tasks.named<Wrapper>("wrapper") {
    distributionType = Wrapper.DistributionType.BIN
}

version = System.getenv("VERSION") ?: modVersion
group = modGroupId

base {
    archivesName = "BetterFoliageRenewed-NeoForge-$minecraftVersion"
}

java.toolchain.languageVersion = JavaLanguageVersion.of(25)

repositories {
    mavenLocal()
    exclusiveContent {
        forRepository { maven("https://www.cursemaven.com") }
        filter { includeGroup("curse.maven") }
    }
}

val generateModMetadata = tasks.register<ProcessResources>("generateModMetadata") {
    val replaceProperties = mapOf(
        "minecraft_version" to minecraftVersion,
        "minecraft_version_range" to minecraftVersionRange,
        "neo_version" to neoVersion,
        "mod_id" to modId,
        "mod_name" to modName,
        "mod_license" to modLicense,
        "mod_version" to version.toString(),
    )
    inputs.properties(replaceProperties)
    expand(replaceProperties)
    from("src/main/templates")
    into(layout.buildDirectory.dir("generated/sources/modMetadata"))
}

sourceSets.main {
    resources {
        srcDir("src/generated/resources")
        srcDir(generateModMetadata)

        exclude("**/*.bbmodel")
        exclude("src/generated/**/.cache")
    }
}

neoForge {
    version = neoVersion

    runs {
        register("client") {
            client()
            gameDirectory = file("run/client")
            systemProperty("neoforge.enabledGameTestNamespaces", modId)
        }

        register("data") {
            clientData()
            programArguments.addAll(
                "--mod", modId,
                "--all",
                "--output", file("src/generated/resources/").absolutePath,
                "--existing", file("src/main/resources/").absolutePath,
            )
        }

        configureEach {
            systemProperty("forge.logging.markers", "REGISTRIES")
            logLevel = org.slf4j.event.Level.DEBUG

            // Only JBR allows enhanced class redefinition, so ignore the option for any other JDKs
            jvmArguments.addAll("-XX:+IgnoreUnrecognizedVMOptions", "-XX:+AllowEnhancedClassRedefinition", "-ea")
        }
    }

    mods {
        register(modId) {
            sourceSet(sourceSets.main.get())
        }
    }

    ideSyncTask(generateModMetadata)
}

val localRuntime: Configuration by configurations.creating
configurations.runtimeClasspath.get().extendsFrom(localRuntime)

dependencies {
}

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            url = layout.projectDirectory.dir("repo").asFile.toURI()
        }
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.jar {
    manifest {
        attributes["Implementation-Version"] = project.version
    }
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}
