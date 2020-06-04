package hr.java.web.radanovic.webShop.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.java.web.radanovic.webShop.model.AppUser;
import hr.java.web.radanovic.webShop.model.CardInfo;
import hr.java.web.radanovic.webShop.model.Expense;
import hr.java.web.radanovic.webShop.model.Product;
import hr.java.web.radanovic.webShop.model.Review;
import hr.java.web.radanovic.webShop.model.Seller;
import hr.java.web.radanovic.webShop.repository.HibCardInfoRep;
import hr.java.web.radanovic.webShop.repository.HibExpenseRep;
import hr.java.web.radanovic.webShop.repository.HibProductRep;
import hr.java.web.radanovic.webShop.repository.HibReviewRep;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SaleService {

	@Autowired
	private UserService userService;

	@Autowired
	private HibCardInfoRep cardRepo;

	@Autowired
	private HibProductRep prodRepo;

	@Autowired
	private HibExpenseRep expenseRepo;

	@Autowired
	private HibReviewRep reviewRepo;

	/**
	 * find all cards associated with the current user that is logged in
	 * 
	 * @return
	 */
	public List<CardInfo> getCards() {
		return cardRepo.findByWallet(userService.getWalletByUser());
	}

	/**
	 * find a card associated with the received card number
	 * 
	 * @param number
	 * @return
	 */
	public CardInfo getCardByNumber(String number) {
		return cardRepo.findByCardNumber(number).get();
	}

	/**
	 * find a card associated with the received id
	 * 
	 * @param id
	 * @return
	 */
	public CardInfo getCardById(Long id) {
		return cardRepo.findById(id).get();
	}

	/**
	 * returns only the last 4 digits from all cards associated with the current
	 * logged in user
	 * 
	 * @return
	 */
	public List<String> getLast4() {
		return getCards().stream().map(e -> e.getLast4()).collect(Collectors.toList());
	}

	/**
	 * saves or updates a card
	 * 
	 * @param card
	 * @return
	 */
	public CardInfo setCard(CardInfo card) {
		return cardRepo.save(card);
	}

	/**
	 * deletes card by removing all unneeded information
	 * 
	 * @param id
	 */
	public void deleteCard(Long id) {
		CardInfo card = cardRepo.findById(id).get();
		card.setCardHolderFirstName(null);
		card.setCardHolderLastName(null);
		card.setCsc(null);
		card.setWallet(null);
		cardRepo.save(card);
	}

	/**
	 * find a product in database by received id
	 * 
	 * @param id
	 * @return
	 */
	public Product getProduct(Long id) {
		return prodRepo.findById(id).get();
	}

	/**
	 * returns all products that belong to the logged in user
	 * 
	 * @return
	 */
	public List<Product> getProductListBySeller() {
		return prodRepo.findAvailableBySeller(userService.getSeller());
	}

	/**
	 * returns all products that belong to selected seller
	 * 
	 * @return
	 */
	public List<Product> getProductListBySeller(Seller seller) {
		return prodRepo.findAvailableBySeller(seller);
	}

	/**
	 * returns all products that has availability greater than 0
	 * 
	 * @return
	 */
	public List<Product> getAvailableProductList() {
		return (List<Product>) prodRepo.findAllAvailable();
	}

	/**
	 * deletes a product by setting its availability to 0
	 * 
	 * @param id
	 */
	public void deleteProduct(Long id) {
		Product prod = prodRepo.findById(id).get();
		prod.setNumberAvailable(0);
		prodRepo.save(prod);
	}

	/**
	 * saves or updates a product to a database
	 * 
	 * @param product
	 * @return
	 */
	public Product setProduct(Product product) {
		return prodRepo.save(product);
	}

	/**
	 * updates the number of available products after committing to a payment
	 * 
	 * @param list
	 * @return
	 */
	public List<Product> updateProductAvailability(List<Product> list) {
		List<Product> dbList = new ArrayList<>();
		for (Product product : list) {
			dbList.add(prodRepo.findById(product.getId()).get());
		}
		for (Product product : dbList) {
			product.setNumberAvailable(product.getNumberAvailable() - 1);
		}
//		return (List<Product>)prodRepo.saveAll(dbList);
		return dbList;
	}

	/**
	 * finds a product with its associated id
	 * 
	 * @param id
	 * @return
	 */
	public Product addToCart(Long id) {
		return prodRepo.findById(id).get();
	}
	
	public List<Product> unfinished(){
		return prodRepo.findUnfinishedBySeller(userService.getSeller());
	}
	
	public List<Product> filterProducts(Long costFrom, Long costTo, String name){
		return prodRepo.findByFilter(costFrom, costTo, name);
	}

	/**
	 * return true if there are less products in the cart than products available
	 * and false if there are more or equal products in the cart than available
	 * products
	 * 
	 * @param cart
	 * @param product
	 * @return
	 */
	public boolean checkAvailability(List<Product> cart, Product product) {
		if (cart.stream().filter(e -> e.equals(product)).collect(Collectors.toList()).size() >= product
				.getNumberAvailable()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * returns list of numbers that representing page numbers needed for the amount
	 * of products that need to be displayed
	 * 
	 * @param page
	 * @param listSize
	 * @return
	 */
	public List<Integer> getPageNumbers(int page, int listSize) {
		if (listSize == 0) {
			List<Integer> list = new ArrayList<>();
			list.add(1);
			return list;
		} else if (page < 3) {
			return IntStream.rangeClosed(1, ((page + 2) >= listSize) ? listSize : (page + 2)).boxed()
					.collect(Collectors.toList());
		} else {
			return IntStream.rangeClosed((page - 2), ((page + 2) >= listSize) ? listSize : (page + 2)).boxed()
					.collect(Collectors.toList());
		}
	}

	/**
	 * saves or updates expense to a database
	 * 
	 * @param expense
	 * @return
	 */
	public Expense setExpense(Expense expense) {
		return expenseRepo.save(expense);
	}

	/**
	 * finds all expenses for the logged in user that are connected to his cards
	 * 
	 * @return
	 */
	public List<Expense> getExpenses() {
		List<Expense> expenseList = new ArrayList<>();
		for (CardInfo card : cardRepo.findByWallet(userService.getWalletByUser())) {
			expenseList.addAll(expenseRepo.findByCardUsed(card));
		}
		return expenseList;
	}

	/**
	 * finds all expenses for the associated card
	 * 
	 * @param card
	 * @return
	 */
	public List<Expense> getExpenses(CardInfo card) {
		return expenseRepo.findByCardUsed(card);
	}

	/**
	 * finds all expenses that don't have a date payed indicating that the payment
	 * of the expense is not finalized
	 * 
	 * @return
	 */
	public List<Expense> getUnpaid() {
		return expenseRepo.findUnpaid();
	}

	/**
	 * adds paid dates that finalizes all the unfinished payments and updates the
	 * database
	 * 
	 * @param unpaidExpenses
	 */
	public void setPaidDates(List<Expense> unpaidExpenses) {
		expenseRepo.saveAll(unpaidExpenses);
	}

	public Map<Expense, List<Review>> getExpenseReviewMap(Long id) {
		List<Expense> expenseList = new ArrayList<>();
		if (id != null) {
			expenseList.addAll(getExpenses(getCardById(id)));
		} else {
			expenseList.addAll(getExpenses());
		}
		Map<Expense, List<Review>> reviewMap = new HashMap<>();
		List<Review> reviewList = new ArrayList<>();
		List<Review> reviewByUser = getReviewsByUser(userService.getUser());
		for (Expense expense : expenseList) {
			for (Product product : expense.getProducts()) {
				reviewList.add(new Review());
				for (Review review : reviewByUser) {
					if (product.equals(review.getProduct())) {
						reviewList.set(reviewList.size() - 1, review);
						break;
					} else {
						reviewList.set(reviewList.size() - 1, new Review());
					}
				}
			}
			reviewMap.put(expense, new ArrayList<Review>(reviewList));
			reviewList.clear();
		}
		return reviewMap;
	}

	/**
	 * returns all sales that a seller has made
	 * 
	 * @return
	 */
	public List<Product> getAllSales() {
		Seller seller = userService.getSeller();
		if (seller.isEmpty()) {
			return null;
		}

		List<Product> prodList = prodRepo.findBySeller(seller);
		List<Expense> expenses = (List<Expense>) expenseRepo.findAll();
		List<Product> productSales = new ArrayList<>();
		for (Expense expense : expenses) {
			productSales.addAll(prodList.stream().filter(expense.getProducts()::contains).collect(Collectors.toList()));
		}
		return productSales;
	}

	/**
	 * saves of updates a review
	 * 
	 * @param review
	 */
	@Transactional
	public void setReview(Review review) {
		log.info("setReview");
		Seller seller;
		if (reviewRepo.userReviewExists(review.getUser(), review.getProduct())) {
			Review dbReview = reviewRepo.findById(review.getId()).get();
			seller = userService.getSeller(review.getProduct().getSeller().getUser().getUsername());
			seller.setRating(
					((seller.getRating() * seller.getReviews()) - dbReview.getGrade()) / (seller.getReviews() - 1));
			seller.setReviews(seller.getReviews() - 1);
			userService.setSeller(seller);
			log.info("2 seller rating: " + seller.getRating() + " reviews: " + seller.getReviews());
			dbReview.setGrade(review.getGrade());
			dbReview.setMessage(review.getMessage());
			review = reviewRepo.save(dbReview);
		} else {
			reviewRepo.save(review);
			seller = userService.getSeller(review.getProduct().getSeller().getUser().getUsername());
			log.info("3 seller rating: " + seller.getRating() + " reviews: " + seller.getReviews());
		}
		seller.setRating(((seller.getRating() * seller.getReviews()) + review.getGrade()) / (seller.getReviews() + 1));
		seller.setReviews(seller.getReviews() + 1);
		log.info("4 seller rating: " + seller.getRating() + " reviews: " + seller.getReviews());
		userService.setSeller(seller);
	}

	/**
	 * finds all reviews made by a specific user
	 * 
	 * @param user
	 * @return
	 */
	public List<Review> getReviewsByUser(AppUser user) {
		return reviewRepo.findByUser(user);
	}

	/**
	 * finds all reviews made for a specific product
	 * 
	 * @param product
	 * @return
	 */
	public List<Review> getReviewsByProduct(Product product) {
		return reviewRepo.findByProduct(product);
	}

	/**
	 * finds a review that corresponds to a id
	 * 
	 * @param id
	 * @return
	 */
	public Review getReview(Long id) {
		Optional<Review> optional = reviewRepo.findById(id);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	public List<Review> getAllSellerReviews(String username){
		return reviewRepo.findAllByListProducts(getProductListBySeller(userService.getSeller(username)));
	}

}
