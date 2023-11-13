package com.garcheng.rpc.example.spring.reference;

import com.garcheng.rpc.example.annotation.RpcRemoteReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
public class SpringRpcReferencePostProcess implements BeanClassLoaderAware, ApplicationContextAware, BeanFactoryPostProcessor {

    private ApplicationContext context;

    private ClassLoader classLoader;

    private RpcClientProperties properties;

    //保存发布的引用bean的信息
    private final Map<String, BeanDefinition> rpcRefBeanDefinitionMap = new ConcurrentHashMap<>();


    public SpringRpcReferencePostProcess(RpcClientProperties properties) {
        this.properties = properties;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);
            String beanClassName = beanDefinition.getBeanClassName();
            if (beanClassName != null) {
                Class<?> clazz = ClassUtils.resolveClassName(beanClassName, classLoader);
                ReflectionUtils.doWithFields(clazz, this::parseRpcReference);
            }
        }
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
        rpcRefBeanDefinitionMap.forEach((beanName,beanDefinition)->{
            if (context.containsBean(beanName)){
                log.warn("SpringContext already register bean {}",beanName);
                return;
            }
            registry.registerBeanDefinition(beanName,beanDefinition);
            log.info("registered RpcReferenceBean {} success",beanName);
        });

    }

    private void parseRpcReference(Field field) {
        RpcRemoteReference remoteReference = AnnotationUtils.getAnnotation(field, RpcRemoteReference.class);
        if (remoteReference != null) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(SpringRpcReferenceBean.class);
            builder.setInitMethodName("init");
            builder.addPropertyValue("interfaceClass", field.getType());
            builder.addPropertyValue("serviceAddress", properties.getServiceAddress());
            builder.addPropertyValue("servicePort", properties.getServicePort());
            AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
            rpcRefBeanDefinitionMap.put(field.getName(), beanDefinition);
        }

    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
