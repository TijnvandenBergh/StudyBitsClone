#!/usr/bin/env bash
shopt -s expand_aliases
alias seeder='java -jar ./target/seeder-1.0-SNAPSHOT-jar-with-dependencies.jar'

GRONINGEN_NAME="RijksuniversiteitGroningen"
GRONINGEN_SEED="000000RijksuniversiteitGroningen"
GENT_NAME="UniversiteitGent"
GENT_SEED="0000000000000000UniversiteitGent"
# Create steward
seeder did 000000000000000000000000Steward1 steward
GRONINGEN_DID=$(seeder did $GRONINGEN_SEED $GRONINGEN_NAME)
GENT_DID=$(seeder did $GENT_SEED $GENT_NAME)

seeder onboard $GRONINGEN_SEED $GRONINGEN_NAME $GRONINGEN_DID
seeder onboard $GENT_SEED $GENT_NAME $GENT_DID

SCHEMA_ID=$(seeder schema)
SCHEMA_COURSE_ID=$(seeder schemacourse)

TRANSCRIPT="TRANSCRIPT"
PROPEDEUSE="PROPEDEUSE"


CRED_COURSE_DEF_ID=$(seeder cred-def http://localhost:8080 $SCHEMA_COURSE_ID $PROPEDEUSE)
CRED_DEF_ID=$(seeder cred-def http://localhost:8080 $SCHEMA_ID $TRANSCRIPT)
echo "CRED DEF ID $CRED_DEF_ID"
seeder student http://localhost:8080 2102241

# seeder exchange-position http://localhost:8081 $CRED_DEF_ID

touch /finished.txt

sleep 30000

