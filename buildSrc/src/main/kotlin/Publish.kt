import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.credentials.HttpHeaderCredentials
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.authentication.http.HttpHeaderAuthentication
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.credentials
import org.gradle.kotlin.dsl.get

object Publish {

    fun MavenPublication.setup(project: Project) = project.run {
        groupId = BuildConstants.Namespace
        version = BuildConstants.VersionName
        from(components[if (isAndroid()) "release" else "java"])
        pom {
            url.set("https://github.com/move-android/MarkyMark-Android")
            organization {
                name.set("Framna")
                url.set("https://www.framna.com/")
            }
            scm {
                connection.set("scm:git:git://github.com/move-android/MarkyMark-Android.git")
                developerConnection.set("scm:git:ssh://git@github.com/move-android/MarkyMark-Android.git")
                url.set("https://github.com/move-android/MarkyMark-Android")
            }
        }
    }

    fun PublishingExtension.addPublishRepository(project: Project) = project.run {
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/move-android/MarkyMark-Android")
                headerAuthentication(getRepoToken().orEmpty())
            }
        }
    }

    fun Project.isAndroid() = plugins.findPlugin("com.android.library") != null ||
            plugins.findPlugin("com.android.application") != null

    fun Project.getRepoUsername() = findProperty("com.moveagency.username") as String

    fun Project.getRepoPassword() = findProperty("com.moveagency.password") as String

    fun Project.getRepoUrl() = findProperty("com.moveagency.repo") as String

    fun Project.getRepoToken() = findProperty("com.moveagency.github.token") as? String

    fun MavenArtifactRepository.headerAuthentication(token: String) {
        credentials(HttpHeaderCredentials::class) {
            name = "Authorization"
            value = "Bearer $token"
        }
        authentication {
            create<HttpHeaderAuthentication>("header")
        }
    }
}
