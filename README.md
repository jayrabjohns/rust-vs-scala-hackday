# rust-vs-scala



docker images | grep scala-service
scala-service                 0.0.1-SNAPSHOT   98c49132e523   2 days ago      270MB
scala-service                 latest           98c49132e523   2 days ago      270MB


docker images | grep rust-service
rust-service-alpine                                        latest           d9ffd2742794   2 days ago      15.4MB
rust-service                                               latest           e399b53b3343   2 days ago      834MB







- Api that produces an event to kafka

7.5M binary (release)
90M (debug)

slim image with release
834MB (uncompressed)


######
alpine image with release
15.4M (uncompressed)
Could probably go lower if we only use the feature flags we need

idling at arounf 1 mb
under load it doesn't increase much but that's more a fault of the test. More deserialization and waiting will have to be done in a realistic setting

#####
scala image using the same job we use for our services
270mb (uncompressed)

sitting at 150mb idle
~200mb under the same load as rust service.

####


Not a performance test but the gains are a proportion of how big the project is. There will be greater returns with larger projects.
COMMANDS

docker-compose up -d

docker run --name rust-service --init --rm --net=host -p 7878:7878 rust-service-alpine

docker run --name scala-service --init --rm --net=host -p 7878:7878 "eu.gcr.io/flexys-development/scala-service"

kafkacat -C -b localhost:9092 -t testing-topic

docker stats


docker images | grep rust-service





explore rust with a goal of seeing how to do common things we would do in scala services
things like running a web server,
sending events to kafka,
running async tasks,
organising code into modules

- web server
- fires event to kafka
- I found dependencies for serving http requests, kafka, json serialisation, tokio which is the asynchronouse runtime responsible for scheduling and running tasks, similar to cats effect which is what we use in scala services
- I made an image for it...which is 15 mb not a large service but still pretty impressive

all this doesn't mean much without a comparison to scala, so I actually made a scala service too. Has the same API but written in a scala type way, it's not a line for line translation.
this is how it performs



Things I didnt get time for were trying to get this working with a CI job,
testing frameworks, 
this working in kubernetes / terraform.