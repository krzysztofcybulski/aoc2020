plugins {
    kotlin("jvm") version "1.4.21"
    groovy
}

group = "me.kcybulski"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jsoup:jsoup:1.13.1")
    testImplementation("org.codehaus.groovy:groovy-all:2.5.10")
    testImplementation("org.spockframework:spock-core:1.3-groovy-2.5")
}
