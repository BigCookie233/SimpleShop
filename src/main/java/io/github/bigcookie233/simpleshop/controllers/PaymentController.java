package io.github.bigcookie233.simpleshop.controllers;

import io.github.bigcookie233.simpleshop.entities.Payment;
import io.github.bigcookie233.simpleshop.entities.Product;
import io.github.bigcookie233.simpleshop.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
public class PaymentController {
    private ProductService productService;

    @Autowired
    public PaymentController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/create-order")
    public String create(Model model, @RequestParam("productUuid") String productUuid, @RequestParam("amount") int amount, @RequestParam("minecraftId") String minecraftId) {
        Product product = this.productService.findProductByUuid(UUID.fromString(productUuid));
        Payment payment = new Payment(amount * product.getPrice());
        model.addAttribute("product", product);
        model.addAttribute("payment", payment);
        model.addAttribute("minecraft_id", minecraftId);
        model.addAttribute("amount", amount);
        return "create-order";
    }
}
