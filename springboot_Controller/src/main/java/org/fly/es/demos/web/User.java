/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.fly.es.demos.web;

import lombok.Data;

/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
@Data
public class User {

    private String name;

    private Integer age;

    private String username;

    private String password;

    public static void main(String[] args) {

        System.out.println(getSubDepartment("中通集团/中通快运/IT信息中心"));
    }

    /**
     * 取 2-3级，但是数据 是从0开始，此处从1开始变为3-4
     *  中通集团/中通快运/IT信息中心/长沙研发部/长沙研发部综合组研发一组
     * @param fullPath
     * @return
     */
    public static String getSubDepartment(String fullPath) {
        if (fullPath == null || fullPath.trim().isEmpty()) {
            return "";
        }
        String[] parts = fullPath.split("/");
        if (parts.length >= 4) {
            return parts[2] + "/" + parts[3]; // 第3、4级
        } else if (parts.length == 3) {
            return parts[2];
        }
        return fullPath;
    }


}
