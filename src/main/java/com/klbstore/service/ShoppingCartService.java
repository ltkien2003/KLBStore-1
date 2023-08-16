package com.klbstore.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.klbstore.config.SecurityRestTemplate;
import com.klbstore.dao.MauSacDAO;
import com.klbstore.dto.AllChiTietGioHangDTO;
import com.klbstore.dto.ChiTietGioHangDTO;
import com.klbstore.jwt.JwtTokenProvider;
import com.klbstore.model.ChiTietGioHang;
import com.klbstore.model.CustomUserDetails;

@Service
public class ShoppingCartService {

    private static final String CART_SESSION_KEY = "cart";

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private MauSacDAO mauSacDAO;

    @Autowired
    private SecurityRestTemplate restTemplate;

    @Autowired
    private JwtTokenProvider tokenProvider;

    String getToken() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String jwt = tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
            return jwt;
        } catch (Exception e) {
            System.out.print("Có lỗi xảy ra khi tạo token: " + e.getMessage());
            return null;
        }
    }

    public AllChiTietGioHangDTO getCart() {
        AllChiTietGioHangDTO cart = (AllChiTietGioHangDTO) httpSession.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            cart = new AllChiTietGioHangDTO();
            httpSession.setAttribute(CART_SESSION_KEY, cart);
        }
        return cart;
    }

    public String addToCart(Integer mauSacId, Integer soLuong) {
        String message = "";
        AllChiTietGioHangDTO cart = getCart();
        JsonNode sanPhamDTO = restTemplate.get("http://localhost:8080/rest/xinchao?hienThi=true&mauSacId=" + mauSacId)
                .get(0);

        if (sanPhamDTO != null) {
            ChiTietGioHangDTO ctghdto = new ChiTietGioHangDTO();
            ctghdto.setSanPhamId(sanPhamDTO.get("sanPham").get("sanPhamId").asLong());
            ctghdto.setTenSanPham(sanPhamDTO.get("sanPham").get("tenSanPham").asText());
            ctghdto.setGiaBan(sanPhamDTO.get("tongGia").asDouble());
            ctghdto.setTongGia(sanPhamDTO.get("tongGia").asDouble() * soLuong);

            ChiTietGioHang ctgh = new ChiTietGioHang();
            ctgh.setSoLuong(soLuong);
            ctgh.setMauSac(mauSacDAO.findById(mauSacId).get());
            ctghdto.setChiTietGioHang(ctgh);

            List<ChiTietGioHangDTO> cartItems = cart.getDanhSachSanPhamTrongGioHang();

            if (cartItems == null) {
                cartItems = new ArrayList<>();
                cart.setDanhSachSanPhamTrongGioHang(cartItems);
            }
            boolean existsInCart = false;

            for (ChiTietGioHangDTO item : cartItems) {
                int newQuantity = item.getChiTietGioHang().getSoLuong() + soLuong;
                int maxQuantityInStock = Integer.parseInt(
                        sanPhamDTO.get("sanPham").get("sanPhamChiTietSanPhams").get(0).get("soLuongTrongKho")
                                .asText());
                if (item.getChiTietGioHang().getMauSac().getMauSacId().equals(mauSacId)) {
                    if (newQuantity > maxQuantityInStock) {
                        newQuantity = maxQuantityInStock;
                        message = "Số lượng sản phẩm trong giỏ hàng đã đạt tối đa.";
                    }
                    item.getChiTietGioHang().setSoLuong(newQuantity);
                    existsInCart = true;
                    break;
                }
            }

            if (!existsInCart) {
                cartItems.add(ctghdto);
            }
        }
        httpSession.setAttribute(CART_SESSION_KEY, cart);

        if (!message.isEmpty()) {
            return message;
        } else {
            return "Sản phẩm đã được thêm vào giỏ hàng.";
        }
    }

    public AllChiTietGioHangDTO getShoppingCartDTO() {
        AllChiTietGioHangDTO cart = (AllChiTietGioHangDTO) httpSession.getAttribute(CART_SESSION_KEY);

        if (cart == null) {
            cart = new AllChiTietGioHangDTO();
            httpSession.setAttribute(CART_SESSION_KEY, cart);
        } else {
            List<ChiTietGioHangDTO> cartItems = cart.getDanhSachSanPhamTrongGioHang();
            int totalQuantity = 0;
            double totalAmount = 0.0;
            if (cartItems != null) {
                for (ChiTietGioHangDTO cartItem : cartItems) {
                    cartItem.setTongGia(cartItem.getChiTietGioHang().getSoLuong() * cartItem.getGiaBan());
                    totalAmount += cartItem.getTongGia();
                    JsonNode sanPhamDTO = restTemplate
                            .get("http://localhost:8080/rest/xinchao?hienThi=true&mauSacId="
                                    + cartItem.getChiTietGioHang().getMauSac().getMauSacId())
                            .get(0);
                    int maxQuantityInStock = Integer.parseInt(
                            sanPhamDTO.get("sanPham").get("sanPhamChiTietSanPhams").get(0).get("soLuongTrongKho")
                                    .asText());
                    if (cartItem.getChiTietGioHang().getSoLuong() == 0
                            || cartItem.getChiTietGioHang().getSoLuong() > maxQuantityInStock) {
                        cartItems.remove(cartItem);
                    }

                }
                totalQuantity = cartItems.size();
            }
            cart.setTongSoLuong(totalQuantity);
            cart.setTongTien(totalAmount);
        }

        return cart;
    }

    public String updateToCart(Integer mauSacId, Integer soLuong) {
        String message = "";
        AllChiTietGioHangDTO cart = getCart();

        List<ChiTietGioHangDTO> cartItems = cart.getDanhSachSanPhamTrongGioHang();
        JsonNode sanPhamDTO = restTemplate.get("http://localhost:8080/rest/xinchao?hienThi=true&mauSacId=" + mauSacId)
                .get(0);
        if (cartItems != null) {
            for (ChiTietGioHangDTO item : cartItems) {
                if (item.getChiTietGioHang().getMauSac().getMauSacId().equals(mauSacId)) {
                    int maxQuantityInStock = Integer.parseInt(
                            sanPhamDTO.get("sanPham").get("sanPhamChiTietSanPhams").get(0).get("soLuongTrongKho")
                                    .asText());
                    if (soLuong > maxQuantityInStock) {
                        soLuong = maxQuantityInStock;
                    }
                    item.getChiTietGioHang().setSoLuong(soLuong);
                    item.setTongGia(item.getGiaBan() * soLuong);
                    break;
                }
            }
            message = "Cập nhật số lượng sản phẩm thành công";
        }

        httpSession.setAttribute(CART_SESSION_KEY, cart);
        return message;
    }

    public String deleteFromCart(Integer mauSacId) {
        String message = "";
        AllChiTietGioHangDTO cart = getCart();

        List<ChiTietGioHangDTO> cartItems = cart.getDanhSachSanPhamTrongGioHang();

        if (cartItems != null) {
            cartItems.removeIf(item -> item.getChiTietGioHang().getMauSac().getMauSacId().equals(mauSacId));

        }

        httpSession.setAttribute(CART_SESSION_KEY, cart);
        return message;
    }

    public void clearCart() {
        AllChiTietGioHangDTO cart = getCart();
        cart.getDanhSachSanPhamTrongGioHang().clear();
        httpSession.setAttribute(CART_SESSION_KEY, cart);
    }

}
