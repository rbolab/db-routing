package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Fund;

import com.mycompany.myapp.domain.Order;
import com.mycompany.myapp.repository.FundRepository;
import com.mycompany.myapp.repository.OrderRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Fund.
 */
@RestController
@RequestMapping("/api")
public class FundResource {

    private final Logger log = LoggerFactory.getLogger(FundResource.class);

    @Inject
    private FundRepository fundRepository;

    @Inject
    private OrderRepository orderRepository;

    /**
     * POST  /funds : Create a new fund.
     *
     * @param fund the fund to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fund, or with status 400 (Bad Request) if the fund has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/funds")
    @Timed
    public ResponseEntity<Fund> createFund(@RequestBody Fund fund) throws URISyntaxException {
        log.debug("REST request to save Fund : {}", fund);
        if (fund.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("fund", "idexists", "A new fund cannot already have an ID")).body(null);
        }
        Fund result = fundRepository.save(fund);
        return ResponseEntity.created(new URI("/api/funds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("fund", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /funds : Updates an existing fund.
     *
     * @param fund the fund to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fund,
     * or with status 400 (Bad Request) if the fund is not valid,
     * or with status 500 (Internal Server Error) if the fund couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/funds")
    @Timed
    public ResponseEntity<Fund> updateFund(@RequestBody Fund fund) throws URISyntaxException {
        log.debug("REST request to update Fund : {}", fund);
        if (fund.getId() == null) {
            return createFund(fund);
        }
        Fund result = fundRepository.save(fund);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("fund", fund.getId().toString()))
            .body(result);
    }

    /**
     * GET  /funds : get all the funds.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of funds in body
     */
    @GetMapping("/funds")
    @Timed
    public List<Fund> getAllFunds() {
        log.debug("REST request to get all Funds");
        List<Order> orders = orderRepository.findAll();
        List<Fund> funds = fundRepository.findAll();
        return funds;
    }

    /**
     * GET  /funds/:id : get the "id" fund.
     *
     * @param id the id of the fund to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fund, or with status 404 (Not Found)
     */
    @GetMapping("/funds/{id}")
    @Timed
    public ResponseEntity<Fund> getFund(@PathVariable Long id) {
        log.debug("REST request to get Fund : {}", id);
        Fund fund = fundRepository.findOne(id);
        return Optional.ofNullable(fund)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /funds/:id : delete the "id" fund.
     *
     * @param id the id of the fund to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/funds/{id}")
    @Timed
    public ResponseEntity<Void> deleteFund(@PathVariable Long id) {
        log.debug("REST request to delete Fund : {}", id);
        fundRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("fund", id.toString())).build();
    }

}
