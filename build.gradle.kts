plugins {
    kotlin("jvm") version "1.4.20"
    groovy
}

group = "me.kcybulski"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation("org.codehaus.groovy:groovy-all:2.5.10")
    testImplementation("org.spockframework:spock-core:1.3-groovy-2.5")
}
