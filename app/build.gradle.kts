plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.maps_map_seancostelloecacho"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.maps_map_seancostelloecacho"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    // PREVIEW
    debugImplementation("androidx.compose.ui:ui-tooling-preview:1.6.0")
    implementation(platform("androidx.compose:compose-bom:2024.03.00"))
    // OTHERS
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.0")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    // Api Google Maps
    implementation("com.google.maps.android:maps-compose:2.11.4")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.1.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    // navigationController
    implementation ("androidx.navigation:navigation-compose:2.7.5")
    // MATERAIL 3 FOR BOTTOM SHEET
    implementation ("androidx.compose.material3:material3:1.3.0-alpha02")
    // CAMERA
    implementation("androidx.camera:camera-core:1.3.2")
    implementation("androidx.camera:camera-camera2:1.3.2")
    implementation("androidx.camera:camera-lifecycle:1.3.2")
    implementation("androidx.camera:camera-view:1.3.2")
    implementation("androidx.camera:camera-extensions:1.3.2")
    // ICONS
    implementation("androidx.compose.material:material-icons-extended:1.6.3")
    // LIVE DATA
    //implementation("androidx.compose.runtime:runtime-livedata.1.5.4") // el de los apuntes no funciona
    implementation("androidx.compose.runtime:runtime-livedata:1.6.1")
    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.7.4"))
    implementation("com.google.firebase:firebase-firestore-ktx:24.10.3")
    implementation("com.google.firebase:firebase-analytics") // Anastasiia


    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    // https://firebase.google.com/docs/android/setup#available-libraries

    // FIRE STORE
    implementation("com.google.firebase:firebase-firestore-ktx")
    // FIREBASE STORAGE
    implementation ("com.google.firebase:firebase-storage-ktx")
    // FIREBASE AUTHENTICATION
    implementation ("com.firebaseui:firebase-ui-auth:7.2.0")
    // DATA STORE
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    // GLIDE IMAGE
    implementation("com.github.bumptech.glide:compose:1.0.0-beta01")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

}


// for the Api key
secrets {
    propertiesFileName = "secrets.properties"
    defaultPropertiesFileName = "local.properties"
    ignoreList.add("keyToIgnore") // Ignore the key "keyToIgnore"
    ignoreList.add("sdk.*") // Ignore all keys matching the regexp "sdk.*"
}

