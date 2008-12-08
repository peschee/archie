package ch.unibe.iam.scg.elexis_statistics.samples;

import java.util.ArrayList;
import java.util.List;

import ch.elexis.data.Anwender;
import ch.elexis.data.Query;
import ch.unibe.iam.scg.elexis_statistics.model.AbstractDataProvider;
import ch.unibe.iam.scg.elexis_statistics.samples.i18n.Messages;

/**
 * Simple User Overview
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class UserStats extends AbstractDataProvider {

	public UserStats() {
		super(Messages.USER_OVERVIEW_TITLE);
	}
	
    protected void initializeDefaultValues() {
    	// Nothing here... Move along.
    }

	protected List<String> createHeadings() {
		final ArrayList<String> headings = new ArrayList<String>();
		
		headings.add(Messages.USER_OVERVIEW_USER);
		headings.add(Messages.USER_OVERVIEW_BIRTHDAY);
		headings.add(Messages.USER_OVERVIEW_GENDER);
		headings.add(Messages.USER_OVERVIEW_VALID);
		headings.add(Messages.USER_OVERVIEW_GROUPS);		
		
		return headings;
	}

	protected List<Object[]> createContent() {
		final List<Object[]> list = new ArrayList<Object[]>();

		final Query<Anwender> query = new Query<Anwender>(Anwender.class);
		final List<Anwender> anwenderList = query.execute();

		this.size = anwenderList.size();
		this.monitor.beginTask("querying database", this.size); // monitoring

		for (final Anwender anwender : anwenderList) {
			final String valid = (anwender.isValid() == true) ? Messages.USER_OVERVIEW_YES : Messages.USER_OVERVIEW_NO;

			final String group = (anwender.getInfoElement("Groups") != null) ? anwender.getInfoElement("Groups").toString()
					: Messages.USER_OVERVIEW_UNDEFINED;

			final Object[] row = { anwender.getLabel(), anwender.getGeburtsdatum(), anwender.getGeschlecht(), valid, group };
			list.add(row);		

			this.monitor.worked(1); // monitoring
		}
		return list;
	}

	@Override
	public String getDescription() {
		return Messages.USER_OVERVIEW_DESCRIPTION;
	}

}
