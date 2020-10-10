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

import dev.alexengrig.mydii.DependencyStorage;
import dev.alexengrig.mydii.finder.DependencyFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.stream.Collectors;

public class DraftDependencyFactory implements DependencyFactory {
    private static final Logger LOG = LoggerFactory.getLogger(DraftDependencyFactory.class);

    @Override
    public <T> T createDependency(Class<T> type, DependencyStorage storage) {
        LOG.debug("Started creation a dependency for type: {}", type.getName());
        Class<T> targetType = getTargetType(type, storage);
        T target = getInstance(targetType, storage);
        LOG.debug("Finished creation dependency for type: {}", type.getName());
        return target;
    }

    private <T> Class<T> getTargetType(Class<T> type, DependencyStorage storage) {
        if (!type.isInterface()) {
            LOG.debug("Type is not an interface: {}", type.getName());
            return type;
        }
        LOG.debug("Type is an interface: {}", type.getName());
        DependencyFinder finder = storage.getConfiguration().getFinder();
        List<Class<T>> implementations = finder.findImplementations(type);
        if (implementations.isEmpty()) {
            throw new IllegalArgumentException("No implementation for interface: " + type);
        } else if (implementations.size() > 1) {
            throw new IllegalArgumentException("Interface has more than 1 implementation: "
                    + implementations.stream().map(Class::getName).collect(Collectors.joining(", ")));
        }
        Class<T> targetType = implementations.get(0);
        LOG.debug("Target type: {}", targetType.getName());
        return targetType;
    }

    private <T> T getInstance(Class<T> type, DependencyStorage storage) {
        Constructor<T> constructor = getConstructor(type);
        Parameter[] parameters = constructor.getParameters();
        Object[] dependencies = getDependencies(parameters, storage);
        return newInstance(constructor, dependencies);
    }

    private <T> Constructor<T> getConstructor(Class<T> type) {
        @SuppressWarnings("unchecked")
        Constructor<T>[] constructors = (Constructor<T>[]) type.getDeclaredConstructors();
        if (constructors.length != 1) {
            throw new IllegalArgumentException("Class has more than 1 constructor: " + type);
        }
        Constructor<T> constructor = constructors[0];
        LOG.debug("Type constructor: {}", constructor);
        return constructor;
    }

    private Object[] getDependencies(Parameter[] parameters, DependencyStorage storage) {
        int parameterCount = parameters.length;
        Object[] dependencies = new Object[parameterCount];
        for (int i = 0; i < parameterCount; i++) {
            Parameter parameter = parameters[i];
            Class<?> parameterType = parameter.getType();
            Object dependency = storage.getDependency(parameterType);
            dependencies[i] = dependency;
            LOG.debug("Type parameter with dependency: {} - {}",
                    parameterType.getName(), dependency.getClass().getName());
        }
        return dependencies;
    }

    private <T> T newInstance(Constructor<T> constructor, Object[] dependencies) {
        try {
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            return constructor.newInstance(dependencies);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Exception of creation new instance", e);
        }
    }
}
