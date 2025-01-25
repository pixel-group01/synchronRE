package com.pixel.synchronre.reportmodule.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasperReportConfig {

    @Value("${report.location}")
    public String reportLocation;

    @Value("${report.location.images}")
    public String imagesLocation;

    @Value("${report.location.images.visa}")
    public String visa;
    @Value("${report.location.images.logonre}")
    public String nreLogo;
    @Value("${report.location.images.logosynchre}")
    public String synchronRelogo;

    @Value("${report.location.note.cession}")
    public String noteCession;

    @Value("${report.location.note.debit}")
    public String noteDebit;

    @Value("${report.location.note.credit}")
    public String noteCredit;

    @Value("${report.location.note.sinistre}")
    public String noteCessionSinistre;

    @Value("${report.location.note.debit.sinistre}")
    public String noteDebitSinistre;

    @Value("${report.location.cheque}")
    public String cheque;

    @Value("${report.location.cheque.sinistre}")
    public String chequeSinistre;

    @Value("${report.location.compteTraite}")
    public String compteTraite;

    @Value("${report.location.situation_financiere_par_cedante_et_rea}")
    public String situationFinanciereParCedanteEtRea;

    @Value("${report.location.situation_financiere_par_cedante}")
    public String situationFinanciereParCedante;
}
