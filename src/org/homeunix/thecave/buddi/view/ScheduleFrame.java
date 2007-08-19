/*
 * Created on May 6, 2006 by wyatt
 */
package org.homeunix.thecave.buddi.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.homeunix.thecave.buddi.Const;
import org.homeunix.thecave.buddi.i18n.keys.ButtonKeys;
import org.homeunix.thecave.buddi.i18n.keys.MenuKeys;
import org.homeunix.thecave.buddi.model.DataModel;
import org.homeunix.thecave.buddi.model.ScheduledTransaction;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import org.homeunix.thecave.buddi.util.InternalFormatter;
import org.homeunix.thecave.buddi.view.dialogs.ScheduleEditorDialog;
import org.homeunix.thecave.buddi.view.swing.ScheduledTransactionListCellRenderer;
import org.homeunix.thecave.moss.exception.WindowOpenException;
import org.homeunix.thecave.moss.swing.model.BackedListModel;
import org.homeunix.thecave.moss.swing.window.MossAssociatedDocumentFrame;
import org.homeunix.thecave.moss.swing.window.MossDocumentFrame;
import org.homeunix.thecave.moss.util.OperatingSystemUtil;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.decorator.HighlighterFactory;

public class ScheduleFrame extends MossAssociatedDocumentFrame implements ActionListener {
	public static final long serialVersionUID = 0;

	private final JButton doneButton;
//	private final JButton cancelButton;
	private final JButton newButton;
	private final JButton editButton;
	private final JButton deleteButton;

//	private final ScheduleEditorDialog scheduleEditor;

	private final JXList list;
	private final BackedListModel<ScheduledTransaction> listModel;
	private final DataModel model;

	public ScheduleFrame(DataModel model, MossDocumentFrame frame){
		super(frame, "ScheduledTransactionFrame" + ((DataModel) frame.getDocument()).getUid());

		this.model = model;
		
//		scheduleEditor = new ScheduleEditorDialog(frame);

		doneButton = new JButton(TextFormatter.getTranslation(ButtonKeys.BUTTON_DONE));
//		cancelButton = new JButton(TextFormatter.getTranslation(ButtonKeys.BUTTON_CANCEL));
		newButton = new JButton(TextFormatter.getTranslation(ButtonKeys.BUTTON_NEW));
		editButton = new JButton(TextFormatter.getTranslation(ButtonKeys.BUTTON_EDIT));
		deleteButton = new JButton(TextFormatter.getTranslation(ButtonKeys.BUTTON_DELETE));

		listModel = new BackedListModel<ScheduledTransaction>(model.getScheduledTransactions());
		list = new JXList();
	}

	@Override
	public void updateButtons() {		
		super.updateButtons();

		editButton.setEnabled(list.getSelectedIndices().length > 0);
		deleteButton.setEnabled(list.getSelectedIndices().length > 0);
	}

	@Override
	public void updateContent() {
		listModel.fireListChanged();

		super.updateContent();
	}

	public void init() {
//		scheduleEditor.loadSchedule(null);

		doneButton.setPreferredSize(InternalFormatter.getButtonSize(doneButton));
		newButton.setPreferredSize(InternalFormatter.getButtonSize(newButton));
		editButton.setPreferredSize(InternalFormatter.getButtonSize(editButton));
		deleteButton.setPreferredSize(InternalFormatter.getButtonSize(deleteButton));

		JPanel editTransactionsButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		editTransactionsButtonPanel.add(newButton);
		editTransactionsButtonPanel.add(editButton);
		editTransactionsButtonPanel.add(deleteButton);

		JScrollPane listScroller = new JScrollPane(list);		

		JPanel scrollPanel = new JPanel(new BorderLayout());
		scrollPanel.add(listScroller, BorderLayout.CENTER);
		scrollPanel.add(editTransactionsButtonPanel, BorderLayout.SOUTH);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//		buttonPanel.add(cancelButton);
		buttonPanel.add(doneButton);

		if (OperatingSystemUtil.isMac()){
			listScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		}

		newButton.addActionListener(this);
		editButton.addActionListener(this);
		deleteButton.addActionListener(this);
		doneButton.addActionListener(this);

		list.addHighlighter(HighlighterFactory.createAlternateStriping(Const.COLOR_EVEN_ROW, Const.COLOR_ODD_ROW));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setModel(listModel);
		list.setCellRenderer(new ScheduledTransactionListCellRenderer());
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//		list.getColumn(0).setCellRenderer(new ScheduledTransactionTableCellRenderer());
//		list.getColumn(0).setCellEditor(new JXTable.GenericEditor());
		list.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()){
					updateButtons();
				}
			}
		});

		list.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			private boolean allowMessage = true;

			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()){
					//Save existing scheduled transactions.  If the save failed, force
					// selection back to the poorly edited transaction.
//					if (allowMessage && !scheduleEditor.saveScheduledTransaction()){
//					allowMessage = false;
//					list.setRowSelectionInterval(listModel.getSelectedIndex(), listModel.getSelectedIndex());
//					return;
//					}

//					if (allowMessage) {
//					listModel.setSelectedScheduedTransaction(list.getSelectedRow());
//					ScheduledTransaction s = (ScheduledTransaction) listModel.getValueAt(list.getSelectedRow(), -1);
//					scheduleEditor.loadSchedule(s);
//					}
//					allowMessage = true;
//					listModel.setSelectedScheduedTransaction(list.getSelectedRow());
				}
			}
		});
//		list.addMouseListener(new MouseAdapter(){
//		@Override
//		public void mouseClicked(MouseEvent e) {
//		super.mouseClicked(e);
//		//Save existing scheduled transactions
//		modify.saveScheduledTransaction();

//		ScheduledTransaction s = (ScheduledTransaction) listModel.getValueAt(list.getSelectedRow(), -1);
//		modify.loadSchedule(s);
//		listModel.setSelectedScheduedTransaction(list.getSelectedRow());
//		}
//		});

		this.setTitle(TextFormatter.getTranslation(MenuKeys.MENU_EDIT_SCHEDULED_ACTIONS));
		this.setLayout(new BorderLayout());
		this.add(scrollPanel, BorderLayout.CENTER);
//		this.add(scheduleEditor, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
		this.getRootPane().setDefaultButton(doneButton);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(newButton)){
			ScheduleEditorDialog editor = new ScheduleEditorDialog(this, null);
			try {
				editor.openWindow();
			}
			catch (WindowOpenException woe){}
			
			list.setSelectedIndex(-1);
			listModel.fireListChanged();
		}
		else if (e.getSource().equals(editButton)){
			Object o = list.getSelectedValue();

			if (o instanceof ScheduledTransaction) {
				ScheduleEditorDialog editor = new ScheduleEditorDialog(this, (ScheduledTransaction) o);
				try {
					editor.openWindow();
				}
				catch (WindowOpenException woe){}
			}
		}
		else if (e.getSource().equals(deleteButton)){
			Object o = list.getSelectedValue();

			if (o instanceof ScheduledTransaction)
				model.removeScheduledTransaction((ScheduledTransaction) o);
			
			list.setSelectedIndex(-1);
			listModel.fireListChanged();
		}
		else if (e.getSource().equals(doneButton)){
//			listModel.save();
			this.closeWindow();
		}
//		else if (e.getSource().equals(cancelButton)){
//		this.closeWindow();
//		}
	}
}
