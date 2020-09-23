#!/bin/bash

HOST_LOG_PATH=/opt/service/blockchain/tree_graph_node
HOST_WEB_PORT=38778
CONTAINER_NETWORK=178.18.0.0/16

while getopts "p:P:n:" opt; do
  case $opt in
    p)
        HOST_LOG_PATH=$OPTARG
        echo "log_path is $HOST_LOG_PATH" ;;
    P)
        HOST_WEB_PORT=$OPTARG
        echo "listen_port is $HOST_WEB_PORT" ;;
    n)
	CONTAINER_NETWORK=$OPTARG
	echo "container_network is $CONTAINER_NETWORK" ;;
    \?)
        echo "$0: invalid option -$OPTARG" >&2
	echo "Usage: $0 [-p log_path] [-P listen_port] [-n container_network]" >&2
	exit
	;;

  esac
done

touch .env
cat > .env <<_EOF_
HOST_LOG_PATH=$HOST_LOG_PATH
HOST_WEB_PORT=$HOST_WEB_PORT
CONTAINER_NETWORK=$CONTAINER_NETWORK
_EOF_

docker-compose up -d
