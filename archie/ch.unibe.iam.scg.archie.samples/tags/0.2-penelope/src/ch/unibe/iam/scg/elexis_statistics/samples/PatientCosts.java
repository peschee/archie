package ch.unibe.iam.scg.elexis_statistics.samples;

import ch.unibe.iam.scg.elexis_statistics.DataSource;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import java.util.ArrayList;
import java.util.List;


/**
 * TODO DOCUMENT ME!
 * 
 * @author dschenk
 * @version
 */
public class PatientCosts extends DataSource {
    protected List<String> tableHeadings;

    public PatientCosts(String name) {
        super(name);
        this.initTableHeadings();
    }

    /* (non-Javadoc)
     * @see ch.unibe.iam.scg.elexis_statistics.DataSource#execute(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public IStatus execute(IProgressMonitor monitor) {
        this.monitor = monitor;
        this.createContent();
        this.initProviders();
        this.monitor.done();
        return Status.OK_STATUS;
    }
    
    private void initTableHeadings() {
        this.tableHeadings = new ArrayList<String>();
        this.tableHeadings.add("Patient"); //i18n?
        this.tableHeadings.add("Gesamtkosten"); //i18n?
    }

    private void initProviders() {
        // TODO
    }

    private void createContent() {
        // TODO
    }

    @Override
    public String getDescription() { //i18n?
        return "Erstellt eine Liste mit allen Patienten, die in der gegebenen " +
        "Zeitspanne eine Konsultation hatten und die gesamten " +
        "Kosten, die bei diesen Patienten anfielen.";
    }

    @Override
    public List<String> getTableHeadings() {
        return this.tableHeadings;
    }

    @Override
    public String getTitle() {
        return "Kosten pro Patient"; //i18n?
    }
}
