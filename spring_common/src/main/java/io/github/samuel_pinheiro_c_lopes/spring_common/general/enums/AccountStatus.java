package io.github.samuel_pinheiro_c_lopes.spring_common.general.enums;

public enum AccountStatus {
	ACTIVE("Conta ativa e operacional."),
    DISABLED("A conta foi desativada, contate a ASTIN."),
    PENDING("Pedido de cadastro solicitado, aguardando aprovação...");

    private final String message;

    AccountStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}