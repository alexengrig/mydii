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

import dev.alexengrig.util.WhereIsFrom;

public class RunnerClassDependencyConfiguration extends DraftDependencyConfiguration {
    public RunnerClassDependencyConfiguration() {
        super(getRunnerClass());
    }

    private static Class<?> getRunnerClass() {
        try {
            String className = WhereIsFrom.up(2).getClassName();
            System.out.println("Runner class name: " + className);
            return Class.forName(className);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to get runner class", e);
        }
    }
}
