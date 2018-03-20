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
package org.sing_group.evoppi.service.bio.differentspecies;

import static javax.transaction.Transactional.TxType.NOT_SUPPORTED;
import static javax.transaction.Transactional.TxType.REQUIRES_NEW;

import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sing_group.evoppi.domain.entities.bio.execution.BlastResult;
import org.sing_group.evoppi.domain.entities.bio.execution.DifferentSpeciesInteractionsResult;
import org.sing_group.evoppi.service.spi.bio.InteractionService;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsContext;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsPersistenceManager;
import org.sing_group.evoppi.service.spi.bio.differentspecies.event.DifferentSpeciesGeneInteractionsEvent;

@Stateless
@PermitAll
@Transactional(NOT_SUPPORTED)
public class DefaultDifferentSpeciesGeneInteractionsPersistenceManager
implements DifferentSpeciesGeneInteractionsPersistenceManager {
  @Inject
  public InteractionService interactionsService;

  @Override
  @Transactional(REQUIRES_NEW)
  public void manageEvent(@Observes DifferentSpeciesGeneInteractionsEvent event) {
    final DifferentSpeciesGeneInteractionsContext context = event.getContext();
    final String resultId = context.getConfiguration().getResultId();
    
    final DifferentSpeciesInteractionsResult result = this.interactionsService.getDifferentSpeciesResult(resultId);
    
    switch (event.getStatus()) {
    case RUNNING:
      result.setRunning();

      event.getContext().getReferenceInteractions().ifPresent(__ -> this.persistReferenceInteractions(event));
      event.getContext().getTargetInteractions().ifPresent(__ -> this.persistTargetInteractions(event));
      event.getContext().getBlastResults().ifPresent(__ -> this.persistBlastResults(event));
      
      break;
    case COMPLETED:
      result.setFinished();
      
      break;
    case FAILED:
      result.setFailed(event.getDescription());
      
      break;
    default:
    }
  }
  
  private DifferentSpeciesInteractionsResult getResultForEvent(DifferentSpeciesGeneInteractionsEvent event) {
    final String resultId = event.getContext().getConfiguration().getResultId();
    
    return this.interactionsService.getDifferentSpeciesResult(resultId);
  }

  private void persistReferenceInteractions(DifferentSpeciesGeneInteractionsEvent event) {
    final DifferentSpeciesInteractionsResult result = this.getResultForEvent(event);
    
    if (!result.hasReferenceInteractions()) {
      final DifferentSpeciesGeneInteractionsContext context = event.getContext();
      
      context.getReferenceInteractions()
        .orElseThrow(IllegalStateException::new)
      .forEach(
        interaction -> {
          result.addInteraction(
            interaction.getGeneAId(),
            interaction.getGeneBId(),
            interaction.getInteractomeDegrees()
          );
        }
      );
    }
  }

  private void persistBlastResults(DifferentSpeciesGeneInteractionsEvent event) {
    final DifferentSpeciesInteractionsResult result = this.getResultForEvent(event);
    
    if (!result.hasBlastResults()) {
      final DifferentSpeciesGeneInteractionsContext context = event.getContext();
      
      final Stream<BlastResult> blastResults = context.getBlastResults().orElseThrow(IllegalStateException::new);
      
      blastResults.forEach(result::addBlastResult);
    }
  }

  private void persistTargetInteractions(DifferentSpeciesGeneInteractionsEvent event) {
    final DifferentSpeciesInteractionsResult result = this.getResultForEvent(event);
    
    if (!result.hasTargetInteractions()) {
      final DifferentSpeciesGeneInteractionsContext context = event.getContext();
      
      context.getTargetInteractions()
        .orElseThrow(IllegalStateException::new)
      .forEach(
        interaction -> {
          result.addInteraction(
            interaction.getGeneAId(),
            interaction.getGeneBId(),
            interaction.getInteractomeDegrees()
          );
        }
      );
    }
  }
}
