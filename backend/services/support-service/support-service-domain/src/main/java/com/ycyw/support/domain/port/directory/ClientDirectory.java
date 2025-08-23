package com.ycyw.support.domain.port.directory;

import com.ycyw.support.domain.model.entity.externals.client.Client;
import com.ycyw.support.domain.model.entity.externals.client.ClientId;

import org.eclipse.jdt.annotation.Nullable;

public interface ClientDirectory {
  @Nullable Client findById(ClientId id);
}
