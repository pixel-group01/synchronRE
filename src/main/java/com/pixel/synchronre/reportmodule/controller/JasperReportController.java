package com.pixel.synchronre.reportmodule.controller;

import com.pixel.synchronre.reportmodule.service.implementation.JasperReportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Controller @RequestMapping(path = "/reports")
public class JasperReportController {

    @Autowired
    private JasperReportService jasperReportService;


    //Récupéartion du chemin vers le report
    @Value("${jasper.report.location}")
    public String reportLocation;

    @GetMapping("/note-cession")
    public void generateReport(HttpServletResponse response, @RequestParam Long affId, @RequestParam Long cesId) throws Exception
    {

        Map<String, Object> params = new HashMap<>();
        params.put("aff_id", affId);
        params.put("ces_id", cesId);
        params.put("param_image", reportLocation + "/images/");
        byte[] reportBytes = jasperReportService.generateReport(params);

        // Configurez l'en-tête de la réponse HTTP
        response.setContentType("application/pdf");
        response.setHeader("Content-disposition", "inline; filename=report.pdf");
        response.setContentLength(reportBytes.length);

        // Écrivez le rapport Jasper dans le flux de sortie de la réponse HTTP
        OutputStream outStream = response.getOutputStream();
        outStream.write(reportBytes);
        outStream.flush();
        outStream.close();
    }
}
