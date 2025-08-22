package com.ycyw.support.infrastructure.adapter.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ycyw.support.infrastructure.adapter.client.ClientDirectoryFeignAdapter.ClientDto;

/*
  name = "user-service" => discovery Eureka (spring.application.name du user-service)
  path = "/api/users"   => à ajuster selon tes contrôleurs
*/
@FeignClient(name = "user-service-application", path = "/clients")
public interface ClientServiceFeignClient {

  @GetMapping("{user}")
  ClientDto getClientByUuidOrEmail(@PathVariable("user") String user);
}
