package org.treegraph.baas.core.das;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.treegraph.baas.core.domain.UserInfoPo;
import org.treegraph.baas.core.domain.UserTokenPo;

public interface UserTokenDas {

  String createToken(@NotNull UserInfoPo user);

  String updateByUserId(String token, @NotEmpty String userId);

  boolean deleteByUserId(String userId);
  
  UserTokenPo findByUserId(String userId);

}
