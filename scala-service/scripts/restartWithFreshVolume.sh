#!/bin/bash

# get the directory that this script is located in
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

docker-compose down --volumes
docker-compose pull
docker-compose up -d

case "$1" in
        -c)
            sbt clean runDev
            ;;
        "")
            sbt runDev
            ;;
        *)
            echo "Invalid flag"
            exit 1
esac