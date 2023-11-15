import java.net.URI

group = "net.cameronbowe" //The project's group.
version = "1.0.0" //The project's version.
description = "A utility to access Vouchley's API with ease." //The project's description.

//The gradle plugins.
plugins {
    id("java") //Add the java plugin (for java development).
    id("signing") //Add the signing plugin (for signing to publish to maven).
    id("maven-publish") //Add the maven publish plugin (for publishing to maven).
    id("com.github.johnrengelman.shadow").version("7.1.2") //Add shadow (for shading dependencies).
}

//The repositories (for dependencies).
repositories {
    mavenCentral() //Add maven central repository (has literally 99% of the repositories).
}

//The dependencies.
dependencies {
    //Shaded dependencies.
    implementation("com.google.code.gson", "gson", "2.8.8") //Implement GSON (for fantastic JSON usage).
    implementation("org.apache.httpcomponents", "httpclient", "4.5.13") //Implement apache http client (for http requests).
}

//The publishing to maven.
publishing {

    //The publishing repository.
    repositories {

        //Add the Maven Central repository.
        maven {
            url = uri("https://s01.oss.sonatype.org/content/repositories/releases/") //The sonatype repository.
            credentials {
                username = project.findProperty("ossrhUsername")?.toString()
                password = project.findProperty("ossrhPassword")?.toString()
            }
        }

    }

    //The publishing publications (what to publish).
    publications {

        //The maven java publication.
        register("mavenJava", MavenPublication::class) {
            artifact(tasks.named("shadowJar").get()) { classifier = "all" } //Publish the shadow jar.
        }

    }
}

//The signing (of the maven publication).
signing {
    sign(publishing.publications["mavenJava"]) //Sign the maven publication.
    sign(configurations.archives.get()) //Sign the archives.
    useGpgCmd() //Use the gpg command.
}

//Compilation.
tasks {

    //Handle java compilation.
    compileJava { //On java compilation.
        options.encoding = "UTF-8" //Make sure text is UTF-8 (stuff bugs out otherwise).
    }

    //Handle shadow jar (shading/relocation).
    shadowJar {
        relocate("com.google.gson", "com.vouchley.relocated.com.google.gson") //Relocate gson (so it doesn't conflict with other plugins).
        relocate("org.apache.http", "com.vouchley.relocated.apache.http") //Relocate apache http (so it doesn't conflict with other plugins).
        exclude("**/*.txt", "**./META-INF/*") //Exclude gibberish files.
    }

}