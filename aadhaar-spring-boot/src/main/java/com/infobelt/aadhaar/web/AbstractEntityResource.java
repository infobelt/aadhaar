package com.infobelt.aadhaar.web;

import com.infobelt.aadhaar.domain.AbstractKeyed;
import com.infobelt.aadhaar.dto.ExceptionReport;
import com.infobelt.aadhaar.query.QueryContext;
import com.infobelt.aadhaar.service.AbstractEntityService;
import com.infobelt.aadhaar.utils.HeaderUtil;
import com.infobelt.aadhaar.utils.PaginationUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@Transactional
public abstract class AbstractEntityResource<T extends AbstractKeyed> {

    @Autowired
    @Getter
    private AbstractEntityService<T> entityService;

    // Provide access to the log for subclasses
    public Logger log() {
        return AbstractEntityResource.log;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionReport> processRuntimeException(Exception ex) {
        ResponseEntity.BodyBuilder builder;
        ExceptionReport exceptionReport;
        ResponseStatus responseStatus = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);
        if (responseStatus != null) {
            builder = ResponseEntity.status(responseStatus.value());
            exceptionReport = new ExceptionReport("error." + responseStatus.value().value(), responseStatus.reason());
        } else {
            builder = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
            exceptionReport = new ExceptionReport("error.internalServerError", "Internal server error");
        }

        log.error("Exception [Incident ID: " + exceptionReport.getExceptionUuid() + "] has been returned.", ex);
        return builder.body(exceptionReport);
    }

    @ApiOperation("Create a new instance of the resource")
    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created the new resource"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "An internal exception has occurred, check the logs for more information", response = ExceptionReport.class)
    }
    )
    public ResponseEntity<T> create(@RequestBody T entity) throws URISyntaxException {
        log.debug("REST request to save {} : {}", entityService.getEntityName(), entity);
        if (entity.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(entityService.getEntityName(), "idexists", "A new " + entityService.getEntityName() + " cannot already have an ID")).body(null);
        }
        T result = entityService.save(entity);
        return ResponseEntity.created(new URI("/api/" + entityService.getEntityPlural() + "/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(entityService.getEntityName(), result.getId().toString()))
                .body(result);
    }

    @ApiOperation("Update the given instance")
    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the resource"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "An internal exception has occurred, check the logs for more information", response = ExceptionReport.class)
    }
    )
    public ResponseEntity<T> update(@RequestBody T entity, @PathVariable Long id) {
        log.debug("REST request to update {}: {}", entityService.getEntityName(), entity);
        entity.setId(id);
        T result = entityService.save(entity);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(entityService.getEntityName(), entity.getId().toString()))
                .body(result);
    }

    // This is from a previous life
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the resource"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "An internal exception has occurred, check the logs for more information", response = ExceptionReport.class)
    }
    )
    @ApiOperation("Update the given instance (legacy method)")
    @PutMapping
    @Deprecated
    public ResponseEntity<T> update(@RequestBody T entity) throws IllegalAccessException {
        return update(entity, entity.getId());
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully listed a page the resources"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "An internal exception has occurred, check the logs for more information", response = ExceptionReport.class)
    }
    )
    @ApiOperation("List a page of the resources")
    @GetMapping
    public Page<T> list(QueryContext queryContext) {
        log.debug("REST request to get a page of " + entityService.getEntityName());
        return entityService.findAll(queryContext);
    }

    @GetMapping("/list")
    @Deprecated
    @ApiOperation("List all of the resources (legacy)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully listed all the resources"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "An internal exception has occurred, check the logs for more information", response = ExceptionReport.class)
    }
    )
    public List<T> oldListAll() {
        log.debug("REST request to get a page of" + entityService.getEntityName());
        return entityService.findAll();
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully listed all the resources"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "An internal exception has occurred, check the logs for more information", response = ExceptionReport.class)
    }
    )
    @ApiOperation("List all of the resources (legacy)")
    @GetMapping(params = "list")
    public List<T> listAll() {
        log.debug("REST request to get a page of" + entityService.getEntityName());
        return entityService.findAll();
    }

    @ApiOperation("Get an resource with the provided ID")
    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully got the resource"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "An internal exception has occurred, check the logs for more information", response = ExceptionReport.class)
    }
    )
    public ResponseEntity<T> get(@PathVariable Long id) {
        log.debug("REST request to get {}: {}", entityService.getEntityName(), id);
        Optional<T> entity = entityService.findOne(id);
        return entity.map((response) -> ResponseEntity.ok().headers(null).body(response)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ApiOperation("Delete the resource with the provided ID")
    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted the resource"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "An internal exception has occurred, check the logs for more information", response = ExceptionReport.class)
    }
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete {} : {}", entityService.getEntityName(), id);
        entityService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(entityService.getEntityName(), id.toString())).build();
    }

    @ApiOperation("Perform a search")
    @GetMapping("/_search")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully searched for the resource"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "An internal exception has occurred, check the logs for more information", response = ExceptionReport.class)
    }
    )
    public ResponseEntity<Page<T>> search(@RequestParam String query, Pageable pageable)
            throws URISyntaxException {
        log().debug("REST request to search for a page of {} with query {}", getEntityService().getEntityPlural(), query);
        Page<T> page = entityService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/" + getEntityService().getEntityPlural() + "/_search");
        return new ResponseEntity<>(page, headers, HttpStatus.OK);
    }
}
