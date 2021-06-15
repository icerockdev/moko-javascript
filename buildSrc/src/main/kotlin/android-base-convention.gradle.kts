import com.android.build.gradle.BaseExtension

configure<BaseExtension> {
    compileSdkVersion(Deps.Android.compileSdk)

    defaultConfig {
        minSdkVersion(Deps.Android.minSdk)
        targetSdkVersion(Deps.Android.targetSdk)
    }
}
