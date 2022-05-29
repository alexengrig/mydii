/*
 * Copyright 2020 - 2022 Alexengrig Dev.
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

import dev.alexengrig.mydii.processor.context.ProxyContext;
import dev.alexengrig.mydii.processor.context.TypeContext;

public interface DependencyKeeper {
    <T> boolean hasDependency(Class<T> type);

    default <T> boolean hasDependency(TypeContext<T> context) {
        return hasDependency(context.getType());
    }

    <T> T getDependency(Class<T> type);

    default <T> T getDependency(TypeContext<T> context) {
        return getDependency(context.getType());
    }

    <T> void keepDependency(Class<T> type, T dependency);

    default <T> T keepDependency(ProxyContext<T> context) {
        keepDependency(context.getType(), context.getProxy());
        return context.getProxy();
    }
}
