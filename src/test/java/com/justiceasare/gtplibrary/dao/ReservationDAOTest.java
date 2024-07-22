package com.justiceasare.gtplibrary.dao;


public class ReservationDAOTest {

}
//
//import com.justiceasare.gtplibrary.model.Reservation;
//import com.justiceasare.gtplibrary.model.ReservationType;
//import com.justiceasare.gtplibrary.util.DatabaseSource;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockedStatic;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.sql.Date;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//
//import java.sql.Connection;
//import java.sql.Date;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.time.LocalDate;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//public class ReservationDAOTest {
//
//    @Mock
//    private DatabaseSource databaseSource;
//
//    @Mock
//    private Connection connection;
//
//    @Mock
//    private PreparedStatement addReservationStmt;
//
//    @Mock
//    private PreparedStatement addTransactionStmt;
//
//    @Mock
//    private ResultSet generatedKeys;
//
//    @InjectMocks
//    private ReservationDAO reservationDAO;
//
//    @BeforeEach
//    public void setUp() throws SQLException {
//        when(databaseSource.getConnection()).thenReturn(connection);
//        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(addReservationStmt);
//        when(connection.prepareStatement(anyString())).thenReturn(addTransactionStmt);
//        when(addReservationStmt.getGeneratedKeys()).thenReturn(generatedKeys);
//    }
//
//    @Test
//    public void testAddReservation_Success() throws SQLException {
//        // Arrange
//        Reservation reservation = new Reservation(0,"Existing Book Title", "existingUser", ReservationType.RETURN, LocalDate.now(), false);
//
//        when(addReservationStmt.executeUpdate()).thenReturn(1);
//        when(generatedKeys.next()).thenReturn(true);
//        when(generatedKeys.getInt(1)).thenReturn(1);
//
//        // Mock the static methods
//        try (MockedStatic<ReservationDAO> reservationDAOMock = mockStatic(ReservationDAO.class)) {
//            reservationDAOMock.when(() -> ReservationDAO.getBookIdByTitle(anyString())).thenReturn(1);
//            reservationDAOMock.when(() -> ReservationDAO.getUserIdByUsername(anyString())).thenReturn(1);
//
//            // Act
//            int result = reservationDAO.addReservation(reservation);
//
//            // Assert
//            assertEquals(1, result, "Reservation ID should be 1");
//            verify(addReservationStmt).setInt(1, 1); // assuming getBookIdByTitle returns 1
//            verify(addReservationStmt).setInt(2, 1); // assuming getUserIdByUsername returns 1
//            verify(addReservationStmt).setString(3, reservation.getReservationType().name());
//            verify(addReservationStmt).setDate(4, Date.valueOf(reservation.getReservationDate()));
//            verify(addReservationStmt).setBoolean(5, reservation.isCompleted());
//        }
//    }
//}
