package org.treegraph.baas.core.das;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.treegraph.baas.core.domain.UserInfoPo;
import org.treegraph.baas.core.domain.UserLoginTokenPo;

public interface UserTokenDas {

  String createToken(@NotNull UserInfoPo user);

  String updateByUserId(String token, @NotEmpty String userId);

  boolean deleteByUserId(String userId);
  
  UserLoginTokenPo findByUserId(String userId);

}
