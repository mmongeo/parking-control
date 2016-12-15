package parkingControl;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import java.awt.Font;

public class MainWindowParking {

	private JFrame frmMainParking;

	private DBMParking dbm = new DBMParking();
	private PosPrinter pp = new PosPrinter();
	private ChargeCalculator chargeCalculator = new ChargeCalculator(dbm);
	private JTable tblCurrentVehicles;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindowParking window = new MainWindowParking();
					window.frmMainParking.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindowParking() {
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmMainParking = new JFrame();
		frmMainParking.setTitle("Parqueo Shalom");
		frmMainParking.setBounds(100, 100, 633, 424);
		frmMainParking.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMainParking.getContentPane().setLayout(null);
		
		JButton btnAdd = new JButton("Agregar nuevo");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DialogInsertVehicle d = new DialogInsertVehicle();
				ParkedVehicle v = d.showDialog();
				if(v != null){
					dbm.insertVehicle(v);	
					pp.printEntryTicket(v);
				}
				refreshTable();
			}
		});
		btnAdd.setBounds(53, 323, 200, 25);
		frmMainParking.getContentPane().add(btnAdd);
		
		JButton bnCharge = new JButton("Cobrar seleccionado");
		bnCharge.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = tblCurrentVehicles.getSelectedRow();
				if(row >= 0){
					DefaultTableModel model = (DefaultTableModel) tblCurrentVehicles.getModel();
					int id = Integer.parseInt((model.getValueAt(row, 4).toString()));
					ParkedVehicle v = dbm.getVehicleById(id);
					ParkedVehicleHistory hv = chargeCalculator.getVehicleWithPrice(v);
					DialogChargePrice dialogCharge = new DialogChargePrice(hv);
					dialogCharge.setModal(true);
					int res = dialogCharge.showDialog();
					if(res == 1){
						dbm.deleteVehicle(hv);
						pp.printReceipt(hv);
					}
				}
				else{
					JOptionPane.showMessageDialog(null, "Seleccione un vehiculo de la tabla de vehiculos");					
				}
				refreshTable();
			}
		});
		bnCharge.setBounds(309, 323, 179, 25);
		frmMainParking.getContentPane().add(bnCharge);
		
		JButton btnRefresh = new JButton("Refrescar tabla de vehiculos");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshTable();
			}
		});
		btnRefresh.setBounds(51, 68, 433, 33);
		frmMainParking.getContentPane().add(btnRefresh);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(53, 127, 431, 184);
		frmMainParking.getContentPane().add(scrollPane);
		
		tblCurrentVehicles = new JTable();
		tblCurrentVehicles.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tblCurrentVehicles.setSelectionModel(new ForcedSelectionModel());
		scrollPane.setViewportView(tblCurrentVehicles);
		refreshTable();
	}
	
	void refreshTable(){
		List<ParkedVehicle> current = dbm.getCurrentVehicles();
		DefaultTableModel model = new DefaultTableModel(ParkedVehicle.getColumnNames(), 0);
		for (ParkedVehicle v : current){
			model.addRow(v.toRow());
		}
		tblCurrentVehicles.setModel(model);
		model.fireTableDataChanged();
	}
}
