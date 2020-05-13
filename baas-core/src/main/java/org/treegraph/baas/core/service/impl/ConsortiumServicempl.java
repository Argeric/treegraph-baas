package org.treegraph.baas.core.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.treegraph.baas.api.dto.ConsInfo;
import org.treegraph.baas.api.dto.request.AddNodesRequest;
import org.treegraph.baas.api.dto.request.CreateBlockchainRequest;
import org.treegraph.baas.api.dto.request.CreateConsAdminRequest;
import org.treegraph.baas.api.dto.request.CreateConsRequest;
import org.treegraph.baas.api.dto.request.DeleteConsRequest;
import org.treegraph.baas.api.dto.request.GetDeployScriptRequest;
import org.treegraph.baas.api.dto.request.ListConsRequest;
import org.treegraph.baas.api.dto.request.ModifyConsRequest;
import org.treegraph.baas.api.dto.response.ListConsResponse;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.common.util.WebHelper;
import org.treegraph.baas.core.das.ConsortiumDas;
import org.treegraph.baas.core.domain.ConsortiumPo;
import org.treegraph.baas.core.enums.ConsortiumEnum;
import org.treegraph.baas.core.service.ConsortiumService;
import org.treegraph.baas.core.util.CollectionUtils;
import org.treegraph.baas.core.util.DateTimeUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.AllArgsConstructor;

@Validated
@Service
@AllArgsConstructor
public class ConsortiumServicempl implements ConsortiumService {
    
    private ConsortiumDas consortiumDas;
    
   
	public ListConsResponse listCons(ListConsRequest req) {
		
		PageHelper.startPage(req.getCurrent(), req.getPageSize());
		List<ConsortiumPo> consList = consortiumDas.findAll();
		PageInfo<ConsortiumPo> pageInfo = new PageInfo<>(consList);
		
		List<ConsortiumPo> conses = pageInfo.getList();
		List<ConsInfo> userInfoList = conses.stream().map(item -> ConsInfo.builder()
				.id(item.getId())
				.name(item.getName())
				.remark(item.getDescription())
				.contact(item.getContactName())
				.phone(item.getContactPhone())
				.email(item.getContactEmail())
				.avatar(new String(item.getAvatar()))
				.createUser(item.getCreateBy())
				.createTime(DateTimeUtils.formatTime(item.getCreateTime(), DateTimeUtils.FORMAT_yyyyMMddHHmmss))
				.status(item.getStatus())
				.step(item.getEstablishStep())
				.build()).collect(Collectors.toList());
		
		ListConsResponse listConsResponse = ListConsResponse.builder()
				.total(pageInfo.getTotal())
				.list(userInfoList)
				.build();
		
		return listConsResponse;
	}
	
	public boolean createCons(CreateConsRequest req) {
		
		List<ConsortiumPo> consList = consortiumDas.findByName(req.getName());
		Assert.isTrue(CollectionUtils.isEmpty(consList), "新创建联盟名称已被使用["+req.getName()+"]");
		
		LocalDateTime now = LocalDateTime.now();
		String oper = WebHelper.getUserId();
		ConsortiumPo cons = ConsortiumPo.builder()
				.id(UUID.randomUUID().toString())
				.name(req.getName())
				.avatar(req.getAvatar() != null ? req.getAvatar().getBytes() : null)
				.description(req.getRemark())
				.contactName(req.getContact())
				.contactEmail(req.getEmail())
				.contactPhone(req.getPhone())
				.status(ConsortiumEnum.Status.ESTABLISHING.getCode())
				.establishStep(ConsortiumEnum.EstablishStep.TO_ESTAB_CONS_ADMIN.getCode())
				.deleteFlag(IntTypeEnum.FALSE.getValue())
				.createTime(now)
				.createBy(oper)
				.updateTime(now)
				.updateBy(oper)
				.build();
		
		return consortiumDas.createConsortium(cons) > 0;
	}
	

	public boolean modifyCons(ModifyConsRequest req) {
		
		List<ConsortiumPo> consList = consortiumDas.findByName(req.getName());
		Assert.isTrue(CollectionUtils.isEmpty(consList), "新创建联盟名称已被使用["+req.getName()+"]");
		
		ConsortiumPo cons = ConsortiumPo.builder()
				.id(req.getId())
				.name(req.getName())
				.description(req.getRemark())
				.contactName(req.getContact())
				.contactPhone(req.getPhone())
				.contactEmail(req.getEmail())
				.avatar(req.getAvatar() != null ? req.getAvatar().getBytes() : null)
				.updateTime(LocalDateTime.now())
				.updateBy( WebHelper.getUserId())
				.build();
		
		return consortiumDas.modifyCons(cons) > 0;
	}

	public boolean deleteCons(DeleteConsRequest req) {
		
		ConsortiumPo cons = ConsortiumPo.builder()
				.id(req.getId())
				.deleteFlag(IntTypeEnum.TRUE.getValue())
				.updateTime(LocalDateTime.now())
				.updateBy( WebHelper.getUserId())
				.build();
		return consortiumDas.modifyCons(cons) > 0;
	}
	

	public boolean createConsAdmin( CreateConsAdminRequest createConsAdminRequest) {
		return false;
	}
	

	public boolean createBlockchain( CreateBlockchainRequest createBlockchainRequest) {
		return false;
	}
	

	public boolean addNodes( AddNodesRequest addNodesRequest) {
		return false;
	}
	

	public void getDeployScript(GetDeployScriptRequest getDeployScriptRequest) {
		
	}
}
