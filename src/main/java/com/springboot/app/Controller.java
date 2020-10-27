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
import org.springframework.web.bind.annotation.ResponseBody;
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
        return "To export pdf @url/export/pdf";
    }

    @Autowired
    RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @RequestMapping(value = "/export/pdf")
    @ResponseBody
    public void getUserList(HttpServletResponse response) throws DocumentException,  IOException {
        ResponseEntity<User[]> user_list = restTemplate.getForEntity("https://api.github.com/users", User[].class);

        User[] usersInfo = user_list.getBody();;

        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "inline; filename=users_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        PDFExporter exporter = new PDFExporter(Arrays.asList(usersInfo));
        exporter.export(response);
    }
}
