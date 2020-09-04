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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;

public class ScanDependencyStorage implements DependencyStorage {
    @Override
    public <T> T getDependency(Class<T> type) {
        @SuppressWarnings("unchecked")
        Constructor<T>[] constructors = (Constructor<T>[]) type.getDeclaredConstructors();
        if (constructors.length != 1) {
            throw new IllegalArgumentException("Class has more than 1 constructor: " + type);
        }
        Constructor<T> constructor = constructors[0];
        if (constructor.getParameterCount() == 0) {
            try {
                return constructor.newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new IllegalArgumentException("Constructor: " + constructor, e);
            }
        }
        Parameter[] parameters = constructor.getParameters();
        Object[] parameterInstances = new Object[parameters.length];
        for (int i = 0, l = parameters.length; i < l; i++) {
            Parameter parameter = parameters[i];
            Class<?> parameterType = parameter.getType();
            parameterInstances[i] = getDependency(parameterType);
        }
        try {
            return constructor.newInstance(parameterInstances);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Constructor: " + constructor, e);
        }
    }
}
