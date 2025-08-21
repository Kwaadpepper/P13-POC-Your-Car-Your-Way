package com.ycyw.users.application.seeder;

import java.util.Locale;

import org.springframework.stereotype.Component;

import com.ycyw.shared.ddd.lib.UseCaseExecutor;
import com.ycyw.users.domain.model.valueobject.Address;
import com.ycyw.users.domain.model.valueobject.BirthDate;
import com.ycyw.users.domain.model.valueobject.Country;
import com.ycyw.users.domain.model.valueobject.Email;
import com.ycyw.users.domain.model.valueobject.RawIdentifier;
import com.ycyw.users.domain.model.valueobject.RawPassword;
import com.ycyw.users.domain.usecase.client.CreateClient;

import net.datafaker.Faker;
import org.eclipse.jdt.annotation.Nullable;

@Component
public class ClientAccountSeeder implements Seeder {
  private static final int AMOUNT_TO_SEED = 10;

  private final Faker dataFaker;
  private final UseCaseExecutor useCaseExecutor;
  private final CreateClient.CreateClientHandler handler;

  public ClientAccountSeeder(
      Faker dataFaker, UseCaseExecutor useCaseExecutor, CreateClient.CreateClientHandler handler) {
    this.dataFaker = dataFaker;
    this.useCaseExecutor = useCaseExecutor;
    this.handler = handler;
  }

  @Override
  public void seed() {

    // Static dummy user account
    createUserAccount("user@example.net", "Password.123", "John Doe");

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
    var email = new Email(providedEmail);
    String phone = dataFaker.phoneNumber().phoneNumberInternational();
    BirthDate birthDate = new BirthDate(dataFaker.date().birthdayLocalDate());
    Address address =
        new Address(
            dataFaker.address().streetAddress(),
            null,
            null,
            dataFaker.address().city(),
            dataFaker.address().zipCode(),
            Country.FRANCE);
    RawIdentifier identifier =
        new RawIdentifier(providedId != null ? providedId : generateId(firstName, lastName));
    final var password = new RawPassword(providedPassword);

    CreateClient.CreateClientInput useCase =
        new CreateClient.CreateClientInput(
            lastName, firstName, email, phone, birthDate, address, identifier, password);

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
