plugins {
    java
}

group = "no.nav.brukernotifikasjon"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("http://packages.confluent.io/maven/")
}

val avroVersion = "1.8.2"
val brukernotifikasjonerVersion = "1.2020.02.07-13.16-fa9d319688b1"
val confluentVersion = "5.0.2"
val log4j2Version = "2.11.1"
val kafkaClientsVersion = "2.0.1"

dependencies {
    implementation("no.nav", "brukernotifikasjon-schemas", brukernotifikasjonerVersion)
    implementation("io.confluent", "kafka-streams-avro-serde", confluentVersion)
    implementation("org.apache.avro", "avro", avroVersion)
    implementation("org.apache.logging.log4j", "log4j-api", log4j2Version)
    implementation("org.apache.logging.log4j", "log4j-core", log4j2Version)
    implementation("org.apache.logging.log4j", "log4j-slf4j-impl", log4j2Version)
    testCompile("junit", "junit", "4.12")
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
