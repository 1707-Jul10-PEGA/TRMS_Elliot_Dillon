package com.revature.trms.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.trms.connectionfactory.ConnectionFactory;
import com.revature.trms.objects.ReimbursementForm;

public class FormDAOImpl extends DAOFactory implements FormDAO {

	private Connection conn = null;
	private PreparedStatement pStmt = null;
	// setup a connection
	public void setup() throws SQLException {
		conn = ConnectionFactory.getInstance().getConnection();
	}	
	
	
	@Override
	public boolean submitReimbursementForm(ReimbursementForm form) throws SQLException {
		String sql = "INSERT INTO REIMBURSEMENT_FORMS "
				+ "(P_ID, START_DATE, START_TIME, STREET_ADDRESS, CITY, STATE, ZIP_CODE, REQUESTED_AMOUNT, TYPE_OF_EVENT, GRADING_FORMAT,"
				+ " DESCRIPTION, WORK_RELATED_JUSTIFICATION, ESTIMATED_TIME_OFF, FINAL_GRADE, FINAL_PRESENTATION, STATUS, TITLE)"
				+ "VALUES"
				+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		setup();
		pStmt = conn.prepareStatement(sql);
		
		pStmt.setInt(1, form.getPID());
		pStmt.setString(2, form.getStartDate());
		pStmt.setString(3, form.getStartTime());
		pStmt.setString(4, form.getStreet());
		pStmt.setString(5, form.getCity());
		pStmt.setString(6, form.getCity());
		pStmt.setString(7, form.getZipCode());
		pStmt.setDouble(8, form.getRequestedAmount());
		pStmt.setString(9, form.getTypeOfEvent());
		pStmt.setBlob(10, form.getGradingFormat());
		pStmt.setString(11, form.getDescription());
		pStmt.setString(12, form.getJustification());
		pStmt.setInt(13, form.getEstimatedTimeOff());
		pStmt.setString(14, form.getFinalGrade());
		pStmt.setBlob(15, form.getFinalPresentation());
		pStmt.setString(16, form.getStatus());
		pStmt.setString(17, form.getTitle());
		int submit = pStmt.executeUpdate();					
		return (submit==1)?true:false;
	}

	@Override
	public List<ReimbursementForm> getEmployeeSubmitedForms(int e_id) throws SQLException {
		List<ReimbursementForm> list = new ArrayList<ReimbursementForm>();
		String sql = "SELECT * "
					+ "FROM REIMBURSEMENT_FORMS RF "
					+ "INNER JOIN TYPE_OF_EVENTS TOE ON TOE.TOE_ID = RF.TYPE_OF_EVENT "
					+ "WHERE RF.P_ID = ?";
		setup();
		pStmt = conn.prepareStatement(sql);
		pStmt.setInt(1, e_id);
		ResultSet rs = pStmt.executeQuery();
		while(rs.next()){
			ReimbursementForm rf = new ReimbursementForm();
			rf.setFID(rs.getInt("F_ID"));
			rf.setPID(rs.getInt("P_ID"));
			rf.setStartDate(rs.getString("START_DATE"));
			rf.setStartTime(rs.getString("START_TIME"));
			rf.setStreet(rs.getString("STREET_ADDRESS"));
			rf.setCity(rs.getString("CITY"));
			rf.setState(rs.getString("STATE"));
			rf.setZipCode(rs.getString("ZIP_CODE"));
			rf.setRequestedAmount(rs.getDouble("REQUESTED_AMOUNT"));
			rf.setTypeOfEvent(rs.getString("EVENT"));
			rf.setGradingFormat(rs.getBlob("GRADING_FORMAT"));
			rf.setFinalPresentation(rs.getBlob("FINAL_PRESENTATION"));
			rf.setStatus(rs.getString("STATUS"));
			rf.setTitle(rs.getString("TITLE"));
			list.add(rf);
		}
		return list;
	}

