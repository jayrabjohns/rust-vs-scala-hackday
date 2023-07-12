# rust-vs-scala
An exploration of rust and its ecosystem with the purpose of building microservices, and a comparison of the same thing in scala.

Work from the Flexys hackday 07/2023.

The goal of this project was to demonstrate an enthusiasm for rust and to explore how to do common things microservices need to do in rust. 

The main benefit of using rust to build microservices instead of scala is that operationally spekaing they're much easier to run. They startup very quickly,  have a very low CPU usage when idle, don't have the heavy memory footprint of the jvm, and most importantly they work with kubernetes' horizontal scaling out the box.

Rust also carries many of the benefits of scala over other general purpose languages; such as, code style influenced from functional languages, a strict compiler, an expressive type system, and it's open source.

It would take time to educate the team on how to effectively use rust, ofcourse like with introducing any technology to an existing stack, but I think the long term benefits far outway this cost. Flexys have observed issues with scala after using it for 7+ years, and I believe rust fixes a lot of those without compromising what made them choose scala in the first place.

## An example microservice
The service consists of a web server exposing an HTTP endpoint, which produces an event to kafka when called. The idea was to use dependencies which would be realistic if I was building this for production.

Dependencies used are:
- warp: HTTP server based on tokio with a functional syntax
- serde: JSON (de/se)rialisation
- kafka:  kafka client library 
- tokio: asynchronouse runtime

The same service was also built in scala. It's not a line for line recreation, but both versions use mannerisms of their respective language. It exposes the same endpoint and publishes the same event onto the same topic on the instance of kafka.

## Next time
I didn't have time to explore everything I wanted to, given the hackday only lasted one working day.

Given more time I would explore:
- getting this example working with CI, probably in the form of a github action.
- testing frameworks
- logging
- setting it up with kubernetes & terraform
- exploring horizontal scaling in kubernetes

## Results

### Docker images
For building instructions [see below](#scala-service)

Using `docker images` we can measure the image size.

```shell
docker images | grep scala-service
scala-service                 0.0.1-SNAPSHOT   98c49132e523   2 days ago      270MB
scala-service                 latest           98c49132e523   2 days ago      270MB
```

```shell
docker images | grep rust-service
rust-service-alpine                                        latest           d9ffd2742794   2 days ago      15.4MB
```

15.4MB vs 270MB.

### Performance
Using `docker stats` we're able to measure CPU and memory statistics of running docker containers.

A summary of averages taken by eye of each service at idle and under load can be found below. This isn't a rigorous performance test, but rather a demonstration of the kinds of performance benefits rust can deliver.

Load was simulated with a helper script found at `scripts/go.sh`.

|       | Memory - Idle | Memory - Load | CPU - Idle | CPU - Load |
| ----- | ------------- | ------------- | ---------- | ---------- |
| Scala | 150MB         | 200MB         | 20%        | 80%        |
| Rust  | 1MB           | 1.5MB         | 0%         | 5%         |

## Running locally
First bring up the service dependencies with docker-compose

```shell
docker-compose up -d
```

They can be brought down again afterwards with this
```shell
docker-compose down
```

Then follow specific instructions for each service below.

### Scala service
You'll need `sbt` and `scala` installed. https://docs.scala-lang.org/getting-started/index.html
```shell
cd scala-service
sbt run
```

To run as a container:
```shell
docker run --name scala-service --init --rm --net=host -p 7878:7878 scala-service
```

The docker image is built using an sbt plugin. https://www.scala-sbt.org/sbt-native-packager/formats/docker.html
```shell
sbt docker:publishLocal
```

### Rust service
You'll need rustup. https://www.rust-lang.org/tools/install
```shell
cd rust-service
cargo run
```

To run as a container:
```shell
docker run --name rust-service --init --rm --net=host -p 7878:7878 rust-service-alpine
```

The docker image is built from the provided dockerfile.
```shell
docker build -t rust-service .
```

### Kafkacat
`kafkacat` can be used to verify that events are indeed being produced to kafka, and by extension that the endpoint is working and identical in both services. https://github.com/edenhill/kcat

```shell
kafkacat -C -b localhost:9092 -t testing-topic
```