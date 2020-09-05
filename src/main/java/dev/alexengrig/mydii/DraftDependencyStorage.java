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

public class DraftDependencyStorage implements DependencyStorage {
    private final DependencyFactory factory;

    public DraftDependencyStorage() {
        this(new RunnerClassDependencyConfiguration());
    }

    public DraftDependencyStorage(DependencyConfiguration configuration) {
        this.factory = new DraftDependencyFactory(configuration.getFinder());
    }

    @Override
    public <T> T getDependency(Class<T> type) {
        return factory.createDependency(type, this::getDependency);
    }
}
