//This servlet is not a part of the final project, it's only for test and demo.

package com.laioffer.job.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laioffer.job.entity.ExampleCoordinates;
import com.laioffer.job.entity.ExampleJob;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ExampleJobServlet", urlPatterns = {"/example_job"})
public class ExampleJobServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //we use jackson to import data automatically by initiating objects in class
        response.setContentType("application/json"); //determine the response's type
        ObjectMapper mapper = new ObjectMapper(); //Use ObjectMapper to do two-way mapping
        ExampleCoordinates coordinates = new ExampleCoordinates(37.4851, -122.148316);
        ExampleJob job = new ExampleJob("Software Engineer", 123456, "Aug 1, 2020", false, coordinates);

        //response.getWriter().print(mapper.writeValueAsString(job)); (两种方法MA，下面👇的省去了一次FUNCTION CALL)
        mapper.writeValue(response.getWriter(), job);
    }
}
