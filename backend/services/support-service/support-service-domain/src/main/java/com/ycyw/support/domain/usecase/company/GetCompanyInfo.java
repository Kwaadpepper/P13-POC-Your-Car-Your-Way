package com.ycyw.support.domain.usecase.company;

import java.util.UUID;

import com.ycyw.shared.ddd.lib.UseCaseHandler;
import com.ycyw.shared.ddd.lib.UseCaseInput;
import com.ycyw.shared.ddd.lib.UseCaseOutput;
import com.ycyw.shared.ddd.objectvalues.Address;
import com.ycyw.shared.ddd.objectvalues.Email;
import com.ycyw.shared.ddd.objectvalues.HttpUrl;
import com.ycyw.shared.ddd.objectvalues.PhoneNumber;
import com.ycyw.support.domain.model.entity.company.CompanyInformation;
import com.ycyw.support.domain.model.valueobject.BusinessHours;
import com.ycyw.support.domain.port.repository.CompanyInformationRepository;

public sealed interface GetCompanyInfo {
  sealed interface Input extends UseCaseInput, GetCompanyInfo {
    record Get() implements Input {}
  }

  sealed interface Output extends UseCaseOutput, GetCompanyInfo {
    // DTO directement en objets valeur du domaine pour éviter la duplication de mapping
    record CompanyInfoDto(
        UUID id,
        Address address,
        PhoneNumber supportPhone,
        BusinessHours phoneSupportBusinessHours,
        BusinessHours chatSupportBusinessHours,
        Email supportEmail,
        HttpUrl website)
        implements Output {}
  }

  final class Handler implements UseCaseHandler<Input, Output>, GetCompanyInfo {
    private final CompanyInformationRepository companyInfoRepository;

    public Handler(CompanyInformationRepository companyInfoRepository) {
      this.companyInfoRepository = companyInfoRepository;
    }

    @Override
    public Output handle(Input unused) {
      final CompanyInformation info = companyInfoRepository.getInfo();
      return mapToDto(info);
    }

    private Output.CompanyInfoDto mapToDto(CompanyInformation entity) {
      return new Output.CompanyInfoDto(
          entity.getId(),
          entity.getCompanyAddress(),
          entity.getPhoneSupport(),
          entity.getPhoneSupportBusinessHours(),
          entity.getChatSupportBusinessHours(),
          entity.getEmailSupport(),
          entity.getWebsite());
    }
  }
}
