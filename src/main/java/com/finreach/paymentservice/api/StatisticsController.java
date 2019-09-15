package com.finreach.paymentservice.api;

import com.finreach.paymentservice.statistics.Statistics;
import com.finreach.paymentservice.util.TransactionsGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/statistics")
public class StatisticsController {

    private final TransactionsGenerator transactionsGenerator;

    public StatisticsController(TransactionsGenerator transactionsGenerator) {
        this.transactionsGenerator = transactionsGenerator;
    }

    @GetMapping(path = "/{second}")
    public Statistics get(@PathVariable("second") Integer second) {
        return transactionsGenerator.calculate(second);
    }

}
