#!/bin/bash

HOST_P2P_PORT=

while getopts "p:" opt; do
  case $opt in
    p)
        HOST_P2P_PORT=$OPTARG
        echo "host_p2p_port is set:$HOST_P2P_PORT"
        ;;
    *)
        echo "$0: invalid option -$OPTARG" >&2
	      echo "Usage: $0 [-p host_p2p_port]" >&2
	      exit
	      ;;
  esac
done

nohup ./conflux_$HOST_P2P_PORT --customized-config customized_config.toml &
