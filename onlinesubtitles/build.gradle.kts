
plugins {
	id("com.android.library")
	kotlin("android")
	alias(libs.plugins.kotlin.serialization)
}

android {
	namespace = "org.jellyfin.androidtv.onlinesubtitles"
	compileSdk = libs.versions.android.compileSdk.get().toInt()

	defaultConfig {
		minSdk = libs.versions.android.minSdk.get().toInt()
	}

	buildFeatures {
		viewBinding = true
	}

	lint {
		lintConfig = file("$rootDir/android-lint.xml")
		abortOnError = false
	}

	testOptions.unitTests.all {
		it.useJUnitPlatform()
	}
}

dependencies {

	implementation(projects.preference)

	// Kotlin
	implementation(libs.kotlinx.coroutines)

	implementation("com.squareup.okhttp3:okhttp:4.12.0")
	implementation(libs.timber)
	implementation(libs.kotlinx.serialization.json)
	implementation(projects.preference)
	implementation(libs.jellyfin.sdk) {
		// Change version if desired
		val sdkVersion = findProperty("sdk.version")?.toString()
		when (sdkVersion) {
			"local" -> version { strictly("latest-SNAPSHOT") }
			"snapshot" -> version { strictly("master-SNAPSHOT") }
			"unstable-snapshot" -> version { strictly("openapi-unstable-SNAPSHOT") }
		}
	}

	implementation(libs.androidx.core)
	implementation(libs.androidx.leanback.preference)
	implementation(libs.androidx.preference)

	// Dependency Injection
	implementation(libs.bundles.koin)

	// Testing
	testImplementation(libs.kotest.runner.junit5)
	testImplementation(libs.kotest.assertions)
	testImplementation(libs.mockk)
}
