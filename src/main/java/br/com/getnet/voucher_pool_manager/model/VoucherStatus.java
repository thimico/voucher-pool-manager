package br.com.getnet.voucher_pool_manager.model;


import jakarta.xml.bind.ValidationException;

public enum VoucherStatus {

    NAT("NAT", "Desativado"),
    ATV("ATV", "Ativado"),
    BLK("BLK", "Bloqueado"),
    UBK("UBK", "Desbloqueado"),
    CAN("CAN", "Cancelado"),
    USD("USD", "Usado"),
    EXP("EXP", "Expirado");

    private String name;

    private String description;

    VoucherStatus(final String name, final String description) {

        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static VoucherStatus fromString(final String status) throws ValidationException {

        for (VoucherStatus type: values()) {
            if (type.name().equals(status)) {
                return type;
            }
        }

        throw new ValidationException("Status nao encontrado: " + status);
    }

    public static VoucherStatus findByCode(final String code) {

        for (VoucherStatus VoucherStatus : values()) {
            if (VoucherStatus.toString().equalsIgnoreCase(code)) {
                return VoucherStatus;
            }
        }

        return null;
    }

}
