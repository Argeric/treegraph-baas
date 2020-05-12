package org.treegraph.baas.core.service;

import java.util.Map;

public interface FootStoneService {

    public <T> T invoke(String url, Map<String, ?> params, Class<T> reponseClass);
}
