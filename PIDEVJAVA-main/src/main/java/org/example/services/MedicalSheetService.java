package org.example.services;

import org.example.models.MedicalSheet;
import org.example.models.SinisterLife;
import org.example.models.User;
import org.example.utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicalSheetService implements IService<MedicalSheet> {
    private final Connection connection;

    public MedicalSheetService() {
        this.connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(MedicalSheet medicalSheet) throws SQLException {
        String query = "INSERT INTO medical_sheet (user_cin_id, sinister_life_id, medical_diagnosis, treatment_plan, medical_reports, duration_of_incapacity, procedure_performed, sick_leave_duration, hospitalization_period, rehabilitation_period, medical_information) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, medicalSheet.getUserCIN().getId());
            preparedStatement.setLong(2, medicalSheet.getSinisterLife().getId());
            preparedStatement.setString(3, medicalSheet.getMedicalDiagnosis());
            preparedStatement.setString(4, medicalSheet.getTreatmentPlan());
            preparedStatement.setString(5, medicalSheet.getMedicalReports());
            preparedStatement.setInt(6, medicalSheet.getDurationOfIncapacity());
            preparedStatement.setString(7, medicalSheet.getProcedurePerformed());
            preparedStatement.setInt(8, medicalSheet.getSickLeaveDuration());
            preparedStatement.setInt(9, medicalSheet.getHospitalizationPeriod());
            preparedStatement.setInt(10, medicalSheet.getRehabilitationPeriod());
            preparedStatement.setString(11, medicalSheet.getMedicalInformation());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(MedicalSheet medicalSheet) throws SQLException {
        String query = "UPDATE medical_sheet SET medical_diagnosis = ?, treatment_plan = ?, medical_reports = ?, duration_of_incapacity = ?, procedure_performed = ?, sick_leave_duration = ?, hospitalization_period = ?, rehabilitation_period = ?, medical_information = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, medicalSheet.getMedicalDiagnosis());
            preparedStatement.setString(2, medicalSheet.getTreatmentPlan());
            preparedStatement.setString(3, medicalSheet.getMedicalReports());
            preparedStatement.setInt(4, medicalSheet.getDurationOfIncapacity());
            preparedStatement.setString(5, medicalSheet.getProcedurePerformed());
            preparedStatement.setInt(6, medicalSheet.getSickLeaveDuration());
            preparedStatement.setInt(7, medicalSheet.getHospitalizationPeriod());
            preparedStatement.setInt(8, medicalSheet.getRehabilitationPeriod());
            preparedStatement.setString(9, medicalSheet.getMedicalInformation());
            preparedStatement.setLong(10, medicalSheet.getId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating medical sheet failed, no rows affected.");
            }
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM medical_sheet WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<MedicalSheet> getAll() throws SQLException {
        List<MedicalSheet> medicalSheets = new ArrayList<>();
        String query = """
        SELECT ms.*, u.id, u.first_name, sl.id , sl.description FROM medical_sheet ms
        INNER JOIN user u ON ms.user_cin_id = u.id
        INNER JOIN sinistre sl ON ms.sinister_life_id = sl.id
        """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                MedicalSheet medicalSheet = new MedicalSheet();
                medicalSheet.setId(resultSet.getLong("ms.id"));
                User user = new User() ;
                user.setFirstName(resultSet.getString("u.first_name"));
                medicalSheet.setUserCIN(user);
                SinisterLife sl = new SinisterLife();
                sl.setDescription(resultSet.getString("sl.description"));
                medicalSheet.setSinisterLife(sl);
                medicalSheet.setMedicalDiagnosis(resultSet.getString("ms.medical_diagnosis"));
                medicalSheet.setTreatmentPlan(resultSet.getString("ms.treatment_plan"));
                medicalSheet.setMedicalReports(resultSet.getString("ms.medical_reports"));
                medicalSheet.setDurationOfIncapacity(resultSet.getInt("ms.duration_of_incapacity"));
                medicalSheet.setProcedurePerformed(resultSet.getString("ms.procedure_performed"));
                medicalSheet.setSickLeaveDuration(resultSet.getInt("ms.sick_leave_duration"));
                medicalSheet.setHospitalizationPeriod(resultSet.getInt("ms.hospitalization_period"));
                medicalSheet.setRehabilitationPeriod(resultSet.getInt("ms.rehabilitation_period"));
                medicalSheet.setMedicalInformation(resultSet.getString("ms.medical_information"));

                medicalSheets.add(medicalSheet);
            }
        }
        return medicalSheets;
    }


    @Override
    public MedicalSheet getById(int id) throws SQLException {

        return null;
    }

    public List<MedicalSheet> getAllSortedByIdDesc() throws SQLException {
        List<MedicalSheet> medicalSheets = new ArrayList<>();
        String query = "SELECT * FROM medical_sheet ORDER BY id DESC";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                MedicalSheet medicalSheet = new MedicalSheet();
                // Map the result set to a MedicalSheet object
                medicalSheets.add(medicalSheet);
            }
        }
        return medicalSheets;
    }

    public MedicalSheet getLastAddedMedicalSheet() throws SQLException {
        List<MedicalSheet> medicalSheets = getAllSortedByIdDesc();
        if (!medicalSheets.isEmpty()) {
            return medicalSheets.get(0); // The first one is the most recently added
        }
        return null; // Handle the case where no MedicalSheet is found
    }

}
