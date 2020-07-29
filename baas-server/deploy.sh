#!/bin/bash
  
stop(){
        docker-compose -p baas down
        docker rmi baas_backend_image
}

start(){
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