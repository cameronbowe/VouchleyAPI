//The project's information.
group = "net.cameronbowe" //The project's group.
version = "1.1.0" //The project's version.
description = "A utility to access Vouchley's API with ease." //The project's description.

//The gradle plugins.
plugins {
    id("java") //Add the java plugin (for java development).
    id("signing") //Add the signing plugin (for signing to publish to maven).
    id("maven-publish") //Add the maven publish plugin (for publishing to maven).
    id("com.github.breadmoirai.github-release").version("2.5.2") //Add the github release plugin (for releasing to github).
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

//The java information.
java {
    withJavadocJar() //Create a javadoc jar.
    withSourcesJar() //Create a sources jar.
}

//The tasks.
tasks {

    //The javadoc creation task.
    javadoc {
        options {
            encoding = "UTF-8" //Set the encoding.
            title = project.name //Set the title.
            header = project.name //Set the header.
            version = project.version.toString() //Set the version.
            description = project.description.toString() //Set the description.
        }
    }

    //The jar creation task.
    jar {
        archiveClassifier.set("") //Set the classifier.
    }

    //The shadow jar creation task.
    shadowJar {
        archiveClassifier.set("shaded") //Set the classifier.
        configurations = listOf(project.configurations.runtimeClasspath.get()) //Set the configurations.
        relocate("com.google.gson", "net.cameronbowe.relocated.com.google.gson") //Relocate gson (so it doesn't conflict with other plugins).
        relocate("org.apache", "net.cameronbowe.relocated.org.apache") //Relocate apache (so it doesn't conflict with other plugins).
        exclude("mozilla/**", "META-INF/**", "module-info.class") //Exclude some stuff.
    }

    //The publish everything task.
    task("publishEverything") {
        dependsOn("publish", "githubRelease") //Depend on publishing and github release (to publish everything).
    }

}

//The github release.
githubRelease {
    token(findProperty("githubToken") as String?) //The github token.

    //The release information.
    tagName = "${project.version}" //Set the tag name.
    targetCommitish = "master" //Set the target commitish.
    releaseName = "${project.version}" //Set the release name.
    body = "The release of version ${project.version}." //Set the body.
    releaseAssets.setFrom(tasks["shadowJar"].outputs, tasks["javadocJar"].outputs, tasks["sourcesJar"].outputs, tasks["jar"].outputs) //Set the release assets.

    //The release options.
    generateReleaseNotes.set(false) //Don't generate release notes.
    allowUploadToExisting.set(true) //Allow upload to existing.
    draft.set(false) //Don't make the release a draft.
    overwrite.set(true) //Allow overwrite.
    dryRun.set(false) //Don't dry run.

}

//The publishing to my software repository.
publishing {

    //The publishing repositories (where to publish).
    repositories {
        maven {
            name = "net.cameronbowe" //The name of the repository.
            url = uri("https://software.cameronbowe.net/public") //The URL of the repository.
            authentication { create<BasicAuthentication>("basic") } //The authentication of the repository.
            credentials(PasswordCredentials::class) { //The credentials of the repository.
                username = findProperty("softwareRepositoryUsername") as String? //The username of the repository.
                password = findProperty("softwareRepositoryPassword") as String? //The password of the repository.
            }
        }
    }

    //The publishing publications (what to publish).
    publications {
        create<MavenPublication>("module") {

            //The artifact information.
            artifactId = project.name //Set the artifact ID.
            groupId = project.group.toString() //Set the group ID.
            version = project.version.toString() //Set the version.
            from(components["java"]) //Set the artifact.

            //The pom information.
            pom {

                //The basic information.
                name = project.name //Set the name.
                url = "https://www.vouchley.com/" //Set the URL.
                description = project.description //Set the description.
                packaging = "jar" //Set the packaging.

                //The license information.
                licenses {
                    license {
                        name = "Creative Commons Zero v1.0 Universal" //Set the name.
                        url = "https://creativecommons.org/publicdomain/zero/1.0/" //Set the URL.
                    }
                }

                //The developer information.
                developers {
                    developer {
                        id = "cameronbowe" //Set the ID.
                        name = "Cameron Bowe" //Set the name.
                        email = "contact@cameronbowe.net" //Set the email.
                    }
                }

                //The contributor information.
                contributors {
                    contributor {
                        name = "Vouchley" //Set the name.
                    }
                }

            }

        }
    }

}

//The signing of the publications.
signing {
    useGpgCmd() //Use the gpg command.
    sign(publishing.publications["module"]) //Sign the module publication.
}