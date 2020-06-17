#!/bin/bash

APP_NAME=baas-server.jar
SOURCE_PATH=/opt/software/maven-repo/org/treegraph/baas/baas-server/1.0.0-SNAPSHOT/baas-server-1.0.0-SNAPSHOT.jar
TARGET_PATH=/opt/service/treegraph-baas/$APP_NAME
BUILD_ID=dontKillMe
source /etc/profile

#启动方法
start(){
        pid=`ps -ef|grep $APP_NAME|grep -v grep|awk '{print $2}'`
        if [ "$pid" ]; then
                echo "$APP_NAME is already running. pid=$pid ."
        else
                rm -rf nohup.out
                rm -rf $APP_NAME
                cp $SOURCE_PATH $TARGET_PATH
                nohup java -Xms1024m -Xmx2048m -jar ../$APP_NAME 2>&1 &
                sleep 30
                echo "$APP_NAME now is running"
        fi
}

#停止方法
stop(){
        pid=`ps -ef|grep $APP_NAME|grep -v grep|awk '{print $2}'`
        if [ "$pid" ]; then
                kill -9 $pid
                echo "Pid:$pid stopped"
        else
                echo "$APP_NAME is not running"
        fi
}

#输出运行状态
status(){
        pid=`ps -ef|grep $APP_NAME|grep -v grep|awk '{print $2}'`
        if [ "$pid" ]; then
                echo "$APP_NAME is running. Pid is ${pid}"
        else
                echo "$APP_NAME is NOT running."
        fi
}

#根据输入参数，选择执行对应方法，不输入则执行使用说明
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
                sleep 5
                start
                ;;
      *)
                echo "Usage:{start|stop|status|restart}"
                ;;
esac

exit 0
