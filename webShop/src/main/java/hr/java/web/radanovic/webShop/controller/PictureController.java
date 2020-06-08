package hr.java.web.radanovic.webShop.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hr.java.web.radanovic.webShop.model.Product;
import hr.java.web.radanovic.webShop.service.SaleService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class PictureController {

	@Autowired
	private SaleService saleservice;

	/**
	 * returns a picture that is located on the server in the form of a url
	 * 
	 * @param productId
	 * @param picId
	 * @return
	 */
	@ResponseBody
	@GetMapping("/product/file/{product}-{pic}")
	public ResponseEntity<Resource> picUrl(@PathVariable("product") String productId,
			@PathVariable("pic") String picId) {
		log.info("responsebody get picture");
		log.info("src\\main\\resources\\productPics\\" + productId + "-" + picId);
		log.info("product " + productId);
		log.info("pic " + picId);
		File file = new File("src\\main\\resources\\static\\img\\" + productId + "-" + picId + ".jpg");
		log.info(file.getAbsolutePath());
		return ResponseEntity.ok().contentLength(file.length()).contentType(MediaType.parseMediaType("image/jpg"))
				.body(new FileSystemResource(file));
	}
	
	@ResponseBody
	@GetMapping("/logo/file/{name}")
	public ResponseEntity<Resource> picUrl(@PathVariable("name") String productId) {
		File file = new File("src\\main\\resources\\static\\img\\" + productId + ".png");
		log.info(file.getAbsolutePath());
		return ResponseEntity.ok().contentLength(file.length()).contentType(MediaType.parseMediaType("image/png"))
				.body(new FileSystemResource(file));
	}

	/**
	 * takes the attached file checks if its a image and if it is a image saves it
	 * on the server as a .jpg with the name format as prodictId-pictureNumber.jpg
	 * 
	 * @param file
	 * @param productId
	 * @param session
	 * @param redirectAttributes
	 * @return
	 */
	@PostMapping("/picture/product/upload/{id}")
	public String handlePictureUpload(@RequestParam("file") MultipartFile file, @PathVariable("id") Long productId,
			HttpSession session, RedirectAttributes redirectAttributes) {
		String path = "src\\main\\resources\\productPics\\";
		// list of all product picture names
		List<String> picList = Arrays.asList(new File(path).listFiles()).stream()
				.filter(e -> e.getName().startsWith(productId + "-")).map(e -> e.getName())
				.collect(Collectors.toList());
		log.info(file.getContentType());
		// get the file type and suffix
		List<String> pictureType = Arrays.asList(file.getContentType().split("/"));
		log.info(pictureType.get(pictureType.size() - 1));
		int i = 1;
		File outputFilepath = null;
		while (true) {
			outputFilepath = new File(path + productId + "-" + i + ".JPG");
			if (outputFilepath.exists()) {
				// if file with the presented filename exists iterate the pictureNumber
				i++;
			} else if (i > 5 || !pictureType.get(0).equals("image")) {
				// if the pictureNumber exceeds the hard limit of 5 or the file is not a image
				// redirect the page back to product add picture page
				redirectAttributes.addAttribute("id", productId);
				return "redirect:/picture/product/add";
			} else
				break;
		}
		InputStream in = null;
		OutputStream out = null;
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(ImageIO.read(file.getInputStream()), pictureType.get(pictureType.size() - 1), os);
			in = new ByteArrayInputStream(os.toByteArray());
			log.info("pic inputstream -> " + in.available());
			out = new FileOutputStream(outputFilepath);
			FileCopyUtils.copy(in, out);
			picList.add(productId + "-" + i + ".JPG");
		} catch (FileNotFoundException e) {
			log.info("Exception -> pic file not found");
			e.printStackTrace();
		} catch (IOException e) {
			log.info("Exception -> pic ioexception");
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
					out.close();
				} catch (IOException e) {
					log.info("Exception IOException picture stream close");
					e.printStackTrace();
				}
			}
		}
		redirectAttributes.addAttribute("productId", productId);
		return "redirect:/picture/product/add";
//		return "redirect:/sellerDashboard/addListing";
	}

	/**
	 * returns the page responsible for uploading the pictures that are associated
	 * with the attached product
	 * 
	 * @param id
	 * @param model
	 * @param redir
	 * @param session
	 * @return
	 */
	@GetMapping("/picture/product/add")
	public String addPictures(@RequestParam("productId") Long id, Model model, RedirectAttributes redir,
			HttpSession session) {
		log.info("product id " + id);
		if (session.getAttribute("user") == null) {
			return "redirect:/";
		}
		Product product = saleservice.getProduct(id);
		if (!product.isEnabled()) {
			model.addAttribute("product", product);
		} else {
			redir.addAttribute("id", id);
			return "retirect:/product/p";
		}
		model.addAttribute("pics",
				Arrays.asList(new File("src\\main\\resources\\productPics\\").listFiles()).stream()
						.filter(e -> e.getName().startsWith(product.getId() + "-")).map(e -> e.getName())
						.collect(Collectors.toList()));
		return "unlistedProducts";
	}

}
