# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.ConscryptHostnameVerifier
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE
# Call、Responseのジェネリック署名を保持する
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep class com.squareup.moshi.** { *; }
-dontwarn com.squareup.moshi.**
-keep class * extends com.squareup.moshi.JsonAdapter {
    public <init>(...);
}
-keep class retrofit2.** { *; }
-dontwarn retrofit2.**
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
-keep class retrofit2.adapter.** { *; }
-keep class retrofit2.converter.** { *; }
# OkHttp
-keep class okhttp3.** { *; }
-dontwarn okhttp3.**

# Kotlin Serialization
-keep class kotlinx.serialization.** { *; }
-dontwarn kotlinx.serialization.**

# ネットワーク関連クラスの保持
-keep class biz.moapp.transcription_app.network.** { *; }