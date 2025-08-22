package com.ycyw.support.domain.model.entity.externals.client;

import com.ycyw.shared.ddd.lib.Entity;
import com.ycyw.shared.ddd.objectvalues.Address;
import com.ycyw.shared.ddd.objectvalues.BirthDate;
import com.ycyw.shared.ddd.objectvalues.Email;
import com.ycyw.shared.ddd.objectvalues.PhoneNumber;

public record Client(
    ClientId id,
    String firstName,
    String lastName,
    Email email,
    PhoneNumber phone,
    BirthDate birthdate,
    Address address)
    implements Entity {}
