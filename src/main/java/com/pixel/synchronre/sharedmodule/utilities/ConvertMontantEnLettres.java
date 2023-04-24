package com.pixel.synchronre.sharedmodule.utilities;



public class ConvertMontantEnLettres {

    private static final String[] UNITES = {
            "", "un", "deux", "trois", "quatre", "cinq", "six", "sept", "huit", "neuf"
    };
    private static final String[] DIZAINES = {
            "", "dix", "vingt", "trente", "quarante", "cinquante", "soixante", "soixante-dix", "quatre-vingt", "quatre-vingt-dix"
    };

    public static String convertir(double montant) {
        int entier = (int) montant;
        int centimes = (int) Math.round((montant - entier) * 100);

        String montantEnLettres = convertirEntier(entier);
        if (centimes > 0) {
            montantEnLettres += " et " + convertirEntier(centimes) + " centime" + (centimes > 1 ? "s" : "");
        }

        return montantEnLettres;
    }

    private static String convertirEntier(int entier) {
        if (entier == 0) {
            return "zéro";
        }

        if (entier < 0) {
            return "moins " + convertirEntier(-entier);
        }

        if (entier <= 9) {
            return UNITES[entier];
        }

        if (entier <= 19) {
            return DIZAINES[1] + " " + UNITES[entier - 10];
        }

        if (entier <= 99) {
            int dizaine = entier / 10;
            int unite = entier % 10;

            if (unite == 0) {
                return DIZAINES[dizaine];
            } else if (dizaine == 7 || dizaine == 9) {
                return DIZAINES[dizaine - 1] + "-" + UNITES[10 + unite];
            } else {
                return DIZAINES[dizaine] + "-" + UNITES[unite];
            }
        }

        if (entier <= 999) {
            int centaine = entier / 100;
            int reste = entier % 100;

            if (reste == 0) {
                return UNITES[centaine] + " cent";
            } else {
                return UNITES[centaine] + " cent " + convertirEntier(reste);
            }
        }

        if (entier <= 999_999) {
            int millier = entier / 1000;
            int reste = entier % 1000;

            if (reste == 0) {
                return convertirEntier(millier) + " mille";
            } else {
                return convertirEntier(millier) + " mille " + convertirEntier(reste);
            }
        }

        if (entier <= 999_999_999) {
            int million = entier / 1_000_000;
            int reste = entier % 1_000_000;

            if (reste == 0) {
                return convertirEntier(million) + " million";
            } else {
                return convertirEntier(million) + " million " + convertirEntier(reste);
            }
        }

        return "nombre trop grand";
    }
}