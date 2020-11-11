package org.treegraph.baas.core.service;

import org.treegraph.baas.core.das.bo.NodeKeypairBo;
import org.treegraph.baas.core.enums.MembershipEnum;
import org.treegraph.baas.core.heartbeat.bo.Heartbeat;
import org.treegraph.baas.core.heartbeat.bo.StatusBo;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public interface BlockchainRpcService {

	MembershipEnum.Status sendMembershipChangeTransactionPlus(long membershipChangeId,
			List<NodeKeypairBo> committeeNodeKeypairs, List<NodeKeypairBo> validatorNodeKeypairs) throws IOException;

	BigInteger getBftEpochNumber(String consId, String nodeId);

	boolean updateCert(String consId, String consCertPem, List<String> membershipNodeIds);
	
	String sendRawTransaction(String consId, String nodeId, String hexEncoded);

	StatusBo getStatus(Heartbeat heartbeat);

	String getType();
}
