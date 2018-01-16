/*-
 * #%L
 * REST
 * %%
 * Copyright (C) 2017 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.sing_group.evoppi.rest.resource.bio;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static org.sing_group.fluent.checker.Checks.requireNonEmpty;

import java.util.function.Function;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.sing_group.evoppi.domain.entities.bio.execution.InteractionsResult;
import org.sing_group.evoppi.domain.entities.execution.Work;
import org.sing_group.evoppi.rest.entity.execution.WorkData;
import org.sing_group.evoppi.rest.entity.mapper.spi.bio.BioMapper;
import org.sing_group.evoppi.rest.entity.mapper.spi.execution.ExecutionMapper;
import org.sing_group.evoppi.rest.filter.CrossDomain;
import org.sing_group.evoppi.rest.resource.route.BaseRestPathBuilder;
import org.sing_group.evoppi.rest.resource.spi.bio.InteractionResource;
import org.sing_group.evoppi.service.spi.bio.InteractionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("interaction")
@Api(value = "interaction")
@Produces({ APPLICATION_JSON, APPLICATION_XML })
@Consumes({ APPLICATION_JSON, APPLICATION_XML })
@Stateless
@Default
@CrossDomain
public class DefaultInteractionResource implements InteractionResource {
  @Inject
  private InteractionService service;
  
  @Inject
  private ExecutionMapper executionMapper;
  
  @Inject
  private BioMapper bioMapper;

  @Context
  private UriInfo uriInfo;
  
  @PostConstruct
  public void postConstruct() {
    final UriBuilder uriBuilder = this.uriInfo.getBaseUriBuilder();
    this.bioMapper.setUriBuilder(uriBuilder);
    this.executionMapper.setUriBuilder(uriBuilder);
  }
  
  @GET
  @ApiOperation(
    value = "Calculates the interactions of a gene according to one or many interactomes. "
      + "The calculus are done asynchronously, thus this method returns a work-data instance with information about "
      + "the asynchronous task doing the calculations.",
    response = WorkData.class,
    code = 200
  )
  @Override
  public Response getInteractions(
    @QueryParam("gene") int geneId,
    @QueryParam("interactome") int[] interactomes,
    @QueryParam("maxDegree") @DefaultValue("1") int maxDegree
  ) {
    if (maxDegree < 1 || maxDegree > 3)
      throw new IllegalArgumentException("maxDegree must be between 1 and 3");
    requireNonEmpty(interactomes, "At least one interactome id should be provided");
    
    final UriBuilder uriBuilder = this.uriInfo.getBaseUriBuilder();
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(uriBuilder);
    
    final Function<Integer, String> resultUriBuilder =
      id -> pathBuilder.interaction().result(id).build().toString();
      
    final Work work = this.service.findInteractionsByGene(
      geneId, interactomes, maxDegree, resultUriBuilder
    );
    
    return Response.ok(this.executionMapper.toWorkData(work)).build();
  }

  @GET
  @Path("result/{id: \\d+}")
  @ApiOperation(
    value = "Returns the result of a interaction calculus. .",
    response = WorkData.class,
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown interaction result: {id}")
  )
  @Override
  public Response getInterationResult(
    @PathParam("id") int id
  ) {
    final InteractionsResult result = this.service.getResult(id);
    
    return Response
      .ok(this.bioMapper.toInteractionQueryResult(result))
    .build();
  }
}
