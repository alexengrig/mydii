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

package dev.alexengrig.mydii.processor.initializing;

import dev.alexengrig.mydii.DependencyStorage;
import dev.alexengrig.mydii.initializer.DependencyInitializer;
import dev.alexengrig.mydii.initializer.DraftDependencyInitializer;
import dev.alexengrig.mydii.processor.context.ObjectContext;

import java.util.ArrayList;
import java.util.List;

public class DraftDependencyInitializingProcessor implements DependencyInitializingProcessor {
    private final DependencyInitializer initializer;
    private final List<DependencyPreInitializingProcessor> preInitializingProcessors;
    private final List<DependencyPostInitializingProcessor> postInitializingProcessors;

    public DraftDependencyInitializingProcessor() {
        this.initializer = new DraftDependencyInitializer();
        this.preInitializingProcessors = new ArrayList<>();
        this.postInitializingProcessors = new ArrayList<>();
    }

    @Override
    public <T> ObjectContext<T> initDependency(ObjectContext<T> context) {
        DependencyStorage storage = context.getStorage();
        Class<T> type = context.getType();
        T object = context.getObject();
        preInitializingProcessors.forEach(processor -> processor.beforeInitializingDependency(type, object, storage));
        initializer.initDependencyIfNeeded(object, storage);
        postInitializingProcessors.forEach(processor -> processor.afterInitializingDependency(type, object, storage));
        return context;
    }
}
