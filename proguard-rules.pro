-dontwarn org.flywaydb.core.internal.**
-dontwarn org.flywaydb.core.api.migration.spring.**
-dontwarn org.flywaydb.core.api.configuration.S3ClientFactory
-dontwarn org.flywaydb.core.Flyway
-dontwarn ch.qos.logback.**
-keep class org.flywaydb.core.** { *; }
-keep class com.google.gson.** { *; }
-keep class org.sqlite.** { *; }
-keep class org.slf4j.** { *; }
-keep class ch.qos.logback.** { *; }
-keepdirectories db/migrations/**