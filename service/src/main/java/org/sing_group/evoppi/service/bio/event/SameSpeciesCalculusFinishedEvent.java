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
package org.sing_group.evoppi.service.bio.event;

import static java.util.stream.Collectors.joining;

import java.io.Serializable;
import java.util.stream.IntStream;

import org.sing_group.evoppi.domain.entities.bio.execution.ExecutionStatus;
import org.sing_group.evoppi.service.spi.execution.event.WorkStepEvent;

public class SameSpeciesCalculusFinishedEvent
extends SameSpeciesCalculusEvent
implements Serializable, WorkStepEvent {
  private static final long serialVersionUID = 1L;

  public SameSpeciesCalculusFinishedEvent(SameSpeciesCalculusEvent event) {
    super(event);
  }

  public SameSpeciesCalculusFinishedEvent(int geneId, int[] interactomes, int maxDegree, int workId, int resultId) {
    super(geneId, interactomes, maxDegree, workId, resultId);
  }

  @Override
  public String getDescription() {
    return String.format("Completed interactions calculation for gene %d in interactomes: %s",
      this.getGeneId(),
      IntStream.of(this.getInteractomes()).mapToObj(Integer::toString).collect(joining(", "))
    );
  }

  @Override
  public double getProgress() {
    return 1d;
  }

  @Override
  public ExecutionStatus getWorkStatus() {
    return ExecutionStatus.COMPLETED;
  }
}