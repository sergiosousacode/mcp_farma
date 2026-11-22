package br.com.mcpfarma.importacao;

public final class GtinValidator {

    private GtinValidator() {
    }

    public static boolean isValid(String gtin) {

        if (gtin == null ||
                !gtin.matches("\\d{8}|\\d{12}|\\d{13}|\\d{14}")) {
            return false;
        }

        int soma = 0;
        int peso = 3;

        for (int i = gtin.length() - 2; i >= 0; i--) {

            int digito = gtin.charAt(i) - '0';

            soma += digito * peso;

            peso = (peso == 3) ? 1 : 3;
        }

        int digitoCalculado = (10 - (soma % 10)) % 10;

        int digitoInformado =
                gtin.charAt(gtin.length() - 1) - '0';

        return digitoCalculado == digitoInformado;
    }
}