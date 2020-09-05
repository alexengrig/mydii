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

package dev.alexengrig.mydii;

import dev.alexengrig.mydii.configuration.DependencyConfiguration;
import dev.alexengrig.mydii.configuration.RunnerClassDependencyConfiguration;
import dev.alexengrig.mydii.factory.DependencyFactory;
import dev.alexengrig.mydii.factory.DraftDependencyFactory;
import dev.alexengrig.mydii.initializer.DependencyInitializer;
import dev.alexengrig.mydii.initializer.DraftDependencyInitializer;
import dev.alexengrig.mydii.proxy.DependencyProxyFactory;
import dev.alexengrig.mydii.proxy.DraftDependencyProxyFactory;
import dev.alexengrig.mydii.setter.DependencySetter;
import dev.alexengrig.mydii.setter.DraftDependencySetter;

public class DraftDependencyStorage implements DependencyStorage {
    private final DependencyConfiguration configuration;
    private final DependencyFactory factory;
    private final DependencySetter setter;
    private final DependencyInitializer initializer;
    private final DependencyProxyFactory proxyFactory;

    public DraftDependencyStorage() {
        this(new RunnerClassDependencyConfiguration());
    }

    public DraftDependencyStorage(DependencyConfiguration configuration) {
        this.configuration = configuration;
        this.factory = new DraftDependencyFactory(configuration.getFinder());
        this.setter = new DraftDependencySetter();
        this.initializer = new DraftDependencyInitializer();
        this.proxyFactory = new DraftDependencyProxyFactory();
    }

    @Override
    public DependencyConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public <T> T getDependency(Class<T> type) {
        T target = factory.createDependency(type, this);
        setter.setDependencyIfNeeded(target, this);
        initializer.initDependencyIfNeeded(target, this);
        return proxyFactory.createDependencyProxyIfNeeded(target, this);
    }
}
