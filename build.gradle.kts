plugins {
    java
    id("com.github.johnrengelman.shadow") version "5.2.0"
    id("net.minecrell.licenser") version "0.4.1"
}

group = "io.github.juuxel"
version = "1.0.0"

repositories {
    mavenCentral()
    maven {
        name = "jitpack"
        url = uri("https://jitpack.io")
    }
}

dependencies {
    implementation(group = "blue.endless", name = "jankson", version = "1.2.0")
    implementation(group = "org.organicdesign", name = "Paguro", version = "3.1.2")

    // Swing stuff
    implementation(group = "com.miglayout", name = "miglayout-swing", version = "4.2")
    implementation(group = "org.pushing-pixels", name = "radiance-substance", version = "2.5.1")

    compileOnly(group = "org.jetbrains", name = "annotations", version = "19.0.0")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

license {
    header = file("HEADER.txt")
}

tasks {
    jar {
        from("LICENSE")
        manifest {
            attributes("Main-Class" to "juuxel.fc0.launcher.Main")
        }
    }

    shadowJar {
        from("LICENSE")
    }

    build {
        dependsOn(shadowJar)
    }
}
