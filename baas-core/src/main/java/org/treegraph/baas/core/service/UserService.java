package org.treegraph.baas.core.service;

import org.treegraph.baas.api.dto.request.CreateUserRequest;

public interface UserService {

	boolean createUser(CreateUserRequest req);
}
