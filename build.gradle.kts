//The project's information.
group = "net.cameronbowe" //The project's group.
version = "1.0.0" //The project's version.
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

//The tasks.
tasks {

    jar {
        delete("${project.name}-${project.version}.jar")
    }

    shadowJar {
        delete("${project.name}-${project.version}-all.jar")
    }

    //The publishing task.
    task("publishEverything") {
        println("Publishing everything...") //Log that everything is being published.
        dependsOn("publish", "githubRelease") //Depend on publishing and github release (to publish everything).
        println("Publishing everything!") //Log that everything has been published.
    }

    //The moving of old files task.
    task("moveOldFiles", type = Copy::class) {
        val versionRegex = Regex("""${project.version}""") //The version regex.
        duplicatesStrategy = DuplicatesStrategy.INCLUDE //Include duplicates.
        val oldFiles = fileTree("build/libs") { include { fileTreeElement -> !fileTreeElement.isDirectory && !versionRegex.containsMatchIn(fileTreeElement.name) } } //Get the old files.
        if (oldFiles.isEmpty) return@task //If there are no old files, return.
        from(oldFiles) //From the old files.
        into("build/libs/other") //Into the other libs folder.
        doLast { delete(oldFiles) } //Delete the old files.
    }

    //The jar creation task.
    jar {
        dependsOn("moveOldFiles") //Depend on moving old files.
        delete("${project.name}-${project.version}-without-depends.jar") //Delete the old jar (if it exists).
        from(sourceSets.main.get().allSource) //Add the main sources.
        archiveClassifier.set("withoutDepends") //Set the classifier.
        archiveFileName.set("${project.name}-${project.version}-without-depends.jar") //Set the file name.
    }

    //The shadow jar creation task.
    shadowJar {
        dependsOn("moveOldFiles") //Depend on moving old files.
        delete("${project.name}-${project.version}-with-depends.jar") //Delete the old jar (if it exists).
        from(sourceSets.main.get().allSource) //Add the main sources.
        archiveClassifier.set("withDepends") //Set the classifier.
        configurations = listOf(project.configurations.runtimeClasspath.get()) //Set the configurations.
        archiveFileName.set("${project.name}-${project.version}-with-depends.jar") //Set the archive file name.
        relocate("com.google.gson", "net.cameronbowe.relocated.com.google.gson") //Relocate gson (so it doesn't conflict with other plugins).
        relocate("org.apache", "net.cameronbowe.relocated.org.apache") //Relocate apache (so it doesn't conflict with other plugins).
        exclude("mozilla/**", "META-INF/**", "module-info.class") //Exclude some stuff.
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
    releaseAssets.setFrom(tasks["shadowJar"].outputs.files, tasks["jar"].outputs.files) //Set the release assets.

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
            artifactId = project.name //Set the artifact ID.
            groupId = project.group.toString() //Set the group ID.
            version = project.version.toString() //Set the version.
            from(components["java"]) //From the java component.
            pom {

                //The basic information.
                name = project.name //Set the name.
                url = "https://www.vouchley.com/" //Set the URL.
                description = project.description //Set the description.

                //The license information.
                licenses {
                    license {
                        name = "Creative Commons Zero v1.0 Universal"
                        url = "https://creativecommons.org/publicdomain/zero/1.0/"
                    }
                }

                //The developer information.
                developers {
                    developer {
                        id = "cameronbowe"
                        name = "Cameron Bowe"
                        email = "contact@cameronbowe.net"
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