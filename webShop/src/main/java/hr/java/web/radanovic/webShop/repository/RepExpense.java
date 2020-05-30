package hr.java.web.radanovic.webShop.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import hr.java.web.radanovic.webShop.model.CardInfo;
import hr.java.web.radanovic.webShop.model.Expense;

public interface RepExpense extends CrudRepository<Expense, Long> {

	List<Expense> findByCardUsed(CardInfo cardUsed);
	
}
