#!/bin/bash
  
stop(){
        cd /opt/service/treegraph-baas-container
        docker-compose -p baas down
        docker rmi baas_backend_image
}

start(){
        cd /opt/service/treegraph-baas-container
        cp /root/.jenkins/workspace/treegraph-baas-container1/baas-server/target/treegraph-baas.jar ./jar
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