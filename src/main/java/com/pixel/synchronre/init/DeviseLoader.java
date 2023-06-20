package com.pixel.synchronre.init;

import com.pixel.synchronre.sharedmodule.enums.PersStatus;
import com.pixel.synchronre.sychronremodule.model.dao.DeviseRepository;
import com.pixel.synchronre.sychronremodule.model.entities.Devise;
import com.pixel.synchronre.sychronremodule.model.entities.Statut;
import com.pixel.synchronre.typemodule.controller.repositories.TypeParamRepo;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import com.pixel.synchronre.typemodule.model.entities.Type;
import com.pixel.synchronre.typemodule.model.entities.TypeParam;
import com.pixel.synchronre.typemodule.model.enums.TypeGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service @RequiredArgsConstructor
public class DeviseLoader implements Loader
{
    private final DeviseRepository devRepo;
    @Override
    public void load()
    {
        Devise dev0 = new Devise ("XOF",  "Franc CFA", "F CFA", "XOF", new Statut("ACT"));
        Devise dev1 = new Devise ("AED",  "Dirham EMIRAT ", "Dirham EMIRAT ", null,  new Statut("ACT"));
        Devise dev2 = new Devise ("AOA",  "Angolan kwanza", "Angolan kwanza", null,  new Statut("ACT"));
        Devise dev3 = new Devise ("BDT",  "Taka Bangladesh", "Taka Bangladesh", null,  new Statut("ACT"));
        Devise dev4 = new Devise ("BHD",  "Dinar Bahrein", "Dinar Bahrein", null,  new Statut("ACT"));
        Devise dev5 = new Devise ("BIF",  "Franc Burundais", "Franc Burundais", null,  new Statut("ACT"));
        Devise dev6 = new Devise ("BTN",  "Bhoutan-Ngultru", "Bhoutan-Ngultru", null,  new Statut("ACT"));
        Devise dev7 = new Devise ("BWP",  "Pula Botswana", "Pula Botswana", null, new Statut("ACT"));
        Devise dev8 = new Devise ("CHF",  "Francs suisse", "Francs suisse", null,  new Statut("ACT"));
        Devise dev9 = new Devise ("CVE",  "Escudo", "Escudo", null,  new Statut("ACT"));
        Devise dev10 = new Devise ("DJF",  "Franc Djibouti", "Franc Djibouti", null,  new Statut("ACT"));
        Devise dev11 = new Devise ("DZD",  "Dinar Algérien", "Dinar Algérien", null,  new Statut("ACT"));
        Devise dev12 = new Devise ("EGP",  "Livre Egypte", "Livre Egypte", null,  new Statut("ACT"));
        Devise dev13 = new Devise ("ETB",  "Birr Ethiopie", "Birr Ethiopie", null,  new Statut("ACT"));
        Devise dev14 = new Devise ("EUR",  "Euro", "Euro", null, new Statut("ACT"));
        Devise dev15 = new Devise ("GBP",  "Livre sterling", "Livre sterling", null, new Statut("ACT"));
        Devise dev16 = new Devise ("GHS",  "Cedis Ghanéen n", "Cedis Ghanéen n", null,  new Statut("ACT"));
        Devise dev17 = new Devise ("GMD",  "Dalasi Gambie", "Dalasi Gambie", null, new Statut("ACT"));
        Devise dev18 = new Devise ("GNF",  "Franc Guinée", "Franc Guinée", null,  new Statut("ACT"));
        Devise dev19 = new Devise ("IDR",  "Rupiah Indonés.", "Rupiah Indonés.", null,  new Statut("ACT"));
        Devise dev20 = new Devise ("INR",  "Roupie indienne", "Roupie indienne", null,  new Statut("ACT"));
        Devise dev21 = new Devise ("IQD",  "Dinar Irakien", "Dinar Irakien", null,  new Statut("ACT"));
        Devise dev22 = new Devise ("IRR",  "Rial Iranien", "Rial Iranien", null,  new Statut("ACT"));
        Devise dev23 = new Devise ("JOD",  "Dinar Jordanien", "Dinar Jordanien", null,  new Statut("ACT"));
        Devise dev24 = new Devise ("JPY",  "YEN", "YEN", null,  new Statut("ACT"));
        Devise dev25 = new Devise ("KES",  "Shilling Kenya", "Shilling Kenya", null,  new Statut("ACT"));
        Devise dev26 = new Devise ("KPW",  "Won -Corée Nord", "Won -Corée Nord", null,  new Statut("ACT"));
        Devise dev27 = new Devise ("KRW",  "Corée du Sud ", "Corée du Sud ", null,  new Statut("ACT"));
        Devise dev28 = new Devise ("KWD",  "Dinar Koweïtien", "Dinar Koweïtien", null, new Statut("ACT"));
        Devise dev29 = new Devise ("LBP",  "Livre Libanaise", "Livre Libanaise", null,  new Statut("ACT"));
        Devise dev30 = new Devise ("LRD",  "Dollar libérien", "Dollar libérien", null,  new Statut("ACT"));
        Devise dev31 = new Devise ("LSL",  "Lesotho-Loti", "Lesotho-Loti", null, new Statut("ACT"));
        Devise dev32 = new Devise ("LYD",  "Dinar Lybien", "Dinar Lybien", null,  new Statut("ACT"));
        Devise dev33 = new Devise ("MAD",  "Dirham Marocain", "Dirham Marocain", null,  new Statut("ACT"));
        Devise dev34 = new Devise ("MGA",  "Ariayry Madaga.", "Ariayry Madaga.", null,  new Statut("ACT"));
        Devise dev35 = new Devise ("MGF",  "Franc magache", "Franc magache", null,  new Statut("ACT"));
        Devise dev36 = new Devise ("MRO",  "Ougiya Maurita.", "Ougiya Maurita.", null,  new Statut("ACT"));
        Devise dev37 = new Devise ("MRU",  "MRU", "MRU", null,  new Statut("ACT"));
        Devise dev38 = new Devise ("MUR",  "Roupie Maurice", "Roupie Maurice", null,  new Statut("ACT"));
        Devise dev39 = new Devise ("MVR",  "Roupie maldive", "Roupie maldive", null, new Statut("ACT"));
        Devise dev40 = new Devise ("MWK",  "Kwacha Malawi", "Kwacha Malawi", null,  new Statut("ACT"));
        Devise dev41 = new Devise ("MYR",  "Ringgit Malais.", "Ringgit Malais.", null,  new Statut("ACT"));
        Devise dev42 = new Devise ("MZM",  "Franc Mozambiq.", "Franc Mozambiq.", null,  new Statut("ACT"));
        Devise dev43 = new Devise ("MZN",  "Metical Mozamb.", "Metical Mozamb.", null,  new Statut("ACT"));
        Devise dev44 = new Devise ("NAD",  null, "0", null,  new Statut("ACT"));
        Devise dev45 = new Devise ("NGN",  "Naira", "Naira", null,  new Statut("ACT"));
        Devise dev46 = new Devise ("NPR",  "Népal Roupie", "Népal Roupie", null,  new Statut("ACT"));
        Devise dev47 = new Devise ("OMR",  "Rial d''OMAN", "Rial d''OMAN", null,  new Statut("ACT"));
        Devise dev48 = new Devise ("PGK",  "PAPOUASIE KINA", "PAPOUASIE KINA", null,  new Statut("ACT"));
        Devise dev49 = new Devise ("PHP",  "Peso philippin", "Peso philippin", null,  new Statut("ACT"));
        Devise dev50 = new Devise ("PKR",  "Pakistan-Roupie", "Pakistan-Roupie", null,  new Statut("ACT"));
        Devise dev51 = new Devise ("QAR",  "Riyal du Qatar", "Riyal du Qatar", null,  new Statut("ACT"));
        Devise dev52 = new Devise ("SZL ",  "Swaziland Lilan", "Swaziland Lilan", null,  new Statut("ACT"));
        Devise dev53 = new Devise ("RWF",  "Franc Rwandais", "Franc Rwandais", null, new Statut("ACT"));
        Devise dev54 = new Devise ("XAF",  "Franc CFA", "F CFA", "XAF", new Statut("ACT"));
        devRepo.saveAll(Arrays.asList(dev0,dev1,dev2,dev3,dev4,dev5,dev6,dev7,dev8,dev9,dev10,dev11,dev12,dev13,dev14,dev15,dev16,dev17,dev18,dev19,dev20,dev21,dev22,dev23,dev24,dev25,dev26,dev27,dev28,dev29,dev30,dev31,dev32,dev33,dev34,dev35,dev36,dev37,dev38,dev39,dev40,dev41,dev42,dev43,dev44,dev45,dev46,dev47,dev48,dev49,dev50,dev51,dev52,dev53,dev54));

    }
}
