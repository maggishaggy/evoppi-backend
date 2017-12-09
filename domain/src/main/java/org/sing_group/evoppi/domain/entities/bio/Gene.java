/*-
 * #%L
 * Domain
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
package org.sing_group.evoppi.domain.entities.bio;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "gene")
public class Gene implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Id
  private int id;
  
  @Lob
  @Column(name = "sequence", nullable = false)
  private String sequence;
  
  @OneToMany(mappedBy = "geneFrom", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Interaction> interactsWith;
  
  @OneToMany(mappedBy = "geneTo", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Interaction> interactedBy;
  
  public int getId() {
    return id;
  }
  
  public String getSequence() {
    return sequence;
  }

  public Stream<Interaction> getInteractsWith() {
    return this.interactsWith.stream();
  }

  public boolean hasInteractionWith(Interaction interaction) {
    return this.interactsWith.contains(interaction);
  }

  public Stream<Interaction> getInteractedBy() {
    return this.interactedBy.stream();
  }

  public boolean hasInteractedBy(Interaction interaction) {
    return this.interactedBy.contains(interaction);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Gene other = (Gene) obj;
    if (id != other.id)
      return false;
    return true;
  }
}