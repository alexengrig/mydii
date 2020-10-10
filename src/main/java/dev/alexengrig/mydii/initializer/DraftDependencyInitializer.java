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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DraftDependencyInitializer implements DependencyInitializer {
    private static final Logger LOG = LoggerFactory.getLogger(DraftDependencyInitializer.class);

    @Override
    public <T> void initDependency(T object, DependencyStorage storage) {
        Class<?> type = object.getClass();
        Method method = getInitMethod(type);
        invokeMethod(method, object);
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
            throw new IllegalStateException("Exception of invoking method", e);
        }
    }

    @Override
    public <T> boolean isNeeded(T object, DependencyStorage storage) {
        return hasInitMethod(object.getClass());
    }

    private boolean hasInitMethod(Class<?> type) {
        try {
            type.getDeclaredMethod("init");
            return true;
        } catch (NoSuchMethodException e) {
            LOG.debug("Class has no 'init' method: {}", type.getName());
            return false;
        }
    }
}
