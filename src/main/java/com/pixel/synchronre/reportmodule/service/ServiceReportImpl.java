package com.pixel.synchronre.reportmodule.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.pixel.synchronre.archivemodule.model.dtos.response.Base64FileDto;
import com.pixel.synchronre.reportmodule.config.JasperReportConfig;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.Base64ToFileConverter;
import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.ReglementRepository;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import com.pixel.synchronre.sychronremodule.model.entities.Reglement;
import com.pixel.synchronre.sychronremodule.model.entities.Repartition;
import jakarta.servlet.http.HttpServletResponse;
import com.google.zxing.EncodeHintType;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service @RequiredArgsConstructor
public class ServiceReportImpl implements IServiceReport
{
    private final JasperReportConfig jrConfig;
    private final DataSource dataSource;
    private final AffaireRepository affRepo;
    private final RepartitionRepository repRepo;
    private ReglementRepository regRepo;

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

    @Override
    public byte[] generateReport(String reportName, Map<String, Object> parameters, List<Object> data, String qrText) throws Exception
    {
        qrText =  qrText != null ? qrText : "Application SynchronRE : Votre Demande de placement porte sur N° Affaire : " + parameters.get("aff_id") + " Assuré : " + parameters.get("aff_assure") + " Numéro de Police : " + parameters.get("fac_numero_police");
        // Génération du code QR
        this.setQrCodeParam(parameters, qrText);

        JasperReport jasperReport = JasperCompileManager.compileReport(jrConfig.reportLocation + "/" + reportName);
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

    @Override
    public byte[] generateNoteCessionFac(Long plaId) throws Exception
    {
        Repartition placement = repRepo.findById(plaId).orElseThrow(()-> new AppException("Placement introuvable"));
        if(!placement.getType().getUniqueCode().equals("REP_PLA")) throw new AppException("Cette repartition n'est pas un placement");
        Map<String, Object> params = new HashMap<>();
        params.put("aff_id", placement.getAffaire().getAffId());
        params.put("aff_assure", placement.getAffaire().getAffAssure());
        params.put("fac_numero_police", placement.getAffaire().getFacNumeroPolice());
        params.put("ces_id", placement.getCessionnaire().getCesId());
        params.put("param_image", jrConfig.imagesLocation);
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
        Repartition repart = repRepo.repartFindByAffaire(affId).orElseThrow(()-> new AppException("Affaire introuvable"));

        Map<String, Object> params = new HashMap<>();
        params.put("aff_id", repart.getAffaire().getAffId());
        params.put("aff_assure", repart.getAffaire().getAffAssure());
        params.put("fac_numero_police", repart.getAffaire().getFacNumeroPolice());
        params.put("param_image", jrConfig.imagesLocation);

        //Affichage du cachet en fonction d'une expression dans l'etat
        if (repart.getRepStaCode().getStaCode().equals("VAL") || repart.getRepStaCode().getStaCode().equals("MAIL")){
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
        if(!placement.getType().getUniqueCode().equals("REP_PLA")) throw new AppException("Cette repartition n'est pas un placement");
        Map<String, Object> params = new HashMap<>();
        params.put("aff_id", placement.getAffaire().getAffId());
        params.put("aff_assure", placement.getAffaire().getAffAssure());
        params.put("fac_numero_police", placement.getAffaire().getFacNumeroPolice());
        params.put("ces_id", placement.getCessionnaire().getCesId());
        params.put("param_image", jrConfig.imagesLocation);
        byte[] reportBytes = this.generateReport(jrConfig.noteCredit, params, new ArrayList<>(), null);
        return reportBytes;
    }

    @Override
    public byte[] generateNoteCessionSinistre(Long plaId) throws Exception
    {
        Repartition placement = repRepo.findById(plaId).orElseThrow(()-> new AppException("Placement introuvable"));
        if(!placement.getType().getUniqueCode().equals("REP_PLA")) throw new AppException("Cette repartition n'est pas un placement");
        Map<String, Object> params = new HashMap<>();
        params.put("aff_id", placement.getAffaire().getAffId());
        params.put("aff_assure", placement.getAffaire().getAffAssure());
        params.put("fac_numero_police", placement.getAffaire().getFacNumeroPolice());
        params.put("ces_id", placement.getCessionnaire().getCesId());
        params.put("param_image", jrConfig.imagesLocation);

        //Affichage du cachet en fonction d'une expression dans l'etat
        if (placement.getRepStaCode().getStaCode().equals("VAL") || placement.getRepStaCode().getStaCode().equals("MAIL")){
            params.put("param_visible", "true");
        }else{
            params.put("param_visible", "false");
        }
        byte[] reportBytes = this.generateReport(jrConfig.noteCessionSinistre, params, new ArrayList<>(), null);
        return reportBytes;
    }

    @Override
    public byte[] generateNoteDebitSinistre(Long affId) throws Exception
    {
        Affaire affaire = affRepo.findById(affId).orElseThrow(()-> new AppException("Affaire introuvable"));
        Map<String, Object> params = new HashMap<>();
        params.put("aff_id", affaire.getAffId());
        params.put("aff_assure", affaire.getAffAssure());
        params.put("fac_numero_police", affaire.getFacNumeroPolice());
        params.put("param_image", jrConfig.imagesLocation);
        byte[] reportBytes = this.generateReport(jrConfig.noteDebitSinistre, params, new ArrayList<>(), null);
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
}
