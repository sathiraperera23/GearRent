package lk.ijse.service.custom;

import lk.ijse.dto.BranchDTO;
import java.util.List;

public interface BranchService {
    boolean addBranch(BranchDTO branch) throws Exception;
    boolean updateBranch(BranchDTO branch) throws Exception;
    boolean deleteBranch(int branchId) throws Exception;
    BranchDTO getBranch(int branchId) throws Exception;
    List<BranchDTO> getAllBranches() throws Exception;
}