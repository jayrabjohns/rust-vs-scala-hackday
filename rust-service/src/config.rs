use std::time::Duration;

pub struct Config<'a> {
    pub kafka: KafkaConfig<'a>
}

pub struct KafkaConfig<'a> {
    pub host: &'a str,
    pub ack_timeout: Duration,
    pub topic: &'a str
}

pub const fn load() -> Config<'static> {
    Config {
        kafka: KafkaConfig { 
            host: "localhost:9092",
            ack_timeout: Duration::from_secs(1),
            topic: "testing-topic"
        }
    }
}