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



package org.sing_group.evoppi.service.security;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import org.sing_group.evoppi.service.security.check.SecurityCheck;
import org.sing_group.evoppi.service.security.check.SecurityCheckResult;

public class AuthorizedExecutor {
  private SecurityCheck[] checks;
  private Function<String, RuntimeException> throwableBuilder;
  private boolean shouldPassAllTheChecks;
  
  public AuthorizedExecutor(boolean shouldPassAllTheChecks, SecurityCheck ... checks) {
    this.shouldPassAllTheChecks = shouldPassAllTheChecks;
    this.checks = checks;
    
    this.throwableBuilder = cause -> new SecurityException("Illegal access. Cause: " + cause);
  }
  
  public AuthorizedExecutor throwing(Function<String, RuntimeException> throwableBuilder) {
    this.throwableBuilder = throwableBuilder;
    
    return this;
  }
  
  private boolean checkValidity(SecurityCheckResult...results) {
    return this.shouldPassAllTheChecks
      ? stream(results).allMatch(SecurityCheckResult::isValid)
      : stream(results).anyMatch(SecurityCheckResult::isValid);
  }
  
  public AuthorizedExecutor throwing(RuntimeException throwableBuilder) {
    return this.throwing(cause -> throwableBuilder);
  }

  private boolean check() {
    final SecurityCheckResult[] results = stream(this.checks)
      .map(SecurityCheck::check)
    .toArray(SecurityCheckResult[]::new);
    
    if (checkValidity(results)) {
      return true;
    } else {
      final String cause = stream(results)
        .filter(result -> !result.isValid())
        .map(SecurityCheckResult::getReason)
        .map(Optional::get)
      .collect(joining(" and "));
      
      throw this.throwableBuilder.apply(cause);
    }
  }

  public void run(Runnable action) {
    this.check();
    
    action.run();
  }

  public <T> T call(Supplier<T> action) {
    this.check();
    
    return action.get();
  }
}
