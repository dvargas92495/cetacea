#!/bin/bash

helpCmd() {
    echo "You could use the following commands:";
    echo "    build-ci: builds and runs the project with gradle and npm for the CI environment";
    echo "    build-prod: builds the project with gradle and npm for the prod environment";
    echo "    ci-npm: npm installs for the CI environment";
    echo "    db-connect: connects to the prod psql database";
    echo "    db-copy: copies to the local psql database mirroring production schema";
    echo "    db-local: connects to the local psql database";
    echo "    db-user: creates a local db user";
    echo "    gen: creates data directory for jOOQ files";
    echo "    help: prints all available commands to console";
    echo "    run: runs the backend server";
    echo "    run-cypress: runs the cypress tests";
    echo "    setup-npm: installs the current version of npm we use";
    echo "    zip: zips the necessary files for deployment";
}

setupNpmCmd() {
    cd client;
    npm install npm@6.9.0 -g;
}

ciNpmCmd() {
    cd client;
    npm ci;
}

buildProdCmd() {
    gradle build;
    setupNpmCmd;
    npm install;
    npm run build:prod;
}

zipCmd() {
    rm cetacea.zip;
    zip -r cetacea.zip .ebextensions lib client/src client/images client/favicon.ico client/.babelrc client/*.html client/package.json client/webpack.config.js client/webpack.config.prod.js src/main build.gradle Buildfile cmd.sh cron.yaml Procfile;
}

runCypressCmd() {
    cd client;
    npm run cypress;
}

runCmd() {
    java -jar -Dapple.awt.UIElement=true build/libs/Cetacea-v0.jar;
}

buildCiCmd() {
    gradle build;
    runCmd &
    cd client;
    npm run build:prod;
}

genCmd() {
    java -classpath "lib/jooq-3.10.1.jar;lib/jooq-meta-3.10.1.jar;lib/jooq-codegen-3.10.1.jar;lib/postgresql-9.4.1212.jar;." org.jooq.util.GenerationTool /datagen.xml
}

dbUserCmd() {
    psql -U postgres -d postgres -c "CREATE USER cetacea WITH PASSWORD 'passwerd'";
}

dbCopyCmd() {
    pg_dump -h "aanlh5mrzrcgku.c2sjnb5f4d57.us-east-1.rds.amazonaws.com" -U "cetacea" -p 5432 -Cs postgres | psql -U cetacea postgres;
}

dbConnectCmd() {
    psql -h "aanlh5mrzrcgku.c2sjnb5f4d57.us-east-1.rds.amazonaws.com" -U "cetacea" -p 5432 -d postgres
}

 dbLocalCmd() {
    psql -U "cetacea" -p 5432 -d postgres
}

if [[ $1 = "build-ci" ]]; then
    buildCiCmd;
elif [[ $1 = "build-prod" ]]; then
    buildProdCmd;
elif [[ $1 = "ci-npm" ]]; then
    ciNpmCmd;
elif [[ $1 = "db-copy" ]]; then
    dbCopyCmd;
elif [[ $1 = "db-connect" ]]; then
    dbConnectCmd;
elif [[ $1 = "db-local" ]]; then
    dbLocalCmd;
elif [[ $1 = "db-user" ]]; then
    dbUserCmd;
elif [[ $1 = "gen" ]]; then
    genCmd;
elif [[ $1 = "help" ]]; then
    helpCmd;
elif [[ $1 = "run" ]]; then
    runCmd;
elif [[ $1 = "run-cypress" ]]; then
    runCypressCmd;
elif [[ $1 = "setup-npm" ]]; then
    setupNpmCmd;
elif [[ $1 = "zip" ]]; then
    zipCmd;
else
    helpCmd;
fi