#!/bin/bash
  
APP_PATH=/opt/service/treegraph-baas-test/treegraph-baas/baas-server

stop(){
        cd $APP_PATH
        docker-compose -p baas down
        docker rmi baas_backend_image
}

start(){
        cd $APP_PATH
        mkdir -p ./target
        cp /root/.jenkins/workspace/treegraph-baas-container1/baas-server/target/treegraph-baas.jar ./target
        docker-compose -p baas up -d
}

status(){
        docker ps|grep baas_backend
}

case "$1" in
        start)
                start
                ;;
        stop)
                stop
                ;;
        status)
                status
                ;;
        restart)
                stop
                start
                status
                ;;
        *)
                echo "Usage:{start|stop|status|restart}"
                ;;
esac

exit 0