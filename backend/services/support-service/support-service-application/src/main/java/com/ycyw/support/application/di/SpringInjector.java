package com.ycyw.support.application.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ycyw.support.domain.port.directory.ClientDirectory;
import com.ycyw.support.domain.port.directory.ReservationDirectory;
import com.ycyw.support.domain.port.repository.CompanyInformationRepository;
import com.ycyw.support.domain.port.repository.ConversationRepository;
import com.ycyw.support.domain.port.repository.FaqRepository;
import com.ycyw.support.domain.port.repository.IssueRepository;
import com.ycyw.support.domain.usecase.company.GetCompanyInfo;
import com.ycyw.support.domain.usecase.conversation.CreateConversation;
import com.ycyw.support.domain.usecase.conversation.GetAllConversation;
import com.ycyw.support.domain.usecase.faq.CreateFaq;
import com.ycyw.support.domain.usecase.faq.GetAllFaq;
import com.ycyw.support.domain.usecase.issue.CreateIssue;
import com.ycyw.support.domain.usecase.issue.GetAllIssue;

import net.datafaker.Faker;

@Configuration
public class SpringInjector {

  // * USECASES
  @Bean
  GetCompanyInfo.Handler getCompanyInfoHandler(CompanyInformationRepository companyInfoRepository) {
    return new GetCompanyInfo.Handler(companyInfoRepository);
  }

  @Bean
  CreateFaq.Handler createFaqHandler(FaqRepository faqRepository) {
    return new CreateFaq.Handler(faqRepository);
  }

  @Bean
  GetAllFaq.Handler getAllFaqHandler(FaqRepository faqRepository) {
    return new GetAllFaq.Handler(faqRepository);
  }

  @Bean
  CreateConversation.Handler createConversationHandler(
      IssueRepository issueRepository, ConversationRepository conversationRepository) {
    return new CreateConversation.Handler(issueRepository, conversationRepository);
  }

  @Bean
  GetAllConversation.Handler getAllConversationHandler(
      ConversationRepository conversationRepository) {
    return new GetAllConversation.Handler(conversationRepository);
  }

  @Bean
  CreateIssue.Handler createIssueHandler(IssueRepository issueRepository) {
    return new CreateIssue.Handler(issueRepository);
  }

  @Bean
  GetAllIssue.Handler getAllIssueHandler(
      ClientDirectory clientDirectory,
      ReservationDirectory reservationDirectory,
      IssueRepository issueRepository,
      ConversationRepository conversationRepository) {
    return new GetAllIssue.Handler(
        issueRepository, clientDirectory, reservationDirectory, conversationRepository);
      IssueRepository issueRepository) {
    return new GetAllIssue.Handler(issueRepository, clientDirectory, reservationDirectory);
  }

  // * OTHER DOMAIN SERVICES

  // * MISCELLANEOUS
  @Bean
  Faker dataFaker() {
    return new Faker();
  }
}
