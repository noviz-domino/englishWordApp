plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.voca.englishwordapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.voca.englishwordapp"
        minSdk = 21
        //noinspection EditedTargetSdkVersion
        targetSdk = 35
        //아래 두줄이 버전 갱신하는 부분. 두줄만 바꿔야함.
        versionCode = 8   //반드시 정수여야함. 구글플레이스토어의 시스템이 인식하려면 이전 숫자보다 부조건 커야함.
        versionName = "8.0"    //사용자에게 보이는 버전.

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.google.android.gms:play-services-ads:23.0.0")
    // 구글admob 챗gpt 코드 추가
    implementation(libs.core.splashscreen)
}