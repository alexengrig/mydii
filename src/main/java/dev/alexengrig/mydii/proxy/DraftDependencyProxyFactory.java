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

package dev.alexengrig.mydii.proxy;

import dev.alexengrig.mydii.DependencyStorage;

import java.lang.reflect.Proxy;
import java.util.Arrays;

public class DraftDependencyProxyFactory implements DependencyProxyFactory {
    @Override
    public <T> T createDependencyProxy(T dependency, DependencyStorage storage) {
        Class<?> type = dependency.getClass();
        @SuppressWarnings("unchecked")
        T dependencyProxy = (T) Proxy.newProxyInstance(type.getClassLoader(), type.getInterfaces(), (proxy, method, args) -> {
            Object result = method.invoke(dependency, args);
            if ("proxy".equals(method.getName())) {
                System.out.println("Call proxy method with args: " + Arrays.toString(args));
            }
            return result;
        });
        return dependencyProxy;
    }

    @Override
    public <T> boolean isNeeded(T dependency, DependencyStorage storage) {
        Class<?> type = dependency.getClass();
        return type.getInterfaces().length > 0 && type.getName().contains("Proxy");
    }
}
