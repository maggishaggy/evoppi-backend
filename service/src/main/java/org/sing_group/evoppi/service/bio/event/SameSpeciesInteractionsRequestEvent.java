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

import java.io.Serializable;

public class SameSpeciesInteractionsRequestEvent extends SameSpeciesCalculusEvent implements Serializable {
  private static final long serialVersionUID = 1L;

  public SameSpeciesInteractionsRequestEvent(SameSpeciesCalculusEvent event) {
    super(event);
  }

  public SameSpeciesInteractionsRequestEvent(int geneId, int[] interactomes, int maxDegree, int workId, int resultId) {
    super(geneId, interactomes, maxDegree, workId, resultId);
  }
}