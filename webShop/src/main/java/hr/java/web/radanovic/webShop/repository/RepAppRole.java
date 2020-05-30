package hr.java.web.radanovic.webShop.repository;

import hr.java.web.radanovic.webShop.enums.Authorities;
import hr.java.web.radanovic.webShop.model.AppRole;

public interface RepAppRole {

	AppRole findByAuthority(Authorities authority);
}
