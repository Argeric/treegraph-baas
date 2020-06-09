一、打开根CA的pki加密引擎
vault secrets enable pki
二、配置根证书最大续约时间
vault secrets tune -max-lease-ttl=87600h pki

三、产生根CA
vault write pki/root/generate/internal common_name=baas.treegraph.org ttl=87600h

四、配置证书发行url
vault write pki/config/urls issuing_certificates="http://127.0.0.1:8200/v1/pki/ca"
五、配置发行证书的角色
vault write pki/roles/baas-treegraph-dot-org allowed_domains=baas.treegraph.org allow_subdomains=true max_ttl=72h
六、通过根CA发行证书
vault write pki/issue/baas-treegraph-dot-org common_name=a.baas.treegraph.org

===========================================
七、打开中间CA的pki加密引擎
vault secrets enable -path=pki_int pki
八、配置中间CA最大续约时间
vault secrets tune -max-lease-ttl=43800h pki_int

九、中间CA发起“证书签名请求”（csr）
vault write pki_int/intermediate/generate/internal common_name="myvault.com Intermediate Authority" ttl=43800h
十、使用根CA对中间证书进行签名
vault write pki/root/sign-intermediate csr=@pki_int.csr format=pem_bundle ttl=43800h
十一、中间CA保存签名
vault write pki_int/intermediate/set-signed certificate=@signed_certificate.pem

十二、中间CA配置证书发行url
vault write pki_int/config/urls issuing_certificates="http://127.0.0.1:8200/v1/pki_int/ca"
十三、配置中间CA发行角色
vault write pki_int/roles/baas-treegraph-dot-org allowed_domains=baas.treegraph.org allow_subdomains=true max_ttl=72h
十四、通过中间CA发行证书
vault write pki_int/issue/baas-treegraph-dot-org common_name=b.baas.treegraph.org