package hr.java.web.radanovic.webShop.scheduling;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import hr.java.web.radanovic.webShop.service.UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SellerRecalculating {

	@Autowired
	private UserService userService;

	@Scheduled(fixedRate = 1000 * 60 * 60 * 24)
	@Transactional
	private void sellerRecalculation() {
		userService.recalculateRatings();
		log.info("recalculate ratings");
	}
}
