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

package dev.alexengrig.mydii.setter;

import dev.alexengrig.mydii.DependencyStorage;

public interface DependencySetter {
    <T> void setDependency(T object, DependencyStorage storage);

    <T> boolean isNeeded(T object, DependencyStorage storage);

    default <T> boolean setDependencyIfNeeded(T object, DependencyStorage storage) {
        boolean needed = isNeeded(object, storage);
        if (needed) {
            setDependency(object, storage);
        }
        return needed;
    }
}
