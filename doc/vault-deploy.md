##一、下载安装vault
* https://www.vaultproject.io/downloads，解压zip文件到安装目录${INSTALL_PATH}

##二、配置环境变量
* 为了方便CLI使用vault命令，建议配置环境变量；安全起见，建议设置环境变量只在当前客户端生效
* export VAULT_ADDR=’http://127.0.0.1:8200’
* export PATH=$PATH:${INSTALL_PATH}
* export VAULT_SKIP_VERIFY=false
或
* echo "export VAULT_ADDR=http://127.0.0.1:8200" >> /etc/profile
* echo "export VAULT_SKIP_VERIFY=false" >> /etc/profile
* source /etc/profile

##三、启动vault
* vault -config vault.hcl
* 或后端启动 nohup vault server -config=config.hcl &


```
vault.hcl

storage "path" {
  path    = "${INSTALL_PATH}/data"
}
listener "tcp" {
 address     = "127.0.0.1:8200"
 tls_disable = 1
}
disable_mlock = true
ui = true
```

##四、初始化
* 执行命令vault operator init，获得解封密钥和root token

##五、解封
* 执行命令vault operator unseal，然后输入密钥，执行由key-threshold参数指定的次数

##六、查看解封状态
* 执行命令vault status，如果输出信息中Sealed属性为false，则解封成功

##七、使用root token登陆
* 执行命令vault login token=${root token}

##八、创建用户供联盟用户使用
1. vault auth enable userpass
2. vault write auth/userpass/users/${user.name} password=${user.password} policies=admins

##九、用户登陆
* 执行命令vault login -method=userpass username=${user.name} password=${user.password}