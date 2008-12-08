package ch.unibe.iam.scg.elexis_statistics.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * TODO: DOCUMENT ME!
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class GraphicalMessage extends Composite {

	private Image image;
	private String message;

	private Composite parent;

	public GraphicalMessage(Composite parent, Image image, String message) {
		super(parent, SWT.FLAT);

		this.parent = parent;
		this.image = image;
		this.message = message;

		this.initialize();
	}

	private void initialize() {
		GridData data = new GridData();
		data.horizontalAlignment = GridData.CENTER;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;

		// holds the loading image and message
		this.setBackground(this.parent.getBackground());
		this.setLayout(new GridLayout());
		this.setLayoutData(data);

		// loading image
		Label imageLabel = new Label(this, SWT.CENTER);

		// set the label image
		imageLabel.setImage(this.image);
		imageLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		imageLabel.setBackground(this.getBackground());

		// loading message
		Label messageLabel = new Label(this, SWT.WRAP | SWT.CENTER);
		messageLabel.setBackground(this.parent.getBackground());
		messageLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		messageLabel.setText(this.message);
	}

}
