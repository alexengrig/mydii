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

package dev.alexengrig.mydii.factory;

import dev.alexengrig.mydii.DependencyFinder;
import dev.alexengrig.mydii.DependencySupplier;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.stream.Collectors;

public class DraftDependencyFactory implements DependencyFactory {
    private final DependencyFinder dependencyFinder;

    public DraftDependencyFactory(DependencyFinder dependencyFinder) {
        this.dependencyFinder = dependencyFinder;
    }

    @Override
    public <T> T createDependency(Class<T> type, DependencySupplier dependencySupplier) {
        Class<T> targetType = getTargetType(type);
        Constructor<T> constructor = getConstructor(targetType);
        Parameter[] parameters = constructor.getParameters();
        Object[] dependencies = getDependencies(parameters, dependencySupplier);
        try {
            return constructor.newInstance(dependencies);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Constructor: " + constructor, e);
        }
    }

    private <T> Class<T> getTargetType(Class<T> type) {
        if (!type.isInterface()) {
            return type;
        }
        List<Class<T>> implementations = dependencyFinder.findImplementations(type);
        if (implementations.isEmpty()) {
            throw new IllegalArgumentException("No implementation for interface: " + type);
        } else if (implementations.size() > 1) {
            throw new IllegalArgumentException("Interface has more than 1 implementation: "
                    + implementations.stream().map(Class::toString).collect(Collectors.joining(", ")));
        }
        return implementations.get(0);
    }

    private <T> Constructor<T> getConstructor(Class<T> type) {
        @SuppressWarnings("unchecked")
        Constructor<T>[] constructors = (Constructor<T>[]) type.getDeclaredConstructors();
        if (constructors.length != 1) {
            throw new IllegalArgumentException("Class has more than 1 constructor: " + type);
        }
        return constructors[0];
    }

    private Object[] getDependencies(Parameter[] parameters, DependencySupplier dependencySupplier) {
        int parameterCount = parameters.length;
        Object[] dependencies = new Object[parameterCount];
        for (int i = 0; i < parameterCount; i++) {
            Parameter parameter = parameters[i];
            Class<?> parameterType = parameter.getType();
            dependencies[i] = dependencySupplier.supply(parameterType);
        }
        return dependencies;
    }
}
