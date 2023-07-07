use serde::{Serialize, Deserialize};

#[derive(Debug, Deserialize, Serialize, Clone)]
pub struct Event {
    pub id: u64,
    pub reason: String,
}