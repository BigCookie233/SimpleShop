package io.github.bigcookie233.simpleshop.controllers;

import io.github.bigcookie233.simpleshop.ApiAdapter;
import io.github.bigcookie233.simpleshop.ApiProperties;
import io.github.bigcookie233.simpleshop.entities.Product;
import io.github.bigcookie233.simpleshop.entities.Transaction;
import io.github.bigcookie233.simpleshop.entities.TransactionStatus;
import io.github.bigcookie233.simpleshop.services.ProductService;
import io.github.bigcookie233.simpleshop.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

@Controller
public class TransactionController {
    private ProductService productService;
    private TransactionService transactionService;
    private ApiAdapter apiAdapter;
    private ApiProperties apiProperties;

    @Autowired
    public TransactionController(ProductService productService, TransactionService transactionService, ApiAdapter apiAdapter, ApiProperties apiProperties) {
        this.productService = productService;
        this.transactionService = transactionService;
        this.apiAdapter = apiAdapter;
        this.apiProperties = apiProperties;
    }

    @GetMapping("/create-transaction")
    public RedirectView createTransaction(@RequestParam("productUuid") String productUuid,
                                          @RequestParam("amount") int amount,
                                          @RequestParam("paymentMethod") String paymentMethod,
                                          @RequestParam("minecraftId") String minecraftId) {
        Product product = this.productService.findProductByUuid(UUID.fromString(productUuid));
        Transaction transaction = new Transaction(product, product.getPrice() * amount);
        this.transactionService.saveTransaction(transaction);
//        return new RedirectView(this.apiAdapter.getPayLink(payment.id, paymentMethod, product.name, payment.amount));
        return new RedirectView("/");
    }

    @GetMapping("complete-transaction")
    public String completeTransaction(Model model,
                                      @RequestParam("pid") String pid,
                                      @RequestParam("trade_no") String trade_no,
                                      @RequestParam("out_trade_no") String transactionId,
                                      @RequestParam("type") String type,
                                      @RequestParam("name") String name,
                                      @RequestParam("money") String money,
                                      @RequestParam("trade_status") String trade_status,
                                      @RequestParam("sign") String sign,
                                      @RequestParam("sign_type") String sign_type) {
        Map<String, String> params = new TreeMap<>();
        params.put("pid", pid);
        params.put("trade_no", trade_no);
        params.put("out_trade_no", transactionId);
        params.put("type", type);
        params.put("name", name);
        params.put("money", money);
        params.put("trade_status", trade_status);
        Transaction transaction = this.transactionService.findTransactionByTransactionId(transactionId);
        if (transaction.getStatus() == TransactionStatus.PENDING) {
            if (!sign_type.equalsIgnoreCase("md5")) {
                transaction.setStatus(TransactionStatus.ERROR);
            } else {
                String correct_sign = ApiAdapter.getSign(params, apiProperties.key);
                if (!sign.equals(correct_sign)) {
                    transaction.setStatus(TransactionStatus.ERROR);
                } else if (trade_status.equals("TRADE_SUCCESS")) {
                    transaction.setStatus(TransactionStatus.SUCCESS);
                } else {
                    transaction.setStatus(TransactionStatus.ERROR);
                }
            }
        }
        this.transactionService.saveTransaction(transaction);
        model.addAttribute("transaction", transaction);
        return "complete";
    }
}
