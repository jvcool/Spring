package com.springboot.app;

import com.fasterxml.jackson.databind.util.JSONPObject;

import com.lowagie.text.DocumentException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@RestController
public class Controller {

    @RequestMapping("/")
    public String index(){
        return "Hello World";
    }

    @Autowired
    RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @RequestMapping(value = "/export/pdf")
    public User[] getProductList(HttpServletResponse response) throws DocumentException,  IOException {
        ResponseEntity<User[]> user_list = restTemplate.getForEntity("https://api.github.com/users", User[].class);

        User[] users_info = user_list.getBody();;

//        ArrayList<User> users_info =  new ArrayList<User>();

//        for(int i = 0; i < users.length; i++){
//            System.out.println(users[i]);
//            ResponseEntity<User> user_info = restTemplate.getForEntity("https://api.github.com/users/"
//                                            + users[i].getLogin(), User.class);
//
//            users_info.add(user_info.getBody());
//        }
//
//        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        PDFExporter exporter = new PDFExporter(Arrays.asList(users_info));
        exporter.export(response);

        return users_info;
    }
}
