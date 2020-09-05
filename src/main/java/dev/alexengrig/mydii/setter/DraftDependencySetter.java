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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class DraftDependencySetter implements DependencySetter {
    @Override
    public <T> void setDependency(T dependency, DependencyStorage storage) {
        Class<?> type = dependency.getClass();
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            if (!Modifier.isFinal(field.getModifiers())) {
                Object fieldDependency = storage.getDependency(field.getType());
                setField(field, dependency, fieldDependency);
            }
        }
    }

    @Override
    public <T> boolean isNeeded(T dependency, DependencyStorage storage) {
        return true;
    }

    private void setField(Field field, Object object, Object value) {
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }
}
