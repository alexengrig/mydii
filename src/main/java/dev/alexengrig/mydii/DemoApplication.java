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

package dev.alexengrig.mydii;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.alexengrig.mydii.domain.DemoDomain;
import dev.alexengrig.mydii.repository.DemoRepository;
import dev.alexengrig.mydii.repository.PermanentDemoRepository;
import dev.alexengrig.mydii.service.ConsoleDemoService;
import dev.alexengrig.mydii.service.DemoService;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public class DemoApplication {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new DemoModule());
        DemoService service = injector.getInstance(DemoService.class);
        service.demonstrate();
    }

    static class DemoModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(DemoDomain.class).toInstance(new DemoDomain());
            bind(DemoRepository.class).toConstructor(getConstructor(PermanentDemoRepository.class, DemoDomain.class));
            bind(DemoService.class).toConstructor(getConstructor(ConsoleDemoService.class, DemoRepository.class));
        }

        private <T> Constructor<T> getConstructor(Class<T> type, Class<?>... parameters) {
            try {
                return type.getConstructor(parameters);
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException(String.format(
                        "No constructor in class '%s' with the following parameters: %s",
                        type, Arrays.toString(parameters)), e);
            }
        }
    }

}
