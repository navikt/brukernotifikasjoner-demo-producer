### Brukernotifikasjoner demo-producer

Dette er et mini-prosjekt som viser hvordan man kan lage en minimal kafka-producer for å publisere brukernotifikasjoner 
som vil vises på forsiden av DittNAV.

## Komme i gang
1. Start DittNAV og dets avhengigheter lokalt på din maskin, følg innstruksjonene på: `https://github.com/navikt/dittnav-docker-compose`
2. Bygg dette prosjektet (demo-producer): `gradle clean build`
3. Skriv en av de følgende kommandoene for å produsere eventer:
* Oppgave: `gradle nyoppgave`
* Beskjed: `gradle nybeskjed`
4. Gå til DittNAV som kjører på lokalhost: `http://localhost:3000`
5. Logg deg inn med brukeren med identen `000`

# Henvendelser

Spørsmål knyttet til koden eller prosjektet kan rettes mot https://github.com/orgs/navikt/teams/personbruker

## For NAV-ansatte

Interne henvendelser kan sendes via Slack i kanalen #team-personbruker.
