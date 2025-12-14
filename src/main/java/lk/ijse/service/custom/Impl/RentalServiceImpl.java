package lk.ijse.service.custom.Impl;

import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.RentalDAO;
import lk.ijse.dto.RentalDTO;
import lk.ijse.entity.Rental;
import lk.ijse.service.custom.RentalService;

import java.util.ArrayList;
import java.util.List;

public class RentalServiceImpl implements RentalService {

    private final RentalDAO rentalDAO =
            (RentalDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.RENTAL);

    @Override
    public boolean saveRental(RentalDTO d) throws Exception {
        return rentalDAO.save(new Rental(
                0,
                d.getCustomerId(),
                d.getEquipmentId(),
                d.getRentedFrom(),
                d.getRentedTo(),
                d.getDailyPrice(),
                d.getSecurityDeposit(),
                d.getReservationId(),
                "Open",
                null
        ));
    }

    @Override
    public boolean updateRental(RentalDTO d) throws Exception {
        // Fetch existing rental first
        Rental existing = rentalDAO.find(d.getRentalId());
        if (existing == null) return false;

        existing.setRentedTo(d.getRentedTo());
        existing.setDailyPrice(d.getDailyPrice());
        existing.setSecurityDeposit(d.getSecurityDeposit());
        existing.setStatus(d.getStatus());

        return rentalDAO.update(existing);
    }

    @Override
    public boolean deleteRental(long id) throws Exception {
        return rentalDAO.delete(id);
    }

    @Override
    public RentalDTO searchRental(long id) throws Exception {
        Rental r = rentalDAO.find(id);
        if (r == null) return null;

        return new RentalDTO(
                r.getRentalId(),
                r.getCustomerId(),
                r.getEquipmentId(),
                r.getRentedFrom(),
                r.getRentedTo(),
                r.getDailyPrice(),
                r.getSecurityDeposit(),
                r.getReservationId(),
                r.getStatus(),
                r.getCreatedAt()
        );
    }

    @Override
    public List<RentalDTO> getAllRentals() throws Exception {
        List<RentalDTO> list = new ArrayList<>();
        for (Rental r : rentalDAO.findAll()) {
            list.add(new RentalDTO(
                    r.getRentalId(),
                    r.getCustomerId(),
                    r.getEquipmentId(),
                    r.getRentedFrom(),
                    r.getRentedTo(),
                    r.getDailyPrice(),
                    r.getSecurityDeposit(),
                    r.getReservationId(),
                    r.getStatus(),
                    r.getCreatedAt()
            ));
        }
        return list;
    }
}