package vn.fs.ApiController;

import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fs.entities.Product;
import vn.fs.entities.ResponseObject;
import vn.fs.repository.ProductRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/app/api/products")
public class HomeApiController {
    @Autowired
    private ProductRepository productRepository;
    @Value("${upload.path}")
    private String pathUploadImage;
    @GetMapping("")
    List<Product> getAllProduct(){
        return productRepository.findAll();
    }
    @GetMapping(value = "/loadImage/{imageName}")
    @ResponseBody
    public byte[] index(@PathVariable String imageName, HttpServletResponse response)
            throws IOException {
        response.setContentType("image/jpeg");
        File file = new File(pathUploadImage + File.separatorChar + imageName);
        InputStream inputStream = null;
        if (file.exists()) {
            try {
                inputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (inputStream != null) {
                return IOUtils.toByteArray(inputStream);
            }
        }
        return IOUtils.toByteArray(inputStream);
    }
    //nhận thông tin chi tiết về sản phẩm
    @GetMapping("/{productId}")
    ResponseEntity<ResponseObject> ProductDetail(@PathVariable Long productId){
        Optional<Product> foundProduct = productRepository.findById(productId);
        return foundProduct.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("Ok", "Query product Success", foundProduct)
                ):
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("Ok", "Cannot find Product With id =" + productId, "foundProduct")
                );
    }

    //Top10 sản phẩm cùng loại
    @GetMapping("/listProductTop/{categoryId}")
    ResponseEntity<ResponseObject> listProductByCategory10(@PathVariable Long categoryId){
        List<Product> foundProduct = productRepository.listProductByCategory10(categoryId);
        return foundProduct.size() > 0 ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("Ok", "Query product Success", foundProduct)
                ):
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("Ok", "Cannot find Product With id =" + categoryId, "foundProduct")
                );
    }
    //Top10 sản phẩm cùng loại
    @GetMapping("/listproduct10")
    ResponseEntity<ResponseObject> listproduct10(){
        List<Product> foundProduct = productRepository.listProductNew20();
        return foundProduct.size() > 0 ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("Ok", "Query product Success", foundProduct)
                ):
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("Ok", "Query product Fails", "foundProduct")
                );
    }

    //Top 10 sản phẩm được gợi ý
    @GetMapping("/bestProduct")
    ResponseEntity<ResponseObject> bestProduct(){
        List<Object[]> products = productRepository.topSaleProduct();

            ArrayList<Integer> listIdProducts = new ArrayList<>();
            for (int i = 0; i < products.size(); i++) {
                String id = String.valueOf(products.get(i)[0]);
                listIdProducts.add(Integer.valueOf(id));
            }

            List<Product> listBestProduct = productRepository.findByInventoryIds(listIdProducts);

        return listBestProduct.size() > 0 ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("Ok", "Query product Success", listBestProduct)
                ):
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("Ok", "Query product Fails", "foundProduct")
                );
    }


}
