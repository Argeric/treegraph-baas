# treegraph-baas
数图baas系统

##一、结构模块
* --baas
* |----baas-api    存放对他接口dto
* |----baas-common 存放各模块公用的helper类等
* |----baas-core   业务逻辑处理层，包括service业务逻辑层和das数据库事务层
* |----baas-server 提供web接口直接访问层，存放controller，和请求拦截器等

##二、软件版本信息
* Springboot 2.x
* Mysql 8.0.x
* Java SE 11(LTS)
