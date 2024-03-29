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

package dev.alexengrig.mydii.configuration;

import dev.alexengrig.mydii.finder.DependencyFinder;
import dev.alexengrig.mydii.finder.PackageScanner;

public class DraftDependencyConfiguration implements DependencyConfiguration {
    private final DependencyFinder finder;

    public DraftDependencyConfiguration(Class<?> baseClass) {
        this(baseClass.getPackage());
    }

    public DraftDependencyConfiguration(String basePackageName) {
        this.finder = new PackageScanner(basePackageName);
    }

    public DraftDependencyConfiguration(Package basePackage) {
        this(basePackage.getName());
    }

    @Override
    public DependencyFinder getFinder() {
        return finder;
    }
}
