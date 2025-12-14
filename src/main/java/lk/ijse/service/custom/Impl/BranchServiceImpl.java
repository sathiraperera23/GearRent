package lk.ijse.service.custom.Impl;

import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.BranchDAO;
import lk.ijse.dto.BranchDTO;
import lk.ijse.entity.Branch;
import lk.ijse.service.custom.BranchService;

import java.util.ArrayList;
import java.util.List;

public class BranchServiceImpl implements BranchService {

    private final BranchDAO branchDAO =
            (BranchDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.BRANCH);

    @Override
    public boolean addBranch(BranchDTO branchDTO) throws Exception {
        Branch branch = new Branch(
                0,
                branchDTO.getCode(),
                branchDTO.getName(),
                branchDTO.getAddress(),
                branchDTO.getContact(),
                null
        );
        return branchDAO.save(branch);
    }

    @Override
    public boolean updateBranch(BranchDTO branchDTO) throws Exception {
        Branch branch = new Branch(
                branchDTO.getBranchId(),
                branchDTO.getCode(),
                branchDTO.getName(),
                branchDTO.getAddress(),
                branchDTO.getContact(),
                null
        );
        return branchDAO.update(branch);
    }

    @Override
    public boolean deleteBranch(int branchId) throws Exception {
        return branchDAO.delete(branchId);
    }

    @Override
    public BranchDTO getBranch(int branchId) throws Exception {
        Branch branch = branchDAO.find(branchId);
        if (branch != null) {
            return new BranchDTO(
                    branch.getBranchId(),
                    branch.getCode(),
                    branch.getName(),
                    branch.getAddress(),
                    branch.getContact(),
                    branch.getCreatedAt()
            );
        }
        return null;
    }

    @Override
    public List<BranchDTO> getAllBranches() throws Exception {
        List<Branch> branchList = branchDAO.findAll();
        List<BranchDTO> dtoList = new ArrayList<>();
        for (Branch branch : branchList) {
            dtoList.add(new BranchDTO(
                    branch.getBranchId(),
                    branch.getCode(),
                    branch.getName(),
                    branch.getAddress(),
                    branch.getContact(),
                    branch.getCreatedAt()
            ));
        }
        return dtoList;
    }
}
