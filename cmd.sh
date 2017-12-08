#!/bin/sh

helpCmd() {
    echo "You could use the following commands:";
    echo "    help: prints all available commands to console";
    echo "    build: builds the project with gradle and npm";
    echo "    zip: zips the necessary files for deployment";
    echo "    run: runs the backend server";
    echo "    gen: creates data directory for jOOQ files";
}

buildCmd() {
    gradle build;
    cd client;
    npm install;
    npm run build;
}

zipCmd() {
    rm cetacea.zip;
    zip -r cetacea.zip .ebextensions lib client/src client/images client/favicon.ico client/.babelrc client/*.html client/package.json client/webpack.config.js src build.gradle Buildfile cmd.sh cron.yaml Procfile;
}

runCmd() {
    java -jar build/libs/Cetacea-v0.jar;
}

genCmd() {
    java -classpath "lib/jooq-3.10.1.jar;lib/jooq-meta-3.10.1.jar;lib/jooq-codegen-3.10.1.jar;lib/postgresql-9.4.1212.jar;." org.jooq.util.GenerationTool /datagen.xml
}
if [[ $1 = "help" ]]; then
    helpCmd;
elif [[ $1 = "build" ]]; then
    buildCmd;
elif [[ $1 = "zip" ]]; then
    zipCmd;
elif [[ $1 = "run" ]]; then
    runCmd;
elif [[ $1 = "gen" ]]; then
    genCmd;
else
    helpCmd;
fi