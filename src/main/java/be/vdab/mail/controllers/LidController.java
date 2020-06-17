package be.vdab.mail.controllers;



import be.vdab.mail.domain.Lid;
import be.vdab.mail.exceptions.KanMailNietZendenException;
import be.vdab.mail.services.LidService;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("leden")
public class LidController {
    private final Logger logger = LoggerFactory.logger(this.getClass());
    private final LidService service;

    public LidController(LidService service) {
        this.service = service;
    }

    @GetMapping("registratieform")
    public ModelAndView registratieform(){
        return new ModelAndView("registratieform").addObject(new Lid("", "", ""));
    }

    @PostMapping
    public String registreer(@Valid Lid lid, Errors errors, RedirectAttributes redirect, HttpServletRequest request){
        if(errors.hasErrors()){
            return "registratieform";
        }
        try{
            service.registreer(lid, request.getRequestURL().toString());
        } catch(KanMailNietZendenException ex){
            logger.error("Kan mail niet verzenden", ex);
            redirect.addAttribute("mailFout", true);
        }
        redirect.addAttribute("id", lid.getId());
        return "redirect:/leden/geregistreerd/{id}";
    }

    @GetMapping("geregistreerd/{id}")
    public String geregistreerd(@PathVariable long id){
        return "geregistreerd";
    }

    @GetMapping("{id}")
    public ModelAndView info(@PathVariable long id){
        var modelAndView = new ModelAndView("lidinfo");
        service.findById(id).ifPresent(lid -> modelAndView.addObject(lid));
        return modelAndView;
    }
}
