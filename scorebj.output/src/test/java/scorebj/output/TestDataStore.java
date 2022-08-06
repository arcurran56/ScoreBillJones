package scorebj.output;

import com.thoughtworks.xstream.persistence.FilePersistenceStrategy;
import com.thoughtworks.xstream.persistence.XmlMap;
import com.thoughtworks.xstream.security.AnyTypePermission;
import scorebj.model.Competition;
import scorebj.model.DataStore;
import scorebj.model.DataStoreException;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;

public class TestDataStore extends DataStore {
    private static TestDataStore dataStore;

    public static DataStore create() throws DataStoreException {
        logger.debug("...creating...");
        try {
            if (dataStore == null) {
                logger.debug("...new datastore creation.");
                dataStore = new TestDataStore();
                dataStore.initialise();
            }
        } catch (Exception e) {
            logger.error(e.getStackTrace());
            throw new DataStoreException();
        }
        return dataStore;
    }

    @Override
    public void initialise() {
        super.logger.debug("...initialising...");

        xStream.addPermission(AnyTypePermission.ANY);
        //xStream.allowTypes(ALLOWED_TYPES);

        File dataLocation = null;
        try {
            dataLocation = new File(ClassLoader.getSystemResource("/scorebj/output/test_data.xml").toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        logger.debug("...writing to " + dataLocation.getAbsolutePath());

        persistenceStrategy =
                new FilePersistenceStrategy(dataLocation, xStream);
        persistentCompetitions = (Map<String, Competition>) Collections.synchronizedMap( (Map<String,Competition>) new XmlMap(persistenceStrategy));

        logger.debug("...exiting initialise");
    }
}
