plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.zonesshift"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.zonesshift"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    lint {
        baseline = file("lint-baseline.xml")
    }

}

dependencies {

    implementation(platform(libs.firebase.bom)) // BOM управляет версиями автоматически
    implementation(libs.google.firebase.auth)
    implementation(libs.google.firebase.firestore)
    implementation(libs.google.firebase.database)
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}