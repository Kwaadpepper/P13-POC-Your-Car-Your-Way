package com.ycyw.support.infrastructure.adapter.client.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ycyw.support.infrastructure.adapter.client.client.ClientDirectoryFeignAdapter.ClientDto;

@FeignClient(name = "user-service-application", path = "/clients")
public interface ClientServiceFeignClient {

  @GetMapping("{user}")
  ClientDto getClientByUuidOrEmail(@PathVariable("user") String user);
}
