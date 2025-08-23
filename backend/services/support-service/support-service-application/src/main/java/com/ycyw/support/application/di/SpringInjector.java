package com.ycyw.support.application.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ycyw.support.domain.port.directory.ClientDirectory;
import com.ycyw.support.domain.port.directory.ReservationDirectory;
import com.ycyw.support.domain.port.repository.CompanyInformationRepository;
import com.ycyw.support.domain.port.repository.ConversationRepository;
import com.ycyw.support.domain.port.repository.FaqRepository;
import com.ycyw.support.domain.port.repository.IssueRepository;
import com.ycyw.support.domain.port.service.SessionVerifyer;
import com.ycyw.support.domain.usecase.company.GetCompanyInfo;
import com.ycyw.support.domain.usecase.conversation.CreateConversation;
import com.ycyw.support.domain.usecase.conversation.GetAllConversation;
import com.ycyw.support.domain.usecase.faq.CreateFaq;
import com.ycyw.support.domain.usecase.faq.GetAllFaq;
import com.ycyw.support.domain.usecase.issue.CreateIssue;
import com.ycyw.support.domain.usecase.issue.GetAllIssue;
import com.ycyw.support.domain.usecase.session.VerifySession;

import net.datafaker.Faker;

@Configuration
public class SpringInjector {
  private final ClientDirectory clientDirectory;
  private final ReservationDirectory reservationDirectory;

  private final CompanyInformationRepository companyInfoRepository;
  private final FaqRepository faqRepository;
  private final IssueRepository issueRepository;
  private final ConversationRepository conversationRepository;

  private final SessionVerifyer sessionVerifyer;

  public SpringInjector(
      ClientDirectory clientDirectory,
      ReservationDirectory reservationDirectory,
      CompanyInformationRepository companyInfoRepository,
      FaqRepository faqRepository,
      IssueRepository issueRepository,
      ConversationRepository conversationRepository,
      SessionVerifyer sessionVerifyer) {
    this.clientDirectory = clientDirectory;
    this.reservationDirectory = reservationDirectory;
    this.companyInfoRepository = companyInfoRepository;
    this.faqRepository = faqRepository;
    this.issueRepository = issueRepository;
    this.conversationRepository = conversationRepository;
    this.sessionVerifyer = sessionVerifyer;
  }

  // * USECASES
  @Bean
  GetCompanyInfo.Handler getCompanyInfoHandler() {
    return new GetCompanyInfo.Handler(companyInfoRepository);
  }

  @Bean
  CreateFaq.Handler createFaqHandler() {
    return new CreateFaq.Handler(faqRepository);
  }

  @Bean
  GetAllFaq.Handler getAllFaqHandler() {
    return new GetAllFaq.Handler(faqRepository);
  }

  @Bean
  CreateConversation.Handler createConversationHandler() {
    return new CreateConversation.Handler(issueRepository, conversationRepository);
  }

  @Bean
  GetAllConversation.Handler getAllConversationHandler() {
    return new GetAllConversation.Handler(conversationRepository);
  }

  @Bean
  CreateIssue.Handler createIssueHandler() {
    return new CreateIssue.Handler(issueRepository);
  }

  @Bean
  GetAllIssue.Handler getAllIssueHandler() {
    return new GetAllIssue.Handler(issueRepository, clientDirectory, reservationDirectory);
  }

  @Bean
  VerifySession.Handler verifySessionHandler() {
    return new VerifySession.Handler(sessionVerifyer);
  }

  // * OTHER DOMAIN SERVICES

  // * MISCELLANEOUS
  @Bean
  Faker dataFaker() {
    return new Faker();
  }
}
