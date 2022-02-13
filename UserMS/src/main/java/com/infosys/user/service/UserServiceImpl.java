package com.infosys.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.infosys.user.dto.BuyerDTO;
import com.infosys.user.dto.CartDTO;
import com.infosys.user.dto.LoginDTO;
import com.infosys.user.dto.SellerDTO;
import com.infosys.user.dto.WishlistDTO;
import com.infosys.user.exception.UserException;
import com.infosys.user.entity.*;

import com.infosys.user.repository.*;

@Service(value = "userService")
@Transactional
public class UserServiceImpl implements UserService {
	
	@Autowired
	private BuyerRepository buyerRespository;
	@Autowired
	private SellerRepository sellerRepository;
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private WishlistRepository wishlistRepository;
	
	//This method has implemented the validation of buyer.
	@Override
	public Boolean validateBuyer(String buyerId) throws UserException{
		Optional<Buyer> b = buyerRespository.findById(buyerId);
		if(b.isEmpty()) {
			return true;
		}
		else {
			return false;
		}
	}
	//This method has implemented the validation of seller.
	@Override
	public Boolean validateSeller(String sellerId) throws UserException{
		Optional<Seller> s = sellerRepository.findById(sellerId);
		if(s.isEmpty()) {
			return true;
		}
		else {
			return false;
		}
	}
	//This method has implemented validation for privileged buyer.
	@Override
	public Boolean validatePrivilegedBuyer(String buyerId) throws UserException{
		Optional<Buyer> b = buyerRespository.findById(buyerId);
		if(b.isEmpty()) {
			return false;
		}
		else {
			if(b.get().getIsPrivileged()) {
				return true;
			}
			else {
				return false;
			}
		}
	}
	//This method has implemented addition of buyer in the database.
	@Override
	public String addBuyer(BuyerDTO buyerDTO) throws UserException {
		if(validateBuyer(buyerDTO.getBuyerId())) {
			if(buyerRespository.findAllPhone(buyerDTO.getPhoneNo()).isEmpty()) {
				Buyer buyer = new Buyer();
				buyer.setBuyerId(buyerDTO.getBuyerId());
				buyer.setName(buyerDTO.getName());
				buyer.setEmail(buyerDTO.getEmail());
				buyer.setPhoneNo(buyerDTO.getPhoneNo());
				buyer.setPassword(buyerDTO.getPassword());
				buyer.setIsActive(true);
				buyer.setRewardPoints(0);
				buyer.setIsPrivileged(false);
				Buyer buyerSuccess = buyerRespository.save(buyer);
				return buyerSuccess.getBuyerId();
			}
			return "Phone Number Is Already In Use!";
		}
		return "Buyer Already Exist";
	}
	//This method has implemented to get all the buyer from the database.
	@Override
	public List<BuyerDTO> getAllBuyer() throws UserException {
		Iterable<Buyer> buyers = buyerRespository.findAll();
		List<BuyerDTO> buyerDTOs = new ArrayList<>();
		buyers.forEach(buyer -> {
			BuyerDTO b = new BuyerDTO();
			b.setBuyerId(buyer.getBuyerId());
			b.setEmail(buyer.getEmail());
			b.setName(buyer.getName());
			b.setPassword(buyer.getPassword());
			b.setPhoneNo(buyer.getPhoneNo());
			b.setIsPrivileged(buyer.getIsPrivileged());
			b.setRewardPoints(buyer.getRewardPoints());
			b.setIsActive(buyer.getIsActive());
			buyerDTOs.add(b);
		});
		if(buyerDTOs.isEmpty()) {
			throw new UserException("Service.BUYERS_NOT_FOUND");
		}
		return buyerDTOs;
	}
	//This method has implemented authentication of buyer.
	public String buyerLogin(LoginDTO loginDTO) {
		Buyer b = buyerRespository.findByEmailId(loginDTO.getEmail());
		if (b != null && b.getPassword().equals(loginDTO.getPassword())) {
			if(b.getRewardPoints() >= 10000) {
				buyerRespository.updatePrivilegedStatus(b.getBuyerId());
			}
			return "Successfully Logged In!";
		}
		return "Login Failed!";
	}
	
