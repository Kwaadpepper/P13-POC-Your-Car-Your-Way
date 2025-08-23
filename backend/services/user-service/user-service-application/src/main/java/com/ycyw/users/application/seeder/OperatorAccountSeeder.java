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

@Component
public class OperatorAccountSeeder implements Seeder {
  private static final int AMOUNT_TO_SEED = 10;

  private final Faker dataFaker;
  private final UseCaseExecutor useCaseExecutor;
  private final CreateOperator.Handler handler;

  public OperatorAccountSeeder(
      Faker dataFaker, UseCaseExecutor useCaseExecutor, CreateOperator.Handler handler) {
    this.dataFaker = dataFaker;
    this.useCaseExecutor = useCaseExecutor;
    this.handler = handler;
  }

  @Override
  public void seed() {
    int i = 0;
    while (i < AMOUNT_TO_SEED) {
      createUserAccount();
      i++;
    }
  }

  private void createUserAccount() {

    var lastName = dataFaker.name().lastName();
    var firstName = dataFaker.name().firstName();
    var name = firstName + " " + lastName;
    var email = new Email(dataFaker.internet().emailAddress());

    RawIdentifier identifier = new RawIdentifier(generateId(firstName, lastName));
    final var password =
        new RawPassword("aA1." + dataFaker.internet().password(8, 16, true, true, true));

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
