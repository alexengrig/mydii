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

package dev.alexengrig.mydii.processor.creation;

import dev.alexengrig.mydii.DependencyStorage;
import dev.alexengrig.mydii.factory.DependencyFactory;
import dev.alexengrig.mydii.factory.DraftDependencyFactory;
import dev.alexengrig.mydii.processor.context.ObjectContext;
import dev.alexengrig.mydii.processor.context.TypeContext;

import java.util.ArrayList;
import java.util.List;

public class DraftDependencyCreationProcessor implements DependencyCreationProcessor {
    private final DependencyFactory factory;
    private final List<DependencyPreCreationProcessor> preCreationProcessors;
    private final List<DependencyPostCreationProcessor> postCreationProcessors;

    public DraftDependencyCreationProcessor() {
        this.factory = new DraftDependencyFactory();
        this.preCreationProcessors = new ArrayList<>();
        this.postCreationProcessors = new ArrayList<>();
    }

    @Override
    public <T> ObjectContext<T> createDependency(TypeContext<T> context) {
        DependencyStorage storage = context.getStorage();
        Class<T> type = context.getType();
        preCreationProcessors.forEach(processor -> processor.beforeCreationDependency(type, storage));
        T object = factory.createDependency(type, storage);
        postCreationProcessors.forEach(processor -> processor.afterCreationDependency(type, object, storage));
        return context.withObject(object);
    }

}
