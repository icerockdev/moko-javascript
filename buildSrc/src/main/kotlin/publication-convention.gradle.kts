plugins {
    id("org.gradle.maven-publish")
}

publishing {
    group = "dev.icerock.moko"
    version = Deps.mokoJavascriptVersion

    repositories.maven("https://api.bintray.com/maven/icerockdev/moko/moko-javascript/;publish=1") {
        name = "bintray"

        credentials {
            username = System.getenv("BINTRAY_USER")
            password = System.getenv("BINTRAY_KEY")
        }
    }
}
