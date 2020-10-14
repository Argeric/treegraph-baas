package org.treegraph.baas;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.treegraph.baas.core.util.Order;

public class OrderedRunner extends BlockJUnit4ClassRunner {

    // 测试用例的方法集合
    private static List<FrameworkMethod> testMethodList;

    public OrderedRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }

    // 重写 computeTestMethods 方法，按指定顺序排序
    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        if (testMethodList == null) {
            testMethodList = super.computeTestMethods().stream()
                .sorted((m1, m2) -> {
                    // 根据测试用例上的 Order 注解来决定执行顺序
                    Order o1 = m1.getAnnotation(Order.class);
                    Order o2 = m2.getAnnotation(Order.class);
                    if (o1 == null || o2 == null) {
                        return 0;
                    }
                    return o1.value() - o2.value();
                }).collect(Collectors.toList());
        }
        return testMethodList;
    }
}