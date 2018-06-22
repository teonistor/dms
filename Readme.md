# Dead Man's Switch on GAE

Based on GAE's [taskqueue-push](https://github.com/GoogleCloudPlatform/java-docs-samples/tree/master/appengine-java8/taskqueues-push) example

<a href="https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/GoogleCloudPlatform/java-docs-samples&page=editor&open_in_editor=appengine-java8/taskqueues-push/README.md">
<img alt="Open in Cloud Shell" src ="http://gstatic.com/cloudssh/images/open-btn.png"></a>

This sample demonstrates how to use the [TaskQueue API][taskqueue-api] on [Google App
Engine][ae-docs].

[taskqueue-api]: https://cloud.google.com/appengine/docs/java/javadoc/com/google/appengine/api/taskqueue/package-summary
[ae-docs]: https://cloud.google.com/appengine/docs/java/

## Setup

    gcloud init

## Running locally
Using package google-cloud-sdk-app-engine-java:

    alias gcj=/usr/lib/google-cloud-sdk/bin/java_dev_appserver.sh # Do this once or put in ~/.bashrc
    mvn package # Do this whenever changing non-.java files
    gcj target/dms-1.0-SNAPSHOT/

Or use the
[Maven gcloud plugin](https://cloud.google.com/appengine/docs/java/tools/using-maven).
To run this sample locally:

    mvn appengine:run

Go to `localhost:8080`. Cloud datastore will be used even when running locally, see file `Wrestling.txt` for a summary of how setting this up can go wrong. Email sending only works when deployed, after authorising a sender in Project Settings in GAE. I think at some point you need to:

    gcloud beta auth application-default login

## Deploying

    mvn appengine:deploy

