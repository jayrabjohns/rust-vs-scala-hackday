mod api;
mod config;

use std::sync::Arc;

use api::router;
use config::{KafkaConfig, Config};
use kafka::producer::{Producer, RequiredAcks};
use tokio::sync::Mutex;
use warp::Filter;

const CONFIG: Config = config::load();

#[tokio::main]
async fn main() {
    let kafka_producer = kafka_producer(CONFIG.kafka);
    start_server(kafka_producer).await;
}

async fn start_server(kafka_producer: Arc<Mutex<Producer>>) {
    let api = router::router(kafka_producer, CONFIG.kafka.topic);
    let router_with_logging = api.with(warp::log("todos"));

    println!("Starting web server...");
    warp::serve(router_with_logging).run(([127, 0, 0, 1], 7878)).await;
}

fn kafka_producer(config: KafkaConfig) -> Arc<Mutex<Producer>> {
    Arc::new(Mutex::new(
        Producer::from_hosts(vec![String::from(config.host)])
            .with_ack_timeout(config.ack_timeout)
            .with_required_acks(RequiredAcks::One)
            .create()
            .expect("Error meaning kafka producer couldn't be built")))
}