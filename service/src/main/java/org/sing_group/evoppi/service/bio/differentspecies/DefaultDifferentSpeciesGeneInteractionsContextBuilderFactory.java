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

import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsConfiguration;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsContext;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsContextBuilder;
import org.sing_group.evoppi.service.spi.bio.differentspecies.DifferentSpeciesGeneInteractionsContextBuilderFactory;
import org.sing_group.evoppi.service.spi.bio.differentspecies.event.DifferentSpeciesGeneInteractionsEventManager;
import org.sing_group.evoppi.service.spi.bio.differentspecies.pipeline.DifferentSpeciesGeneInteractionsPipeline;

public class DefaultDifferentSpeciesGeneInteractionsContextBuilderFactory
implements DifferentSpeciesGeneInteractionsContextBuilderFactory {
  @Override
  public DifferentSpeciesGeneInteractionsContextBuilder createBuilderFor(
    DifferentSpeciesGeneInteractionsContext context
  ) {
    return new DefaultDifferentSpeciesGeneInteractionsContextBuilder(context);
  }
  
  @Override
  public DifferentSpeciesGeneInteractionsContextBuilder createBuilderFor(
    DifferentSpeciesGeneInteractionsPipeline pipeline,
    DifferentSpeciesGeneInteractionsConfiguration configuration,
    DifferentSpeciesGeneInteractionsEventManager eventManager
  ) {
    return new DefaultDifferentSpeciesGeneInteractionsContextBuilder(pipeline, configuration, eventManager);
  }
}
