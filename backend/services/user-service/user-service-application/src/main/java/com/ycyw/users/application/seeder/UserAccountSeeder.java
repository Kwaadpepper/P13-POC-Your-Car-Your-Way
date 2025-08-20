package com.ycyw.users.application.seeder;

import org.springframework.stereotype.Component;

import com.ycyw.users.domain.usecase.user.CreateUser;

import net.datafaker.Faker;

@Component
public class UserAccountSeeder implements Seeder {
  private static final int AMOUNT_TO_SEED = 10;

  private final Faker dataFaker;
  private final CreateUser.CreateUserHandler handler;

  public UserAccountSeeder(Faker dataFaker, CreateUser.CreateUserHandler handler) {
    this.dataFaker = dataFaker;
    this.handler = handler;
  }

  @Override
  public void seed() {
    Faker faker = dataFaker.getFaker();

    // Static dummy user account
    createUserAccount("user@example.net");

    int i = 0;
    while (i < AMOUNT_TO_SEED) {
      String emailStr = faker.internet().emailAddress();
      createUserAccount(emailStr);
      i++;
    }
  }

  private void createUserAccount(String email) {

    CreateUser.CreateUserInput useCase = new CreateUser.CreateUserInput(email);

    handler.execute(useCase);
  }
}
