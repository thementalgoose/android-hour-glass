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

# Proguard Configuration for Realm (http://realm.io)
# For detailed discussion see: https://groups.google.com/forum/#!topic/realm-java/umqKCc50JGU
# Additionally you need to keep your Realm Model classes as well
# For example:
# -keep class com.yourcompany.realm.** { *; }
-keepnames public class * extends io.realm.RealmObject
-keep class io.realm.annotations.RealmModule
-keep @io.realm.annotations.RealmModule class *
-keep class io.realm.internal.Keep
-keep @io.realm.internal.Keep class *
-dontwarn io.realm.**

-dontwarn androidx.appcompat.view.ContextThemeWrapper

-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

-keep class io.realm.annotations.RealmModule
-keep @io.realm.annotations.RealmModule class *

-keep class io.realm.internal.Keep
-keep @io.realm.internal.Keep class * { *; }

-keep class io.realm.internal.KeepMember
-keep @io.realm.internal.KeepMember class * { @io.realm.internal.KeepMember *; }

-dontwarn javax.**
-dontwarn io.realm.**
-keep class io.realm.RealmCollection
-keep class io.realm.OrderedRealmCollection
-keepclasseswithmembernames class io.realm.**

-if @kotlinx.serialization.Serializable class **
-keepclassmembers class <1> {
    static <1>$Companion Companion;
}

# Keep `serializer()` on companion objects (both default and named) of serializable classes.
-if @kotlinx.serialization.Serializable class ** {
    static **$* *;
}
-keepclassmembers class <1>$<3> {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep `INSTANCE.serializer()` of serializable objects.
-if @kotlinx.serialization.Serializable class ** {
    public static ** INSTANCE;
}
-keepclassmembers class <1> {
    public static <1> INSTANCE;
    kotlinx.serialization.KSerializer serializer(...);
}

# @Keep @Serializable and @Polymorphic are used at runtime for polymorphic serialization.
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault

# Serializer for classes with named companion objects are retrieved using `getDeclaredClasses`.
# If you have any, uncomment and replace classes with those containing named companion objects.
#-keepattributes InnerClasses # Needed for `getDeclaredClasses`.
#-if @kotlinx.serialization.Serializable class
#com.example.myapplication.HasNamedCompanion, # <-- List serializable classes with named companions.
#com.example.myapplication.HasNamedCompanion2
#{
#    static **$* *;
#}
#-keepnames class <1>$$serializer { # -keepnames suffices; class is kept when serializer() is kept.
#    static <1>$$serializer INSTANCE;
#}
-keep class androidx.core.widget.NestedScrollView { *; }
-keep class androidx.constraintlayout.motion.widget.MotionLayout { *; }

# SimpleXML Annotations
-keepattributes *Annotation*
-keepattributes Signature

-dontwarn org.xmlpull.v1.**
-dontnote org.xmlpull.v1.**
-keep class org.xmlpull.** { *; }
-keepclassmembers class org.xmlpull.** { *; }

-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

-dontwarn androidx.appcompat.view.ContextThemeWrapper
-dontwarn androidx.window.extensions.embedding.ActivityEmbeddingComponent
-dontwarn androidx.window.extensions.layout.WindowLayoutComponent
-dontwarn androidx.window.sidecar.SidecarDeviceState
-dontwarn androidx.window.sidecar.SidecarInterface

-keep class com.newrelic.** { *; }
-dontwarn com.newrelic.**
-keepattributes Exceptions, Signature, InnerClasses, LineNumberTable

