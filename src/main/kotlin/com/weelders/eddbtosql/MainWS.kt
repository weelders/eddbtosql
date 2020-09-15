package com.weelders.eddbtosql

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MainWS
{

    @GetMapping("/test")
    fun testMethode(): String
    {
        println("/test")

        return "I'm ok !"
    }
}