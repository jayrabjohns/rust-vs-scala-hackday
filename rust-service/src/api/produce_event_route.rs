use std::{convert::Infallible, sync::Arc};
use kafka::producer::{Producer, Record};
use tokio::sync::Mutex;
use warp::hyper::StatusCode;

use crate::api::model::{fire_event_request::FireEventRequest, event::Event};

pub async fn produce_event(id: u64, request_body: FireEventRequest, producer: Arc<Mutex<Producer>>, topic: String)-> Result<impl warp::Reply, Infallible> {
    println!("Firing event with id {id} because {0}", request_body.reason);

    let event = Event {id, reason: request_body.reason };
    let json = serde_json::to_string(&event).unwrap(); // bad
    let record = Record::from_value(topic.as_str(), json.into_bytes());
    let result = producer.lock().await.send(&record);

    match result {
        Ok(()) => Ok(StatusCode::OK),
        Err(e) => {
            println!("Error sending event to kafka: {:#?}", e);
            Ok(StatusCode::INTERNAL_SERVER_ERROR)
        }
    }
}
