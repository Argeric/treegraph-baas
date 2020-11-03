package org.treegraph.baas.core.heartbeat.bo;

import conflux.web3j.response.Status;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatusBo {
    private String statusRequest;
    private Status statusResponse;
    private String error;
}