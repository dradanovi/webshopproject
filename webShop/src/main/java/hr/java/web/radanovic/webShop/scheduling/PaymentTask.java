package hr.java.web.radanovic.webShop.scheduling;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import hr.java.web.radanovic.webShop.model.Expense;
import hr.java.web.radanovic.webShop.model.Product;
import hr.java.web.radanovic.webShop.service.SaleService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PaymentTask {

	@Autowired
	private SaleService saleService;

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	/**
	 * payment task that triggers every hour and sends all unpaid tasks to be paid
	 */
	// per hour
	@Scheduled(fixedRate = 1000 * 60 * 60)
	@Transactional
	private void proccessPayment() {
		log.info("Schedule trigger");
		List<Expense> unpaidList = saleService.getUnpaid();
		for (Expense expense : unpaidList) {
			log.info("expense id:" + expense.getId());
			log.info("payer card id:" + expense.getCardUsed().getId());
			Map<Long, BigDecimal> mapSellers = new HashMap<>();
			for (Product product : expense.getProducts()) {
				if (mapSellers.containsKey(product.getSeller().getId())) {
					mapSellers.put(product.getSeller().getId(),
							mapSellers.get(product.getSeller().getId()).add(product.getCost()));
				} else {
					mapSellers.put(product.getSeller().getId(), product.getCost());
				}
			}
			for (Long id : mapSellers.keySet()) {
				log.info("Seller id:" + id + ". Profit:" + mapSellers.get(id));
			}
			expense.setDatePayed(LocalDateTime.now());
			log.info(expense.toString());
		}
		saleService.setPaidDates(unpaidList);
		log.info("local date time" + LocalDateTime.now().format(formatter));
	}

}
