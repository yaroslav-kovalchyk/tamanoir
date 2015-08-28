package com.jaspersoft.tamanoir;

import com.jaspersoft.tamanoir.connection.ConnectionProcessorFactory;
import com.jaspersoft.tamanoir.dto.ConnectionDescriptor;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * Created by serhii.blazhyievskyi on 8/26/2015.
 */
public class EhCacheProcessorStorage {
    public static final String CONNECTIONS_CACHE_NAME = "metadata";
    private static Cache cache;

    public EhCacheProcessorStorage() {
        CacheManager cm = CacheManager.getInstance();
        cache = cm.getCache(CONNECTIONS_CACHE_NAME);
    }

    public <T> T getInstance(ConnectionProcessorFactory connectionProcessorFactory, Class<T> processorClass) {
        T processor;

        //First checking cache
        Element element;
        if ((element = cache.get(processorClass)) != null) {
            return (T)element.getObjectValue();
        }

        //No luck, create new processor
        processor = connectionProcessorFactory.getProcessor(processorClass);

        cache.put(new Element(processorClass, processor));
        return processor;
    }

    public void shutdown(){
        CacheManager.getInstance().shutdown();
    }

}
