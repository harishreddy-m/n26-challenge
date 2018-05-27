package tech.harish.apps.n26.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Api(tags="Home Page",description = "Default page displayed at root url")
@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping
    @ApiOperation("Get documentation")
    public String home() {
        return "redirect:swagger-ui.html";
    }

}
