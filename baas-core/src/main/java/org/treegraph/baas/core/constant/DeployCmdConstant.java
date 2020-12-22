package org.treegraph.baas.core.constant;

public class DeployCmdConstant {

  public static final String MKDIR = "mkdir -p /opt/service/blockchain";

  public static final String SCP_SCRIPT = "scp ./deploy/{0}/{1}/deploy.zip {2}:/opt/service/blockchain/{3}.zip";

  public static final String UNZIP_SCRIPT = "cd /opt/service/blockchain;unzip {0}.zip;rm {1}.zip";

  public static final String SCP_BINARY = "scp ./deploy/conflux_production {0}:/opt/service/blockchain/{1}/conflux_{2}";

  public static final String LAUNCH_BLOCKCHAIN = "cd /opt/service/blockchain/{0};bash ./auto_deploy.sh -p {1}";
}