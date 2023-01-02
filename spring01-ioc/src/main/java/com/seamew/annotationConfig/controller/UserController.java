package com.seamew.annotationConfig.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller("userController")
@Scope("singleton")
public class UserController
{

}
