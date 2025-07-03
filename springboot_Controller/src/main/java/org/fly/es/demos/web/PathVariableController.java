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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
@RequestMapping("/test")
@Controller
public class PathVariableController {



    /*
    get 请求
        @RequestParam 的示例
        http://127.0.0.1:8080/test/search?keyword=11
        @RequestMapping(value = "/search", method = RequestMethod.GET)  等价写法 @GetMapping("/search")
     */
    @GetMapping("/search")
    @ResponseBody
    public String searchProducts(@RequestParam String keyword) {
        return "获取参数: " + keyword;
    }


    /*
        使用 @PathVariable 的示例
        http://127.0.0.1:8080/test/11
     */
    @GetMapping("/{id}")
    @ResponseBody
    public String getProductById(@PathVariable String id) {
        return "获取参数： " + id ;
    }


    // 混合使用示例
    // http://127.0.0.1:8080/test/category/11/search?keyword=keyword11&page=22
    @GetMapping("/category/{categoryId}/search")
    @ResponseBody
    public String searchInCategory(
            @PathVariable String categoryId,
            @RequestParam String keyword,
            @RequestParam(required = false, defaultValue = "1") int page) {
        return String.format("在分类 %s 中搜索 '%s' (第 %d 页)", categoryId, keyword, page);
    }



    // http://127.0.0.1:8080/test/user/123/roles/222
    @RequestMapping(value = "/user/{userId}/roles/{roleId}", method = RequestMethod.GET)
    @ResponseBody
    public String getLogin(@PathVariable("userId") String userId, @PathVariable("roleId") String roleId) {
        return "User Id : " + userId + " Role Id : " + roleId;
    }

    // http://127.0.0.1:8080/test/javabeat/somewords
    @RequestMapping(value = "/javabeat/{regexp1:[a-z-]+}", method = RequestMethod.GET)
    @ResponseBody
    public String getRegExp(@PathVariable("regexp1") String regexp1) {
        return "URI Part : " + regexp1;
    }





    /*
        post 请求
        一. 接收 Headers：Content-Type=application/x-www-form-urlencoded 表单数据
        接受方式 1：直接使用 @RequestParam 接收
        http://127.0.0.1:8080/test/login1?username=张三&password=123

     */
    @PostMapping("/login1")
    @ResponseBody
    public String login1(@RequestParam String username, @RequestParam String password) {
        return "login1: Username: " + username + ", Password: " + password;
    }


    //接受方式 2：使用 Map 接收所有参数
    @PostMapping("/login2")
    @ResponseBody
    public String login2(@RequestParam Map<String, String> params) {
        return "login2: Username: " + params.get("username") + ", Password: " + params.get("password");
    }

    //接受方式 3：绑定到对象（推荐）
    @PostMapping("/login3")
    @ResponseBody
    public String login3(User user) { // 自动绑定到对象
        return "login3: Username: " + user.getUsername() + ", Password: " + user.getPassword();
    }


    /*
        post 请求
        二. 接收 Headers：Content-Type=application/json 表单数据
        @RequestBody 用于解析 HTTP 请求体（raw 数据）,并复制给user对象，不加就是空的。
     */
    @PostMapping("/raw1-User")
    @ResponseBody
    public User raw1(@RequestBody User user) { // 自动映射 json 到 User 对象
        return user; // Spring 会自动将返回的对象转为 JSON
    }

    @PostMapping("/raw2-Map")
    @ResponseBody
    public Map<String, Object> raw2(@RequestBody Map<String, Object> map) {
        return map; // 直接返回 Map，Spring 会转为 JSON
    }


    /*
        post 请求
        三. 接收 Headers：Content-Type=text/plain 表单数据
        @RequestBody 用于解析 HTTP 请求体（raw 数据）,并复制给user对象，不加就是空的。
     */
    @PostMapping("/text1")
    @ResponseBody
    public String text1(@RequestBody String text) {
        return "Received: " + text;
    }

    @PostMapping("/text2")
    @ResponseBody
    public Map<String, Object> text2(@RequestBody Map<String, Object> map) {
        return map;
    }

}
