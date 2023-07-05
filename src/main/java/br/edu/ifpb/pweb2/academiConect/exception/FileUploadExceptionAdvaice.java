package br.edu.ifpb.pweb2.academiConect.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class FileUploadExceptionAdvaice {

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxUploadSize;

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ModelAndView handleMaxSizeException(MaxUploadSizeExceededException e, RedirectAttributes redattr) {
        ModelAndView mav = new ModelAndView("redirect:/declaracoes");
        redattr.addFlashAttribute("mensagem", String.format("Arquivo excede o tamanho máximo de (%s)!", maxUploadSize));
        return mav;
    }
    
}
