package com.ycyw.support.domain.model.entity.externals.agency;

import com.ycyw.shared.ddd.lib.Entity;
import com.ycyw.shared.ddd.objectvalues.Address;
import com.ycyw.shared.ddd.objectvalues.Coordinates;
import com.ycyw.shared.ddd.objectvalues.Email;
import com.ycyw.shared.ddd.objectvalues.PhoneNumber;

public record Agency(
    String label, PhoneNumber phone, Email email, Address address, Coordinates coordinates)
    implements Entity {}
