FROM rust:1.70.0-slim AS base

WORKDIR /app

# Install target platform (Cross-Compilation) --> Needed for Alpine
RUN rustup target add x86_64-unknown-linux-musl

COPY Cargo.toml Cargo.lock ./

COPY ./src ./src

RUN cargo build --target x86_64-unknown-linux-musl --release


FROM alpine:3.18.2 AS runtime 

COPY --from=base /app/target/x86_64-unknown-linux-musl/release/rust-service /app/rust-service

ENTRYPOINT ["/app/rust-service"]