	@Override
	public List<ReimbursementForm>  getPendingRequests(int s_id) throws SQLException {
		List<ReimbursementForm> list = new ArrayList<ReimbursementForm>();
		String sql = "SELECT * "
					+ "FROM REIMBURSEMENT_FORMS RF "
					+ "INNER JOIN APPROVAL_TABLE APP_TAB ON APP_TAB.F_ID = RF.F_ID "
					+ "INNER JOIN  TYPE_OF_EVENTS TOE ON TOE.TOE_ID = RF.TYPE_OF_EVENT "
					+ "WHERE APP_TAB.SUPERVISOR = ? "
					+ "OR APP_TAB.DEPARTMENT_HEAD = ? "
					+ "OR APP_TAB.BENCO = ?"
					+ "OR APP_TAB.DIRECTOR_MANAGER = ?";
		setup();
		pStmt = conn.prepareStatement(sql);
		pStmt.setInt(1, s_id);
		pStmt.setInt(2, s_id);
		pStmt.setInt(3, s_id);
		pStmt.setInt(4, s_id);
		
		ResultSet rs = pStmt.executeQuery();
		while(rs.next()){
			ReimbursementForm rf = new ReimbursementForm();
			rf.setFID(rs.getInt("F_ID"));
			rf.setPID(rs.getInt("P__ID"));
			rf.setStartDate(rs.getString("START_DATE"));
			rf.setStartTime(rs.getString("START_TIME"));
			rf.setStreet(rs.getString("STREET_ADDRESS"));
			rf.setCity(rs.getString("CITY"));
			rf.setState(rs.getString("STATE"));
			rf.setZipCode(rs.getString("ZIP_CODE"));
			rf.setRequestedAmount(rs.getDouble("REQUESTED_AMOUNT"));
			rf.setTypeOfEvent(rs.getString("EVENT"));
			rf.setGradingFormat(rs.getBlob("GRADING_FORMAT"));
			rf.setFinalPresentation(rs.getBlob("FINAL_PRESENTATION"));
			rf.setStatus(rs.getString("STATUS"));
			rf.setTitle(rs.getString("TITLE"));
			list.add(rf);
		}
		return list;
	}
	
	public int getEmployeesCurrentSubmissionCount(int e_id) throws SQLException{
		int currentSubmissions = 0;
		String sql = "SELECT COUNT(*) as COUNT "
					+ "FROM REIMBURSEMENT_FORMS "
					+ "RF WHERE  RF.P_ID = ?";
		setup();
		pStmt = conn.prepareStatement(sql);
		pStmt.setInt(1, e_id);
		ResultSet rs = pStmt.executeQuery();
		while(rs.next()){
			currentSubmissions = rs.getInt("COUNT");
		}
		return currentSubmissions;
	}
	
	public int getEmployeeReviewCount(int s_id) throws SQLException{
		int reviewCount = 0;
		
		String sql = "SELECT COUNT(*) AS COUNT "
				+ "FROM REIMBURSEMENT_FORMS RF "
				+ "INNER JOIN APPROVAL_TABLE APP_TAB ON APP_TAB.F_ID = RF.F_ID "
				+ "INNER JOIN  TYPE_OF_EVENTS TOE ON TOE.TOE_ID = RF.TYPE_OF_EVENT "
				+ "WHERE APP_TAB.SUPERVISOR = ? "
				+ "OR APP_TAB.DEPARTMENT_HEAD = ? "
				+ "OR APP_TAB.BENCO = ? "
				+ "OR APP_TAB.DIRECTOR_MANAGER = ?";
		setup();
		pStmt = conn.prepareStatement(sql);
		pStmt.setInt(1, s_id);
		pStmt.setInt(2, s_id);
		pStmt.setInt(3, s_id);
		pStmt.setInt(4, s_id);
		
		ResultSet rs  = pStmt.executeQuery();
		while(rs.next()){
			reviewCount = rs.getInt("COUNT");
		}
		
		return reviewCount;
	}

	public ReimbursementForm getReimbursementForm(int f_id) throws SQLException{
		ReimbursementForm rf = null;
		String sql = "SELECT * " 
					+ "FROM REIMBURSEMENT_FORMS RF "
					+ "INNER JOIN  TYPE_OF_EVENTS TOE ON TOE.TOE_ID = RF.TYPE_OF_EVENT" 
					+ "WHERE RF.F_ID = ?";
		setup();
		pStmt = conn.prepareStatement(sql);
		pStmt.setInt(1, f_id);

		ResultSet rs = pStmt.executeQuery();
		while (rs.next()) {
			rf = new ReimbursementForm();
			rf.setFID(rs.getInt("F_ID"));
			rf.setPID(rs.getInt("P__ID"));
			rf.setStartDate(rs.getString("START_DATE"));
			rf.setStartTime(rs.getString("START_TIME"));
			rf.setStreet(rs.getString("STREET_ADDRESS"));
			rf.setCity(rs.getString("CITY"));
			rf.setState(rs.getString("STATE"));
			rf.setZipCode(rs.getString("ZIP_CODE"));
			rf.setRequestedAmount(rs.getDouble("REQUESTED_AMOUNT"));
			rf.setTypeOfEvent(rs.getString("EVENT"));
			rf.setGradingFormat(rs.getBlob("GRADING_FORMAT"));
			rf.setFinalPresentation(rs.getBlob("FINAL_PRESENTATION"));
			rf.setStatus(rs.getString("STATUS"));
			rf.setTitle(rs.getString("TITLE"));
		}
		return rf;
	}
}
