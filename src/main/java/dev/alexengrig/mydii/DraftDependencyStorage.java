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
import dev.alexengrig.mydii.keeper.DependencyKeeper;
import dev.alexengrig.mydii.keeper.DraftDependencyKeeper;
import dev.alexengrig.mydii.processor.context.DependencyContext;
import dev.alexengrig.mydii.processor.context.DraftDependencyContext;
import dev.alexengrig.mydii.processor.context.ObjectContext;
import dev.alexengrig.mydii.processor.context.ProxyContext;
import dev.alexengrig.mydii.processor.context.TypeContext;
import dev.alexengrig.mydii.processor.creation.DependencyCreationProcessor;
import dev.alexengrig.mydii.processor.creation.DraftDependencyCreationProcessor;
import dev.alexengrig.mydii.processor.initializing.DependencyInitializingProcessor;
import dev.alexengrig.mydii.processor.initializing.DraftDependencyInitializingProcessor;
import dev.alexengrig.mydii.processor.proxying.DependencyProxyingProcessor;
import dev.alexengrig.mydii.processor.proxying.DraftDependencyProxyingProcessor;
import dev.alexengrig.mydii.processor.setting.DependencySettingProcessor;
import dev.alexengrig.mydii.processor.setting.DraftDependencySettingProcessor;

public class DraftDependencyStorage implements DependencyStorage {
    private final DependencyConfiguration configuration;
    private final DependencyKeeper keeper;
    private final DependencyCreationProcessor creationProcessor;
    private final DependencySettingProcessor settingProcessor;
    private final DependencyInitializingProcessor initializingProcessor;
    private final DependencyProxyingProcessor proxyingProcessor;
    private final DependencyContext context;

    public DraftDependencyStorage() {
        this(new RunnerClassDependencyConfiguration());
    }

    public DraftDependencyStorage(DependencyConfiguration configuration) {
        this.configuration = configuration;
        this.keeper = new DraftDependencyKeeper();
        this.creationProcessor = new DraftDependencyCreationProcessor();
        this.settingProcessor = new DraftDependencySettingProcessor();
        this.initializingProcessor = new DraftDependencyInitializingProcessor();
        this.proxyingProcessor = new DraftDependencyProxyingProcessor();
        this.context = new DraftDependencyContext(this);
    }

    @Override
    public DependencyConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public <T> T getDependency(Class<T> type) {
        TypeContext<T> typeContext = context.withType(type);
        if (keeper.hasDependency(typeContext)) {
            return keeper.getDependency(typeContext);
        }
        ObjectContext<T> objectContext = creationProcessor.createDependency(typeContext);
        objectContext = settingProcessor.setDependency(objectContext);
        objectContext = initializingProcessor.initDependency(objectContext);
        ProxyContext<T> proxyContext = proxyingProcessor.proxyDependency(objectContext);
        return keeper.keepDependency(proxyContext);
    }
}
