plugins {
    java
}

group = "no.nav.brukernotifikasjon"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://packages.confluent.io/maven/")
    maven("https://jitpack.io")
}

val avroVersion = "1.8.2"
val brukernotifikasjonerVersion = "1.2021.01.18-11.12-b9c8c40b98d1"
val confluentVersion = "5.0.2"
val log4j2Version = "2.16.0"
val kafkaClientsVersion = "2.0.1"

dependencies {
    implementation("com.github.navikt", "brukernotifikasjon-schemas", brukernotifikasjonerVersion)
    implementation("io.confluent", "kafka-streams-avro-serde", confluentVersion)
    implementation("org.apache.avro", "avro", avroVersion)
    implementation("org.apache.logging.log4j", "log4j-api", log4j2Version)
    implementation("org.apache.logging.log4j", "log4j-core", log4j2Version)
    implementation("org.apache.logging.log4j", "log4j-slf4j-impl", log4j2Version)
    testImplementation("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks {

    register("nyoppgave", JavaExec::class) {
        main = "no.nav.brukernotifikasjon.OppgaveProducer"
        classpath = sourceSets["main"].runtimeClasspath
    }

    register("nybeskjed", JavaExec::class) {
        main = "no.nav.brukernotifikasjon.BeskjedProducer"
        classpath = sourceSets["main"].runtimeClasspath
    }

}
