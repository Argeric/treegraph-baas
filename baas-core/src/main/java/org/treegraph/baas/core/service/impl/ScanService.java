package org.treegraph.baas.core.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.treegraph.baas.api.dto.FavoriteInfo;
import org.treegraph.baas.api.dto.request.AddScanFavoriteRequest;
import org.treegraph.baas.api.dto.request.DeleteFavoriteRequest;
import org.treegraph.baas.api.dto.request.ListFavoriteRequest;
import org.treegraph.baas.api.dto.request.ModifyFavoriteRequest;
import org.treegraph.baas.api.dto.response.ListFavoriteResponse;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.common.util.WebHelper;
import org.treegraph.baas.core.das.BlockchainDas;
import org.treegraph.baas.core.das.ScanCustomizedFavoriteDas;
import org.treegraph.baas.core.domain.BlockChainPo;
import org.treegraph.baas.core.domain.ScanCustomizedFavoritePo;
import org.treegraph.baas.core.enums.ErrorCodeEnum;
import org.treegraph.baas.core.enums.ScanEnum.FavoriteType;
import org.treegraph.baas.core.exception.AlertException;
import org.treegraph.baas.core.exception.BusinessException;

@Service
@AllArgsConstructor
public class ScanService {

  private final RestTemplate restTemplate;

  private final BlockchainDas blockchainDas;

  private final ScanCustomizedFavoriteDas scanCustomizedFavoriteDas;

  public Object invoke(String url, Map<String, String> uriVariables) {
    HttpHeaders header = new HttpHeaders();
    ResponseEntity<Object> response;

    if (url.contains("create") || url.contains("update") || url.contains("delete")) {
      header.setContentType(
          MediaType.parseMediaType("application/x-www-form-urlencoded; charset=UTF-8"));
      MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
      if (uriVariables != null) {
        uriVariables.forEach(body::add);
      }
      HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, header);
      response = restTemplate.postForEntity(url, request, Object.class);
    } else {
      header.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
      if (uriVariables != null) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        uriVariables.forEach(builder::queryParam);
        url = builder.build().encode().toString();
      }
      HttpEntity<Object> request = new HttpEntity<>(null, header);
      response = restTemplate.exchange(url, HttpMethod.GET, request, Object.class);
    }

    if (response.getStatusCode() != HttpStatus.OK) {
      throw new BusinessException(ErrorCodeEnum.SCAN_EXCEPTION);
    } else {
      return response.getBody();
    }
  }

  public <T> T invoke(String url, Map<String, ?> uriVariables,
      ParameterizedTypeReference<T> responseType) {
    HttpHeaders header = new HttpHeaders();
    header.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
    HttpEntity<String> requestEntity = new HttpEntity<>(null, header);
    ResponseEntity<T> resp = restTemplate
        .exchange(url, HttpMethod.GET, requestEntity, responseType, uriVariables);
    if (resp.getStatusCode() != HttpStatus.OK) {
      throw new BusinessException(ErrorCodeEnum.SCAN_EXCEPTION);
    } else {
      return resp.getBody();
    }
  }

  public Long addFavorite(AddScanFavoriteRequest req) {
    String blockchainId = req.getBlockchainId();
    Assert.notNull(blockchainId, "blockchainId.not.found");
    Assert.notNull(req.getHash(), "hash.not.found");
    Assert.notNull(req.getType(), "type.not.found");
    FavoriteType favoriteType = FavoriteType.getByCode(req.getType());
    Assert.notNull(favoriteType, "favorite.type.not.supported");
    BlockChainPo blockChain = blockchainDas.findById(blockchainId)
        .orElseThrow(() -> new AlertException("blockchain.not.found"));

    List<ScanCustomizedFavoritePo> favoriteList = scanCustomizedFavoriteDas
        .findAll(WebHelper.getUserId(), req.getBlockchainId());
    Optional<ScanCustomizedFavoritePo> existFavorite = favoriteList.stream().filter(
        item -> (item.getType() == favoriteType.getCode() && item.getHash().equals(req.getHash())))
        .findFirst();
    if(existFavorite.isPresent()){
      throw new AlertException("favorite.existed, you can edit it!");
    }

    LocalDateTime now = LocalDateTime.now();
    String oper = WebHelper.getUserId();
    ScanCustomizedFavoritePo favorite = ScanCustomizedFavoritePo.builder()
        .consortiumId(blockChain.getConsortiumId())
        .blockchainId(blockchainId)
        .type(favoriteType.getCode())
        .hash(req.getHash())
        .remark(req.getRemark())
        .deleteFlag(IntTypeEnum.FALSE.getValue())
        .createTime(now)
        .createBy(oper)
        .updateTime(now)
        .updateBy(oper)
        .build();
    scanCustomizedFavoriteDas.add(favorite);
    return favorite.getId();
  }

  public boolean modifyFavorite(ModifyFavoriteRequest req) {
    Assert.notNull(req.getId(), "favorite.id.not.found");
    ScanCustomizedFavoritePo favorite = scanCustomizedFavoriteDas.findById(req.getId())
        .orElseThrow(() -> new AlertException("favorite.not.found"));

    ScanCustomizedFavoritePo updateFavorite = ScanCustomizedFavoritePo.builder()
        .id(favorite.getId())
        .remark(req.getRemark())
        .updateTime(LocalDateTime.now())
        .updateBy(WebHelper.getUserId())
        .build();
    return scanCustomizedFavoriteDas.modify(updateFavorite);
  }

  public boolean deleteFavorite(DeleteFavoriteRequest req) {
    Assert.notNull(req.getId(), "favorite.id.not.found");
    scanCustomizedFavoriteDas.findById(req.getId())
        .orElseThrow(() -> new AlertException("favorite.not.found"));
    return scanCustomizedFavoriteDas.delete(req.getId());
  }

  public ListFavoriteResponse listFavorites(ListFavoriteRequest req) {
    PageHelper.startPage(req.getCurrent(), req.getPageSize());
    List<ScanCustomizedFavoritePo> favoriteList = scanCustomizedFavoriteDas
        .findAll(WebHelper.getUserId(), req.getBlockchainId());
    PageInfo<ScanCustomizedFavoritePo> pageInfo = new PageInfo<>(favoriteList);
    List<ScanCustomizedFavoritePo> favorites = pageInfo.getList();

    List<FavoriteInfo> favoriteInfoList = favorites.stream().map(item -> FavoriteInfo.builder()
        .id(item.getId())
        .type(item.getType())
        .hash(item.getHash())
        .remark(item.getRemark())
        .build()).collect(Collectors.toList());

    return ListFavoriteResponse.builder()
        .current(req.getCurrent())
        .pageSize(req.getPageSize())
        .total(pageInfo.getTotal())
        .list(favoriteInfoList)
        .build();
  }
}
