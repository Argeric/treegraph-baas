package org.treegraph.baas.core.service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import org.treegraph.baas.core.das.bo.NodeKeypairBo;
import org.treegraph.baas.core.enums.MembershipEnum;

public interface BlockchainRpcService {

	MembershipEnum.Status sendMembershipChangeTransactionPlus(long membershipChangeId,
			List<NodeKeypairBo> committeeNodeKeypairs, List<NodeKeypairBo> validatorNodeKeypairs) throws IOException;

	BigInteger getBftEpochNumber(String consId, String nodeId);

	boolean updateCert(String consId, String consCertPem, List<String> membershipNodeIds);
}
