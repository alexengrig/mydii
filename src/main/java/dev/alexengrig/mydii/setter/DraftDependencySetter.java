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

package dev.alexengrig.mydii.setter;

import dev.alexengrig.mydii.DependencyStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class DraftDependencySetter implements DependencySetter {
    private static final Logger LOG = LoggerFactory.getLogger(DraftDependencySetter.class);

    @Override
    public <T> void setDependency(T object, DependencyStorage storage) {
        Class<?> type = object.getClass();
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            if (!Modifier.isFinal(field.getModifiers())) {
                Object fieldObject = storage.getDependency(field.getType());
                LOG.debug("Set value to field: {} - {}.{}",
                        fieldObject.getClass().getName(), field.getDeclaringClass().getTypeName(), field.getName());
                setField(field, object, fieldObject);
            } else {
                LOG.debug("Field is final: {}.{}", field.getDeclaringClass().getTypeName(), field.getName());
            }
        }
    }

    @Override
    public <T> boolean isNeeded(T object, DependencyStorage storage) {
        return true;
    }

    private void setField(Field field, Object object, Object value) {
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Exception of setting field value", e);
        }
    }
}
