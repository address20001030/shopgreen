package vn.fs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.fs.entities.Product;

/**
 * @author DongTHD
 *
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	@Query(value = "SELECT * FROM products WHERE product_name = ?", nativeQuery = true)
	public Optional<Product> ProductName(String product_name);
	// List product by category
	@Query(value = "SELECT * FROM products WHERE category_id = ?", nativeQuery = true)
	public List<Product> listProductByCategory(Long categoryId);

	@Query(value = "SELECT *  FROM products where price >=?1 and price <=?2",nativeQuery = true)
	public List<Product> searchPrice(int from, int to);

	// 10 sản phẩm hàng đầu theo danh mục
	@Query(value = "SELECT * FROM products AS b WHERE b.category_id = ?;", nativeQuery = true)
	List<Product> listProductByCategory10(Long categoryId);
	
	// Liệt kê sản phẩm mới
	@Query(value = "SELECT * FROM products ORDER BY entered_date DESC limit 20;", nativeQuery = true)
	public List<Product> listProductNew20();
	
	// Tìm kiếm sản phẩm
	@Query(value = "SELECT * FROM products WHERE product_name LIKE %?1%" , nativeQuery = true)
	public List<Product> searchProduct(String productName);
	
	// Đếm số lượng theo sản phẩm
	@Query(value = "SELECT c.category_id,c.category_name,\r\n"
			+ "COUNT(*) AS SoLuong\r\n"
			+ "FROM products p\r\n"
			+ "JOIN categories c ON p.category_id = c.category_id\r\n"
			+ "GROUP BY c.category_name;" , nativeQuery = true)
	List<Object[]> listCategoryByProductName();
	
	// Top 20 sản phẩm bán chạy nhất
	@Query(value = "SELECT p.product_id,\r\n"
			+ "COUNT(*) AS SoLuong\r\n"
			+ "FROM order_details p\r\n"
			+ "JOIN products c ON p.product_id = c.product_id\r\n"
			+ "GROUP BY p.product_id\r\n"
			+ "ORDER by SoLuong DESC limit 20;", nativeQuery = true)
	public List<Object[]> bestSaleProduct20();
	
	@Query(value = "select * from products o where product_id in :ids", nativeQuery = true)
	List<Product> findByInventoryIds(@Param("ids") List<Integer> listProductId);

	@Query(value = "SELECT p.product_id, p.description,\r\n"
			+" p.discount, p.entered_date, p.price,\r\n"
			+" p.product_image, p.product_name, p.quantity,\r\n"
			+" p.status, p.category_id, p.favorite FROM products p\r\n" +
			"JOIN order_details o ON p.product_id = o.product_id\r\n" +
			"GROUP BY p.product_name\r\n" +
			"ORDER BY SUM(o.quantitydetail) DESC\r\n" +
			"limit 10;", nativeQuery = true)
	public List<Object[]> topSaleProduct();

}
