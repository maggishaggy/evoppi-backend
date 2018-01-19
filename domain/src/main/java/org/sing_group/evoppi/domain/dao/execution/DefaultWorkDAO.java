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
package org.sing_group.evoppi.domain.dao.execution;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.evoppi.domain.dao.DAOHelper;
import org.sing_group.evoppi.domain.dao.spi.execution.WorkDAO;
import org.sing_group.evoppi.domain.entities.execution.Work;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultWorkDAO implements WorkDAO {

  @PersistenceContext
  protected EntityManager em;
  protected DAOHelper<Integer, Work> dh;

  public DefaultWorkDAO() {
    super();
  }

  public DefaultWorkDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DAOHelper.of(Integer.class, Work.class, this.em);
  }

  @Override
  public Work get(int workId) {
    return this.dh.get(workId)
      .orElseThrow(() -> new IllegalArgumentException("Unknown work id: " + workId));
  }

  @Override
  public Work createNew(String name, String description, String resultReference) {
    return this.dh.persist(new Work(name, description, resultReference));
  }
}