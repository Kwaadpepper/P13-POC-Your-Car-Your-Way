package com.ycyw.users.application.seeder;

import java.util.Locale;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.ycyw.shared.ddd.lib.UseCaseExecutor;
import com.ycyw.shared.ddd.objectvalues.Email;
import com.ycyw.users.domain.model.valueobject.RawIdentifier;
import com.ycyw.users.domain.model.valueobject.RawPassword;
import com.ycyw.users.domain.model.valueobject.Role;
import com.ycyw.users.domain.usecase.operator.CreateOperator;

import net.datafaker.Faker;
import org.eclipse.jdt.annotation.Nullable;

@Component
public class OperatorAccountSeeder implements Seeder {
  private static final int AMOUNT_TO_SEED = 10;

  private final Faker dataFaker;
  private final UseCaseExecutor useCaseExecutor;
  private final CreateOperator.CreateOperatorHandler handler;

  public OperatorAccountSeeder(
      Faker dataFaker,
      UseCaseExecutor useCaseExecutor,
      CreateOperator.CreateOperatorHandler handler) {
    this.dataFaker = dataFaker;
    this.useCaseExecutor = useCaseExecutor;
    this.handler = handler;
  }

  @Override
  public void seed() {

    // Static dummy user account
    createUserAccount("user@example.net", "Password.123", "Jane Doe");

    int i = 0;
    while (i < AMOUNT_TO_SEED) {
      final var email = dataFaker.internet().emailAddress();
      final var password = "aA1." + dataFaker.internet().password(8, 16, true, true, true);
      createUserAccount(email, password, null);
      i++;
    }
  }

  private void createUserAccount(
      String providedEmail, String providedPassword, @Nullable String providedId) {

    var lastName = dataFaker.name().lastName();
    var firstName = dataFaker.name().firstName();
    var name = firstName + " " + lastName;
    var email = new Email(providedEmail);

    RawIdentifier identifier =
        new RawIdentifier(providedId != null ? providedId : generateId(firstName, lastName));
    final var password = new RawPassword(providedPassword);

    var useCase =
        new CreateOperator.CreateOperatorInput(
            name, email, Set.of(Role.SUPPORT), identifier, password);

    useCaseExecutor.execute(this.handler, useCase);
  }

  private String generateId(String firstName, String lastName) {
    // Première lettre du prénom en minuscule
    String firstInitial = firstName.substring(0, 1).toLowerCase(Locale.ROOT);

    // Nom de famille en minuscule, sans espaces ni caractères spéciaux
    String cleanedLastName = lastName.toLowerCase(Locale.getDefault()).replaceAll("[^a-z0-9]", "");

    // Concaténation et retour de l'identifiant
    return firstInitial + cleanedLastName;
  }
}
