import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.initialization.Environment.Properties

//The project's information.
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

//The tasks.
tasks {

    //The without dependencies jar creation task.
    task("withoutDepends", type = Jar::class) {
        from(sourceSets.main.get().allSource) //Add the main sources.
        archiveClassifier.set("withoutDepends") //Set the classifier.
        archiveFileName.set("${project.name}-${project.version}-without-depends.jar") //Set the file name.
    }

    //The with dependencies jar creation task.
    task("withDepends", type = ShadowJar::class) {
        from(sourceSets.main.get().allSource) //Add the main sources.
        archiveClassifier.set("withDepends") //Set the classifier.
        configurations = listOf(project.configurations.runtimeClasspath.get()) //Set the configurations.
        archiveFileName.set("${project.name}-${project.version}-with-depends.jar") //Set the archive file name.
        relocate("com.google.gson", "net.cameronbowe.relocated.com.google.gson") //Relocate gson (so it doesn't conflict with other plugins).
        relocate("org.apache", "net.cameronbowe.relocated.org.apache") //Relocate apache (so it doesn't conflict with other plugins).
        exclude("mozilla/**", "META-INF/**", "module-info.class") //Exclude some stuff.
    }

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

        //The without dependencies publication.
        create<MavenPublication>("withoutDepends") {
            artifactId = project.name //Set the artifact ID.
            groupId = project.group.toString() //Set the group ID.
            version = project.version.toString() //Set the version.
            artifact(tasks["withoutDepends"]) //Set the artifact.
        }

        //The with dependencies publication.
        create<MavenPublication>("withDepends") { //Create a maven publication.
            artifactId = project.name //Set the artifact ID.
            groupId = project.group.toString() //Set the group ID.
            version = project.version.toString() //Set the version.
            artifact(tasks["withDepends"]) //Set the artifact.
        }

    }

}

//The signing of the publications.
signing {
    useGpgCmd() //Use the gpg command.
    sign(publishing.publications["withoutDepends"]) //Sign the without dependencies publication.
    sign(publishing.publications["withDepends"]) //Sign the with dependencies publication.
}