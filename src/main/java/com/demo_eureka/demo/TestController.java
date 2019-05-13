package com.demo_eureka.demo;

import com.demo_eureka.demo.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;


@RestController
public class TestController {
    @Autowired
    RestTemplate restTemplate;
    private final Logger logger=  LoggerFactory.getLogger(TestController.class);
    @RequestMapping("/hi")
    public String hi(@RequestParam String id){
        return restTemplate.getForObject("http://eureka-server/hi?id="+id, String.class);
    }

    @Bean
    @LoadBalanced
    RestTemplate restTemplate(){
        return new RestTemplate();
    }

    /**
     * ResponseEntity<T> getForEntity(String url, Class<T> responseType, Object... uriVariables)
     */
    @GetMapping("/entity/type")
    public User getForEntityIdentifyByType(){
        //不传参返回指定类型结果
        ResponseEntity<User> entity = restTemplate.getForEntity("http://eureka-server/user", User.class);
        User body = entity.getBody();
        logger.info("user:"+body);
        return body;
    }

    /**
     * ResponseEntity<T> getForEntity(String url, Class<T> responseType, Object... uriVariables)
     * 使用占位符对参数进行替换，内部使用String.format方法实现
     */
    @GetMapping(value="/entity")
    //如果接收的参数是使用参数没有使用?有则使用@PathVariable，否则用@RequestParam
    public String getForEntityByQuestionMarkParam(@RequestParam("name") String name){
        //主要测试getEntity方法，这里测试直接传参
        return restTemplate.getForEntity("http://eureka-server/greet/{1}", String.class, name).getBody();
    }

    /**
     * getForEntity方法内部会提取map中，以占位符为key的值作为参数回填入url中
     * ResponseEntity<T> getForEntity(String url, Class<T> responseType, Map<String, ?> uriVariables)
     */
    @GetMapping(value="/entity/map/{name}")
    //如果接收的参数是使用参数没有使用?有则使用@PathVariable，否则用@RequestParam
    public String getForEntityByMap(@PathVariable("name") String name){
        //主要测试getEntity方法，这里测试map传参
        Map<String, String> reqMap = new HashMap();
        reqMap.put("name",name);
        return restTemplate.getForEntity("http://eureka-server/greet/{name}", String.class,reqMap).getBody();
    }

    /**
     * ResponseEntity<T> getForObject(URI url, Class<T> responseType)
     */
    @GetMapping("/entity/uri")
    public String getForEntityByURI(){
        //使用uri进行传参并访问
        UriComponents uriComponents = UriComponentsBuilder.fromUriString("http://eureka-server/greet/{name}").build().expand("laozhang").encode();
        URI uri = uriComponents.toUri();
        return restTemplate.getForEntity(uri, String.class).getBody();
    }

    /**
     * T getForObject(String url, Class<T> responseType)
     */
    @GetMapping("/object")
    public User getForObjectWithNoParam(){
        //相比getForEntity方法，获取对象可以省去调用getBody
        return restTemplate.getForObject("http://eureka-server/user", User.class);
    }

    /**
     * T getForObject(String url, Class<T> responseType, Map<String, ?> uriVariables)
     */
    @GetMapping("/object/map")
    public User getForObjectByMap(){
        //使用map传参
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("name","hello");
        return restTemplate.getForObject("http://eureka-server/user", User.class, paramMap);
    }

    /**
     * T getForObject(String url, Class<T> responseType, Object... uriVariables)
     */
    @GetMapping("/object/param/{name}")
    public User getForObjectByParam(@PathVariable String name){
        return restTemplate.getForObject("http://eureka-server/user/{name}",User.class, name);
    }

    /**
     * T getForObject(URI url, Class<T> responseType)
     */
    @GetMapping("/object/uri/{name}")
    public User getForObjectByURI(@PathVariable String name){
        UriComponents uriComponents = UriComponentsBuilder.fromUriString("http://eureka-server/user/{name}")
                .build().expand(name).encode();
        URI uri = uriComponents.toUri();
        return restTemplate.getForObject(uri,User.class);
    }
}
