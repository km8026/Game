package com.project.liar.controller;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.liar.entity.Acticle;
import com.project.liar.repository.ActicleRepository;

@Controller
public class ListController {
  @Autowired
  ActicleRepository acticleRepository;

  @GetMapping("/list")
  @ResponseBody
  public List<Acticle> kindList(){
  List<Acticle> a = acticleRepository.findAll();
    return a;
}

@GetMapping("/random_article")
public String random(Model model) {
    List<Acticle> allActicles = acticleRepository.findAll();
    Random random = new Random();
    Acticle randomActicle = allActicles.get(random.nextInt(allActicles.size()));
    model.addAttribute("randomActicle", randomActicle);
    return "random_article";
}
}
  


