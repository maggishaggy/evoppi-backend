/*-
 * #%L
 * Service
 * %%
 * Copyright (C) 2017 - 2018 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
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
package org.sing_group.evoppi.service.bio.differentspecies.pipeline;

import static java.util.Objects.requireNonNull;
import static javax.transaction.Transactional.TxType.NOT_SUPPORTED;

import java.util.stream.Stream;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sing_group.evoppi.domain.entities.spi.bio.HasGeneInteractionIds;
import org.sing_group.evoppi.service.bio.GenePairIndexer;
import org.sing_group.evoppi.service.bio.GenePairIndexer.GenePairIndex;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsContext;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsContextBuilder;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsContextBuilderFactory;
import org.sing_group.evoppi.service.spi.bio.differentspecies.pipeline.SingleDifferentSpeciesGeneInteractionsStep;

@Default
@Transactional(NOT_SUPPORTED)
public class DefaultDifferentSpeciesCompleteReferenceInteractionsGeneInteractionsStep
implements SingleDifferentSpeciesGeneInteractionsStep {
  
  private GenePairIndexer interactomeIndexer;
  private DifferentSpeciesGeneInteractionsContextBuilderFactory contextBuilderFactory;
  
  private Integer interactomeId;
  
  DefaultDifferentSpeciesCompleteReferenceInteractionsGeneInteractionsStep() {}

  public DefaultDifferentSpeciesCompleteReferenceInteractionsGeneInteractionsStep(
    GenePairIndexer interactomeIndexer,
    DifferentSpeciesGeneInteractionsContextBuilderFactory contextBuilderFactory
  ) {
    this.setInteractomeIndexer(interactomeIndexer);
    this.setContextBuilderFactory(contextBuilderFactory);
  }

  @Inject
  public void setInteractomeIndexer(GenePairIndexer interactomeIndexer) {
    this.interactomeIndexer = requireNonNull(interactomeIndexer);
  }
  
  @Inject
  public void setContextBuilderFactory(DifferentSpeciesGeneInteractionsContextBuilderFactory contextBuilderFactory) {
    this.contextBuilderFactory = requireNonNull(contextBuilderFactory);
  }
  
  public void setInteractomeId(int interactomeId) {
    this.interactomeId = interactomeId;
  }
  
  @Override
  public String getName() {
    return "Reference interactome completion: " + this.interactomeId;
  }
  
  @Override
  public int getOrder() {
    return -1;
  }

  @Override
  public boolean isComplete(DifferentSpeciesGeneInteractionsContext context) {
    return context.getReferenceCompletedInteractions().isPresent();
  }

  @Override
  public DifferentSpeciesGeneInteractionsContext execute(DifferentSpeciesGeneInteractionsContext context) {
    if (this.interactomeId == null)
      throw new IllegalStateException("Interactome id should be set before executing the step");
    
    final GenePairIndex interactomeIndex = this.interactomeIndexer.createForInteractome(this.interactomeId);

    final DifferentSpeciesGeneInteractionsContextBuilder contextBuilder = this.contextBuilderFactory.createBuilderFor(context);
    
    final Stream<HasGeneInteractionIds> completedInteractions = context.getReferenceInteractions()
      .orElseThrow(() -> new IllegalStateException("Context should have reference interactions"))
      .filter(interaction -> interaction.getInteractomeId() != this.interactomeId)
      .filter(interaction -> !interactomeIndex.has(interaction))
      .map(interaction -> HasGeneInteractionIds.of(this.interactomeId, interaction))
      .distinct();
    
    contextBuilder.setReferenceCompletedInteractions(completedInteractions);
    
    return contextBuilder.build();
  }
}