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

package dev.alexengrig.mydii.finder;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class PackageScanner implements DependencyFinder {
    private final List<Class<?>> classes;

    public PackageScanner(Class<?> baseClass) {
        this(baseClass.getPackage());
    }

    public PackageScanner(Package basePackage) {
        this(basePackage.getName());
    }

    public PackageScanner(String basePackage) {
        this.classes = getClasses(basePackage);
    }

    private List<Class<?>> getClasses(String basePackage) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            Enumeration<URL> urls = classLoader.getResources(basePackage.replace('.', '/'));
            LinkedList<File> files = new LinkedList<>();
            LinkedList<Class<?>> classes = new LinkedList<>();
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                try {
                    File file = new File(url.toURI());
                    if (file.isDirectory()) {
                        File[] children = file.listFiles();
                        if (children != null) {
                            files.addAll(Arrays.asList(children));
                        }
                    } else {
                        classes.add(getClass(basePackage, file));
                    }
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
            while (!files.isEmpty()) {
                File file = files.remove();
                if (file.isDirectory()) {
                    File[] children = file.listFiles();
                    if (children != null) {
                        files.addAll(Arrays.asList(children));
                    }
                } else {
                    classes.add(getClass(basePackage, file));
                }
            }
            return classes;
        } catch (IOException e) {
            throw new IllegalArgumentException("For package: " + basePackage, e);
        }
    }

    private Class<?> getClass(String basePackage, File file) {
        String path = file.getAbsolutePath();
        String name = path.substring(0, path.lastIndexOf('.')).replace(File.separatorChar, '.');
        int index = name.indexOf(basePackage);
        if (index == -1) {
            throw new IllegalArgumentException("File '" + file + "' not found in package: " + basePackage);
        }
        String className = name.substring(index);
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("For file '" + file + "' in package: " + basePackage, e);
        }
    }

    @Override
    public <T> List<Class<T>> findImplementations(Class<T> type) {
        if (!type.isInterface()) {
            throw new IllegalArgumentException("Type is not an interface: " + type);
        }
        return classes.stream()
                .filter(t -> !t.isInterface() && type.isAssignableFrom(t))
                .map(t -> {
                    @SuppressWarnings("unchecked")
                    Class<T> target = (Class<T>) t;
                    return target;
                })
                .collect(Collectors.toList());
    }
}
