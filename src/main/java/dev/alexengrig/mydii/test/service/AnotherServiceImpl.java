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

package dev.alexengrig.mydii.test.service;

import dev.alexengrig.mydii.test.repository.Repository;

public class AnotherServiceImpl implements AnotherService {
    private final Repository repository;

    public AnotherServiceImpl(Repository repository) {
        this.repository = repository;
        System.out.println("Create " + getClass());
    }

    private void init() {
        System.out.println("Init " + getClass());
    }

    @Override
    public void serveDifferently() {
        System.out.println("This is another service with repository: " + repository);
    }
}
