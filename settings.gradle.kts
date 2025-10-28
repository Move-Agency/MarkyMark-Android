rootProject.name = "MarkyMark-Android"
include(":markymark")
include(":sample")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

pluginManagement.repositories.m2ciPlugin()


fun RepositoryHandler.m2ciPlugin() = addGitHubRepository(path = "move-android/m2ci")

fun RepositoryHandler.addGitHubRepository(path: String) {
    val gitHubToken = (settings.extra["com.moveagency.github.token"] as? String).orEmpty()
    maven {
        name = path
        url = uri("https://maven.pkg.github.com/$path")
        headerAuthentication(gitHubToken)
    }
}

fun MavenArtifactRepository.headerAuthentication(token: String) {
    credentials(HttpHeaderCredentials::class) {
        name = "Authorization"
        value = "Bearer $token"
    }
    authentication {
        create<HttpHeaderAuthentication>("header")
    }
}
