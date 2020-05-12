package org.treegraph.baas.core.util;

import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;

import java.io.IOException;
import java.util.List;

import org.treegraph.baas.core.enums.ErrorCodeEnum;
import org.treegraph.baas.core.exception.BusinessException;

public class ParameterValidateUtil {
    /**
     * 验证输入参数
     * 
     * @param param
     * @throws IOException
     */
    public static void validateParameter(Object param) {
        Validator validator = new Validator();
        List<ConstraintViolation> violations = validator.validate(param);
        if (violations != null && !violations.isEmpty()) {
            throw new BusinessException(ErrorCodeEnum.INVALID_PARAMS.getCode(), violations.get(0).getMessage());
        }
    }
}
