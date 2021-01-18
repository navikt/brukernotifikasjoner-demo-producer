package no.nav.brukernotifikasjon;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import no.nav.brukernotifikasjon.schemas.Nokkel;
import no.nav.brukernotifikasjon.schemas.Oppgave;
import no.nav.brukernotifikasjon.schemas.builders.OppgaveBuilder;
import org.apache.kafka.clients.producer.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Properties;

public class OppgaveProducer {

    private final static Logger LOG = LogManager.getLogger();

    public static void main(String[] args) {
        final Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081/");

        final KafkaProducer<Nokkel, Oppgave> producer = new KafkaProducer<>(props);

        // close producer on shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("closing producer...");
            producer.flush();
            producer.close();
            LOG.info("done!");
        }));

        Nokkel nokkel = createNokkel();
        Oppgave oppgave = createOppgave();
        final ProducerRecord<Nokkel, Oppgave> record = new ProducerRecord<>("aapen-brukernotifikasjon-nyOppgave-v1-testing", nokkel, oppgave);

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

    private static Oppgave createOppgave() {
        LocalDateTime now = LocalDateTime.now();
        String fnr = "12345678901";
        String grupperingsId = "gruppeId1";
        String tekst = "Denne er en oppgave produsert av et eksempel. (" + now.atZone(ZoneId.of("Europe/Oslo")) + ")";
        URL link = createLink();
        int sikkerhetsnivaa = 4;
        return new OppgaveBuilder()
                .withTidspunkt(now)
                .withFodselsnummer(fnr)
                .withGrupperingsId(grupperingsId)
                .withTekst(tekst)
                .withLink(link)
                .withSikkerhetsnivaa(sikkerhetsnivaa)
                .build();
    }

    private static URL createLink() {
        URL link = null;
        try {
            link = new URL("https://www.nav.no");
        } catch (MalformedURLException e) {
            LOG.error("URL hadde ugyldig format", e);
        }
        return link;
    }
}
