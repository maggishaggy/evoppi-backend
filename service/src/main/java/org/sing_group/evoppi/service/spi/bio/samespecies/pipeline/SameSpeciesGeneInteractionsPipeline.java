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

package org.sing_group.evoppi.service.spi.bio.samespecies.pipeline;

import org.sing_group.evoppi.service.spi.bio.samespecies.SameSpeciesGeneInteractionsConfiguration;
import org.sing_group.evoppi.service.spi.bio.samespecies.SameSpeciesGeneInteractionsContext;
import org.sing_group.evoppi.service.spi.bio.samespecies.pipeline.event.SameSpeciesGeneInteractionsEvent;
import org.sing_group.evoppi.service.spi.bio.samespecies.pipeline.event.SameSpeciesGeneInteractionsEventManager;
import org.sing_group.evoppi.service.spi.execution.pipeline.Pipeline;

public interface SameSpeciesGeneInteractionsPipeline
extends Pipeline<
  SameSpeciesGeneInteractionsConfiguration,
  SameSpeciesGeneInteractionsContext,
  SameSpeciesGeneInteractionsStep,
  SameSpeciesGeneInteractionsPipeline,
  SameSpeciesGeneInteractionsEvent,
  SameSpeciesGeneInteractionsEventManager
> {
  public static final String MULTIPLE_CACULATE_INTERACTIONS_STEP_ID = "MULTIPLE CALCULATE INTERACTIONS";
  public static final String SINGLE_CACULATE_INTERACTIONS_STEP_ID = "SINGLE CALCULATE INTERACTIONS";
  public static final String MULTIPLE_COMPLETE_INTERACTIONS_STEP_ID = "MULTIPLE COMPLETE INTERACTIONS";
  public static final String SINGLE_COMPLETE_INTERACTIONS_STEP_ID = "SINGLE COMPLETE INTERACTIONS";
}
