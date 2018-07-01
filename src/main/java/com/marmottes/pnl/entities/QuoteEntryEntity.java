package com.marmottes.pnl.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class QuoteEntryEntity implements Serializable {
    private String isin;
    private String buyPrice;
    private Integer quantity;
    private String buyFee;
    private String sellFee;
}
