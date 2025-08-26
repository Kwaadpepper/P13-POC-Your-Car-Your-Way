package com.ycyw.support.domain.port.directory;

import com.ycyw.shared.ddd.lib.Directory;
import com.ycyw.support.domain.model.entity.externals.client.Client;
import com.ycyw.support.domain.model.entity.externals.client.ClientId;

public interface ClientDirectory extends Directory<Client, ClientId> {}
