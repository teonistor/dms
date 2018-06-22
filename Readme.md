# Dead Man's Switch on GAE

Based on GAE's [taskqueue-push](https://github.com/GoogleCloudPlatform/java-docs-samples/tree/master/appengine-java8/taskqueues-push) example

## Setup

    gcloud init

## Running locally
Using package google-cloud-sdk-app-engine-java (from e.g. `apt`):

    alias gcj=/usr/lib/google-cloud-sdk/bin/java_dev_appserver.sh # Do this once or put in ~/.bashrc
    mvn package # Do this whenever changing non-.java files, assuming an IDE with JIT compilation is used e.g. Eclipse
    gcj target/dms-1.0-SNAPSHOT/

Or using the
[Maven gcloud plugin](https://cloud.google.com/appengine/docs/java/tools/using-maven):

    mvn appengine:run

Go to `localhost:8080`. Cloud datastore will be used even when running locally, see file `Wrestling.txt` for a summary of how setting this up can go wrong. Email sending only works when deployed, after authorising a sender in Project Settings in GAE. I think at some point you need to:

    gcloud beta auth application-default login

## Deploying

    mvn appengine:deploy
    gcloud app deploy src/main/webapp/WEB-INF/cron.yaml # Whenever you change cron configuration

