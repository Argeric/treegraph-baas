#!/bin/bash
name='tree-graph-node'
SCRIPTPATH="$( cd "$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"

[[ $(docker ps -f "name=$name" --format '{{.Names}}') == $name ]] || docker run -v "$SCRIPTPATH":/opt/node --name "$name" -d tree-graph-node

docker exec -it "$name" /bin/sh /opt/node/start.sh