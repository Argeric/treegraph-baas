package org.treegraph.baas.core.service;

import java.util.List;

import org.treegraph.baas.api.dto.UserInfo;
import org.treegraph.baas.api.dto.request.CreateUserRequest;
import org.treegraph.baas.api.dto.request.DeleteUserRequest;
import org.treegraph.baas.api.dto.request.ListUserRequest;
import org.treegraph.baas.api.dto.request.ModifyPwdRequest;
import org.treegraph.baas.api.dto.request.ModifyUserRequest;
import org.treegraph.baas.api.dto.request.QueryUserRequest;
import org.treegraph.baas.api.dto.response.ListUserResponse;

public interface UserService {

	boolean createUser(CreateUserRequest req);
	
	boolean deleteUser(DeleteUserRequest req);
	
	List<UserInfo> queryUser(QueryUserRequest queryUserRequest);
	
	ListUserResponse listUser(ListUserRequest listUserRequest);
	
	boolean modifyPwd(ModifyPwdRequest modifyPwdRequest);
	
	boolean modifyUser(ModifyUserRequest modifyUserRequest);
}
