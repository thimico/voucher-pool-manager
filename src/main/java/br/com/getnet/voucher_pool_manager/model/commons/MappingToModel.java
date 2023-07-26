package br.com.getnet.voucher_pool_manager.model.commons;

import br.com.getnet.voucher_pool_manager.domain.Voucher;
import br.com.getnet.voucher_pool_manager.model.VoucherRequest;
import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class MappingToModel {

    public static class StringToLocalDateConverter implements Converter<String, LocalDate> {
        @Override
        public LocalDate convert(MappingContext<String, LocalDate> context) {
            if (context.getSource() == null) {
                return null;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            try {
                return LocalDate.parse(context.getSource(), formatter);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date format. Please use 'dd/MM/yyyy' format.", e);
            }
        }
    }

    public static final PropertyMap<VoucherRequest, Voucher> mapVoucherRequestToVoucher = new PropertyMap<>() {
        @Override
        protected void configure() {
            map().getOfertaEspecial().setId(source.getOfertaEspecialId());
            using(new StringToLocalDateConverter()).map(source.getValidade(), destination.getValidade());
        }
    };

}
