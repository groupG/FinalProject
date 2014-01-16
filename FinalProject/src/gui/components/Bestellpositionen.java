package gui.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Configuration;

public class Bestellpositionen extends JPanel implements Configuration,
		ListSelectionListener {

	private static final long serialVersionUID = -6721680340100703148L;

	private JList<String> list;
	private DefaultListModel<String> listModel;

	private static final String add = "Hinzufügen";
	private static final String del = "Entfernen";

	private JButton btnAdd;
	private JButton btnDel;

	private JTextField txtInput;

	public Bestellpositionen(String title) {
		super(new GridBagLayout());

		this.listModel = new DefaultListModel<String>();
//		this.listModel.addElement("Test");

		this.list = new JList<String>(this.listModel);
		this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.list.setSelectedIndex(-1);
		this.list.addListSelectionListener(this);
		this.list.setVisibleRowCount(5);
		this.list.setName(title);

		JScrollPane listScrollPane = new JScrollPane(this.list);

		this.btnAdd = new JButton(Bestellpositionen.add);
		AddListener addListener = new AddListener(this.btnAdd);
		this.btnAdd.addActionListener(addListener);
		this.btnAdd.setName("add");
		this.btnAdd.setActionCommand("add");
		this.btnAdd.setEnabled(false);

		this.btnDel = new JButton(Bestellpositionen.del);
		this.btnDel.setName("del");
		this.btnDel.setActionCommand("del");
		this.btnDel.setEnabled(false);
		this.btnDel.addActionListener(new DeleteListener());

		this.txtInput = new JTextField(10);
		this.txtInput.setName("bstPosInput");
		this.txtInput.addActionListener(addListener);
		this.txtInput.getDocument().addDocumentListener(addListener);
//		String bestellPosString = this.listModel.getElementAt(this.list.getSelectedIndex()).toString();

		JPanel buttonPanel = new JPanel(new GridBagLayout());
		JPanel inputPanel = new JPanel(new GridBagLayout());
		JLabel posLabel = new JLabel("Bestellpositionen");

		addComponent(inputPanel, posLabel, new Insets(0,5,0,5), 0, 0);
		addComponent(inputPanel, this.txtInput, new Insets(0,-10,0,5),1,0);

		addComponent(buttonPanel, this.btnAdd, new Insets(0,5,0,5), 0, 0);
		addComponent(buttonPanel, this.btnDel, new Insets(0,5,0,5),1,0);

		addComponent(this, inputPanel, new Insets(0,0,0,0), 0, 0);
		addComponent(this, buttonPanel, new Insets(0,0,0,0), 0, 1);
		addComponent(this, listScrollPane, new Insets(0,0,0,0), 0, 2);
	}

	public int getList(){
		return this.listModel.getSize();
	}
	public void addComponent(JPanel panel, Component c,
			Insets insets, int x, int y) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.insets = insets;
		panel.add(c, constraints);
	}

	public void addComponent(JPanel panel, Component c,
			Insets insets, int x, int y, int width, int height, double weightx,
			double weighty) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = width;
		constraints.gridheight = height;
		constraints.weightx = weightx;
		constraints.weighty = weighty;
		constraints.insets = insets;
		panel.add(c, constraints);
	}

	class DeleteListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			int index = list.getSelectedIndex();
			int size = listModel.getSize();

			if (size == 0) {
				btnDel.setEnabled(false);
			} else {
				if (index == listModel.getSize()) {
					index--;
				}
				list.setSelectedIndex(index);
				list.ensureIndexIsVisible(index);
			}
			listModel.remove(index);
		}
	}

	class AddListener implements ActionListener, DocumentListener {
		private boolean alreadyEnabled = false;
		private JButton button;

		public AddListener(JButton button) {
			this.button = button;
		}

		public void actionPerformed(ActionEvent ae) {
			String input = txtInput.getText();

			if (input.equals("") || alreadyInList(input)) {
				Toolkit.getDefaultToolkit().beep();
				txtInput.requestFocusInWindow();
				txtInput.selectAll();
				return;
			}

			int index = list.getSelectedIndex();
			if (index == -1) {
				index = 0;
			} else {
				index++;
			}

			// listModel.insertElementAt(txtInput.getText(), index);
			listModel.addElement(txtInput.getText());

			txtInput.requestFocusInWindow();
			txtInput.setText("");

			list.setSelectedIndex(index);
			//list.ensureIndexIsVisible(index);
		}

		protected boolean alreadyInList(String input) {
			return listModel.contains(input);
		}

		public void insertUpdate(DocumentEvent de) {
			enableButton();
		}

		public void removeUpdate(DocumentEvent de) {
			handleEmptyTextField(de);
		}

		public void changedUpdate(DocumentEvent de) {
			if (!handleEmptyTextField(de)) {
				enableButton();
			}
		}

		private void enableButton() {
			if (!alreadyEnabled) {
				button.setEnabled(true);
			}
		}

		private boolean handleEmptyTextField(DocumentEvent de) {
			if (de.getDocument().getLength() <= 0) {
				button.setEnabled(false);
				alreadyEnabled = false;
				return true;
			}
			return false;
		}
	}

	public void valueChanged(ListSelectionEvent le) {
		if (le.getValueIsAdjusting() == false) {
			if (list.getSelectedIndex() == -1) {
				btnDel.setEnabled(false);
			} else {
				txtInput.setText(list.getSelectedValue());
				btnDel.setEnabled(true);
			}
		}
	}
}