package de.rowlo.diffeclipse.screen;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

import de.rowlo.diffeclipse.DiffEclipse;
import de.rowlo.diffeclipse.model.tree.TreeModel;
import de.rowlo.diffeclipse.provider.TreeModelContentProvider;
import de.rowlo.diffeclipse.provider.TreeModelLabelProvider;
import de.rowlo.diffeclipse.sorter.TreeModelSorter;
import de.rowlo.diffeclipse.util.TreeModelFilterUtil;

public class DiffEclipseForm extends Composite {

	private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	private final Text textLocationPlainEclipse;
	private final Text textLocationMissingExtensions;
	private final Text textLocationExtendedEclipse;
	private final Button buttonChooseLocationPlainEclipse;
	private final Button buttonChooseLocationMissingExtensions;
	private final Button buttonChooseLocationExtendedEclipse;
	private final Button buttonHideSameFilesInPlainEclipse;
	private final Button buttonHideSameFilesInExtendedEclipse;
	private final Button buttonExtractMissingExtensions;
	private final Button buttonQuit;
	private final Section sectionPlainEclipse;
	private final Section sectionMissingExtension;
	private final Section sectionExtendedEclipse;
	private final Tree treeContentsPlainEclipse;
	private final TreeViewer treeViewerPlainEclipse;
	private final Tree treeContentsMissingExtensions;
	private final TreeViewer treeViewerMissingExtensions;
	private final Tree treeContentsExtendedEclipse;
	private final TreeViewer treeViewerExtendedEclipse;
	private final Button buttonFeaturesAndPlugins;
	private final SashForm sashForm;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public DiffEclipseForm(Composite parent, int style) {
		super(parent, style);
		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				toolkit.dispose();
			}
		});
		toolkit.adapt(this);
		toolkit.paintBordersFor(this);
		setLayout(new GridLayout(1, false));

		sashForm = new SashForm(this, SWT.SMOOTH);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		toolkit.adapt(sashForm);
		toolkit.paintBordersFor(sashForm);

		Composite compositeLeftColumn = toolkit.createComposite(sashForm, SWT.NONE);
		toolkit.paintBordersFor(compositeLeftColumn);
		compositeLeftColumn.setLayout(new GridLayout(2, false));

		textLocationPlainEclipse = toolkit.createText(compositeLeftColumn, "New Text", SWT.NONE);
		textLocationPlainEclipse
				.setToolTipText("Enter folder of an Eclipse application that's missing a set of extensions.");
		textLocationPlainEclipse.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textLocationPlainEclipse.setText("");

		buttonChooseLocationPlainEclipse = toolkit.createButton(compositeLeftColumn, "...", SWT.NONE);
		buttonChooseLocationPlainEclipse.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		buttonChooseLocationPlainEclipse
				.setToolTipText("Choose folder of an Eclipse application that's missing a set of extensions.");

		sectionPlainEclipse = toolkit.createSection(compositeLeftColumn, Section.EXPANDED | Section.TITLE_BAR);
		sectionPlainEclipse.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		toolkit.paintBordersFor(sectionPlainEclipse);
		sectionPlainEclipse.setText("Plain Eclipse");

		buttonHideSameFilesInPlainEclipse = toolkit.createButton(sectionPlainEclipse, "Hide Same Files", SWT.CHECK);
		sectionPlainEclipse.setTextClient(buttonHideSameFilesInPlainEclipse);
		buttonHideSameFilesInPlainEclipse
				.setToolTipText("Hide files that are common to Plain Eclipse and Extended Eclipse.");

		treeViewerPlainEclipse = new TreeViewer(sectionPlainEclipse, SWT.BORDER);
		treeContentsPlainEclipse = treeViewerPlainEclipse.getTree();
		toolkit.paintBordersFor(treeContentsPlainEclipse);
		sectionPlainEclipse.setClient(treeContentsPlainEclipse);

		Composite compositeMiddleColumn = toolkit.createComposite(sashForm, SWT.NONE);
		toolkit.paintBordersFor(compositeMiddleColumn);
		compositeMiddleColumn.setLayout(new GridLayout(2, false));

		textLocationMissingExtensions = toolkit.createText(compositeMiddleColumn, "New Text", SWT.NONE);
		textLocationMissingExtensions
				.setToolTipText("Enter folder where missing extensions (plugins and features) should be exported to.");
		textLocationMissingExtensions.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textLocationMissingExtensions.setText("");

		buttonChooseLocationMissingExtensions = toolkit.createButton(compositeMiddleColumn, "...", SWT.NONE);
		buttonChooseLocationMissingExtensions.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		buttonChooseLocationMissingExtensions
				.setToolTipText("Choose folder where missing extensions (plugins and features) should be exported to.");

		sectionMissingExtension = toolkit.createSection(compositeMiddleColumn, Section.EXPANDED | Section.TITLE_BAR);
		sectionMissingExtension.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		toolkit.paintBordersFor(sectionMissingExtension);
		sectionMissingExtension.setText("Missing Extensions in Plain Eclipse");

		treeViewerMissingExtensions = new TreeViewer(sectionMissingExtension, SWT.BORDER | SWT.MULTI);
		treeContentsMissingExtensions = treeViewerMissingExtensions.getTree();
		toolkit.paintBordersFor(treeContentsMissingExtensions);
		sectionMissingExtension.setClient(treeContentsMissingExtensions);

		buttonFeaturesAndPlugins = toolkit.createButton(sectionMissingExtension, "Features And Plug-ins", SWT.CHECK);
		sectionMissingExtension.setTextClient(buttonFeaturesAndPlugins);

		Composite compositeRightColumn = toolkit.createComposite(sashForm, SWT.NONE);
		toolkit.paintBordersFor(compositeRightColumn);
		compositeRightColumn.setLayout(new GridLayout(2, false));

		textLocationExtendedEclipse = toolkit.createText(compositeRightColumn, "New Text", SWT.NONE);
		textLocationExtendedEclipse
				.setToolTipText("Enter folder of an Eclipse application that contains a set of extensions missing in another Eclipse application.");
		textLocationExtendedEclipse.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textLocationExtendedEclipse.setText("");

		buttonChooseLocationExtendedEclipse = toolkit.createButton(compositeRightColumn, "...", SWT.NONE);
		buttonChooseLocationExtendedEclipse.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		buttonChooseLocationExtendedEclipse
				.setToolTipText("Choose folder of an Eclipse application that contains a set of extensions missing in another Eclipse application.");

		sectionExtendedEclipse = toolkit.createSection(compositeRightColumn, Section.EXPANDED | Section.TITLE_BAR);
		sectionExtendedEclipse.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		toolkit.paintBordersFor(sectionExtendedEclipse);
		sectionExtendedEclipse.setText("Extended Eclipse");

		buttonHideSameFilesInExtendedEclipse = toolkit.createButton(sectionExtendedEclipse, "Hide Same Files",
				SWT.CHECK);
		sectionExtendedEclipse.setTextClient(buttonHideSameFilesInExtendedEclipse);
		buttonHideSameFilesInExtendedEclipse
				.setToolTipText("Hide files that are common to Plain Eclipse and Extended Eclipse.");

		treeViewerExtendedEclipse = new TreeViewer(sectionExtendedEclipse, SWT.BORDER);
		treeContentsExtendedEclipse = treeViewerExtendedEclipse.getTree();
		toolkit.paintBordersFor(treeContentsExtendedEclipse);
		sectionExtendedEclipse.setClient(treeContentsExtendedEclipse);
		treeViewerExtendedEclipse.setLabelProvider(TreeModelLabelProvider.INSTANCE);
		treeViewerExtendedEclipse.setContentProvider(TreeModelContentProvider.INSTANCE);
		treeViewerExtendedEclipse.setInput(TreeModel.createRoot());
		treeViewerExtendedEclipse.setSorter(TreeModelSorter.INSTANCE);
		treeViewerExtendedEclipse.addFilter(TreeModelFilterUtil.INSTANCE.getExtendedEclipseViewerFilter());
		treeViewerMissingExtensions.setLabelProvider(TreeModelLabelProvider.INSTANCE);
		treeViewerMissingExtensions.setContentProvider(TreeModelContentProvider.INSTANCE);
		treeViewerMissingExtensions.setInput(TreeModel.createRoot());
		treeViewerMissingExtensions.setSorter(TreeModelSorter.INSTANCE);
		treeViewerMissingExtensions.addFilter(TreeModelFilterUtil.INSTANCE.getMissingExtensionsViewerFilter());
		treeViewerPlainEclipse.setLabelProvider(TreeModelLabelProvider.INSTANCE);
		treeViewerPlainEclipse.setContentProvider(TreeModelContentProvider.INSTANCE);
		treeViewerPlainEclipse.setInput(TreeModel.createRoot());
		treeViewerPlainEclipse.setSorter(TreeModelSorter.INSTANCE);
		treeViewerPlainEclipse.addFilter(TreeModelFilterUtil.INSTANCE.getPlainEclipseViewerFilter());
		sashForm.setWeights(new int[] { 1, 1, 1 });

		Composite compositeButtonBar = toolkit.createComposite(this, SWT.NONE);
		compositeButtonBar.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		toolkit.paintBordersFor(compositeButtonBar);
		{
			TableWrapLayout twl_compositeButtonBar = new TableWrapLayout();
			twl_compositeButtonBar.numColumns = 3;
			compositeButtonBar.setLayout(twl_compositeButtonBar);
		}

		Composite compositeSpacerButtonBar = toolkit.createComposite(compositeButtonBar, SWT.NONE);
		TableWrapData twd_compositeSpacerButtonBar = new TableWrapData(TableWrapData.FILL, TableWrapData.TOP, 1, 1);
		twd_compositeSpacerButtonBar.maxHeight = 2;
		twd_compositeSpacerButtonBar.grabHorizontal = true;
		compositeSpacerButtonBar.setLayoutData(twd_compositeSpacerButtonBar);
		toolkit.paintBordersFor(compositeSpacerButtonBar);

		buttonExtractMissingExtensions = toolkit.createButton(compositeButtonBar, "Extract Missing Extensions",
				SWT.NONE);
		buttonExtractMissingExtensions.setImage(DiffEclipse.getImage(DiffEclipse.IMAGE_EXTRACT));
		buttonExtractMissingExtensions.setLayoutData(new TableWrapData(TableWrapData.RIGHT, TableWrapData.TOP, 1, 1));

		buttonQuit = toolkit.createButton(compositeButtonBar, "Quit", SWT.NONE);
		buttonQuit.setImage(DiffEclipse.getImage(DiffEclipse.IMAGE_QUIT));
		buttonQuit.setLayoutData(new TableWrapData(TableWrapData.RIGHT, TableWrapData.TOP, 1, 1));

	}

	public Text getTextLocationPlainEclipse() {
		return textLocationPlainEclipse;
	}

	public Button getButtonChooseLocationPlainEclipse() {
		return buttonChooseLocationPlainEclipse;
	}

	public Text getTextLocationMissingExtensions() {
		return textLocationMissingExtensions;
	}

	public Button getButtonChooseLocationMissingExtensions() {
		return buttonChooseLocationMissingExtensions;
	}

	public Text getTextLocationExtendedEclipse() {
		return textLocationExtendedEclipse;
	}

	public Button getButtonChooseLocationExtendedEclipse() {
		return buttonChooseLocationExtendedEclipse;
	}

	public Button getButtonHideSameFilesInPlainEclipse() {
		return buttonHideSameFilesInPlainEclipse;
	}

	public Button getButtonHideSameFilesInExtendedEclipse() {
		return buttonHideSameFilesInExtendedEclipse;
	}

	public Tree getTreeContentsPlainEclipse() {
		return treeContentsPlainEclipse;
	}

	public Tree getTreeContentsMissingExtensions() {
		return treeContentsMissingExtensions;
	}

	public Tree getTreeContentsExtendedEclipse() {
		return treeContentsExtendedEclipse;
	}

	public Button getButtonExtractMissingExtensions() {
		return buttonExtractMissingExtensions;
	}

	public Button getButtonQuit() {
		return buttonQuit;
	}

	public Section getSectionPlainEclipse() {
		return sectionPlainEclipse;
	}

	public Section getSectionMissingExtension() {
		return sectionMissingExtension;
	}

	public Section getSectionExtendedEclipse() {
		return sectionExtendedEclipse;
	}

	public TreeViewer getTreeViewerPlainEclipse() {
		return treeViewerPlainEclipse;
	}

	public TreeViewer getTreeViewerMissingExtensions() {
		return treeViewerMissingExtensions;
	}

	public TreeViewer getTreeViewerExtendedEclipse() {
		return treeViewerExtendedEclipse;
	}

	public SashForm getSashForm() {
		return sashForm;
	}

	public Button getButtonFeaturesAndPlugins() {
		return buttonFeaturesAndPlugins;
	}
}
