plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    `maven-publish`
    signing
}

android {
    namespace = "org.connecttag.lib.kotlin.core"
    compileSdk = 37

    defaultConfig {
        minSdk = 23

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    buildFeatures {
        compose = true
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])

                groupId = project.property("LIB_GROUP").toString()
                artifactId = project.property("LIB_ARTIFACT_ID").toString()
                version = project.property("LIB_VERSION").toString()

                pom {
                    name.set("ConnectTag Lib Core")
                    description.set("Core library for ConnectTag Kotlin projects")
                    url.set("https://github.com/connecttag/connecttag")
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("developer-id")
                            name.set("Connect Tag")
                            email.set("connecttagye@gmail.com")
                        }
                    }
                    scm {
                        connection.set("scm:git:github.com/connecttag/connecttag.git")
                        developerConnection.set("scm:git:ssh://github.com/connecttag/connecttag.git")
                        url.set("https://github.com/connecttag/connecttag/tree/main")
                    }
                }
            }
        }
        repositories {
            // Local repository
            maven {
                name = "Local"
                url = uri(layout.buildDirectory.dir("repo"))
            }
            // Online repository example (MavenCentral/Nexus)
            /*
            maven {
                name = "Remote"
                url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                credentials {
                    username = project.findProperty("ossrhUsername")?.toString()
                    password = project.findProperty("ossrhPassword")?.toString()
                }
            }
            */
        }
    }
}

signing {
    // Only sign if we have the prop erties set up (usually in ~/.gradle/gradle.properties)
    if (project.hasProperty("signing.keyId")) {
        sign(publishing.publications["release"])
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.foundation.android)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation("androidx.webkit:webkit:1.16.0")
    implementation(libs.napier)
    implementation("androidx.work:work-runtime-ktx:2.11.2")
    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.accompanist.flowlayout)
    implementation(libs.kotlinx.serialization.json)
}
