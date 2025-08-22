package com.ycyw.support.application.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
  GetCompanyInfo.GetCompanyInfoHandler getCompanyInfoHandler(
      CompanyInformationRepository companyInfoRepository) {
    return new GetCompanyInfo.GetCompanyInfoHandler(companyInfoRepository);
  }

  @Bean
  CreateFaq.CreateFaqHandler createFaqHandler(FaqRepository faqRepository) {
    return new CreateFaq.CreateFaqHandler(faqRepository);
  }

  @Bean
  GetAllFaq.GetAllFaqHandler getAllFaqHandler(FaqRepository faqRepository) {
    return new GetAllFaq.GetAllFaqHandler(faqRepository);
  }

  @Bean
  CreateConversation.CreateConversationHandler createConversationHandler(
      IssueRepository issueRepository, ConversationRepository conversationRepository) {
    return new CreateConversation.CreateConversationHandler(
        issueRepository, conversationRepository);
  }

  @Bean
  GetAllConversation.GetAllConversationHandler getAllConversationHandler(
      ConversationRepository conversationRepository) {
    return new GetAllConversation.GetAllConversationHandler(conversationRepository);
  }

  @Bean
  CreateIssue.CreateIssueHandler createIssueHandler(IssueRepository issueRepository) {
    return new CreateIssue.CreateIssueHandler(issueRepository);
  }

  @Bean
  GetAllIssue.GetAllIssueHandler getAllIssueHandler(IssueRepository issueRepository) {
    return new GetAllIssue.GetAllIssueHandler(issueRepository);
  }

  // * OTHER DOMAIN SERVICES

  // * MISCELLANEOUS
  @Bean
  Faker dataFaker() {
    return new Faker();
  }
}
