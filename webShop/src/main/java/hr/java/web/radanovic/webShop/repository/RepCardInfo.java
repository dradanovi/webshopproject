package hr.java.web.radanovic.webShop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import hr.java.web.radanovic.webShop.model.CardInfo;
import hr.java.web.radanovic.webShop.model.Wallet;

public interface RepCardInfo extends CrudRepository<CardInfo, Long> {

	List<CardInfo> findByWallet(Wallet wallet);
	Optional<CardInfo> findByCardNumber(String cardNumber);
	
}
