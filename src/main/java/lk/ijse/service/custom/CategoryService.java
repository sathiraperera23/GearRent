package lk.ijse.service.custom;

import lk.ijse.dto.CategoryDTO;
import lk.ijse.service.ServiceFactory;
import lk.ijse.service.SuperService;

import java.util.List;

public interface CategoryService extends SuperService {
    boolean addCategory(CategoryDTO dto) throws Exception;
    boolean updateCategory(CategoryDTO dto) throws Exception;
    boolean deleteCategory(int categoryId) throws Exception;
    CategoryDTO searchCategory(int categoryId) throws Exception;
    List<CategoryDTO> getAllCategories() throws Exception;
}
