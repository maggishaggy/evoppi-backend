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
package org.sing_group.evoppi.service.spi.bio.samespecies;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.sing_group.evoppi.service.bio.entity.InteractionIds;
import org.sing_group.evoppi.service.spi.bio.samespecies.pipeline.SameSpeciesGeneInteractionsPipeline;
import org.sing_group.evoppi.service.spi.bio.samespecies.pipeline.SameSpeciesGeneInteractionsStep;
import org.sing_group.evoppi.service.spi.bio.samespecies.pipeline.event.SameSpeciesGeneInteractionsEvent;
import org.sing_group.evoppi.service.spi.bio.samespecies.pipeline.event.SameSpeciesGeneInteractionsEventManager;
import org.sing_group.evoppi.service.spi.execution.pipeline.PipelineContext;

public interface SameSpeciesGeneInteractionsContext
extends PipelineContext<
  SameSpeciesGeneInteractionsConfiguration,
  SameSpeciesGeneInteractionsContext,
  SameSpeciesGeneInteractionsStep,
  SameSpeciesGeneInteractionsPipeline,
  SameSpeciesGeneInteractionsEvent,
  SameSpeciesGeneInteractionsEventManager
> {
  public Optional<Map<Integer, Set<InteractionIds>>> getInteractionsByDegree();
  
  public default Optional<IntStream> getInteractionsDegrees() {
    return this.getInteractionsByDegree().map(
      interactions -> interactions.keySet().stream()
        .mapToInt(Integer::intValue)
    );
  }
  
  public default Optional<Stream<InteractionIds>> getInteractionsWithDegree(int degree) {
    return this.getInteractionsByDegree()
      .map(interactions -> interactions.get(degree))
      .map(Set::stream);
  }
  
  public default Optional<Boolean> hasInteractionsWithDegree(int degree) {
    return this.getInteractionsDegrees().map(
      interactions -> interactions.anyMatch(interactionDegree -> interactionDegree == degree)
    );
  }
  
  public default Optional<Stream<InteractionIds>> getInteractions() {
    return this.getInteractionsByDegree().map(
      interactions -> interactions.values().stream()
        .flatMap(Set::stream)
    );
  }
  
  public Optional<Stream<InteractionIds>> getCompletedInteractions();
}
