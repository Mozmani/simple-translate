package io.mozmani.translateinterface

import groovy.util.logging.Slf4j
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.client.RestTemplate

@Slf4j
@Controller
class TranslationController {

    RestTemplate restTemplate = new RestTemplate()

    @GetMapping
    String fetchHome(Model model) {
        model.addAttribute('translation', new Translation())
        return 'simpleTranslate'
    }

    @PostMapping(value = '/sendTranslation')
    String translateText(@ModelAttribute Translation translation, Model model) {
        model.addAttribute('translation', translation)
        TranslationResponse tr = translateRequest(translation)
        model.addAttribute('translationText', tr.output)
        model.addAttribute('origin', tr.originLang)
        return 'simpleTranslate'
    }

    private TranslationResponse translateRequest(Translation translation) {
        String uri = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=auto&tl=${translation.targetLanguage}&dt=t&q="
        uri += translation.input
        ResponseEntity<String> res = restTemplate.getForEntity(uri, String.class)
        String[] rawScrape = res.body.split(',')
        String output = rawScrape[0].replace('[', '')
        String lang = rawScrape[12]

        return new TranslationResponse(output: output, originLang: lang)
    }
}