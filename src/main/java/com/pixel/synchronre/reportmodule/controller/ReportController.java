package com.pixel.synchronre.reportmodule.controller;

import com.pixel.synchronre.reportmodule.service.interfac.IReportExporter;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;

@RestController @RequestMapping("/reports")
@RequiredArgsConstructor @PreAuthorize("permitAll()")
public class ReportController
{
    private final IReportExporter reportExporter;
    @GetMapping(path = "/test")
    File test() throws JRException, SQLException {
        return reportExporter.exportReport("src/main/resources/report2/test.jrxml", new HashMap<>(), "C:\\Users\\DMP_Portable\\Desktop\\report\\test2.pdf");
    }
}
