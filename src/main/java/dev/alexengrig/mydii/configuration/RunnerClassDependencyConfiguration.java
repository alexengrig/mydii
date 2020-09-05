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

package dev.alexengrig.mydii.configuration;

import java.util.StringJoiner;

public class RunnerClassDependencyConfiguration extends DraftDependencyConfiguration {
    public RunnerClassDependencyConfiguration() {
        super(getRunnerClass());
    }

    private static Class<?> getRunnerClass() {
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        try {
            String className = stackTrace[stackTrace.length - 1].getClassName();
            System.out.println("Runner class name: " + className);
            return Class.forName(className);
        } catch (Exception e) {
            StringJoiner joiner = new StringJoiner("\n");
            joiner.add("Failed to get runner class from stack trace:");
            for (int i = 0, l = stackTrace.length; i < l; i++) {
                StackTraceElement element = stackTrace[i];
                joiner.add(String.format("%d - %s#%s:%d",
                        i, element.getClassName(), element.getMethodName(), element.getLineNumber()));
            }
            throw new IllegalStateException(joiner.toString(), e);
        }
    }
}
