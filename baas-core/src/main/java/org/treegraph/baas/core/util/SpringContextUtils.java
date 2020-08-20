package org.treegraph.baas.core.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtils implements ApplicationContextAware{
    private static ApplicationContext applicationContext;  
    
    //获取上下文  
    public static ApplicationContext getApplicationContext() {  
        return applicationContext;  
    }  
  
    //设置上下文  
    public void setApplicationContext(ApplicationContext applicationContext) {  
        SpringContextUtils.applicationContext = applicationContext;  
    }  
  
    //通过名字获取上下文中的bean  
    public static Object getBean(String name){  
        if(applicationContext==null){
            throw new UnsupportedOperationException("未初始化，无法调用");
        }
        return applicationContext.getBean(name);  
    }  
      
    //通过类型获取上下文中的bean  
    public static <T> T getBean(Class<T> requiredType){  
        if(applicationContext==null){
            throw new UnsupportedOperationException("未初始化，无法调用");
        }
        return applicationContext.getBean(requiredType);  
    }
    
    /**
     * 获取指定配置参数值
     * @param paramKey 配置参考key
     * @return
     */
    public static String getProperty(String paramKey){  
        if(applicationContext==null){
            throw new UnsupportedOperationException("未初始化，无法调用");
        }
        return applicationContext.getEnvironment().getProperty(paramKey);
    }

    /**
     * {@inheritDoc}
     */
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(applicationContext==null && event.getApplicationContext().getParent()==null){
            setApplicationContext(event.getApplicationContext());
        }
    }  
}
