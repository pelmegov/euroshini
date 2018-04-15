package ru.pelmegov.euroshini.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.pelmegov.euroshini.domain.RevenueHistory;
import ru.pelmegov.euroshini.domain.SaleHistory;
import ru.pelmegov.euroshini.repository.RevenueHistoryRepository;
import ru.pelmegov.euroshini.repository.SaleHistoryRepository;
import ru.pelmegov.euroshini.service.mapper.RevenueHistoryMapper;
import ru.pelmegov.euroshini.service.mapper.SaleHistoryMapper;
import ru.pelmegov.euroshini.web.rest.util.PaginationUtil;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@RestController
@RequestMapping("/management/reports")
public class ReportResource {

    private final RevenueHistoryRepository revenueHistoryRepository;
    private final RevenueHistoryMapper revenueHistoryMapper;

    private final SaleHistoryRepository saleHistoryRepository;
    private final SaleHistoryMapper saleHistoryMapper;

    @Autowired
    public ReportResource(RevenueHistoryRepository revenueHistoryRepository, RevenueHistoryMapper revenueHistoryMapper, SaleHistoryRepository saleHistoryRepository, SaleHistoryMapper saleHistoryMapper) {
        this.revenueHistoryRepository = revenueHistoryRepository;
        this.revenueHistoryMapper = revenueHistoryMapper;
        this.saleHistoryRepository = saleHistoryRepository;
        this.saleHistoryMapper = saleHistoryMapper;
    }

    @GetMapping(path = "/revenue", params = {"fromDate", "toDate"})
    public ResponseEntity<List<RevenueHistory>> getRevenueHistory(
        @RequestParam(value = "fromDate") LocalDate fromDate,
        @RequestParam(value = "toDate") LocalDate toDate,
        Pageable pageable) {
        final List<RevenueHistory> revenueHistories = revenueHistoryRepository.findByCreatedDateAfterAndCreatedDateBefore(
            fromDate.atStartOfDay().toInstant(ZoneOffset.UTC),
            toDate.atStartOfDay().toInstant(ZoneOffset.UTC)
        );
        Page page = new PageImpl(revenueHistories, pageable, revenueHistories.size());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/management/reports/revenue");
        return new ResponseEntity<>(revenueHistoryMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    @GetMapping(path = "/sale", params = {"fromDate", "toDate"})
    public ResponseEntity<List<RevenueHistory>> getSaleHistory(
        @RequestParam(value = "fromDate") LocalDate fromDate,
        @RequestParam(value = "toDate") LocalDate toDate,
        Pageable pageable) {
        final List<SaleHistory> revenueHistories = saleHistoryRepository.findByCreatedDateAfterAndCreatedDateBefore(
            fromDate.atStartOfDay().toInstant(ZoneOffset.UTC),
            toDate.atStartOfDay().toInstant(ZoneOffset.UTC)
        );
        Page page = new PageImpl(revenueHistories, pageable, revenueHistories.size());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/management/reports/sale");
        return new ResponseEntity<>(saleHistoryMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

}
