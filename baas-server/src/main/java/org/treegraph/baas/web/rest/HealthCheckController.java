package org.treegraph.baas.web.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.treegraph.baas.api.dto.BaasResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "健康检查模块")
public class HealthCheckController {
	
    @RequestMapping(value = {"/health"}, method = RequestMethod.GET)
    @ApiOperation("健康检查接口")
    public BaasResult<Void> healthCheck() {
    	
        return BaasResult.success();
    }
}
