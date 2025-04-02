// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}

// For top-level build.gradle, you typically don't put implementation dependencies here
// Instead, you might want to define dependency versions or common configurations
subprojects {
    afterEvaluate {
        dependencies {
            // Use single quotes or double quotes consistently
            implementation 'com.appodeal.ads:sdk:3.5.1.0'
        }
    }
}