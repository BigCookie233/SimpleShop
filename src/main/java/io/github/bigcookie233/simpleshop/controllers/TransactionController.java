package io.github.bigcookie233.simpleshop.controllers;

import io.github.bigcookie233.simpleshop.ApiAdapter;
import io.github.bigcookie233.simpleshop.entities.Action;
import io.github.bigcookie233.simpleshop.entities.Product;
import io.github.bigcookie233.simpleshop.entities.Transaction;
import io.github.bigcookie233.simpleshop.entities.TransactionStatus;
import io.github.bigcookie233.simpleshop.services.ConfigurablePropertyService;
import io.github.bigcookie233.simpleshop.services.ProductService;
import io.github.bigcookie233.simpleshop.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private ConfigurablePropertyService configurablePropertyService;

    @Autowired
    public TransactionController(ProductService productService, TransactionService transactionService, ApiAdapter apiAdapter, ConfigurablePropertyService configurablePropertyService) {
        this.productService = productService;
        this.transactionService = transactionService;
        this.apiAdapter = apiAdapter;
        this.configurablePropertyService = configurablePropertyService;
    }

    @GetMapping("create-transaction")
    public RedirectView createTransaction(@RequestParam("productUuid") String productUuid,
                                          @RequestParam("amount") int amount,
                                          @RequestParam("paymentMethod") String paymentMethod,
                                          @RequestParam("minecraftId") String minecraftId) {
        Product product = this.productService.findProductByUuid(UUID.fromString(productUuid));
        if (validateMinecraftId(minecraftId)) {
            Transaction transaction = new Transaction(product, product.getPrice() * amount, minecraftId);
            this.transactionService.saveTransaction(transaction);
            return new RedirectView(this.apiAdapter.getPayLink(transaction.getTransactionId(), paymentMethod, product.getName(), transaction.getAmount()));
        } else {
            return null;
        }
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
        processTransaction(transaction, sign_type, sign, trade_status, params);
        model.addAttribute("transaction", transaction);
        return "complete-transaction";
    }

    @GetMapping("update-transaction")
    public ResponseEntity<String> updateTransaction(@RequestParam("pid") String pid,
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
        processTransaction(transaction, sign_type, sign, trade_status, params);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    public static boolean validateMinecraftId(String id) {
        // 检查字符串长度是否小于 16
        if (id.length() < 3 || id.length() >= 16) {
            return false;
        }

        // 检查字符串是否仅包含英文字母、数字和下划线
        return id.matches("^[a-zA-Z0-9_]+$");
    }

    private void processTransaction(Transaction transaction, String sign_type, String sign, String trade_status, Map<String, String>params) {
        if (transaction.getStatus() == TransactionStatus.PENDING) {
            if (!sign_type.equalsIgnoreCase("md5") || !sign.equals(ApiAdapter.getSign(params, configurablePropertyService.findPropertyByName("key").value))) {
                transaction.setStatus(TransactionStatus.ERROR);
            } else {
                if (!trade_status.equals("TRADE_SUCCESS")) {
                    transaction.setStatus(TransactionStatus.FAILED);
                } else {
                    try {
                        Action action = new Action(configurablePropertyService.findPropertyByName("remote_uuid").value, configurablePropertyService.findPropertyByName("uuid").value, transaction.getProduct().getAction());
                        action.buildCommand(transaction.getMinecraftId());
                        this.apiAdapter.executeCommand(action);
                        transaction.setStatus(TransactionStatus.SUCCESS);
                    } catch (Exception e) {
                        transaction.setStatus(TransactionStatus.ERROR);
                    }
                }
            }
            this.transactionService.saveTransaction(transaction);
        }
    }
}
