package com.marmottes.pnl.rest;

import com.marmottes.pnl.rest.dto.PnlDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@RequestMapping("/pnl")
public interface ProfitAndLoss {
    @RequestMapping(value = "/{isin}/{value}/{quantity}/{buyFee}/{sellFee}", method = RequestMethod.POST)
    String register(@PathVariable String isin, @PathVariable String value, @PathVariable Integer quantity,
                    @PathVariable String buyFee, @PathVariable String sellFee);

    @RequestMapping(value = "/{isin}", method = RequestMethod.DELETE)
    String remove(@PathVariable String isin);

    @RequestMapping(value = "/{isin}/{count}")
    PnlDto get(@PathVariable String isin, @PathVariable Integer count);

    @RequestMapping()
    List<PnlDto> getAll();
}
