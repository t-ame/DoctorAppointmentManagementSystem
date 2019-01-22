package com.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.java.dto.Address;
import com.java.service.AddressService;

@BasePathAwareController
public class AddressController {

	@Autowired
	AddressService addService;

	@GetMapping(path = "addresses/{id}")
	public ResponseEntity<?> findAddress(@PathVariable("id") int id) {

		Address address = addService.findAddressById(id);
		if (address == null) {
			return ResponseEntity.notFound().build();
		}
		Link selfLink = ControllerLinkBuilder
				.linkTo(ControllerLinkBuilder.methodOn(AddressController.class).findAddress(id)).withSelfRel();
		address.add(selfLink);

		return ResponseEntity.ok(address);
	}

}
