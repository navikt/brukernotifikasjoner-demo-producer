package no.nav.brukernotifikasjon;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import no.nav.brukernotifikasjon.schemas.Beskjed;
import no.nav.brukernotifikasjon.schemas.Nokkel;
import org.apache.kafka.clients.producer.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Properties;

public class BeskjedProducer {

    private final static Logger LOG = LogManager.getLogger();

    public static void main(String[] args) {
        final Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081/");

        final KafkaProducer<Nokkel, Beskjed> producer = new KafkaProducer<>(props);

        // close producer on shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("closing producer...");
            producer.flush();
            producer.close();
            LOG.info("done!");
        }));

        Nokkel nokkel = createNokkel();
        Beskjed beskjed = createBeskjed();
        final ProducerRecord<Nokkel, Beskjed> record = new ProducerRecord<>("aapen-brukernotifikasjon-nyBeskjed-v1-testing", nokkel, beskjed);

        //produce record
        producer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (exception == null) {
                    LOG.info("Record send to Topic: " + metadata.topic() + " Partition: " + metadata.partition() + " Offset: " + metadata.offset());
                } else {
                    LOG.error("Failed to send record to Kafka", exception);
                }

            }
        });

    }

    private static Nokkel createNokkel() {
        Long unikEventIdForDenneSystembrukeren = Instant.now().toEpochMilli();
        return new Nokkel("enSystemBruker", unikEventIdForDenneSystembrukeren.toString());
    }

    private static Beskjed createBeskjed() {
        Instant tidspunkt = Instant.now();
        String fnr = "000";
        String grupperingsId = "gruppeId1";
        String tekst = "Denne er en oppgave produsert av et eksempel. (" + tidspunkt.atZone(ZoneId.of("Europe/Oslo")) + ")";
        String link = "https://www.nav.no";
        int sikkerhetsnivaa = 4;
        Instant synligFremTil = Instant.now().plus(1, ChronoUnit.HOURS);
        return new Beskjed(tidspunkt.toEpochMilli(), synligFremTil.toEpochMilli(), fnr, grupperingsId, tekst, link, sikkerhetsnivaa);
    }

}
