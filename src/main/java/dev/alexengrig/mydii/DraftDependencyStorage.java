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

public class DraftDependencyStorage implements DependencyStorage {
    private final DependencyFactory dependencyFactory;

    public DraftDependencyStorage() {
        Class<?> baseClass = getBaseClass();
        DependencyFinder dependencyFinder = new PackageScanner(baseClass);
        this.dependencyFactory = new DraftDependencyFactory(dependencyFinder);
    }

    private Class<?> getBaseClass() {
        try {
            String className = new Throwable().getStackTrace()[1].getClassName();
            return Class.forName(className);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to get base class");
        }
    }

    @Override
    public <T> T getDependency(Class<T> type) {
        return dependencyFactory.createDependency(type, this::getDependency);
    }
}
