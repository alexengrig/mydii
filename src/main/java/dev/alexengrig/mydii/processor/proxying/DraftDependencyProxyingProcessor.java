/*
 * Copyright 2020 Alexengrig Dev.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.alexengrig.mydii.processor.proxying;

import dev.alexengrig.mydii.DependencyStorage;
import dev.alexengrig.mydii.processor.context.ObjectContext;
import dev.alexengrig.mydii.processor.context.ProxyContext;
import dev.alexengrig.mydii.proxy.DependencyProxyFactory;
import dev.alexengrig.mydii.proxy.DraftDependencyProxyFactory;

import java.util.ArrayList;
import java.util.List;

public class DraftDependencyProxyingProcessor implements DependencyProxyingProcessor {
    private final DependencyProxyFactory proxyFactory;
    private final List<DependencyPreProxyingProcessor> preProxyingProcessors;
    private final List<DependencyPostProxyingProcessor> postProxyingProcessors;

    public DraftDependencyProxyingProcessor() {
        this.proxyFactory = new DraftDependencyProxyFactory();
        this.preProxyingProcessors = new ArrayList<>();
        this.postProxyingProcessors = new ArrayList<>();
    }

    @Override
    public <T> ProxyContext<T> proxyDependency(ObjectContext<T> context) {
        DependencyStorage storage = context.getStorage();
        Class<T> type = context.getType();
        T object = context.getObject();
        preProxyingProcessors.forEach(processor -> processor.beforeProxyingDependency(type, object, storage));
        T proxy = proxyFactory.createDependencyProxyIfNeeded(object, storage);
        postProxyingProcessors.forEach(processor -> processor.afterProxyingDependency(type, object, proxy, storage));
        return context.withProxy(proxy);
    }
}
