package org.treegraph.baas.api.dto.response;

import java.util.List;

import org.treegraph.baas.api.dto.NodeInfo;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Builder;
import lombok.Data;

/**
 * @author zt
 * @version 1.0
 * @date May 11, 2020
 */
@Data
@Builder
public class QueryBlockchainDetailResponse {

  private String id;//区块链id
  private String name;//区块链名称
  private String alertEmail;//报警邮件
  private List<NodeInfo> nodes;
  private String consId;//联盟id
  private String consName;//联盟名称
  @JSONField(serialize = false)
  private String consAvatar;//联盟头像
  private String consContact;//联系人姓名
  private String consEmail;//联系人邮箱
  private String consPhone;//联系人电话
  private String consensus;//区块链状态
  private String algorithm;//节点数
  private String tps;//TPS
  private String txCount;//日新增交易
}