	//This method has implemented addition of seller.
	@Override
	public String addSeller(SellerDTO sellerDTO) throws UserException {
		if(validateSeller(sellerDTO.getSellerId())) {
			if(sellerRepository.findAllPhoneNo(sellerDTO.getPhoneNo()).isEmpty()) {
				Seller seller = new Seller();
				seller.setSellerId(sellerDTO.getSellerId());
				seller.setName(sellerDTO.getName());
				seller.setEmail(sellerDTO.getEmail());
				seller.setPhoneNo(sellerDTO.getPhoneNo());
				seller.setPassword(sellerDTO.getPassword());
				seller.setIsActive(true);
				Seller sellerSuccess = sellerRepository.save(seller);
				return sellerSuccess.getSellerId();
			}
			return "Phone Number Is Already In Use!";
		}
		return "Seller Already Exist!";
	}
	//This method has implemented to get all the sellers.
	@Override
	public List<SellerDTO> getAllSeller() throws UserException {
		Iterable<Seller> sellers = sellerRepository.findAll();
		List<SellerDTO> sellerDTOs = new ArrayList<>();
		sellers.forEach(seller -> {
			SellerDTO s = new SellerDTO();
			s.setSellerId(seller.getSellerId());
			s.setEmail(seller.getEmail());
			s.setName(seller.getName());
			s.setPhoneNo(seller.getPhoneNo());
			s.setPassword(seller.getPassword());
			s.setIsActive(seller.getIsActive());
			sellerDTOs.add(s);
		});
		if(sellerDTOs.isEmpty()) {
			throw new UserException("Service.SELLERS_NOT_FOUND");
		}
		return sellerDTOs;
	}
	//This method has implemented authentication of Seller.
	public String sellerLogin(LoginDTO loginDTO) {
		Seller s = sellerRepository.findByEmailId(loginDTO.getEmail());
		if (s != null && s.getPassword().equals(loginDTO.getPassword())) {
			return "Successfully Logged In!";
		}
		return "Login Failed!";
	}
	//This method is use to delete buyer from database.
	public void deleteBuyer(String buyerId) throws UserException{
		Optional<Buyer> buyer = buyerRespository.findById(buyerId);
		if(buyer.isEmpty()) {
			throw new UserException("Service.BUYER_NOT FOUND");
		}
		buyerRespository.deleteById(buyerId);
	}
	//This method is use to delete seller from database.
	public void deleteSeller(String sellerId) throws UserException{
		Optional<Seller> seller = sellerRepository.findById(sellerId);
		if(seller.isEmpty()) {
			throw new UserException("Service.SELLER_NOT_FOUND");
		}
		sellerRepository.deleteById(sellerId);
	}
	//This method is use to add cart in the database.
	@Override
	public String addCart(CartDTO cartDTO) throws UserException {
		Optional<Buyer> b = buyerRespository.findById(cartDTO.getBuyerId());
		if(b.isEmpty()) {
			throw new UserException("Service.BUYER_NOT FOUND");
		}
		Cart cart = new Cart();
		cart.setBuyerId(cartDTO.getBuyerId());
		cart.setProductId(cartDTO.getProductId());
		cart.setQuantity(cartDTO.getQuantity());
		Cart cartSuccess = cartRepository.save(cart);
		return cartSuccess.getBuyerId();
	}
	//This method is use to get details of the cart on the basis of buyer ID.
	@Override
	public List<CartDTO> getCart(String buyerId) throws UserException {
		Iterable<Cart> carts = cartRepository.findCart(buyerId);
		List<CartDTO> cartDTOs = new ArrayList<>();
		carts.forEach(cart -> {
			CartDTO c = new CartDTO();
			c.setBuyerId(cart.getBuyerId());
			c.setProductId(cart.getProductId());
			c.setQuantity(cart.getQuantity());
			cartDTOs.add(c);
		});
		if(cartDTOs.isEmpty()) {
			throw new UserException("Service.CART_NOT_FOUND");
		}
		return cartDTOs;
	}
	//This method is use to delete the cart from database.
	public void deleteCart(String buyerId, String productId) throws UserException{
		Optional<Cart> cart = cartRepository.findByCart(buyerId, productId);
		if(cart.isEmpty()) {
			throw new UserException("Service.CART_NOT_FOUND");
		}
		cartRepository.deleteByCart(buyerId, productId);
	}
	//This method is use to add wish list to the database.
	@Override
	public String addWishlist(WishlistDTO wishlistDTO) throws UserException {
		Optional<Buyer> b = buyerRespository.findById(wishlistDTO.getBuyerId());
		if(b.isEmpty()) {
			throw new UserException("Service.BUYER_NOT FOUND");
		}
		Wishlist wishlist = new Wishlist();
		wishlist.setBuyerId(wishlistDTO.getBuyerId());
		wishlist.setProductId(wishlistDTO.getProductId());
		Wishlist wishlistSuccess = wishlistRepository.save(wishlist);
		return wishlistSuccess.getBuyerId();
	}
	//This method is use to delete wish list from the database.
	public void deleteWishlist(String buyerId, String productId) throws UserException{
		Optional<Wishlist> wishlist = wishlistRepository.findByWishlist(buyerId, productId);
		if(wishlist.isEmpty()) {
			throw new UserException("Service.WISHLIST_NOT_FOUND");
		}
		wishlistRepository.deleteByWishlist(buyerId, productId);
	}
	//This method is use to move wish list from cart.
	public void moveWishlist(CartDTO cartDTO) throws UserException{
		Optional<Wishlist> wishlist = wishlistRepository.findByWishlist(cartDTO.getBuyerId(), cartDTO.getProductId());
		if(wishlist.isEmpty()) {
			throw new UserException("Service.WISHLIST_NOT_FOUND");
		}
		Cart cart = new Cart();
		cart.setBuyerId(cartDTO.getBuyerId());
		cart.setProductId(cartDTO.getProductId());
		cart.setQuantity(cartDTO.getQuantity());
		cartRepository.save(cart);
		wishlistRepository.deleteByWishlist(cartDTO.getBuyerId(), cartDTO.getProductId());
	}
}
