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

package dev.alexengrig.mydii.processor.context;

import dev.alexengrig.mydii.DependencyStorage;

public class DraftObjectContext<T> extends DraftTypeContext<T> implements ObjectContext<T> {
    protected final T object;

    public DraftObjectContext(DependencyStorage storage, Class<T> type, T object) {
        super(storage, type);
        this.object = object;
    }

    @Override
    public T getObject() {
        return object;
    }

    @Override
    public ProxyContext<T> withProxy(T proxy) {
        return new DraftProxyContext<>(storage, type, object, proxy);
    }
}
