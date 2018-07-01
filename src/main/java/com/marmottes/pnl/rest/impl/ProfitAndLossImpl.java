package com.marmottes.pnl.rest.impl;

import com.marmottes.pnl.rest.ProfitAndLoss;
import com.marmottes.pnl.rest.dto.PnlDto;
import com.marmottes.pnl.services.ProfitAndLossService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pnl")
public class ProfitAndLossImpl implements ProfitAndLoss {
    private ProfitAndLossService profitAndLossService;

    @Autowired
    public ProfitAndLossImpl(ProfitAndLossService profitAndLossService) {
        this.profitAndLossService = profitAndLossService;
    }

    @Override
    @RequestMapping(value = "/{isin}/{value}/{quantity}/{buyFee}/{sellFee}", method = RequestMethod.POST)
    public String register(@PathVariable String isin, @PathVariable String value, @PathVariable Integer quantity,
                           @PathVariable String buyFee, @PathVariable String sellFee) {
        profitAndLossService.register(isin, value, quantity, buyFee, sellFee);
        return "";
    }

    @Override
    @RequestMapping(value = "/{isin}", method = RequestMethod.DELETE)
    public String remove(@PathVariable String isin) {
        profitAndLossService.remove(isin);
        return "";
    }

    @Override
    @RequestMapping(value = "/{isin}/{count}")
    public PnlDto get(@PathVariable String isin, @PathVariable Integer count) {
        return profitAndLossService.get(isin);
    }

    @Override
    @RequestMapping()
    public List<PnlDto> getAll() {
        return profitAndLossService.getAll();
    }
}
