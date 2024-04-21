package io.github.bigcookie233.simpleshop;

import io.github.bigcookie233.simpleshop.entities.Payment;
import io.github.bigcookie233.simpleshop.entities.Product;
import io.github.bigcookie233.simpleshop.jdbc.JdbcDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class PaymentController {
    private JdbcDao jdbcDao;
    private ApiAdapter apiAdapter;

    @Autowired
    public PaymentController(JdbcDao jdbcDao, ApiAdapter apiAdapter) {
        this.jdbcDao = jdbcDao;
        this.apiAdapter = apiAdapter;
    }

    @GetMapping("/create")
    public String create(Model model, @RequestParam("productUuid") String productUuid, @RequestParam("amount") int amount, @RequestParam("minecraftId") String minecraftId) {
        Product product = jdbcDao.getProduct(productUuid);
        Payment payment = new Payment(amount*product.price);
        model.addAttribute("product", product);
        model.addAttribute("payment", payment);
        model.addAttribute("minecraft_id", minecraftId);
        model.addAttribute("amount", amount);
        return "create";
    }

    @GetMapping("/process-payment")
    public RedirectView processPayment(@RequestParam("productUuid") String productUuid, @RequestParam("amount") int amount, @RequestParam("paymentMethod") String paymentMethod, @RequestParam("minecraftId") String minecraftId) {
        Product product = jdbcDao.getProduct(productUuid);
        Payment payment = new Payment(product.price*amount);
        payment.submit();
        return new RedirectView(this.apiAdapter.getPayLink(payment.id, paymentMethod, product.name, payment.amount));
    }
}
