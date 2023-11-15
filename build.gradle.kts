version = "1.0" //The project's version.

//The gradle plugins.
plugins {
    id("java") //Add the java plugin (for java development).
    id("com.github.johnrengelman.shadow").version("7.1.2") //Add shadow (for shading dependencies).
}

//The java information.
java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8)) //Set the java version to 8.
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

//Compilation.
tasks {

    //Handle java compilation.
    compileJava { //On java compilation.
        options.release.set(8) //Make sure the java version is 8.
        options.encoding = "UTF-8" //Make sure text is UTF-8 (stuff bugs out otherwise).
    }

    //Handle shadow jar (shading/relocation).
    shadowJar {
        relocate("com.google.gson", "com.vouchley.relocated.com.google.gson") //Relocate gson (so it doesn't conflict with other plugins).
        relocate("org.apache.http", "com.vouchley.relocated.apache.http") //Relocate apache http (so it doesn't conflict with other plugins).
        exclude("**/*.txt", "**./META-INF/*") //Exclude gibberish files.
    }

}