import java.net.URI

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
    repositories {
        maven {
            url = URI.create("https://oss.sonatype.org/content/repositories/releases/") //The repository's url.
            credentials { //The repository's credentials.
                username = project.findProperty("ossrhUsername")?.toString() //The repository's username.
                password = project.findProperty("ossrhPassword")?.toString() //The repository's password.
            }
        }
    }
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"]) //Publish the java component.
            artifact(tasks.named("shadowJar").get()) //Publish the shadow jar.
            pom { //Publish the pom.
                name.set(project.name) //The project's name.
                description.set(project.description) //The project's description.
                url.set("https://vouchley.net") //The project's url.
                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers { //The project's developers.
                    developer { //The developer.
                        id.set("cameronbowe") //The developer's id.
                        name.set("Cameron Bowe") //The developer's name.
                        email.set(project.findProperty("ossrhEmail")?.toString()) //The developer's email.
                    }
                }
                scm { //The project's scm.
                    url.set("https://github.com/cameronbowe/VouchleyAPI") //The project's url.
                    connection.set("scm:git:git://github.com/cameronbowe/VouchleyAPI.git") //The project's connection.
                    developerConnection.set("scm:git:ssh://git@github.com:cameronbowe/VouchleyAPI.git") //The project's developer connection.
                }
            }
        }
    }
}

//The signing (of the maven publication).
signing {
    sign(publishing.publications["mavenJava"]) //Sign the maven publication.
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


    //Publish to maven local.
    named("publishToMavenLocal").configure {
        doLast {
            println("Publishing project to local Maven repository...") //Log that we are publishing to maven local.
        }
    }
}