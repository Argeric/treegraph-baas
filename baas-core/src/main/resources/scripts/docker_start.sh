#!/bin/bash

PORT_PEER_TO_PEER={0}
PORT_JSON_RPC={1}
PORT_G_RPC={2}
CONTAINER_NAME={3}
CONTAINER_STATUS=$(docker ps -f "name=$CONTAINER_NAME" --format '{{.Status}}')"
SCRIPT_PATH="$( cd "$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"

if [ "$CONTAINER_STATUS" ]; then
  if [[ $CONTAINER_STATUS =~ ^Up.* ]]; then
      echo "$CONTAINER_NAME is already running"
  fi
  if [[ $CONTAINER_STATUS =~ ^Exited.* ]]; then
      docker start "$CONTAINER_NAME"
  fi
else
  docker run --name "$CONTAINER_NAME" \
  -v "$SCRIPT_PATH":/opt/node \
  -p "$PORT_PEER_TO_PEER":32323 \
  -p "$PORT_JSON_RPC":12537 \
  -p "$PORT_G_RPC":22537 \
  --memory 16g \
  --memory-swap 4g \
  --cpus 8 \
  -d tree-graph-docker
  sleep 30
  echo "$CONTAINER_NAME now is running"
fi
