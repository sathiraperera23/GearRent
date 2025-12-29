package lk.ijse.dao.custom;


import lk.ijse.dao.CrudDAO;
import lk.ijse.entity.Branch;

import java.util.List;

public interface BranchDAO extends CrudDAO<Branch, Integer> {
    List<Branch> findByFilter(String codeOrName, String startDate, String endDate) throws Exception;
    int getBranchCount() throws Exception;
}
