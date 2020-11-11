package org.treegraph.baas.core.util;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.treegraph.baas.core.component.BizTimer;
import org.treegraph.baas.core.constant.ConfluxConstant;
import org.treegraph.baas.core.das.MembershipChangeDas;
import org.treegraph.baas.core.das.NodeCustomizedConfigDas;
import org.treegraph.baas.core.das.NodeDas;
import org.treegraph.baas.core.domain.MembershipChangePo;
import org.treegraph.baas.core.domain.NodeCustomizedConfigPo;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.enums.MembershipEnum;
import org.treegraph.baas.core.heartbeat.bo.Heartbeat;
import org.treegraph.baas.core.heartbeat.impl.HeartbeatListener;
import org.treegraph.baas.core.task.CheckMembershipChangeTimerTask;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		// get nodes
		ApplicationContext ac = event.getApplicationContext();
		NodeDas nodeDas = ac.getBean(NodeDas.class);
		List<NodePo> nodes = nodeDas.findAll(null, null);
		if(CollectionUtils.isEmpty(nodes)) return;
		
		// get customized config
		List<String> nodeIds = nodes.stream().map(NodePo::getId).collect(Collectors.toList());
		NodeCustomizedConfigDas customizedConfigDas = ac.getBean(NodeCustomizedConfigDas.class);
		List<NodeCustomizedConfigPo> customizedConfigList = customizedConfigDas.findAll(null, nodeIds);	
		Map<String, Integer> rpcHttpPortMap = customizedConfigList.stream()
				.filter(item -> item.getPropertyName().equalsIgnoreCase(ConfluxConstant.PROPERTY_JSONRPC_PORT))
				.collect(Collectors.toMap(NodeCustomizedConfigPo::getNodeId, item -> Integer.parseInt(item.getPropertyValue())));
		
		// register nodes
		HeartbeatListener heartbeatListener = HeartbeatListener.getInstance();
		nodes.forEach(node -> {
			if(heartbeatListener.isNodeValid(node.getConsortiumId(), node.getId())){
				Heartbeat nodeInfo = Heartbeat.builder()
						.id(node.getId())
						.groupId(node.getConsortiumId())
						.ip(node.getNodeIp())
						.port(rpcHttpPortMap.get(node.getId())).build();
				heartbeatListener.registerNode(node.getConsortiumId(), node.getId(), nodeInfo);
			}
		});
		
		// get uncompleted membership change
		MembershipChangeDas membershipChangeDas = ac.getBean(MembershipChangeDas.class);
		List<MembershipChangePo> membershipChanges = membershipChangeDas.getMembershipChangeByStatus(
				Lists.newArrayList(MembershipEnum.Status.LAUNCH_CHECK_THREAD_SUCCESS.getCode()));
		if(CollectionUtils.isEmpty(membershipChanges)) return;
		membershipChanges.forEach(item -> {
			long intervalSec = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")) - item.getUpdateTime().toEpochSecond(ZoneOffset.of("+8"));
			long delayTimeSec = intervalSec >= 60 ? 0 : intervalSec;
			BizTimer.DELAY_TIMER.newTimeout(new CheckMembershipChangeTimerTask(item.getId()), delayTimeSec, TimeUnit.SECONDS);});
		
		// init SSL
		SSLUtils.initSSL();
		log.debug("ApplicationStartup.onApplicationEvent completed!");
	}
}
