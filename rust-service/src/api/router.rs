use std::sync::Arc;

use kafka::producer::Producer;
use tokio::sync::Mutex;
use warp::Filter;

use super::produce_event_route;

pub fn router(kafka_producer: Arc<Mutex<Producer>>, topic: &str) -> impl Filter<Extract = (impl warp::Reply,), Error = warp::Rejection> + Clone {
    produce_event_route(Arc::clone(&kafka_producer), topic)
      //.or(some_other_route)
}

fn produce_event_route(kafka_producer: Arc<Mutex<Producer>>, topic: &str) -> impl Filter<Extract = (impl warp::Reply,), Error = warp::Rejection> + Clone {
    warp::path!("fire" / u64)
        .and(warp::post())
        .and(warp::body::json())
        .and(with(kafka_producer))
        .and(with(String::from(topic)))
        .and_then(produce_event_route::produce_event)
}

fn with<T>(a: T) -> impl Filter<Extract = (T,), Error = std::convert::Infallible> + Clone 
where T: Clone + Send {
    warp::any().map(move || a.clone())
}


