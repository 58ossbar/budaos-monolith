/*
 * Copyright 2026 BudaOS Team
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

package com.budaos;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 布道师学习通单体应用启动类
 *
 * @author BudaOS Team
 * @since 1.0.0
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.budaos"})
@MapperScan(basePackages = {"com.budaos.modules.**.persistence", "com.budaos.modules.**.mapper"})
@EnableScheduling
public class BudaosApplication {

    public static void main(String[] args) {
        SpringApplication.run(BudaosApplication.class, args);
        System.out.println("========================================");
        System.out.println("   布道师学习通启动成功！");
        System.out.println("   访问地址: http://localhost:9080");
        System.out.println("========================================");
    }
}
