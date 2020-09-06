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

package dev.alexengrig.mydii.keeper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DraftDependencyKeeper implements DependencyKeeper {
    private final Map<Class<?>, Object> dependencyByType;

    public DraftDependencyKeeper() {
        this.dependencyByType = new ConcurrentHashMap<>();
    }

    @Override
    public <T> boolean hasDependency(Class<T> type) {
        return dependencyByType.containsKey(type);
    }

    @Override
    public <T> T getDependency(Class<T> type) {
        @SuppressWarnings("unchecked")
        T dependency = (T) dependencyByType.get(type);
        return dependency;
    }

    @Override
    public <T> void keepDependency(Class<T> type, T dependency) {
        dependencyByType.put(type, dependency);
    }
}
