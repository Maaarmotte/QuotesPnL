package com.marmottes.pnl.services;

import com.marmottes.pnl.entities.QuoteEntryEntity;
import com.marmottes.pnl.rest.dto.PnlDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProfitAndLossService {
    private static final int STANDARD_PRECISION = 2;
    private static final String NO_PRICE_STR = "Bestens";

    private QuotesService quotesService;
    private DataSerializerService dataSerializerService;

    private Map<String, QuoteEntryEntity> registeredQuotes;

    @Autowired
    public ProfitAndLossService(QuotesService quotesService, DataSerializerService dataSerializerService) {
        this.quotesService = quotesService;
        this.dataSerializerService = dataSerializerService;
        this.registeredQuotes = (Map<String, QuoteEntryEntity>) dataSerializerService.deserialize("quotes-pnl");

        if (this.registeredQuotes == null) {
            this.registeredQuotes = new HashMap<>();
        }
    }

    public void register(String isin, String value, Integer quantity, String buyFee, String sellFee) {
        registeredQuotes.put(isin, new QuoteEntryEntity(isin, value, quantity, buyFee, sellFee));
        dataSerializerService.serialize(registeredQuotes, "quotes-pnl");
    }

    public PnlDto get(String isin) {
        QuotesService.QuoteDetails quoteDetails = quotesService.getQuoteDetails(isin);
        QuoteEntryEntity quoteEntryEntity = registeredQuotes.get(isin);

        if (quoteDetails == null || quoteEntryEntity == null) {
            return PnlDto.builder().name(isin).build();
        }

        String type = "bid";
        String bid = quoteDetails.get(type);

        if (NO_PRICE_STR.equals(bid)) {
            type = "last";
            bid = quoteDetails.get(type);
        }

        if (bid == null || QuotesService.NO_VALUE_STRING.equals(bid)) {
            return PnlDto.builder().name(isin).build();
        }

        BigDecimal currentValue = new BigDecimal(bid);
        BigDecimal buyPrice = new BigDecimal(quoteEntryEntity.getBuyPrice());
        BigDecimal quantity = BigDecimal.valueOf(quoteEntryEntity.getQuantity());
        BigDecimal buyFee = new BigDecimal(quoteEntryEntity.getBuyFee());
        BigDecimal sellFee = new BigDecimal(quoteEntryEntity.getSellFee());
        BigDecimal pnl = currentValue
                .subtract(buyPrice)
                .multiply(quantity)
                .subtract(buyFee)
                .subtract(sellFee)
                .setScale(STANDARD_PRECISION, BigDecimal.ROUND_HALF_EVEN);

        return PnlDto.builder()
                .isin(isin)
                .name(quoteDetails.get("name"))
                .quantity(quantity.toPlainString())
                .currency(isin.split("_")[2])
                .buyPrice(buyPrice.toPlainString())
                .currentPrice(currentValue.toPlainString())
                .pnl(pnl.toPlainString())
                .type(type)
                .build();
    }

    public List<PnlDto> getAll() {
        return registeredQuotes.keySet().stream()
                .map(this::get)
                .collect(Collectors.toList());
    }

    public void remove(String isin) {
        registeredQuotes.remove(isin);
    }
}
