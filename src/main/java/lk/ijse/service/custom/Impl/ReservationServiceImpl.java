package lk.ijse.service.custom.impl;

import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.ReservationDAO;
import lk.ijse.dto.ReservationDTO;
import lk.ijse.entity.Reservation;
import lk.ijse.service.custom.ReservationService;

import java.util.ArrayList;
import java.util.List;

public class ReservationServiceImpl implements ReservationService {

    private final ReservationDAO reservationDAO =
            (ReservationDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.RESERVATION);

    @Override
    public boolean saveReservation(ReservationDTO dto) throws Exception {
        return reservationDAO.save(new Reservation(
                0,
                dto.getCustomerId(),
                dto.getEquipmentId(),
                dto.getReservedFrom(),
                dto.getReservedTo(),
                dto.getTotalPrice(),
                "Pending",
                null
        ));
    }

    @Override
    public boolean updateReservation(ReservationDTO dto) throws Exception {
        return reservationDAO.update(new Reservation(
                dto.getReservationId(),
                0,
                0,
                null,
                dto.getReservedTo(),
                dto.getTotalPrice(),
                dto.getStatus(),
                null
        ));
    }

    @Override
    public boolean deleteReservation(long id) throws Exception {
        return reservationDAO.delete(id);
    }

    @Override
    public ReservationDTO searchReservation(long id) throws Exception {
        Reservation r = reservationDAO.find(id);
        if (r == null) return null;

        return new ReservationDTO(
                r.getReservationId(),
                r.getCustomerId(),
                r.getEquipmentId(),
                r.getReservedFrom(),
                r.getReservedTo(),
                r.getTotalPrice(),
                r.getStatus(),
                r.getCreatedAt()
        );
    }

    @Override
    public List<ReservationDTO> getAllReservations() throws Exception {
        List<ReservationDTO> list = new ArrayList<>();
        for (Reservation r : reservationDAO.findAll()) {
            list.add(new ReservationDTO(
                    r.getReservationId(),
                    r.getCustomerId(),
                    r.getEquipmentId(),
                    r.getReservedFrom(),
                    r.getReservedTo(),
                    r.getTotalPrice(),
                    r.getStatus(),
                    r.getCreatedAt()
            ));
        }
        return list;
    }
}
