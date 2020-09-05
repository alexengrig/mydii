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

package dev.alexengrig.mydii.initializer;

import dev.alexengrig.mydii.DependencyStorage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DraftDependencyInitializer implements DependencyInitializer {
    @Override
    public <T> void initDependency(T dependency, DependencyStorage storage) {
        Class<?> type = dependency.getClass();
        Method method = getInitMethod(type);
        invokeMethod(method, dependency);
    }

    @Override
    public <T> boolean isNeeded(T dependency, DependencyStorage storage) {
        return hasInitMethod(dependency.getClass());
    }

    private boolean hasInitMethod(Class<?> type) {
        try {
            type.getDeclaredMethod("init");
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    private Method getInitMethod(Class<?> type) {
        try {
            return type.getDeclaredMethod("init");
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("No 'init' method in class: " + type);
        }
    }

    private void invokeMethod(Method method, Object object) {
        try {
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            method.invoke(object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }
}
