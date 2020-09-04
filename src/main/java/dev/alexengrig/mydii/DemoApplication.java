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

import dev.alexengrig.mydii.domain.DemoDomain;
import dev.alexengrig.mydii.repository.PermanentDemoRepository;
import dev.alexengrig.mydii.service.ConsoleDemoService;
import dev.alexengrig.mydii.service.DemoService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class DemoApplication {
    public static void main(String[] args) {
        Class<?>[] classes = {DemoDomain.class, PermanentDemoRepository.class, ConsoleDemoService.class};
        ApplicationContext context = new AnnotationConfigApplicationContext(classes);
        DemoService service = context.getBean(DemoService.class);
        service.demonstrate();
    }
}
