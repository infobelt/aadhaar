package com.infobelt.aadhaar.web;

import com.infobelt.aadhaar.domain.AbstractEntity;
import com.infobelt.aadhaar.query.QueryContext;
import com.infobelt.aadhaar.service.AbstractEntityService;
import com.infobelt.aadhaar.utils.HeaderUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional
public abstract class AbstractEntityResource<T extends AbstractEntity> {

    @Autowired
    @Getter
    private AbstractEntityService<T> entityService;

    // Provide access to the log for subclasses
    public Logger log() {
        return this.log;
    }

    @PostMapping("/")
    public ResponseEntity<T> create(@RequestBody T entity) throws URISyntaxException, IllegalAccessException {
        log.debug("REST request to save {} : {}", entityService.getEntityName(), entity);
        if (entity.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(entityService.getEntityName(), "idexists", "A new " + entityService.getEntityName() + " cannot already have an ID")).body(null);
        }
        T result = entityService.save(entity);
        return ResponseEntity.created(new URI("/api/" + entityService.getEntityPlural() + "/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(entityService.getEntityName(), result.getId().toString()))
            .body(result);
    }

    @PutMapping("/")
    public ResponseEntity<T> update(@RequestBody T entity) throws IllegalAccessException {
        log.debug("REST request to update {}: {}", entityService.getEntityName(), entity);
//        entity.setId(id);
        T result = entityService.save(entity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(entityService.getEntityName(), entity.getId().toString()))
            .body(result);
    }

    @GetMapping("/")
    public Page<T> list(QueryContext queryContext) {
        log.debug("REST request to get a page of " + entityService.getEntityName());
        return entityService.findAll(queryContext);
    }

    @GetMapping("/list")
    public List<T> list(){
        log.debug("REST request to get a page of" + entityService.getEntityName());
        return entityService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<T> get(@PathVariable Long id) {
        log.debug("REST request to get {}: {}", entityService.getEntityName(), id);
        Optional<T> entity = entityService.findOne(id);
        return entity.map((response) -> ResponseEntity.ok().headers(null).body(response)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete {} : {}", entityService.getEntityName(), id);
        entityService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(entityService.getEntityName(), id.toString())).build();
    }
}
