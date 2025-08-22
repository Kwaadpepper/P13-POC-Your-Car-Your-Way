package com.ycyw.support.application.presenter;

import java.time.DayOfWeek;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.ycyw.shared.application.Presenter;
import com.ycyw.shared.ddd.objectvalues.TimeRange;
import com.ycyw.support.application.dto.CompanyInfoViewDto;
import com.ycyw.support.application.exception.exceptions.ResourceNotFoundException;
import com.ycyw.support.domain.model.valueobject.Address;
import com.ycyw.support.domain.model.valueobject.BusinessHours;
import com.ycyw.support.domain.model.valueobject.PhoneNumber;
import com.ycyw.support.domain.usecase.company.GetCompanyInfo;

import org.eclipse.jdt.annotation.Nullable;

@Component
public class CompanyInfoPresenter implements Presenter<CompanyInfoViewDto, GetCompanyInfo.Output> {

  @Override
  public CompanyInfoViewDto present(GetCompanyInfo.Output model) {
    if (model instanceof GetCompanyInfo.Output.CompanyInfoDto output) {
      return toDto(output);
    }
    throw new ResourceNotFoundException("Client not found");
  }

  private CompanyInfoViewDto toDto(GetCompanyInfo.Output.CompanyInfoDto model) {
    return new CompanyInfoViewDto(
        toAddressDto(model.address()),
        toBusinessDto(model.supportPhone(), model.phoneSupportBusinessHours()),
        toBusinessDto(null, model.chatSupportBusinessHours()),
        model.supportEmail().value(),
        model.website().value());
  }

  private CompanyInfoViewDto.AddressViewDto toAddressDto(Address address) {
    return new CompanyInfoViewDto.AddressViewDto(
        address.line1(),
        address.line2(),
        address.line3(),
        address.city(),
        address.zipCode(),
        address.country().getIsoCode());
  }

  private CompanyInfoViewDto.BusinessDto toBusinessDto(
      @Nullable PhoneNumber phone, BusinessHours businessHours) {
    Map<DayOfWeek, CompanyInfoViewDto.TimeRangeDto> hours = new LinkedHashMap<>();
    for (Entry<DayOfWeek, TimeRange> e : businessHours.hours().entrySet()) {
      hours.put(e.getKey(), toTimeRangeDto(e.getValue()));
    }
    return new CompanyInfoViewDto.BusinessDto(phone != null ? phone.value() : null, hours);
  }

  private CompanyInfoViewDto.TimeRangeDto toTimeRangeDto(TimeRange timeRange) {
    return new CompanyInfoViewDto.TimeRangeDto(
        timeRange.start().toString(), timeRange.end().toString());
  }
}
