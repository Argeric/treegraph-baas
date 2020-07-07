#!/bin/bash
  
IMAGE_NAME=registry.cn-beijing.aliyuncs.com/conflux-chain/baas-server
IMAGE_TAG=$1
CONTAINER_NAME=baas-server
HOST_PORT=19797
#dockerfile expose
CONTAINER_PORT=9797

old_container_ids=`docker ps|grep $IMAGE_NAME|grep -v $IMAGE_TAG|awk '{print $1}'`
if [ -n "$old_container_ids" ]; then
        docker rm -f $old_container_ids
fi
old_image_ids=`docker images|grep $IMAGE_NAME|grep -v $IMAGE_TAG|awk '{print $3}'`
if [ -n "$old_image_ids" ]; then
        docker rmi $old_image_ids
fi

container_id=`docker ps|grep $IMAGE_NAME:$IMAGE_TAG|awk '{print $1}'`
if [ -n "$container_id" ]; then
        echo "$IMAGE_NAME:$IMAGE_TAG is already running. containerId=$container_id. restart..."
        docker restart $container_id
else
        echo "$IMAGE_NAME:$IMAGE_TAG is not running. first start..."
        docker run -d -p $HOST_PORT:$CONTAINER_PORT --name $CONTAINER_NAME $IMAGE_NAME:$IMAGE_TAG
fi