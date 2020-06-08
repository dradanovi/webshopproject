package hr.java.web.radanovic.webShop.scheduling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import hr.java.web.radanovic.webShop.model.Review;
import hr.java.web.radanovic.webShop.model.Seller;
import hr.java.web.radanovic.webShop.repository.HibReviewRep;
import hr.java.web.radanovic.webShop.service.UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SellerRecalculating {

	@Autowired
	private HibReviewRep reviewRepo;

	@Autowired
	private UserService userService;

	@Scheduled(fixedRate = 1000 * 60 * 60 * 24)
	@Transactional
	private void sellerRecalculation() {
		List<Review> reviewList = new ArrayList<>();
		reviewRepo.findAll().forEach(reviewList::add);
		Map<Seller, List<Review>> map = new HashMap<>();

		reviewList.forEach(e -> {
			if (map.containsKey(e.getProduct().getSeller())) {
				map.get(e.getProduct().getSeller()).add(e);
			} else {
				map.put(e.getProduct().getSeller(), new ArrayList<Review>(Arrays.asList(e)));
			}
		});

		for (Seller seller : map.keySet()) {
			Seller sellerDB = userService.getSeller(seller.getUser().getUsername());
			Double number = map.get(seller).stream().mapToLong(e -> e.getGrade()).sum() / (double) map.get(seller).size();
			if(!sellerDB.getRating().equals(number)) {
				log.info("seller/(old/new) " + sellerDB.getId() + "/(" + sellerDB.getRating() + "/" + number + ")");
				sellerDB.setRating(number);
				userService.updateSeller(sellerDB);
			}
			log.info("seller/(old/new) " + sellerDB.getId() + "/(" + sellerDB.getRating() + "/" + number + ")");
		}
	}
}
