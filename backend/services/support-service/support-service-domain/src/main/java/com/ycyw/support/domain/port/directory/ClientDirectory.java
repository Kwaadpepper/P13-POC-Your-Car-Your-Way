package com.ycyw.support.domain.port.directory;

import java.util.UUID;

import com.ycyw.support.domain.model.entity.externals.client.Client;

import org.eclipse.jdt.annotation.Nullable;

public interface ClientDirectory {
  @Nullable Client findById(UUID id);
}
