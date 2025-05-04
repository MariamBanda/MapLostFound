plugins {
    id("com.android.application")
}

android {
    namespace = "com.prac.lostfound"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.prac.lostfound"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Material Components
    implementation("com.google.android.material:material:1.7.0")

    // Maps and Places
    implementation("com.google.android.gms:play-services-maps:18.2.0")  // Dependency for Google Maps
    implementation("com.google.android.libraries.places:places:3.2.0")
    // AndroidX dependencies
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
}
