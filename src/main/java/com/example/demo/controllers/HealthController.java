// package com.example.demo.controllers;

// import org.springframework.web.bind.annotation.RestController;

// import com.example.demo.models.TestModel;
// import com.example.demo.repositories.TestRepository;
// import org.aspectj.weaver.ast.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;;


// @RestController
// public class HealthController {

//     @Autowired
//     private TestRepository testRepository;

//     @org.springframework.web.bind.annotation.GetMapping("/health")
//     public String health() {
//         return "OK";
//     }

//     @PostMapping("/path")
//     public TestModel postMethodName() {
//         TestModel testModel = new TestModel("test", 1);
//         testRepository.save(testModel);
//         TestModel retrive = testRepository.findAll().get(0);


//         return retrive;
 

        


//     }
    
    
// }
