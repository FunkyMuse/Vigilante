-keep,includedescriptorclasses class net.sqlcipher.** { *; }
-keep,includedescriptorclasses interface net.sqlcipher.** { *; }
# NavArgsLazy creates NavArgs instances using reflection
## remove when alpha03 is released
-if public class ** implements androidx.navigation.NavArgs
-keepclassmembers public class <1> {
    ** fromBundle(android.os.Bundle);
}