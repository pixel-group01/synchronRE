package com.pixel.synchronre.reportmodule.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.pixel.synchronre.reportmodule.config.JasperReportConfig;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sychronremodule.model.dao.*;
import com.pixel.synchronre.sychronremodule.model.dto.interlocuteur.response.InterlocuteurListResp;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceBordereau;
import com.google.zxing.EncodeHintType;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.*;

@Service @RequiredArgsConstructor
public class ServiceReportImpl implements IServiceReport
{
    private final JasperReportConfig jrConfig;
    private final DataSource dataSource;
    private final AffaireRepository affRepo;
    private final RepartitionRepository repRepo;
    private final SinRepo sinRepo;
    private final ReglementRepository regRepo;
    private final ResourceLoader resourceLoader;
    private final InterlocuteurRepository interRepo;
    private final BordereauRepository bordRep;
    private final IserviceBordereau bordService;
    private final TraiteNPRepository traiteNPRepo;
    private final CedRepo cedRepo;
    private final TrancheRepository trancheRepo;


    private void setQrCodeParam(Map<String, Object> parameters, String qrText) throws Exception
    {
        //String qrText = "Application SynchronRE : Votre Demande de placement porte sur N° Affaire : " + parameters.get("aff_id") + " Assuré : " + parameters.get("aff_assure") + " Numéro de Police : " + parameters.get("fac_numero_police");
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix;
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            bitMatrix = qrCodeWriter.encode(qrText, BarcodeFormat.QR_CODE, 200, 200, hints);
            MatrixToImageWriter.writeToStream(bitMatrix, "png", stream);
            parameters.put("qrCode", new ByteArrayInputStream(stream.toByteArray()));
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private InputStream getImages(String path) throws IOException {
        String resourcePath = "classpath:"+path ;
        Resource resource = resourceLoader.getResource(resourcePath);
        return resource.getInputStream();
    }

    @Override
    public byte[] generateReport(String reportName, Map<String, Object> parameters, List<Object> data, String qrText) throws Exception
    {
        qrText =  qrText != null ? qrText : "Application SynchronRE : Numéro Fac : " + parameters.get("aff_id") + " Assuré : " + parameters.get("aff_assure") + " Numéro de Police : " + parameters.get("fac_numero_police");
        // Génération du code QR
        String resourcePath = "classpath:"+jrConfig.reportLocation + "/" + reportName;
        Resource resource = resourceLoader.getResource(resourcePath);
        System.out.println(resource.getURL());
        this.setQrCodeParam(parameters, qrText);
        parameters.put("logo_nre", this.getImages(jrConfig.nreLogo));
        parameters.put("logo_synchronre", this.getImages(jrConfig.synchronRelogo));
        parameters.put("visa", this.getImages(jrConfig.visa));

        JasperReport jasperReport = JasperCompileManager.compileReport(resource.getInputStream());
        // Remplissez le rapport Jasper en utilisant la connexion JDBC
        Connection connection = dataSource.getConnection();
        JRBeanCollectionDataSource jrBeanCollectionDataSource = data == null || data.isEmpty() ? null : new JRBeanCollectionDataSource(data);

        JasperPrint jasperPrint = jrBeanCollectionDataSource == null
                ? JasperFillManager.fillReport(jasperReport, parameters, connection)
                : JasperFillManager.fillReport(jasperReport, parameters, jrBeanCollectionDataSource);

        // Exportez le rapport au format PDF
        byte[] reportBytes = JasperExportManager.exportReportToPdf(jasperPrint);

        // Fermez la connexion
        connection.close();

        return reportBytes;
    }

    private String getImagesPath() throws IOException {
        String imageLocation = "classpath:"+jrConfig.imagesLocation ;
        return resourceLoader.getResource(imageLocation).getURL().getPath();
    }

    @Override
    public byte[] generateNoteCessionFac(Long plaId, String interlocuteur) throws Exception
    {
        Repartition placement = repRepo.findById(plaId).orElseThrow(()-> new AppException("Placement introuvable"));
        String repTypeCode = repRepo.getTypeRepCode(plaId).orElse("");
        if(!repTypeCode.equals("REP_PLA")) throw new AppException("Cette repartition n'est pas un placement");
        Long cesId = repRepo.getCesIdByRepId(plaId);
        if(cesId == null) throw new AppException("Auncun cessionnaire trouvé sur le placement " + plaId);
        Map<String, Object> params = new HashMap<>();
        Affaire affaire = affRepo.getAffaireByRepId(plaId);
        if(affaire == null) throw new AppException("Aucune affaire trouvée sur le placement " + plaId);
        placement.setAffaire(affaire);
        params.put("aff_id", placement.getAffaire().getAffId());
        params.put("aff_assure", placement.getAffaire().getAffAssure());
        params.put("fac_numero_police", placement.getAffaire().getFacNumeroPolice());
        params.put("ces_id", cesId);
        params.put("interlocuteur", interlocuteur);
        params.put("param_image", this.getImagesPath());
        if (placement.getRepStaCode().getStaCode().equals("VAL") || placement.getRepStaCode().getStaCode().equals("MAIL")){
            params.put("param_visible", "true");
        }
        else{
            params.put("param_visible", "false");
        }
        byte[] reportBytes = this.generateReport(jrConfig.noteCession, params, new ArrayList<>(), null);
        return reportBytes;
    }

    @Override
    public byte[] generateNoteDebitFac(Long affId) throws Exception
    {
        Affaire aff = affRepo.findById(affId).orElseThrow(()-> new AppException("Affaire introuvable"));
        if(!bordRep.noteDebExistsByAffId(affId)) bordService.createNoteDebit(affId);

        Map<String, Object> params = new HashMap<>();
        params.put("aff_id", aff.getAffId());
        params.put("aff_assure", aff.getAffAssure());
        params.put("fac_numero_police", aff.getFacNumeroPolice());
        params.put("param_image", this.getImagesPath());

        //Affichage du cachet en fonction d'une expression dans l'etat
        if (aff.getStatut().getStaCode().equals("VAL") ||aff.getStatut().getStaCode().equals("MAIL")){
            params.put("param_visible", "true");
        }else{
            params.put("param_visible", "false");
        }

        byte[] reportBytes = this.generateReport(jrConfig.noteDebit, params, new ArrayList<>(), null);
        return reportBytes;
    }

    @Override
    public byte[] generateNoteCreditFac(Long affId, Long cesId) throws Exception
    {
        Repartition placement = repRepo.getPlacementByAffIdAndCesId(affId,cesId).orElseThrow(()-> new AppException("Placement introuvable"));
        String repTypeCode = repRepo.getTypeRepCode(placement.getRepId()).orElse("");
        if(!repTypeCode.equals("REP_PLA")) throw new AppException("Cette repartition n'est pas un placement");
        Affaire affaire = affRepo.findById(affId).orElseThrow(()->new AppException("Affaire introuvable " + affId));
        placement.setAffaire(affaire);
        Map<String, Object> params = new HashMap<>();
        params.put("aff_id", placement.getAffaire().getAffId());
        params.put("aff_assure", placement.getAffaire().getAffAssure());
        params.put("fac_numero_police", placement.getAffaire().getFacNumeroPolice());
        params.put("ces_id", cesId);
        params.put("param_image", this.getImagesPath());
        byte[] reportBytes = this.generateReport(jrConfig.noteCredit, params, new ArrayList<>(), null);
        return reportBytes;
    }

    @Override
    public byte[] generateNoteCessionSinistre(Long sinId, Long cesId, String interlocuteur) throws Exception
    {
        Affaire affaire = affRepo.getAffaireBySinId(sinId).orElseThrow(()-> new AppException("Affaire introuvable"));
         Sinistre sinistre = sinRepo.findById(sinId).orElseThrow(()->new AppException("Sinistre introuvable"));
        String sinStatut = sinistre.getStatut() == null ? null : sinistre.getStatut().getStaCode();
        Map<String, Object> params = new HashMap<>();
        params.put("aff_id", sinId);
        params.put("aff_assure", affaire.getAffAssure());
        params.put("interlocuteur", interlocuteur);
        params.put("fac_numero_police", affaire.getFacNumeroPolice());
        params.put("ces_id", cesId);
        params.put("param_image", this.getImagesPath());

        //Affichage du cachet en fonction d'une expression dans l'etat
        if(sinStatut == null)params.put("param_visible", "false");
        else if (sinStatut.equals("APAI") || sinStatut.equals("CPAI") || sinStatut.equals("RET-COMPTA") || sinStatut.equals("CPAI-CREV") || sinStatut.equals("CREV") || sinStatut.equals("SOLDE")){
            params.put("param_visible", "true");
        }else{
            params.put("param_visible", "false");
        }
        String qrText = "Votre note de cession sur le sinistre N°" + sinistre.getSinCode() + " relatif à l'affaire N°" + affaire.getAffCode() + " pour le compte de l'assuré " + affaire.getAffAssure();
        byte[] reportBytes = this.generateReport(jrConfig.noteCessionSinistre, params, new ArrayList<>(), qrText);
        return reportBytes;
    }

    @Override
    public byte[] generateNoteCessionSinistre(Long sinId, Long cesId) throws Exception
    {
        InterlocuteurListResp interlocuteur = interRepo.getInterlocuteursPrincipalBySinAndCes(sinId, cesId);
        if(interlocuteur == null) throw new AppException("L'interlocuteur principal est inconnu");
        return this.generateNoteCessionSinistre(sinId, cesId, interlocuteur.getIntNom());
    }

    @Override
    public byte[] generateNoteDebitSinistre(Long sinId) throws Exception
    {
        Affaire affaire = affRepo.getAffaireBySinId(sinId).orElseThrow(()-> new AppException("Affaire introuvable"));
        String sinCode = sinRepo.getSinCode(sinId);
        Map<String, Object> params = new HashMap<>();
        params.put("aff_id", sinId);
        params.put("aff_assure", affaire.getAffAssure());
        params.put("fac_numero_police", sinCode);
        params.put("param_image", this.getImagesPath());
        String qrText = "Votre note de débit sur le sinistre N°" + sinCode + " relatif à l'affaire N°" + affaire.getAffCode() + " pour le compte de l'assuré " + affaire.getAffAssure();
        byte[] reportBytes = this.generateReport(jrConfig.noteDebitSinistre, params, new ArrayList<>(), qrText);
        return reportBytes;
    }

    @Override
    public byte[] generateCheque(Long regId) throws Exception
    {
        Reglement reglement =  regRepo.findById(regId).orElseThrow(()-> new AppException(("Règlement introuvable")));
        Map<String, Object> params = new HashMap<>();
        params.put("reg_id", reglement.getRegId());
        byte[] reportBytes = this.generateReport(jrConfig.cheque, params, new ArrayList<>(), null);
        return reportBytes;
    }

    @Override
    public byte[] generateChequeSinistre(Long regId) throws Exception {
        Reglement reglement =  regRepo.findById(regId).orElseThrow(()-> new AppException(("Règlement introuvable")));
        Map<String, Object> params = new HashMap<>();
        params.put("reg_id", reglement.getRegId());
        byte[] reportBytes = this.generateReport(jrConfig.chequeSinistre, params, new ArrayList<>(), null);
        return reportBytes;
    }

    @Override
    public byte[] generateNoteCessionFac(Long plaId) throws Exception {
        InterlocuteurListResp interlocuteur = interRepo.getInterlocuteursPrincipalResp(plaId);
        return this.generateNoteCessionFac(plaId, interlocuteur == null ? "Non spécifié" : interlocuteur.getIntNom() + " " + interlocuteur.getIntPrenom());
    }

    @Override
    public byte[] generateCompteTraite(Long traitenpId, Long cedenteId, Long trancheId, String periodicite, LocalDate periode) throws Exception {
        TraiteNonProportionnel traite = traiteNPRepo.findById(traitenpId).orElseThrow(()-> new AppException("Traité introuvable"));
        Cedante cedante = cedRepo.findById(cedenteId).orElseThrow(()-> new AppException("Cédante introuvable"));
        Tranche tranche = trancheRepo.findById(trancheId).orElseThrow(()-> new AppException("Tranche introuvable"));
        Map<String, Object> params = new HashMap<>();
        params.put("traitenpId", traite.getTraiteNpId());
        params.put("cedenteId", cedante.getCedId());
        params.put("trancheId", tranche.getTrancheId());
        params.put("periodicite", periodicite);
        params.put("periode", periode);
        params.put("param_image", this.getImagesPath());
        byte[] reportBytes = this.generateReport(jrConfig.compteTraite, params, new ArrayList<>(), null);
        return reportBytes;
    }
}
