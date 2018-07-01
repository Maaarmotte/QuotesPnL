package com.marmottes.pnl.rest.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PnlDto {
    private String isin;
    private String name;
    private String quantity;
    private String currency;
    private String buyPrice;
    private String currentPrice;
    private String pnl;
    private String type;
}
